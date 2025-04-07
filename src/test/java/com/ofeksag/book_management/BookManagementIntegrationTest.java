package com.ofeksag.book_management;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofeksag.book_management.dto.BookRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookManagementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setup() throws Exception {
        adminToken = obtainToken("admin", "pass");
        userToken = obtainToken("user", "pass");
    }

    private String obtainToken(String username, String password) throws Exception {
        String loginPayload = String.format("""
                {
                  "username": "%s",
                  "password": "%s"
                }
                """, username, password);
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("token").asText();
    }

    @Nested
    class AuthTests {

        @Test
        void testLoginSuccessShouldReturnToken() throws Exception {
            String loginJson = """
                    {
                      "username": "admin",
                      "password": "pass"
                    }
                    """;
            MvcResult result = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginJson))
                    .andExpect(status().isOk())
                    .andReturn();
            String responseBody = result.getResponse().getContentAsString();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String token = jsonNode.get("token").asText();
            assertThat(token).isNotBlank();
        }

        @Test
        void testLoginFailureShouldReturnUnauthorized() throws Exception {
            String loginJson = """
                    {
                      "username": "admin",
                      "password": "wrongpass"
                    }
                    """;
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginJson))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testLoginMissingPassword() throws Exception {
            String payload = """
            {
              "username": "admin"
            }
            """;
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class BookRetrievalTests {

        @Test
        void testGetAllBooks() throws Exception {
            mockMvc.perform(get("/api/books")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(1)));

            mockMvc.perform(get("/api/books")
                            .header("Authorization", "Bearer " + userToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(1)));
        }

        @Test
        void testSeededBooksExist() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/books")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andReturn();
            JsonNode jsonResponse = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(jsonResponse.size()).isGreaterThanOrEqualTo(10);
        }
    }

    @Nested
    class BookCreationTests {

        @Test
        void testCreateBookAsAdminSuccess() throws Exception {
            BookRequestDTO bookDto = new BookRequestDTO();
            bookDto.setTitle("Integration Test Book");
            bookDto.setAuthor("Test Author");
            bookDto.setPublishedDate(LocalDate.of(2024, 1, 1));
            bookDto.setIsbn("1234567890123");

            MvcResult result = mockMvc.perform(post("/api/books")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.message").value("Book added successfully."))
                    .andReturn();
            JsonNode jsonResponse = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(jsonResponse.get("id").asLong()).isGreaterThan(0);
        }

        @Test
        void testCreateBookValidationError() throws Exception {
            BookRequestDTO invalidBook = new BookRequestDTO();
            invalidBook.setTitle("Invalid Book");
            invalidBook.setAuthor("Invalid Author");
            invalidBook.setPublishedDate(LocalDate.of(2024, 1, 1));
            MvcResult result = mockMvc.perform(post("/api/books")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidBook)))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            Map<String, String> errors = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
            assertThat(errors.get("isbn")).isEqualTo("isbn is required");
        }

        @Test
        void testCreateBookWithUnrecognizedField() throws Exception {
            String payload = """
                {
                  "title": "Extra Field Book",
                  "author": "Test Author",
                  "publishedDate": "2024-01-01",
                  "isbn": "9876543210123",
                  "unknownField": "unexpected"
                }
                """;
            mockMvc.perform(post("/api/books")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message")
                            .value("Unrecognized one field or more. You should provide just: title, author, publish date, isbn."));
        }

        @Test
        void testCreateBookAsUserShouldFail() throws Exception {
            BookRequestDTO bookDto = new BookRequestDTO();
            bookDto.setTitle("User Book");
            bookDto.setAuthor("Test Author");
            bookDto.setPublishedDate(LocalDate.of(2024, 1, 1));
            bookDto.setIsbn("5555555555555");
            mockMvc.perform(post("/api/books")
                            .header("Authorization", "Bearer " + userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookDto)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testCreateBookWithBlankTitle() throws Exception {
            BookRequestDTO bookDto = new BookRequestDTO();
            bookDto.setTitle("  ");
            bookDto.setAuthor("Valid Author");
            bookDto.setPublishedDate(LocalDate.of(2024, 1, 1));
            bookDto.setIsbn("1234567890123");
            MvcResult result = mockMvc.perform(post("/api/books")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookDto)))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            Map<String, String> errors = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
            assertThat(errors.get("title")).isEqualTo("title cannot be blank");
        }

        @Test
        void testCreateBookWithBlankAuthor() throws Exception {
            BookRequestDTO bookDto = new BookRequestDTO();
            bookDto.setTitle("Valid Title");
            bookDto.setAuthor("  ");
            bookDto.setPublishedDate(LocalDate.of(2024, 1, 1));
            bookDto.setIsbn("1234567890123");
            MvcResult result = mockMvc.perform(post("/api/books")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookDto)))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            Map<String, String> errors = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
            assertThat(errors.get("author")).isEqualTo("author cannot be blank");
        }

        @Test
        void testCreateBookWithNullPublishedDate() throws Exception {
            BookRequestDTO bookDto = new BookRequestDTO();
            bookDto.setTitle("Valid Title");
            bookDto.setAuthor("Valid Author");
            bookDto.setPublishedDate(null);
            bookDto.setIsbn("1234567890123");
            MvcResult result = mockMvc.perform(post("/api/books")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookDto)))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            Map<String, String> errors = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
            assertThat(errors.get("publishedDate")).isEqualTo("publishedDate is required");
        }
    }

    @Nested
    class BookUpdateTests {

        @Test
        void testUpdateBook() throws Exception {
            BookRequestDTO createDto = new BookRequestDTO();
            createDto.setTitle("Book To Update");
            createDto.setAuthor("Original Author");
            createDto.setPublishedDate(LocalDate.of(2024, 1, 1));
            createDto.setIsbn("1111111111111");
            MvcResult createResult = mockMvc.perform(post("/api/books")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isCreated())
                    .andReturn();
            JsonNode createdJson = objectMapper.readTree(createResult.getResponse().getContentAsString());
            long bookId = createdJson.get("id").asLong();
            assertThat(bookId).isGreaterThan(0);
            BookRequestDTO updateDto = new BookRequestDTO();
            updateDto.setTitle("Updated Title");
            updateDto.setAuthor("Updated Author");
            updateDto.setPublishedDate(LocalDate.of(2025, 1, 1));
            updateDto.setIsbn("1111111111111");
            mockMvc.perform(put("/api/books/" + bookId)
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Book updated successfully."));
            mockMvc.perform(put("/api/books/" + bookId)
                            .header("Authorization", "Bearer " + userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testUpdateNonExistingBook() throws Exception {
            long nonExistingId = 999999L;
            BookRequestDTO updateDto = new BookRequestDTO();
            updateDto.setTitle("Non Existing");
            updateDto.setAuthor("No Author");
            updateDto.setPublishedDate(LocalDate.of(2025, 1, 1));
            updateDto.setIsbn("3333333333333");
            mockMvc.perform(put("/api/books/" + nonExistingId)
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class BookDeletionTests {

        @Test
        void testDeleteBook() throws Exception {
            BookRequestDTO bookDto = new BookRequestDTO();
            bookDto.setTitle("Book To Delete");
            bookDto.setAuthor("Author");
            bookDto.setPublishedDate(LocalDate.of(2024, 1, 1));
            bookDto.setIsbn("2222222222222");
            MvcResult createResult = mockMvc.perform(post("/api/books")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookDto)))
                    .andExpect(status().isCreated())
                    .andReturn();
            JsonNode createdJson = objectMapper.readTree(createResult.getResponse().getContentAsString());
            long bookId = createdJson.get("id").asLong();
            assertThat(bookId).isGreaterThan(0);
            mockMvc.perform(delete("/api/books/" + bookId)
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk());
            mockMvc.perform(delete("/api/books/" + bookId)
                            .header("Authorization", "Bearer " + userToken))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class SecurityTests {

        @Test
        void testAccessWithoutToken() throws Exception {
            mockMvc.perform(get("/api/books"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testAccessWithInvalidToken() throws Exception {
            String invalidToken = "Bearer invalid.token.value";
            mockMvc.perform(get("/api/books")
                            .header("Authorization", invalidToken))
                    .andExpect(status().isUnauthorized());
        }
    }
}