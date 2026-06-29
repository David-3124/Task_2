package helpers;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseUrl {

    @BeforeAll
    static void setUpBaseUri() {
        RestAssured.baseURI = EndpointHelper.PAGE_URL;
    }
}
