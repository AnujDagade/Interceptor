package com.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResponseInterceptorTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testResponseInterception() {
        String url = "http://localhost:" + port + "/api/test";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        System.out.println("Response body: '" + response.getBody() + "'");
        System.out.println("Response length: " + (response.getBody() != null ? response.getBody().length() : "null"));
        
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("This is the intercepted response"));
        assertFalse(response.getBody().contains("Original response from controller"));
    }

    @Test
    public void testHelloEndpointInterception() {
        String url = "http://localhost:" + port + "/api/hello";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        System.out.println("Hello response body: '" + response.getBody() + "'");
        System.out.println("Hello response length: " + (response.getBody() != null ? response.getBody().length() : "null"));
        
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("This is the intercepted response"));
        assertFalse(response.getBody().contains("Hello from the backend!"));
    }
}