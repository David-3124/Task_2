package helpers;

import config.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.CreateOrder;

import static io.restassured.RestAssured.given;

public class CreateOrderHelper {

    @Step("Создание заказа с авторизацией")
    public static Response createOrderWithAuthorization(CreateOrder orderData, String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(orderData)
                .when()
                .post(Endpoints.CREATE_ORDER_ENDPOINT);
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrderWithoutAuthorization(CreateOrder orderData) {
        return given()
                .header("Content-Type", "application/json")
                .body(orderData)
                .when()
                .post(Endpoints.CREATE_ORDER_ENDPOINT);
    }
}
