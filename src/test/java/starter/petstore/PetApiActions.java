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

        Long petId = postPet(pet);
        Serenity.getCurrentSession().put("previousStepDataKey", petId);
        return petId;
    }

    private static Long postPet(Pet pet) {
        return given()
                .baseUri("https://petstore.swagger.io")
                .basePath("/v2/pet")
                .body(pet, ObjectMapperType.GSON)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON).post().getBody().as(Pet.class, ObjectMapperType.GSON).getId();
    }

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

    @Then("I get a {int} error")
    public void iGetAError(int arg0) {
        then().statusCode(arg0);
    }

    @When("I ask for a pet using a {string} ID {string}")
    public void iAskForAPetUsingANonExistingID(String typeOfId, String arg0) {
        when().get("https://petstore.swagger.io/v2/pet" + arg0);
    }

    @Given("that john is interested in buying pet, get {string} id")
    public void thatJohnIsInterestedInBuyingPetGetId(String petName) {
        Pet pet = new Pet(petName, "available");
        Long petId = postPet(pet);
        Serenity.getCurrentSession().put("previousStepDataKey", petId);
    }

    @Then("he sends an order for the dog with quantity as {string}")
    public void heSendsAnOrderForTheDogWithQuantityAs(String arg0) {
        Order order = new Order((long) (Serenity.getCurrentSession().get("previousStepDataKey")), Integer.parseInt(arg0));
        Serenity.getCurrentSession().put("orderData", order);
        given()
                .baseUri("https://petstore.swagger.io")
                .basePath("/v2/store/order")
                .body(order, ObjectMapperType.GSON)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON).post();
    }

    @Then("he should get the order id which shouldn't be null or {int}")
    public void heShouldGetTheOrderIdWhichShouldnTBeNullOr(int arg0) {
        // store the id in previousStepDataKey
        Serenity.getCurrentSession().put("orderId", then().extract().path("id"));
        System.out.println(Serenity.getCurrentSession().get("orderId"));
        then().body("id", Matchers.not(Matchers.equalTo(arg0)));
    }

    @Then("he should be able to get the order details using the order id")
    public void heShouldBeAbleToGetTheOrderDetailsUsingTheOrderId() {
        long orderId = (long) Serenity.getCurrentSession().get("orderId");
        when().get("https://petstore.swagger.io/v2/store/order/" + orderId);
    }

    @Then("he should be able to get the order details using the order id which should be same as the order sent")
    public void heShouldBeAbleToGetTheOrderDetailsUsingTheOrderIdWhichShouldBeSameAsTheOrderSent() {
        Order order = (Order) Serenity.getCurrentSession().get("orderData");
        Integer orderId = (Integer) Serenity.getCurrentSession().get("orderId");
        when().get("https://petstore.swagger.io/v2/store/order/" + orderId);
        then().body("petId", Matchers.equalTo(order.getPetId()));
        then().body("quantity", Matchers.equalTo(order.getQuantity()));
    }
}

