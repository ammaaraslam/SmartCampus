package com.mycompany.smartcampus;

import com.mycompany.smartcampus.filters.LoggingFilter;
import com.mycompany.smartcampus.mappers.LinkedResourceNotFoundExceptionMapper;
import com.mycompany.smartcampus.mappers.RoomNotEmptyExceptionMapper;
import com.mycompany.smartcampus.mappers.SensorUnavailableExceptionMapper;
import com.mycompany.smartcampus.mappers.ThrowableExceptionMapper;
import com.mycompany.smartcampus.resources.DiscoveryResource;
import com.mycompany.smartcampus.resources.RoomResource;
import com.mycompany.smartcampus.resources.SensorResource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ammaar Aslam (W2120486)
 */
@ApplicationPath("/api/v1")
public class JAXRSConfiguration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(DiscoveryResource.class);
        resources.add(RoomResource.class);
        resources.add(SensorResource.class);
        resources.add(RoomNotEmptyExceptionMapper.class);
        resources.add(LinkedResourceNotFoundExceptionMapper.class);
        resources.add(SensorUnavailableExceptionMapper.class);
        resources.add(ThrowableExceptionMapper.class);
        resources.add(LoggingFilter.class);
        return resources;
    }
}