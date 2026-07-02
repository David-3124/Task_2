package helpers;

import Config.Endpoints;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class DeleteUserHelper {

    @Step("Удаление пользователя")
    public static void deleteUser(String accessToken) {

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .delete(Endpoints.USER_ENDPOINT)
                .then()
                .statusCode(202);
        System.out.println("Пользователь удалён. Токен: " + accessToken);
    }
}
