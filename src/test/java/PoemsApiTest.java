

import app.config.HibernateConfig;
import app.daos.PoemDAO;
import app.dtos.PoemDTO;
import app.enums.PoemType;
import app.routes.PoemRoutes;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PoemsApiTest {

    private static Javalin app;
    private static PoemDAO poemDAO;

    @BeforeAll
    static void beforeAll() {
        // Sæt Hibernate til at køre med testcontainers (se HibernateConfig)
        HibernateConfig.setTest(true);

        // Start Javalin på en tilfældig port
        app = Javalin.create().start(0);
        PoemRoutes.addRoutes(app);

        // Konfigurer RestAssured baseURI til at ramme den port
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = app.port();

        // DAO til at rydde/populere DB i @BeforeEach
        poemDAO = PoemDAO.getInstance(HibernateConfig.getEntityManagerFactoryForTest());
    }

    @AfterAll
    static void afterAll() {
        app.stop();
    }

    @BeforeEach
    void setupData() {
        // Ryd op før hver test
        poemDAO.deleteAll();

        // Opret en poem så vi altid har mindst 1 i databasen
        poemDAO.create(new PoemDTO(0, "Morning Haiku", "Soft winds rise", "Matsuo Bashō", PoemType.HAIKU));
    }

    // ----------- TESTS -----------

    @Test
    void testGetAllPoems() {
        when()
                .get("/poems")
                .then()
                .statusCode(200)
                .body("size()", is(1)) // vi har præcis 1 i @BeforeEach
                .body("[0].title", equalTo("Morning Haiku"));
    }

    @Test
    void testGetPoemById() {
        // Vi ved første poem altid får id = 1 i ny database
        when()
                .get("/poems/1")
                .then()
                .statusCode(200)
                .body("author", equalTo("Matsuo Bashō"));
    }

    @Test
    void testCreatePoem() {
        given()
                .contentType("application/json")
                .body(new PoemDTO(0, "Love Sonnet", "Shall I compare thee...", "Shakespeare", PoemType.SONNET))
                .when()
                .post("/poems")
                .then()
                .statusCode(201)
                .body("title", equalTo("Love Sonnet"))
                .body("id", greaterThan(0));
    }

    @Test
    void testUpdatePoem() {
        PoemDTO update = new PoemDTO(0, "Updated Title", "New text", "New Author", PoemType.TANKA);

        given()
                .contentType("application/json")
                .body(update)
                .when()
                .put("/poems/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Title"))
                .body("author", equalTo("New Author"));
    }

    @Test
    void testDeletePoem() {
        when()
                .delete("/poems/1")
                .then()
                .statusCode(204);

        // Skal give 404 efter delete
        when()
                .get("/poems/1")
                .then()
                .statusCode(404);
    }
}

