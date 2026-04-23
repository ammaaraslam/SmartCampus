/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.exceptions;

/**
 * @author Ammaar Aslam (W2120486)
 */
public class LinkedResourceNotFoundException extends Exception {
    private String resourceType;
    private String resourceId;

    public LinkedResourceNotFoundException(String resourceType, String resourceId) {
        super("Referenced " + resourceType + " '" + resourceId + "' does not exist");
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}