import config.BaseUrl;
import helpers.AuthorizationUserHelper;
import helpers.DeleteUserHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.AuthorizationUser;
import utils.CreateUser;

import java.util.Random;

import static helpers.CreateUserHelper.createUserRequest;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest extends BaseUrl {

    private String email;
    private String password;
    private String name;

    @BeforeEach
    @Step("Подготовка рандомных тестовых данных")
    public void generateTestData() {
        int random = new Random().nextInt(100);
        email = "Email_" + random + "@yandex.ru";
        password = "Pass_" + random;
        name = "Name_" + random;
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUser() {
        CreateUser user = new CreateUser(email, password, name);
        Response response = createUserRequest(user);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя который уже зарегистрирован, Ошибка 403")
    public void creatingIdenticalUser() {
        CreateUser userOne = new CreateUser(email, password, name);
        Response responseOne = createUserRequest(userOne);
        responseOne.then()
                .statusCode(200)
                .body("success", equalTo(true));

        CreateUser userTwo = new CreateUser(email, password, name);
        Response responseTwo = createUserRequest(userTwo);
        responseTwo.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя не заполняя обязательный параметр, Ошибка 403")
    public void CreateUserNotInsufficientData() {
        CreateUser userOne = new CreateUser(null, password, name);
        Response responseOne = createUserRequest(userOne);
        responseOne.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @AfterEach
    @Step("Очистка данных после выполнения тест кейса")
    public void cleaningData() {
        AuthorizationUser auth = new AuthorizationUser(email, password);
        String accessToken = AuthorizationUserHelper.getAccessToken(auth);
        if (accessToken != null) {
            DeleteUserHelper.deleteUser(accessToken);
        }
    }
}

