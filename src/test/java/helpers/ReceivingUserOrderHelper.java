package helpers;

import config.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ReceivingUserOrderHelper {

    @Step("Получение заказов пользователя с авторизацией")
    public static Response receivingUserOrder(String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get(Endpoints.CREATE_ORDER_ENDPOINT)
                .then()
                .extract().response();
    }

    @Step("Получение заказов пользователя без авторизации")
    public static Response receivingUserOrderWithoutAuth() {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(Endpoints.CREATE_ORDER_ENDPOINT)
                .then()
                .extract().response();
    }
}
