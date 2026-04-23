/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.exceptions;

/**
 * @author Ammaar Aslam (W2120486)
 */
public class RoomNotEmptyException extends Exception {
    private String roomId;

    public RoomNotEmptyException(String roomId) {
        super("Room " + roomId + " cannot be deleted because it contains active sensors");
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}