import Config.BaseUrl;
import helpers.*;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.AuthorizationUser;
import utils.CreateOrder;

import java.util.List;
import java.util.Random;

import static helpers.CreateUserHelper.createNewUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest extends BaseUrl {
    private String email;
    private String password;
    private String name;
    private String accessToken;

    @BeforeEach
    @Step("Подготовка рандомных тестовых данных")
    public void generateTestData() {
        int random = new Random().nextInt(10000);
        email = "Email_" + random + "@yandex.ru";
        password = "Pass_" + random;
        name = "Name_" + random;

        createNewUser(email, password, name);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuthorization() {
        AuthorizationUser auth = new AuthorizationUser(email, password);
        accessToken = AuthorizationUserHelper.getAccessToken(auth);

        Response ingredientsResponse = GetIngredientsHelper.getIngredients();
        List<String> allIngredientIds = ingredientsResponse.path("data._id");

        CreateOrder order = new CreateOrder(allIngredientIds);
        Response response = CreateOrderHelper.createOrderWithAuthorization(order, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthorization() {
        Response ingredientsResponse = GetIngredientsHelper.getIngredients();
        List<String> allIngredientIds = ingredientsResponse.path("data._id");

        CreateOrder order = new CreateOrder(allIngredientIds);
        Response response = CreateOrderHelper.createOrderWithoutAuthorization(order);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов, Ошибка 400")
    public void createOrderWithoutIngredient() {
        AuthorizationUser auth = new AuthorizationUser(email, password);
        accessToken = AuthorizationUserHelper.getAccessToken(auth);

        List<String> allIngredientIds = null;

        CreateOrder order = new CreateOrder(allIngredientIds);
        Response response = CreateOrderHelper.createOrderWithAuthorization(order, accessToken);
        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов, Ошибка 500")
    public void createOrderIncorrectHash() {
        AuthorizationUser auth = new AuthorizationUser(email, password);
        accessToken = AuthorizationUserHelper.getAccessToken(auth);

        List<String> allIngredientIds = List.of("sdf42gsfdg34", "sdhfgh543653464");

        CreateOrder order = new CreateOrder(allIngredientIds);
        Response response = CreateOrderHelper.createOrderWithAuthorization(order, accessToken);
        response.then()
                .statusCode(500);
    }

    @AfterEach
    @Step("Очистка данных после выполнения тест кейса")
    public void cleaningData() {
        if (accessToken != null) {
            DeleteUserHelper.deleteUser(accessToken);
        }
    }
}
