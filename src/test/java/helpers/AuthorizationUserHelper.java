package helpers;

import config.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.AuthorizationUser;

import static io.restassured.RestAssured.given;

public class AuthorizationUserHelper {

    @Step("Получение токена доступа")
    public static String getAccessToken(AuthorizationUser authorization) {

        Response response = given()
                .header("Content-Type", "application/json")
                .body(authorization)
                .when()
                .post(Endpoints.LOGIN_ENDPOINT);

        if (response.statusCode() == 200) {
            return response.path("accessToken");
        }
        return null;
    }
}
