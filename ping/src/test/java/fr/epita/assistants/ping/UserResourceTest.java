package fr.epita.assistants.ping;

import fr.epita.assistants.ping.data.model.UserModel;
import fr.epita.assistants.ping.data.repository.UserRepository;
import fr.epita.assistants.ping.utils.Logger;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserResourceTest {

    @Inject
    UserRepository userRepository;

    @BeforeAll
    public void startTests() {
        Logger.info("--------------------------------");
        Logger.info("Début de la suite de tests pour UserResource");
        Logger.info("--------------------------------");
    }

    @AfterAll
    public void endTests() {
        Logger.info("--------------------------------");
        Logger.info("Fin de la suite de tests pour UserResource");
        Logger.info("--------------------------------");
    }

    @BeforeEach
    @Transactional
    public void setup() {
        userRepository.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"khoren.pasdrmadjian", "martin.lemetais", "dardan.bytyqi", "hugo.viala", "nathan.fontaine"})
    @TestSecurity(user = "testUser", roles = "admin")
    public void testCreateUserEndpoint_Valid(String login) {
        Logger.info("Test de création valide pour : " + login);
        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"supersecret\", \"isAdmin\": false}", login);
        String expectedDisplayName = toDisplayName(login);

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/user")
        .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("login", equalTo(login))
            .body("displayName", equalTo(expectedDisplayName))
            .body("isAdmin", equalTo(false))
            .body("avatar", equalTo(null));

        UserModel user = userRepository.findByLogin(login);
        Assertions.assertNotNull(user, "L'utilisateur '" + login + "' aurait dû être créé en BDD.");
        Logger.success("Création valide réussie pour : " + login);
    }

    @Test
    @TestSecurity(user = "testUser", roles = "admin")
    public void testCreateUserEndpoint_AlreadyExists() {
        Logger.info("Test de création d'un utilisateur qui existe déjà");
        String login = "test.user";
        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"secret\", \"isAdmin\": false}", login);

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/api/user").then().statusCode(200);

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/api/user")
            .then().statusCode(409);
        Logger.success("Le test de conflit (409) a réussi.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"jo.hn.doe", "khoren p", "invalid"})
    @TestSecurity(user = "testUser", roles = "admin")
    public void testCreateUserEndpoint_InvalidFormat(String login) {
        Logger.info("Test de création avec format de login invalide : " + login);
        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"supersecret\", \"isAdmin\": false}", login);
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/user")
        .then()
            .statusCode(400);
        Logger.success("Le test de format invalide (400) a réussi pour : " + login);
    }

    @Test
    @TestSecurity(user = "testUser", roles = "user")
    public void testCreateUserEndpoint_NotAdmin() {
        Logger.info("Test de création par un utilisateur non-admin");
        String requestBody = "{\"login\": \"some.user\", \"password\": \"supersecret\", \"isAdmin\": false}";
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/user")
        .then()
            .statusCode(403);
        Logger.success("Le test de droits insuffisants (403) a réussi.");
    }

    @Test
    @TestSecurity(user = "testUser", roles = "admin")
    public void testAvatarNullCreateUser() {
        Logger.info("Test de création d'un utilisateur avec un avatar null");
        String requestBody = "{\"login\": \"some.user\", \"password\": \"supersecret\", \"isAdmin\": false, \"avatar\": null}";
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/user")
        .then()
            .statusCode(200);
        Logger.success("Le test de création d'un utilisateur avec un avatar null a bien renvoyé une erreur.");
    }

    @Test
    @Transactional
    @TestSecurity(user = "testUser", roles = "admin")
    public void testGetUserEndpoint_Valid() {
        // Il faut ajouter un user dans la base de données en utilisant l'endpoint
        String requestBody = "{\"login\": \"some.user\", \"password\": \"supersecret\", \"isAdmin\": false}";
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/user")
        .then()
            .statusCode(200);
        
        UserModel user = userRepository.findByLogin("some.user");
        Assertions.assertNotNull(user, "L'utilisateur '" + "some.user" + "' aurait dû être créé en BDD.");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/user/{id}", user.getId())
        .then()
            .statusCode(200)
            .body("id", equalTo(user.getId().toString()))
            .body("login", equalTo(user.getLogin()))
            .body("displayName", equalTo("Some User"))
            .body("isAdmin", equalTo(false))
            .body("avatar", equalTo(null));

        Logger.success("Le test de récupération d'un utilisateur valide a réussi.");

    }

    private String toDisplayName(String login) {
        String[] parts = login.split("[._-]");
        StringBuilder displayName = new StringBuilder();
        for (String part : parts) {
            displayName.append(Character.toUpperCase(part.charAt(0)))
                       .append(part.substring(1))
                       .append(" ");
        }
        return displayName.toString().trim();
    }
} 