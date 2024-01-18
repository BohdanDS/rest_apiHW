package api.tests;

import models.RegistrationBodyModel;
import models.RegistrationResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.RegistrationSpec.*;

public class RegistrationTest extends TestBase {

    @Test
    @DisplayName("Successfully registration new user")
    void successRegistrationTest(){

        RegistrationBodyModel registrationData = new RegistrationBodyModel();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        RegistrationResponseModel response = step("Make Request", ()->
                given(registrationRequestSpec)
                        .body(registrationData)

                .when()
                        .post("/register")

                .then()
                        .spec(registrationResponseSpec)
                        .extract().as(RegistrationResponseModel.class));

        step("Check request", ()->{
            assertEquals(4, response.getId());
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        });
    }

    @Test
    @DisplayName("Registration with empty password")
    void emptyPasswordRegistrationTest(){

        RegistrationBodyModel registrationData = new RegistrationBodyModel();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("");


        RegistrationResponseModel response =  step("Make Request", ()->
                given(registrationRequestSpec)
                        .body(registrationData)

                .when()
                        .post("/register")

                .then()
                        .spec(errorResponseSpec)
                        .extract().as(RegistrationResponseModel.class));

        assertEquals("Missing password", response.getError());

    }

    @Test
    @DisplayName("Registration without email address")
    void emptyEmailRegistrationTest(){

        RegistrationBodyModel registrationData = new RegistrationBodyModel();
        registrationData.setEmail("");
        registrationData.setPassword("pistol");

        RegistrationResponseModel response =
                given(registrationRequestSpec)
                    .body(registrationData)

                .when()
                    .post("/register")

                .then()
                    .extract().as(RegistrationResponseModel.class);

        assertEquals("Missing email or username", response.getError());

    }

}
