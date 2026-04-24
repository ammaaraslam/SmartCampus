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

---

## Answers to Coursework Questions

### Question 1: In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new Instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

JAX-RS uses **per-request instantiation** - a new resource instance is created for each HTTP request. This requires careful synchronization when accessing shared data.
 
**Our solution:** DataStore singleton with **synchronized methods** prevents race conditions (lost updates, dirty reads, ConcurrentModificationException).
 
```java
public synchronized Room addRoom(Room room) { ... }
public synchronized List<Sensor> getAllSensors() { ... }
```
 
**Why it works:** Only one thread can execute synchronized methods at a time, ensuring atomic read-write operations across concurrent requests while maintaining data consistency.

### Question 2: Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

**HATEOAS** (Hypermedia As The Engine Of Application State) embeds navigation links in API responses, enabling **client discoverability** without hardcoding URLs.
 
**Our Discovery Endpoint:**
```json
{
  "apiVersion": "1.0",
  "resources": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}
```
 
**Benefits:**
- **Decoupling:** Clients discover endpoints dynamically, not hardcoded
- **API Evolution:** Endpoint changes don't break clients (automatic discovery)
- **Self-documenting:** API becomes self-explanatory without separate docs
- **State-driven navigation:** Clients discover valid next actions from responses

### Question 3: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.

**Trade-off analysis:**
 
| Aspect | IDs Only | Full Objects |
|--------|----------|--------------|
| Bandwidth | ✅ Minimal | ❌ Higher |
| Requests | ❌ N additional calls | ✅ Single request |
| Latency | ❌ High (chatty) | ✅ Low |
| Client work | ❌ Parse N responses | ✅ Single parse |
 
**Our decision:** Return full Room objects because:
- Room entities are lightweight (4 fields: id, name, capacity, sensorIds)
- Clients typically need complete data for display
- Single-request satisfaction improves user experience
- Trade-off favors latency over bandwidth

### Question 4: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

**Definition:** An operation is idempotent if invoking it multiple times produces the same **end-state result** as invoking it once.
 
**Our implementation proof:**
- **First DELETE /rooms/LIB-301** → Room deleted, returns **204 No Content**
- **Second DELETE /rooms/LIB-301** → Room already gone, returns **404 Not Found**
**System state after each request:** Identical - the room is absent.
 
**RFC 7231 compliance:** HTTP DELETE is defined as idempotent. This makes **network retries safe** - clients can safely re-attempt failed DELETE requests without risking duplicate deletions or data corruption.

### Question 5: We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

The `@Consumes(MediaType.APPLICATION_JSON)` annotation enforces **content-type validation before the method executes**.
 
**Scenario - Client sends wrong format:**
```bash
curl -X POST /api/v1/sensors \
  -H "Content-Type: text/plain" \
  -d 'invalid data'
```
 
**JAX-RS Response:** HTTP **415 Unsupported Media Type**
- Request rejected **before method execution**
- No deserialization attempted
- Fails fast, prevents partial processing
**How it works:**
1. JAX-RS inspects `Content-Type` header
2. Compares against `@Consumes` declaration
3. If no match → returns 415 immediately
4. If match → proceeds to JSON deserialization via MessageBodyReader
**Best Practice:** Always specify @Consumes to enforce API contracts and prevent invalid input processing.

### Question 6: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

We use `@QueryParam("type")` for filtering: `GET /api/v1/sensors?type=CO2`
 
**Why not path-based** (`GET /api/v1/sensors/type/CO2`):
 
1. **Single resource representation** - `/api/v1/sensors` always represents the sensors collection; query params refine it without creating new resources
2. **Scalability** - Multiple filters: `?type=CO2&status=ACTIVE&roomId=LIB-301` vs unmaintainable path nesting
3. **Optional filtering** - Query params are naturally optional; path structure implies mandatory segments
4. **RESTful convention** - Resources (nouns) in path, filters (queries) in query string
5. **Standards compliance** - GitHub, Twitter, Google use query params for filtering
**Implementation:**
```java
@GET
public Response getAllSensors(@QueryParam("type") String type) {
    if (type != null && !type.isEmpty()) {
        return Response.ok(dataStore.getSensorsByType(type)).build();
    }
    return Response.ok(dataStore.getAllSensors()).build();
}
```

### Question 7: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?

**Pattern implementation:**
```java
// In SensorResource
@Path("{sensorId}/readings")
public SensorReadingResource getSensorReadings(@PathParam("sensorId") String sensorId) {
    return new SensorReadingResource(sensorId);
}
```
 
**Benefits:**
 
| Aspect | Sub-Resources | Monolithic Controller |
|--------|---------------|----------------------|
| Responsibility | One class per resource | All endpoints in one class |
| Methods per file | ~5-10 focused methods | 50+ scattered methods |
| Maintainability | Change one resource only | Full class review required |
| Testing | Isolated unit tests | Coupled integration tests |
| Scalability | ~50 endpoints: manageable | ~500 endpoints: unmaintainable |
| Code readability | Clear, self-documenting | 3000+ line files |
 
