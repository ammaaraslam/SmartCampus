/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.services;

import com.mycompany.smartcampus.models.Room;
import com.mycompany.smartcampus.models.Sensor;
import com.mycompany.smartcampus.models.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ammaar Aslam (W2120486)
 */
public class DataStore {
    private static final DataStore instance = new DataStore();
    private final Map<String, Room> rooms;
    private final Map<String, Sensor> sensors;
    private final Map<String, List<SensorReading>> readings;

    private DataStore() {
        this.rooms = new HashMap<>();
        this.sensors = new HashMap<>();
        this.readings = new HashMap<>();
        initializeSampleData();
    }

    public static DataStore getInstance() {
        return instance;
    }

    private void initializeSampleData() {
        Room room1 = new Room("LIB-301", "Library Quiet Study", 30);
        Room room2 = new Room("LAB-202", "Computer Lab", 50);
        rooms.put("LIB-301", room1);
        rooms.put("LAB-202", room2);

        Sensor sensor1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor sensor2 = new Sensor("CO2-001", "CO2", "ACTIVE", 450.0, "LIB-301");
        Sensor sensor3 = new Sensor("OCC-001", "Occupancy", "ACTIVE", 15.0, "LAB-202");

        sensors.put("TEMP-001", sensor1);
        sensors.put("CO2-001", sensor2);
        sensors.put("OCC-001", sensor3);

        room1.addSensorId("TEMP-001");
        room1.addSensorId("CO2-001");
        room2.addSensorId("OCC-001");

        readings.put("TEMP-001", new ArrayList<>());
        readings.put("CO2-001", new ArrayList<>());
        readings.put("OCC-001", new ArrayList<>());
    }

    public synchronized Room addRoom(Room room) {
        rooms.put(room.getId(), room);
        readings.keySet().stream()
                .filter(sensorId -> !readings.containsKey(sensorId))
                .forEach(sensorId -> readings.put(sensorId, new ArrayList<>()));
        return room;
    }

    public synchronized Room getRoom(String id) {
        return rooms.get(id);
    }

    public synchronized List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public synchronized boolean deleteRoom(String id) {
        return rooms.remove(id) != null;
    }

    public synchronized boolean roomExists(String id) {
        return rooms.containsKey(id);
    }

    public synchronized Sensor addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
        readings.put(sensor.getId(), new ArrayList<>());
        return sensor;
    }

    public synchronized Sensor getSensor(String id) {
        return sensors.get(id);
    }

    public synchronized List<Sensor> getAllSensors() {
        return new ArrayList<>(sensors.values());
    }

    public synchronized List<Sensor> getSensorsByType(String type) {
        List<Sensor> filtered = new ArrayList<>();
        for (Sensor sensor : sensors.values()) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                filtered.add(sensor);
            }
        }
        return filtered;
    }

    public synchronized boolean sensorExists(String id) {
        return sensors.containsKey(id);
    }

    public synchronized boolean deleteSensor(String id) {
        Sensor sensor = sensors.remove(id);
        if (sensor != null && rooms.containsKey(sensor.getRoomId())) {
            rooms.get(sensor.getRoomId()).removeSensorId(id);
        }
        readings.remove(id);
        return true;
    }

    public synchronized SensorReading addReading(String sensorId, SensorReading reading) {
        if (!readings.containsKey(sensorId)) {
            readings.put(sensorId, new ArrayList<>());
        }
        readings.get(sensorId).add(reading);
        return reading;
    }

    public synchronized List<SensorReading> getReadings(String sensorId) {
        return new ArrayList<>(readings.getOrDefault(sensorId, new ArrayList<>()));
    }

    public synchronized void updateSensorValue(String sensorId, double value) {
        Sensor sensor = sensors.get(sensorId);
        if (sensor != null) {
            sensor.setCurrentValue(value);
        }
    }
}