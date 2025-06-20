package fr.epita.assistants.ping;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ProjectResourceTest {

    @Test
    public void testGetProjectsEndpoint_NotAuthenticated() {
        given()
          .when().get("/api/projects")
          .then()
             .statusCode(401);
    }
} 