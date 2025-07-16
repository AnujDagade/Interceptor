package com.interceptor.interceptor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class ResponseInterceptor implements Filter {

    private static final String RESPONSE_FILE_PATH = "response-content.txt";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Create a response wrapper to capture the original response
        ResponseWrapper wrapper = new ResponseWrapper(httpResponse);
        
        // Continue with the original request processing
        chain.doFilter(request, wrapper);
        
        // Read content from file
        String fileContent = readFileContent();
        
        // Set the file content as response body
        httpResponse.setContentType("text/plain");
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.getWriter().write(fileContent);
        httpResponse.getWriter().flush();
    }

    private String readFileContent() {
        try {
            ClassPathResource resource = new ClassPathResource(RESPONSE_FILE_PATH);
            if (resource.exists()) {
                try (InputStream inputStream = resource.getInputStream()) {
                    return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                }
            } else {
                return "Default response: File not found at " + RESPONSE_FILE_PATH;
            }
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    // Custom response wrapper to capture response content
    private static class ResponseWrapper extends HttpServletResponseWrapper {
        private StringWriter stringWriter;
        private PrintWriter writer;

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
            stringWriter = new StringWriter();
            writer = new PrintWriter(stringWriter);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return writer;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                @Override
                public void write(int b) throws IOException {
                    writer.write(b);
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                }
            };
        }

        @Override
        public void setContentLength(int len) {
            // Don't set content length as we're replacing the content
        }

        public String getContent() {
            writer.flush();
            return stringWriter.toString();
        }
    }
}