**SOLID Principle:** Each class has **single responsibility** → easier to reason about, test, and extend.
 
**Real-world impact:** Enterprise APIs with 500+ endpoints benefit tremendously from sub-resources; monolithic controllers become unmaintainable nightmares.

### Question 8: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

**Scenario:** Client POSTs sensor with nonexistent `roomId`
 
| Status | Semantics | Client Interpretation | Correct? |
|--------|-----------|----------------------|----------|
| 404 | "Resource does not exist" | "Endpoint broken/API changed" | ❌ No |
| 422 | "Request valid but semantic error" | "My input invalid, retry with valid roomId" | ✅ Yes |
 
**Our implementation:**
```java
@POST
public Response createSensor(Sensor sensor) throws LinkedResourceNotFoundException {
    if (!dataStore.roomExists(sensor.getRoomId())) {
        throw new LinkedResourceNotFoundException("Room", sensor.getRoomId());
    }
    // ... creation logic
}
 
// Maps to HTTP 422 via ExceptionMapper
```
 
**Why 422 is correct:**
- **Distinguishes error types:** 404 = "endpoint broken", 422 = "invalid reference"
- **Retryability cue:** 422 signals user input correction needed; 404 suggests API change
- **Monitoring clarity:** Dashboard tracks 422 as validation failures vs 404 as infrastructure issues
- **Industry standard:** GitHub, Stripe, Shopify use 422 for validation errors
**Comparison with other 4xx codes:**
- **400 Bad Request:** Malformed JSON, syntax errors
- **409 Conflict:** Resource exists but state prevents operation (room has sensors)
- **422 Unprocessable Entity:** Request valid but references invalid dependency ✓
- **404 Not Found:** Direct path resource missing, endpoint doesn't exist


### Question 9: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

**Never expose stack traces to clients:**
 
```
java.lang.NullPointerException: Cannot invoke "Sensor.getId()"
  at com.mycompany.smartcampus.resources.SensorResource.updateSensor(SensorResource.java:42)
  at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:27)
```
 
**Security risks:**
1. **Architecture disclosure** - Package names reveal project structure and framework versions
2. **Source code hints** - Line numbers point to vulnerable code locations for targeted attacks
3. **Library enumeration** - Exact versions match known CVEs for exploitation
4. **Tech stack exposure** - Reveals database (PostgreSQL), messaging (RabbitMQ), caching (Redis)
5. **Business logic exposure** - Method names reveal features (processPayment, calculateCommission)
6. **Reconnaissance data** - Attackers map attack surface without direct probing
7. **Credential hints** - File paths expose config locations, potentially leaking API keys

 
**Our approach:**
```java
@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        // Return ONLY generic message to client
        ErrorResponse errorResponse = new ErrorResponse(
            500,
            "Internal Server Error - An unexpected error occurred",  // Generic, safe
            ""
        );
        // Log full stack trace internally
        logger.log(Level.SEVERE, "Unexpected error", exception);
        
        return Response.status(500).entity(errorResponse).build();
    }
}
```
 
**Client sees:** Generic "Internal Server Error" (no stack trace, no package names, no line numbers)


### Question 10: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

**Manual logging (Anti-pattern):**
```java
@GET
public Response getAllSensors(@QueryParam("type") String type) {
    logger.info("GET /sensors, type=" + type);  // Duplicated
    List<Sensor> sensors = /* logic */;
    logger.info("Response: " + sensors.size());  // Duplicated
    return Response.ok(sensors).build();
}
// Repeat for 10+ methods = 20+ duplicated log statements
```
 
**Filter-based logging (Best practice):**
```java
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext req) throws IOException {
        logger.info("Incoming: " + req.getMethod() + " " + req.getUriInfo().getRequestUri());
    }
    
    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
        logger.info("Response status: " + res.getStatus());
    }
}
// ONE filter handles ALL requests globally
```
 
**Key advantages:**
 
| Aspect | Filters | Manual Logging |
|--------|---------|-----------------|
| Code duplication | Zero | 20+ scattered calls |
| Consistency | Guaranteed uniform format | Variable, error-prone |
| Configuration | Single point to modify | 20+ edits scattered |
| Testing | Decoupled from business logic | Coupled, harder to test |
| Maintainability | One file to update | 10+ files to modify |
| Extensibility | Add new filters anytime | Requires every method edit |
| Performance optimization | Conditional filtering easy | Difficult to implement |
 
**Cross-cutting concerns principle:** Logging is a cross-cutting concern (affects all methods). Filters provide clean separation from business logic, following **Aspect-Oriented Programming (AOP)** principles.
