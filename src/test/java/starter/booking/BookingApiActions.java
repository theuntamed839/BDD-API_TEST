package starter.booking;
import com.google.gson.Gson;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.steps.UIInteractions;
import org.hamcrest.Matchers;
import org.junit.Assert;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.MatcherAssert.assertThat;

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

    @When("I get the booking with id {string}")
    public void iGetTheBookingWithId(String id) {
        String response = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking/" + id)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON).get().body().asString();
        Serenity.getCurrentSession().put("responseData", response);
    }

    @Then("the response code should be {int}")
    public void theResponseCodeShouldBe(int statusCode) {
        then().statusCode(statusCode);
    }

    @Given("I have a booking")
    public void iHaveABooking() {
        String token = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/auth")
                .body("{ \"username\": \"admin\", \"password\": \"password123\" }")
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON).post().getBody().jsonPath().get("token");
        Serenity.getCurrentSession().put("token", token);
        Booking booking = new Booking("Jim", "Brown", 111, true, new Bookingdates("2018-01-01", "2019-01-11"), "Breakfast");
        Response response = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking")
                .header("Cookie", "token=" + token )
                .body(booking, ObjectMapperType.GSON)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON).post();
        Serenity.getCurrentSession().put("bookingId", response.getBody().jsonPath().get("bookingid"));
        System.out.println(Serenity.getCurrentSession().get("bookingId"));
    }

    @When("I get the booking by id")
    public void iGetTheBookingById() {
        int id = (int) (Serenity.getCurrentSession().get("bookingId"));
        String response = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking/" + id)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON).get().body().asString();
        Serenity.getCurrentSession().put("responseData", response);
    }

    @Then("I should get the booking")
    public void iShouldGetTheBooking() {
        String response = (String) (Serenity.getCurrentSession().get("responseData"));
        Booking booking = new Gson().fromJson(response, Booking.class);
        Assert.assertEquals("Jim", booking.getFirstname());
        Assert.assertEquals("Brown", booking.getLastname());
        Assert.assertEquals(111, booking.getTotalprice());
        Assert.assertTrue(booking.isDepositpaid());
        Assert.assertEquals("2018-01-01", booking.getBookingdates().getCheckin());
        Assert.assertEquals("2019-01-01", booking.getBookingdates().getCheckout());
        Assert.assertEquals("Breakfast", booking.getAdditionalneeds());

    }

    @Then("I update the booking with new values for firstname {string} and lastname {string}")
    public void iUpdateTheBookingWithNewValuesForFirstnameAndLastname(String firstName, String lastName) {
        String token = (String) (Serenity.getCurrentSession().get("token"));
        Booking booking = new Booking();
        booking.setFirstname(firstName);
        booking.setLastname(lastName);
        int id = (int) (Serenity.getCurrentSession().get("bookingId"));
        given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking/" + id)
                .header("Cookie", "token=" + token )
                .body(booking, ObjectMapperType.GSON)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON).patch();

    }

    @When("I get the booking by invalid id")
    public void iGetTheBookingByInvalidId() {
        String response = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking/-1")
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON).get().body().asString();
        Serenity.getCurrentSession().put("responseData", response);
    }



    @When("I delete the booking")
    public void theTokenForAuthWeWantToDeleteBookingWithIdAs() {

        String token = (String) (Serenity.getCurrentSession().get("token"));
        int id = (int) (Serenity.getCurrentSession().get("bookingId"));
        given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking/" + id)
                .header("Cookie", "token=" + token )
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON).delete();
    }



}