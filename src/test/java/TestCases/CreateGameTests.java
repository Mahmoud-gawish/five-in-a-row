package TestCases;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLOutput;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static util.Utils.getRandomString;

public class CreateGameTests {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;
    public JSONObject requestParams;

    // constructor
    public CreateGameTests(){

        requestParams = new JSONObject();
    }

    /*******************************************************
     * Crete a before class to set the Base URL
     * create an instance from ResponseSpecification to validate the response as JSON type
     ******************************************************/

    @BeforeClass
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://piskvorky.jobs.cz/api/v1").build();
        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON).build();
    }


    @Test
    public void test_Create_validGame(){

        /*******************************************************
         * Create a new user
         * get the user Token to create a new game
         ******************************************************/

        String name = getRandomString(3);
        String email =  getRandomString(4) +"@mail.com" ;
        requestParams.put("nickname", name);
        requestParams.put("email", email);

        String token  = given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("user").then().extract().path("userToken");

        /*******************************************************
         * use the token created above to create a valid game
         ******************************************************/

        requestParams.put("userToken",token);

        given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("connect").then().log().body().
                spec(responseSpec).assertThat().statusCode(201);
    }


    /*******************************************************
     * test case used to check creation of a game with invalid token
     ******************************************************/
    @Test
    public void test_createGameWithInvalid_userToken(){

        requestParams.put("userToken","240e426b-d77b-48bc-ae0e-7646bt5997");

        given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("connect").then().log().body().
                spec(responseSpec).assertThat().statusCode(401).and().
                assertThat().body("errors.'userToken'",equalTo("Invalid user token."));

    }

}
