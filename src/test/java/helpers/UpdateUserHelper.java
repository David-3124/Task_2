package helpers;

import Config.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.GetAndUpdateInfoUser;

import static io.restassured.RestAssured.given;

public class UpdateUserHelper {

    @Step("Обновление данных пользователя с авторизацией")
    public static Response updateUserRequest(GetAndUpdateInfoUser userData, String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(userData)
                .when()
                .patch(Endpoints.USER_ENDPOINT);
    }

    @Step("Обновление данных пользователя без авторизации")
    public static Response updateUserRequestWithoutAuth(GetAndUpdateInfoUser userData) {
        return given()
                .header("Content-Type", "application/json")
                .body(userData)
                .when()
                .patch(Endpoints.USER_ENDPOINT);
    }
}
