# Smart Campus Sensor & Room Management API

A RESTful API for managing campus rooms and their associated sensors, built with Java EE 8, JAX-RS, and Jersey.

**Author:** Ammaar Aslam (W2120486/20232527)
**Course:** 5COSC022W - University of Westminster

---

## Quick Start

### Prerequisites

- **Java JDK 8+** installed
- **Maven 3.6+** installed
- **Apache Tomcat 9.0+** running locally
- **Git** installed

### Setup Instructions

#### 1. Clone the Repository

```bash
git clone https://github.com/ammaaraslam/SmartCampus.git
cd SmartCampus
```

#### 2. Build the Project

```bash
mvn clean install
```

This will compile all source files and package the application as a `.war` file in `target/SmartCampus-1.0-SNAPSHOT.war`.

#### 3. Deploy to Tomcat

**Option A: NetBeans (Recommended)**
1. Open the project in NetBeans
2. Right-click the project → **Run**
3. Application will deploy automatically

**Option B: Manual Deployment**
1. Copy `target/SmartCampus-1.0-SNAPSHOT.war` to Tomcat's `webapps/` directory
2. Restart Tomcat
3. Access at `http://localhost:8080/SmartCampus`

#### 4. Verify Installation

```bash
curl http://localhost:8080/SmartCampus/api/v1
```

Expected response:
```json
{
  "apiVersion": "1.0",
  "title": "Smart Campus Sensor & Room Management API",
  "resources": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}
```

---

## API Specifications

### Base URL
```
http://localhost:8080/SmartCampus/api/v1
```

### Content-Type
All requests and responses use `application/json`.

---

## API Endpoints

### 1. Discovery Resource

#### GET `/api/v1`
Returns API metadata and available resources.

**Response (200 OK):**
```json
{
  "apiVersion": "1.0",
  "title": "Smart Campus Sensor & Room Management API",
  "resources": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}
```

---

### 2. Room Management

#### GET `/api/v1/rooms`
List all rooms.

**Response (200 OK):**
```json
[
  {
    "id": "LIB-301",
    "name": "Library Study Room 301",
    "capacity": 20,
    "sensorIds": ["TEMP-001", "CO2-001"]
  },
  {
    "id": "LAB-202",
    "name": "Computer Lab 202",
    "capacity": 30,
    "sensorIds": ["OCC-001"]
  }
]
```

---

#### POST `/api/v1/rooms`
Create a new room.

**Request Body:**
```json
{
  "id": "ROOM-101",
  "name": "Conference Room 101",
  "capacity": 15,
  "sensorIds": []
}
```

**Response (201 Created):**
```json
{
  "id": "ROOM-101",
  "name": "Conference Room 101",
  "capacity": 15,
  "sensorIds": []
}
```

---

#### GET `/api/v1/rooms/{roomId}`
Retrieve a specific room.

**Response (200 OK):**
```json
{
  "id": "LIB-301",
  "name": "Library Study Room 301",
  "capacity": 20,
  "sensorIds": ["TEMP-001", "CO2-001"]
}
```

**Response (404 Not Found):**
```json
{
  "statusCode": 404,
  "message": "Room not found: INVALID-ID",
  "timestamp": "1719327600000",
  "path": "/api/v1/rooms/INVALID-ID"
}
```

---

#### DELETE `/api/v1/rooms/{roomId}`
Delete a room (must have no sensors).

**Response (204 No Content)**

**Response (409 Conflict)** - Room has sensors:
```json
{
  "statusCode": 409,
  "message": "Cannot delete room LIB-301: it contains sensors",
  "timestamp": "1719327600000",
  "path": "/api/v1/rooms/LIB-301"
}
```

---

### 3. Sensor Management

#### GET `/api/v1/sensors`
List all sensors (with optional type filter).

**Query Parameters:**
- `type` (optional): Filter by sensor type

**Response (200 OK):**
```json
[
  {
    "id": "TEMP-001",
    "type": "TEMPERATURE",
    "status": "ACTIVE",
    "currentValue": 22.5,
    "roomId": "LIB-301"
  },
  {
    "id": "CO2-001",
    "type": "CO2",
    "status": "ACTIVE",
    "currentValue": 410,
    "roomId": "LIB-301"
  }
]
```

---

#### POST `/api/v1/sensors`
Create a new sensor.

**Request Body:**
```json
{
  "id": "HUMIDITY-001",
  "type": "HUMIDITY",
  "status": "ACTIVE",
  "currentValue": 65,
  "roomId": "LIB-301"
}
```

