package starter.petstore;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.steps.UIInteractions;
import org.hamcrest.Matchers;

import static net.serenitybdd.rest.SerenityRest.*;

public class PetApiActions extends UIInteractions {

    @Given("Kitty is available in the pet store")
    public Long givenKittyIsAvailableInPetStore() {

        Pet pet = new Pet("Kitty", "available");

        Long newId = given()
                .baseUri("https://petstore.swagger.io")
                .basePath("/v2/pet")
                .body(pet, ObjectMapperType.GSON)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON).post().getBody().as(Pet.class, ObjectMapperType.GSON).getId();
        Serenity.getCurrentSession().put("previousStepDataKey", newId);
        return newId;
    }

//    @When("I ask for a pet using Kitty's ID: {0}")
//    public void whenIAskForAPetWithId(Long id) {
//        when().get("/" + id);
//    }
    @When("I ask for a pet using Kitty's ID: {long}")
    public void whenIAskForAPetWithId(Long id) {
        when().get("/" + id);
    }

    @Then("I get Kitty as result")
    public void thenISeeKittyAsResult() {
        then().body("name", Matchers.equalTo("Kitty"));
    }

    @When("I ask for a pet using Kitty's ID")
    public void iAskForAPetUsingKittySID() {
        long previousStepData = (long) (Serenity.getCurrentSession().get("previousStepDataKey"));
        when().get("/" + previousStepData);
    }


//    @When("I ask for a pet using Kitty's ID: {int}")
//    public void iAskForAPetUsingKittySID(int arg0) {
//    }
}

