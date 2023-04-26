package io.quarkiverse.xxl.job.it;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class XxlJobResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/xxl-job")
                .then()
                .statusCode(200)
                .body(is("Hello xxl-job"));
    }
}
