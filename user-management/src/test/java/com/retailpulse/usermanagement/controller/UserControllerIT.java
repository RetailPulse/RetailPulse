package com.retailpulse.usermanagement.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIT {

    private static Long createdUserId;

    @LocalServerPort
    private int port;

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Order(1)
    void shouldCreateUser() {
        String requestBody = """
                {
                    "username": "admin",
                    "password": "password1",
                    "name": "Admin",
                    "email": "admin@retailpulse.com",
                    "roles": ["ADMIN"]
                }
                """;

        createdUserId = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("username", equalTo("admin")).extract().jsonPath().getLong("id");
    }

    @Test
    @Order(2)
    void shouldGetUserById() {
        given()
                .port(port)
                .when()
                .get("/api/users/" + createdUserId)
                .then()
                .statusCode(200)
                .body("username", equalTo("admin"));
    }

    @Test
    @Order(3)
    void shouldGetAllUsers() {
        given()
                .port(port)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @Order(4)
    void shouldReturnNotFoundForNonExistentUser() {
        given()
                .port(port)
                .when()
                .get("/api/users/999")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(5)
    void shouldGetUserByName() {
        given()
                .port(port)
                .when()
                .get("/api/users/search?name=Admin")
                .then()
                .statusCode(200)
                .body("name", equalTo("Admin"));
    }

    @Test
    @Order(6)
    void shouldReturnNotFoundForNonExistentUserByName() {
        given()
                .port(port)
                .when()
                .get("/api/users/search?name=NonExistent")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(7)
    void shouldUpdateUser() {
        String requestBody = """
                {
                    "name": "Admin Updated",
                    "email": "adminupdated@retailpulse.com",
                    "roles": ["ADMIN", "MANAGER", "CASHIER"],
                    "enabled": true
                }
                """;
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/api/users/" + createdUserId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Admin Updated"))
                .body("email", equalTo("adminupdated@retailpulse.com"))
                .body("roles", containsInAnyOrder("ADMIN", "MANAGER", "CASHIER"))
                .body("enabled", equalTo(true));
    }

    @Test
    @Order(8)
    void shouldChangePassword() {
        String requestBody = """
                {
                    "oldPassword": "password1",
                    "newPassword": "password2"
                }
                """;
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch("/api/users/"+ createdUserId +"/change-password")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(9)
    void shouldReturnNotFoundForNonExistentUserToDelete() {
        given()
                .port(port)
                .when()
                .delete("/api/users/999")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(10)
    void shouldDeleteUser() {
        given()
                .port(port)
                .when()
                .delete("/api/users/" + createdUserId)
                .then()
                .statusCode(204);
    }

}