**Response (201 Created):**
```json
{
  "id": "HUMIDITY-001",
  "type": "HUMIDITY",
  "status": "ACTIVE",
  "currentValue": 65,
  "roomId": "LIB-301"
}
```

**Response (422 Unprocessable Entity)** - Room doesn't exist:
```json
{
  "statusCode": 422,
  "message": "Cannot create sensor: linked Room resource not found (ID: INVALID-ROOM)",
  "timestamp": "1719327600000",
  "path": "/api/v1/sensors"
}
```

---

#### GET `/api/v1/sensors/{sensorId}`
Retrieve a specific sensor.

**Response (200 OK):**
```json
{
  "id": "TEMP-001",
  "type": "TEMPERATURE",
  "status": "ACTIVE",
  "currentValue": 22.5,
  "roomId": "LIB-301"
}
```

---

#### DELETE `/api/v1/sensors/{sensorId}`
Delete a sensor.

**Response (204 No Content)**

---

### 4. Sensor Readings (Sub-Resource)

#### GET `/api/v1/sensors/{sensorId}/readings`
Retrieve all readings from a sensor.

**Response (200 OK):**
```json
[
  {
    "id": "read-uuid-1",
    "value": 22.5,
    "timestamp": "1719327600000"
  },
  {
    "id": "read-uuid-2",
    "value": 23.1,
    "timestamp": "1719327660000"
  }
]
```

---

#### POST `/api/v1/sensors/{sensorId}/readings`
Add a reading to a sensor.

**Request Body:**
```json
{
  "value": 23.5
}
```

**Response (201 Created):**
```json
{
  "id": "read-uuid-4",
  "value": 23.5,
  "timestamp": "1719327780000"
}
```

**Response (403 Forbidden)** - Sensor is unavailable:
```json
{
  "statusCode": 403,
  "message": "Cannot add reading to sensor TEMP-001: sensor status is MAINTENANCE",
  "timestamp": "1719327600000",
  "path": "/api/v1/sensors/TEMP-001/readings"
}
```

---

## HTTP Status Codes

| Code | Status | Description |
|------|--------|-------------|
| 200 | OK | Successful GET request |
| 201 | Created | Successful POST request |
| 204 | No Content | Successful DELETE request |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Room deletion with active sensors |
| 422 | Unprocessable Entity | Linked resource not found |
| 403 | Forbidden | Sensor unavailable |
| 500 | Internal Server Error | Server error |

---

## Example Usage

### Create a Room
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "id": "NEW-101",
    "name": "New Conference Room",
    "capacity": 20,
    "sensorIds": []
  }'
```

### Create a Sensor
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "TEMP-NEW",
    "type": "TEMPERATURE",
    "status": "ACTIVE",
    "currentValue": 21.0,
    "roomId": "NEW-101"
  }'
```

### Add a Reading
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors/TEMP-NEW/readings \
  -H "Content-Type: application/json" \
  -d '{"value": 22.5}'
```

### Get All Temperature Sensors
```bash
curl http://localhost:8080/SmartCampus/api/v1/sensors?type=TEMPERATURE
```

### Delete a Sensor
```bash
curl -X DELETE http://localhost:8080/SmartCampus/api/v1/sensors/TEMP-NEW
```

---

## Technologies

- Java EE 8 with JAX-RS 2.0
- Jersey 2.35 (JAX-RS implementation)
- HK2 (dependency injection)
- JSON-B (JSON binding)
- Apache Tomcat 9.0
- Maven 3.6+

---

## Project Structure

```
src/main/java/com/mycompany/smartcampus/
├── JAXRSConfiguration.java
├── models/ (Room, Sensor, SensorReading)
├── resources/ (DiscoveryResource, RoomResource, SensorResource, SensorReadingResource)
├── services/ (DataStore)
├── exceptions/ (RoomNotEmptyException, LinkedResourceNotFoundException, SensorUnavailableException)
├── mappers/ (Exception mappers for HTTP responses)
├── filters/ (LoggingFilter)
└── dto/ (ErrorResponse)
```

---


## Data Storage

In-memory storage (HashMap) with thread-safe synchronized access. Data is lost on application restart.

---

## Troubleshooting

**Build error:** Update Maven cache
```bash
mvn clean install
rm -rf ~/.m2/repository/org/glassfish
mvn clean install
```

**404 errors:** Verify Tomcat deployment and WAR file location.
