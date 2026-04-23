/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.exceptions;

/**
 * @author Ammaar Aslam (W2120486)
 */
public class SensorUnavailableException extends Exception {
    private String sensorId;
    private String status;

    public SensorUnavailableException(String sensorId, String status) {
        super("Sensor " + sensorId + " is currently in " + status + " status and cannot accept readings");
        this.sensorId = sensorId;
        this.status = status;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getStatus() {
        return status;
    }
}