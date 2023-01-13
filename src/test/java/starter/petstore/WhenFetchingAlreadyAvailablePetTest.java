package starter.petstore;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.given;

@ExtendWith(SerenityJUnit5Extension.class)
public class WhenFetchingAlreadyAvailablePetTest {

    Long newPetId = null;
    PetApiActions petApi;

    @Test
    public void fetchAlreadyAvailablePet() {
        newPetId = petApi.givenKittyIsAvailableInPetStore();
        petApi.whenIAskForAPetWithId(newPetId);
        petApi.thenISeeKittyAsResult();
    }

    @Test
    public void thatTokenIsGenerated() {
        String token = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/auth")
                .body("{ \"username\": \"admin\", \"password\": \"password123\" }")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON).post().getBody().jsonPath().get("token");
        System.out.println(token);
        Serenity.getCurrentSession().put("token", token);
    }
}