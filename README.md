# Interceptor

A Java Spring Boot application that intercepts HTTP requests from the backend and replaces response bodies with content from a file.

## Features

- **Request Interception**: Intercepts all HTTP requests to the application
- **Response Replacement**: Replaces the original response body with content from a configurable file
- **File-based Content**: Reads response content from `src/main/resources/response-content.txt`
- **Automatic Content Type**: Sets response content type to `text/plain` with UTF-8 encoding

## Prerequisites

- Java 11 or higher
- Maven 3.6+

## How to Build

```bash
mvn clean compile
```

## How to Run

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## How to Test

Run the included unit tests:

```bash
mvn test
```

## Test Endpoints

The application includes test endpoints to verify the interceptor functionality:

- `GET /api/test` - Returns original response: "Original response from controller"
- `GET /api/hello` - Returns original response: "Hello from the backend!"

Both endpoints will return the content from the `response-content.txt` file instead of their original responses.

## Configuration

### Response Content File

Edit `src/main/resources/response-content.txt` to change what gets returned by the interceptor:

```
This is the intercepted response content from file!

The original response has been replaced with this content from the file.
You can modify this file to change what gets returned by the interceptor.
```

### Application Properties

Configure the application through `src/main/resources/application.properties`:

```properties
server.port=8080
logging.level.com.interceptor=DEBUG
```

## How It Works

1. **Filter Registration**: The `ResponseInterceptor` class implements `javax.servlet.Filter` and is registered to intercept all requests (`/*`)
2. **Request Processing**: When a request comes in, the filter captures the original response using a custom `ResponseWrapper`
3. **Content Replacement**: After the original request is processed, the filter reads content from the configured file
4. **Response Modification**: The original response body is replaced with the file content
5. **Content Delivery**: The modified response is sent back to the client

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/interceptor/
│   │       ├── InterceptorApplication.java      # Main Spring Boot application
│   │       ├── config/
│   │       │   └── InterceptorConfig.java       # Filter configuration
│   │       ├── controller/
│   │       │   └── TestController.java          # Test endpoints
│   │       └── interceptor/
│   │           └── ResponseInterceptor.java     # Main interceptor logic
│   └── resources/
│       ├── application.properties               # Application configuration
│       └── response-content.txt                 # Response content file
└── test/
    └── java/
        └── com/interceptor/
            └── ResponseInterceptorTest.java      # Unit tests
```

## Example Usage

1. Start the application:
   ```bash
   mvn spring-boot:run
   ```

2. Test the interceptor:
   ```bash
   curl http://localhost:8080/api/test
   ```

3. Expected output:
   ```
   This is the intercepted response content from file!

   The original response has been replaced with this content from the file.
   You can modify this file to change what gets returned by the interceptor.
   ```

4. Modify the response content:
   ```bash
   echo "New intercepted response!" > src/main/resources/response-content.txt
   ```

5. Test again to see the updated response:
   ```bash
   curl http://localhost:8080/api/test
   ```

## Notes

- The interceptor only affects successful responses (HTTP 200). Error responses (404, 500, etc.) are not intercepted
- The response content type is automatically set to `text/plain` with UTF-8 encoding
- Content length is automatically calculated based on the file content
- The interceptor applies to all endpoints in the application