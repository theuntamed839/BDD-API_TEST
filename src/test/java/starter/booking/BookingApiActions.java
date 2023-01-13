package starter.booking;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.steps.UIInteractions;
import org.hamcrest.Matchers;

import static net.serenitybdd.rest.SerenityRest.*;

public class BookingApiActions extends UIInteractions {
    @Given("that token is generated")
    public void thatTokenIsGenerated() {
        String token = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/auth")
                .body("{ \"username\": \"admin\", \"password\": \"password123\" }")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON).post().getBody().jsonPath().get("token");
        Serenity.getCurrentSession().put("token", token);
    }

    @When("user sends a PUT request to booking with id as {string} following payload first_name as {string}, last_name as {string}, total_price as {string}, deposit_paid as {string}, checkin as {string}, checkout as {string}")
    public void userSendsAPUTRequestToBookingWithIdAsFollowingPayloadFirst_nameAsLast_nameAsTotal_priceAsDeposit_paidAsCheckinAsCheckoutAs(String id, String firstName, String lastName, String totalPrice, String depositPaid, String checkIn, String checkOut) {
        String token = (String) (Serenity.getCurrentSession().get("token"));
        Booking booking = new Booking(firstName, lastName, Integer.parseInt(totalPrice), Boolean.parseBoolean(depositPaid), new Bookingdates(checkIn, checkOut), "Breakfast");
        // get booking in json
//        String bookingJson = ObjectMapperType.GSON.getMapper().toJson(booking);
//        given()
//                .baseUri("https://restful-booker.herokuapp.com")
//                .basePath("/booking/" + id)
//                .header("Cookie", "token=" + token )
//                .body(booking, ObjectMapperType.GSON)
//                .accept(ContentType.JSON)
//                .contentType(ContentType.JSON).put();

        given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking/" + id)
                .header("Cookie", "token=" + token )
                .body(booking, ObjectMapperType.GSON)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON).put();
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        then().statusCode(statusCode);
    }

    @Given("that token is generated which is invalid")
    public void thatTokenIsGeneratedWhichIsInvalid() {
        Serenity.getCurrentSession().put("token", "invalidToken");
    }

    @When("user sends a PUT request to booking with id as {string} following payload first_name as {string}, last_name as {string}, total_price as {string}, checkin as {string}, checkout as {string}")
    public void userSendsAPUTRequestToBookingWithIdAsFollowingPayloadFirst_nameAsLast_nameAsTotal_priceAsCheckinAsCheckoutAs(String id, String firstName, String lastName, String totalPrice, String checkIn, String checkOut) {
        String token = (String) (Serenity.getCurrentSession().get("token"));
        given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking/" + id)
                .header("Cookie", "token=" + token )
                .body("{ \"firstname\": \"" + firstName + "\", \"lastname\": \"" + lastName + "\", \"totalprice\": " + totalPrice + ", \"bookingdates\": { \"checkin\": \"" + checkIn + "\", \"checkout\": \"" + checkOut + "\" } }")
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON).put();
    }
}