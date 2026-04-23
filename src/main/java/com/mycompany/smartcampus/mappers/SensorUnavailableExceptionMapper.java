/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.mappers;

import com.mycompany.smartcampus.dto.ErrorResponse;
import com.mycompany.smartcampus.exceptions.SensorUnavailableException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Ammaar Aslam (W2120486)
 */
@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                403,
                exception.getMessage(),
                "/api/v1/sensors/" + exception.getSensorId() + "/readings"
        );
        return Response.status(Response.Status.FORBIDDEN)
                .entity(errorResponse)
                .build();
    }
}