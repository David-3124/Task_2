package helpers;

import config.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.CreateUser;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserHelper {

    @Step("Создание пользователя с проверкой успеха")
    public static CreateUser createNewUser(String email, String password, String name) {
        CreateUser user = new CreateUser(email, password, name);
        Response response = createUserRequest(user);
        response.then().statusCode(200).body("success", equalTo(true));
        return user;
    }

    @Step("Запроса на создание нового пользователя")
    public static Response createUserRequest(CreateUser user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(Endpoints.CREATE_USER_ENDPOINT);
    }
}
