import config.BaseUrl;
import helpers.AuthorizationUserHelper;
import helpers.DeleteUserHelper;
import helpers.UpdateUserHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.AuthorizationUser;
import utils.GetAndUpdateInfoUser;

import java.util.Random;

import static helpers.CreateUserHelper.createNewUser;
import static org.hamcrest.core.IsEqual.equalTo;

public class UpdateUserTest extends BaseUrl {
    private String email;
    private String password;
    private String name;
    private String newName;
    private String newEmail;
    private String accessToken;

    @BeforeEach
    @Step("Подготовка рандомных тестовых данных")
    public void generateTestData() {
        int random = new Random().nextInt(100);
        email = "email_" + random + "@yandex.ru";
        newEmail = "new_email_" + random + "@yandex.ru";
        password = "Pass_" + random;
        name = "Name_" + random;
        newName = "New_name_" + random;

        createNewUser(email, password, name);
        AuthorizationUser auth = new AuthorizationUser(email, password);
        accessToken = AuthorizationUserHelper.getAccessToken(auth);
    }

    @Test
    @DisplayName("Изменение имени с авторизацией")
    public void updateNameWithAuth() {
        GetAndUpdateInfoUser updateData = new GetAndUpdateInfoUser();
        updateData.setName(newName);

        Response response = UpdateUserHelper.updateUserRequest(updateData, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo(newName))
                .body("user.email", equalTo(email));
    }

    @Test
    @DisplayName("Изменение мейла с авторизацией")
    public void updateEmailWithAuth() {
        GetAndUpdateInfoUser updateData = new GetAndUpdateInfoUser();
        updateData.setEmail(newEmail);

        Response response = UpdateUserHelper.updateUserRequest(updateData, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail))
                .body("user.name", equalTo(name));
    }

    @Test
    @DisplayName("Изменение данных без авторизации, Ошибка 401")
    public void updateDataWithoutAuth() {
        GetAndUpdateInfoUser updateData = new GetAndUpdateInfoUser();
        updateData.setName(newName);

        Response response = UpdateUserHelper.updateUserRequestWithoutAuth(updateData);
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение мейла на уже зарегистрированный мейл, Ошибка 403")
    public void updateEmailRegistered() {
        String existingEmail; // для теста конфликта
        String existingPassword;

        int newRandom = new Random().nextInt(1000);
        existingEmail = "email_e" + newRandom + "@yandex.ru";
        existingPassword = "Pass_p" + newRandom;
        String existingName = "Name_n" + newRandom;
        createNewUser(existingEmail, existingPassword, existingName);

        GetAndUpdateInfoUser updateData = new GetAndUpdateInfoUser();
        updateData.setEmail(existingEmail);

        Response response = UpdateUserHelper.updateUserRequest(updateData, accessToken);
        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

    @AfterEach
    @Step("Очистка данных после выполнения тест кейса")
    public void cleaningData() {
        if (accessToken != null) {
            DeleteUserHelper.deleteUser(accessToken);
        }
    }
}
