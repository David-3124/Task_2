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

public class ReceivingUserOrderTest extends BaseUrl {
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
    @DisplayName("Получение заказов конкретного пользователя, авторизованный пользователь")
    public void getOrderWithAuthorization() {
        AuthorizationUser auth = new AuthorizationUser(email, password);
        accessToken = AuthorizationUserHelper.getAccessToken(auth);

        Response ingredientsResponse = GetIngredientsHelper.getIngredients();
        List<String> allIngredientIds = ingredientsResponse.path("data._id");

        CreateOrder order = new CreateOrder(allIngredientIds);
        CreateOrderHelper.createOrderWithAuthorization(order, accessToken);

        Response response = ReceivingUserOrderHelper.receivingUserOrder(accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя, неавторизованный пользователь")
    public void getOrderWithoutAuthorization() {
        Response ingredientsResponse = GetIngredientsHelper.getIngredients();
        List<String> allIngredientIds = ingredientsResponse.path("data._id");

        CreateOrder order = new CreateOrder(allIngredientIds);
        CreateOrderHelper.createOrderWithoutAuthorization(order);

        Response response = ReceivingUserOrderHelper.receivingUserOrderWithoutAuth();
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @AfterEach
    @Step("Очистка данных после выполнения тест кейса")
    public void cleaningData() {
        if (accessToken != null) {
            DeleteUserHelper.deleteUser(accessToken);
        }
    }
}
