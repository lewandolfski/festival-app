package com.capgemini.festivalapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WelcomeController {
    
    @GetMapping("/")
    @ResponseBody
    public String welcome() {
        return """
            <html>
            <head><title>Festival Application</title></head>
            <body>
                <h1>🎵 Festival Application Backend</h1>
                <h2>Available Endpoints:</h2>
                <ul>
                    <li><a href="/api/djs">GET /api/djs</a> - List all DJs</li>
                    <li><a href="/api/djs/name/Calvin%20Harris">GET /api/djs/name/{name}</a> - Find DJ by name</li>
                    <li><a href="/api/djs/long-names">GET /api/djs/long-names</a> - DJs with names longer than 6 characters</li>
                    <li><a href="/api/performances">GET /api/performances</a> - List all performances</li>
                    <li><a href="/h2-console">H2 Database Console</a> (JDBC URL: jdbc:h2:mem:testdb, User: sa, Password: empty)</li>
                    <li><a href="/actuator/health">Health Check</a></li>
                    <li><a href="/actuator/info">Info</a></li>
                </ul>
                <p><strong>Festival Application - Exercises 3 & 4 Complete!</strong></p>
                <p>✅ REST API with Exception Handling | ✅ Integration Testing</p>
            </body>
            </html>
            """;
    }
}
