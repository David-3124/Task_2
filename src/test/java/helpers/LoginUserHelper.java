package helpers;

import config.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.LoginUser;

import static io.restassured.RestAssured.given;

public class LoginUserHelper {

    @Step("Авторизация пользователя")
    public static Response loginUserRequest(LoginUser loginUser) {
        return given()
                .header("Content-type", "application/json")
                .body(loginUser)
                .when()
                .post(Endpoints.LOGIN_ENDPOINT);
    }
}
