/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.resources;

import com.mycompany.smartcampus.exceptions.LinkedResourceNotFoundException;
import com.mycompany.smartcampus.models.Sensor;
import com.mycompany.smartcampus.services.DataStore;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Ammaar Aslam (W2120486)
 */
@Path("sensors")
public class SensorResource {
    private DataStore dataStore = DataStore.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensors;
        if (type != null && !type.isEmpty()) {
            sensors = dataStore.getSensorsByType(type);
        } else {
            sensors = dataStore.getAllSensors();
        }
        return Response.ok(sensors).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) throws LinkedResourceNotFoundException {
        if (!dataStore.roomExists(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room", sensor.getRoomId());
        }

        Sensor created = dataStore.addSensor(sensor);
        dataStore.getRoom(sensor.getRoomId()).addSensorId(sensor.getId());
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = dataStore.getSensor(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found: " + sensorId)
                    .build();
        }
        return Response.ok(sensor).build();
    }

    @DELETE
    @Path("{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        if (!dataStore.sensorExists(sensorId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found: " + sensorId)
                    .build();
        }
        dataStore.deleteSensor(sensorId);
        return Response.noContent().build();
    }

    @Path("{sensorId}/readings")
    public SensorReadingResource getSensorReadings(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}