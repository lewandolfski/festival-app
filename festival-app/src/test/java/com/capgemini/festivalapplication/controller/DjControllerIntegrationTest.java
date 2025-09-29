package com.capgemini.festivalapplication.controller;

import com.capgemini.festivalapplication.dto.DjDto;
import com.capgemini.festivalapplication.entity.Dj;
import com.capgemini.festivalapplication.repository.DjRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for DjController.
 * Tests all REST endpoints, HTTP methods, exception handling, and JSON content generation.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class DjControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private DjRepository djRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Dj testDj;
    private DjDto testDjDto;

    @BeforeEach
    void setUp() {
        // Setup MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Clear database before each test
        djRepository.deleteAll();
        
        // Create test data
        testDj = new Dj("Test DJ", "Electronic", "testdj@example.com");
        testDj = djRepository.save(testDj);
        
        testDjDto = new DjDto();
        testDjDto.setName("New DJ");
        testDjDto.setGenre("House");
        testDjDto.setEmail("newdj@example.com");
    }

    /**
     * Test GET /api/djs - Get all DJs
     * Verifies: HTTP 200, JSON array response, correct content
     */
    @Test
    void getAllDjs_ShouldReturnAllDjs_WithHttp200() throws Exception {
        mockMvc.perform(get("/api/djs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testDj.getId())))
                .andExpect(jsonPath("$[0].name", is("Test DJ")))
                .andExpect(jsonPath("$[0].genre", is("Electronic")))
                .andExpect(jsonPath("$[0].email", is("testdj@example.com")));
    }

    /**
     * Test GET /api/djs/{id} - Get DJ by ID
     * Verifies: HTTP 200, correct JSON content
     */
    @Test
    void getDjById_WithValidId_ShouldReturnDj_WithHttp200() throws Exception {
        mockMvc.perform(get("/api/djs/{id}", testDj.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testDj.getId())))
                .andExpect(jsonPath("$.name", is("Test DJ")))
                .andExpect(jsonPath("$.genre", is("Electronic")))
                .andExpect(jsonPath("$.email", is("testdj@example.com")));
    }

    /**
     * Test GET /api/djs/{id} - Get DJ by non-existing ID
     * Verifies: HTTP 404, proper error response
     */
    @Test
    void getDjById_WithNonExistingId_ShouldReturnHttp404() throws Exception {
        mockMvc.perform(get("/api/djs/{id}", "non-existing-id"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("not found")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Test POST /api/djs - Create new DJ
     * Verifies: HTTP 201, correct JSON response, database persistence
     */
    @Test
    @Transactional
    void createDj_WithValidData_ShouldCreateDj_WithHttp201() throws Exception {
        String djJson = objectMapper.writeValueAsString(testDjDto);

        MvcResult result = mockMvc.perform(post("/api/djs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(djJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("New DJ")))
                .andExpect(jsonPath("$.genre", is("House")))
                .andExpect(jsonPath("$.email", is("newdj@example.com")))
                .andReturn();

        // Verify database persistence
        String responseContent = result.getResponse().getContentAsString();
        Dj createdDj = objectMapper.readValue(responseContent, Dj.class);
        
        assertTrue(djRepository.existsById(createdDj.getId()));
        assertEquals(2, djRepository.count()); // Original + new DJ
    }

    /**
     * Test POST /api/djs - Create DJ with invalid data
     * Verifies: HTTP 400, validation error response
     */
    @Test
    void createDj_WithInvalidData_ShouldReturnHttp400() throws Exception {
        DjDto invalidDto = new DjDto();
        invalidDto.setName(""); // Empty name
        invalidDto.setGenre(""); // Empty genre
        invalidDto.setEmail("invalid-email"); // Invalid email format

        String djJson = objectMapper.writeValueAsString(invalidDto);

        mockMvc.perform(post("/api/djs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(djJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Validation failed")))
                .andExpect(jsonPath("$.validationErrors", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        // Verify no DJ was created
        assertEquals(1, djRepository.count()); // Only the original test DJ
    }

    /**
     * Test PUT /api/djs/{id} - Update existing DJ
     * Verifies: HTTP 200, correct JSON response, database update
     */
    @Test
    @Transactional
    void updateDj_WithValidData_ShouldUpdateDj_WithHttp200() throws Exception {
        DjDto updateDto = new DjDto();
        updateDto.setName("Updated DJ");
        updateDto.setGenre("Techno");
        updateDto.setEmail("updated@example.com");

        String djJson = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put("/api/djs/{id}", testDj.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(djJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testDj.getId())))
                .andExpect(jsonPath("$.name", is("Updated DJ")))
                .andExpect(jsonPath("$.genre", is("Techno")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));

        // Verify database update
        Dj updatedDj = djRepository.findById(testDj.getId()).orElseThrow();
        assertEquals("Updated DJ", updatedDj.getName());
        assertEquals("Techno", updatedDj.getGenre());
        assertEquals("updated@example.com", updatedDj.getEmail());
    }

    /**
     * Test PUT /api/djs/{id} - Update non-existing DJ
     * Verifies: HTTP 400, proper error response, no new record created
     */
    @Test
    void updateDj_WithNonExistingId_ShouldReturnHttp400() throws Exception {
        String djJson = objectMapper.writeValueAsString(testDjDto);

        mockMvc.perform(put("/api/djs/{id}", "non-existing-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(djJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Cannot update non-existing DJ")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        // Verify no new record was created
        assertEquals(1, djRepository.count()); // Only the original test DJ
    }

    /**
     * Test DELETE /api/djs/{id} - Delete existing DJ
     * Verifies: HTTP 204, database deletion
     */
    @Test
    @Transactional
    void deleteDj_WithValidId_ShouldDeleteDj_WithHttp204() throws Exception {
        mockMvc.perform(delete("/api/djs/{id}", testDj.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify database deletion
        assertFalse(djRepository.existsById(testDj.getId()));
        assertEquals(0, djRepository.count());
    }

    /**
     * Test DELETE /api/djs/{id} - Delete non-existing DJ
     * Verifies: HTTP 404, proper error response
     */
    @Test
    void deleteDj_WithNonExistingId_ShouldReturnHttp404() throws Exception {
        mockMvc.perform(delete("/api/djs/{id}", "non-existing-id"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("not found")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        // Verify original DJ still exists
        assertTrue(djRepository.existsById(testDj.getId()));
        assertEquals(1, djRepository.count());
    }

    /**
     * Test GET /api/djs/genre/{genre} - Search DJs by genre
     * Verifies: HTTP 200, correct filtering
     */
    @Test
    void getDjsByGenre_ShouldReturnMatchingDjs_WithHttp200() throws Exception {
        // Create additional DJ with different genre
        Dj houseDj = new Dj("House DJ", "House", "house@example.com");
        djRepository.save(houseDj);

        mockMvc.perform(get("/api/djs/genre/{genre}", "Electronic"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].genre", is("Electronic")));
    }

    /**
     * Test GET /api/djs/name/{name} - Search DJs by name
     * Verifies: HTTP 200, correct filtering
     */
    @Test
    void getDjsByName_ShouldReturnMatchingDjs_WithHttp200() throws Exception {
        mockMvc.perform(get("/api/djs/name/{name}", "Test DJ"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test DJ")));
    }

    /**
     * Test GET /api/djs/long-names - Get DJs with long names
     * Verifies: HTTP 200, correct filtering logic
     */
    @Test
    void getDjsWithLongNames_ShouldReturnDjsWithLongNames_WithHttp200() throws Exception {
        // Create DJ with long name (>6 characters)
        Dj longNameDj = new Dj("Very Long DJ Name", "Trance", "long@example.com");
        djRepository.save(longNameDj);

        mockMvc.perform(get("/api/djs/long-names"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Very Long DJ Name")));
    }

    /**
     * Test malformed JSON request
     * Verifies: HTTP 400, proper error handling
     */
    @Test
    void createDj_WithMalformedJson_ShouldReturnHttp400() throws Exception {
        String malformedJson = "{ \"name\": \"Test\", \"genre\": }"; // Invalid JSON

        mockMvc.perform(post("/api/djs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * Test unsupported HTTP method
     * Verifies: HTTP 405 Method Not Allowed
     */
    @Test
    void unsupportedHttpMethod_ShouldReturnHttp405() throws Exception {
        mockMvc.perform(patch("/api/djs"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }
}
