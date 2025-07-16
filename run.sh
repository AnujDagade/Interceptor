#!/bin/bash

# Build and run the Interceptor application

echo "Building the application..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo "Build successful! Starting the application..."
    echo "Access the application at: http://localhost:8080"
    echo "Test endpoints:"
    echo "  - http://localhost:8080/api/test"
    echo "  - http://localhost:8080/api/hello"
    echo ""
    echo "Press Ctrl+C to stop the application"
    echo ""
    mvn spring-boot:run
else
    echo "Build failed! Please check the errors above."
    exit 1
fi