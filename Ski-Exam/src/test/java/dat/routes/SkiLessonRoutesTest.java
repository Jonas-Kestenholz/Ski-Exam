package dat.routes;

import dat.config.HibernateConfig;
import dat.utils.Populator;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SkiLessonRoutesTest {

    private Javalin app;

    @BeforeAll
    void setup() {
        app = Javalin.create(config -> config.router.apiBuilder(() -> {
            path("/skilessons", new SkiLessonRoutes().getRoutes());
        })).start(7070);

        RestAssured.baseURI = "http://localhost:" + app.port();
    }

    @BeforeEach
    void populate() {
        Populator.populateSkiLessons(HibernateConfig.getEntityManagerFactory());
    }

    @AfterAll
    void tearDown() {
        app.stop();
    }

    @Test
    @Order(1)
    @DisplayName("GET: Henter alle ski-lektioner")
    void testGetAllLessons() {
        given()
                .when()
                .get("/skilessons")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    @Order(2)
    @DisplayName("GET: Henter én lektion ud fra ID")
    void testGetLessonById() {
        given()
                .when()
                .get("/skilessons/1")
                .then()
                .statusCode(200)
                .body("skiLesson.id", equalTo(1));
    }

    @Test
    @Order(3)
    @DisplayName("POST: Opretter en ny lektion uden auth")
    void testCreateLessonWithoutAuth() {
        String json = """
        {
          "name": "TestLesson",
          "starttime": "2025-01-15T10:00:00",
          "endtime": "2025-01-15T12:00:00",
          "latitude": 45.0,
          "longitude": 10.0,
          "price": 350.0,
          "level": "BEGINNER"
        }
        """;

        given()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("/skilessons")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("name", equalTo("TestLesson"));
    }

    @Test
    @Order(4)
    @DisplayName("PUT: Opdaterer en lektion uden auth")
    void testUpdateLessonWithoutAuth() {
        String json = """
        {
          "name": "UpdatedLesson",
          "starttime": "2025-01-15T08:00:00",
          "endtime": "2025-01-15T10:00:00",
          "latitude": 45.0,
          "longitude": 10.0,
          "price": 400.0,
          "level": "INTERMEDIATE"
        }
        """;

        given()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .put("/skilessons/1")
                .then()
                .statusCode(anyOf(is(200), is(204)))
                .body("name", equalTo("UpdatedLesson"));
    }

    @Test
    @Order(5)
    @DisplayName("DELETE: Sletter en lektion uden auth")
    void testDeleteLessonWithoutAuth() {
        given()
                .when()
                .delete("/skilessons/1")
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }
}
