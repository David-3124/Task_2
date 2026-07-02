package helpers;

import Config.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class GetIngredientsHelper {

    @Step("Получение ингредиентов")
    public static Response getIngredients() {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(Endpoints.GET_INGREDIENTS_ENDPOINT)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract().response();
    }
}
