package api.tests;


import models.UserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import static specs.UserSpec.*;

public class UserTest extends TestBase {

    @Test
    @DisplayName("Get user data by Id")
    void getExistingUserByIdTest() {

        UserResponseModel response = step("Make Request", () ->
                given(userRequestSpecification)

                        .when()
                        .get("/users/2")

                        .then()
                        .spec(userSuccessResponseSpecification)
                        .extract().as(UserResponseModel.class));


        step("Check request", () -> {
            assertEquals(2, response.getData().getId());
            assertEquals("janet.weaver@reqres.in", response.getData().getEmail());
            assertEquals("Janet", response.getData().getFirst_name());
            assertEquals("Weaver", response.getData().getLast_name());
            assertEquals("https://reqres.in/img/faces/2-image.jpg", response.getData().getAvatar());
        });
    }

    @Test
    @DisplayName("Request not exisiting user by id")
    void getNotExistingUserByIdTest() {

        UserResponseModel response = step("Make Request", () ->
                given(userRequestSpecification)

                        .when()
                        .get("/users/23")

                        .then()
                        .spec(userNotFoundResponseSpecification)
                        .extract().as(UserResponseModel.class));

        step("Check request", () -> {
            assertNull(response.getData());
            assertNull(response.getSupport());
        });
    }
}