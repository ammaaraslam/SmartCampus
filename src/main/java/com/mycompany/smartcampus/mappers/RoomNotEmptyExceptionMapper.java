/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.mappers;

import com.mycompany.smartcampus.dto.ErrorResponse;
import com.mycompany.smartcampus.exceptions.RoomNotEmptyException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Ammaar Aslam (W2120486)
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                409,
                exception.getMessage(),
                "/api/v1/rooms/" + exception.getRoomId()
        );
        return Response.status(Response.Status.CONFLICT)
                .entity(errorResponse)
                .build();
    }
}