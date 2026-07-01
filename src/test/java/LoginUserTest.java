import helpers.AuthorizationUserHelper;
import helpers.BaseUrl;
import helpers.DeleteUserHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.AuthorizationUser;
import utils.LoginUser;

import java.util.Random;

import static helpers.CreateUserHelper.createNewUser;
import static helpers.LoginUserHelper.loginUserRequest;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest extends BaseUrl {

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
    @DisplayName("Логин под существующим пользователем")
    public void loginExistingUser() {
        createNewUser(email, password, name);
        LoginUser loginUser = new LoginUser(email, password);
        Response response = loginUserRequest(loginUser);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем, Ошибка 401")
    public void loginIncorrectNamePassword() {
        LoginUser loginUser = new LoginUser(email, password);
        Response response = loginUserRequest(loginUser);
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
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
