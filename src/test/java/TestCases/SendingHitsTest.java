package TestCases;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static util.Utils.getRandomString;

public class SendingHitsTest {


    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;
    public JSONObject requestParams;

    // constructor
    public SendingHitsTest(){

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
    public void test_createValidHits(){

        /*******************************************************
         * Create a new user
         * get the user Token to create a new game
         ******************************************************/

        String name = getRandomString(3);
        String email =  getRandomString(4) +"@mail.com" ;
        requestParams.put("nickname", name);
        requestParams.put("email", email);

        String userToken  = given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("user").then().extract().path("userToken");

        /*******************************************************
         * use the token created above to create a valid game
         ******************************************************/

        requestParams.put("userToken",userToken);

       String gameToken =  given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("connect").then().
                spec(responseSpec).assertThat().
                statusCode(201).and().extract().path("gameToken");

        requestParams.put("userToken", userToken);
        requestParams.put("gameToken", gameToken);
        requestParams.put("positionX", 1);
        requestParams.put("positionY", 2);

        given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("play").then().log().body().
                spec(responseSpec).assertThat().statusCode(410).and().assertThat().
                body("errors.missingPlayer",equalTo("Waiting for second player"));

    }

    @Test
    public void test_invalidGAmeToken(){

        /*******************************************************
         * Create a new user
         * get the user Token to create a new game
         ******************************************************/

        String name = getRandomString(3);
        String email =  getRandomString(4) +"@mail.com" ;
        requestParams.put("nickname", name);
        requestParams.put("email", email);

        String userToken  = given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("user").then().extract().path("userToken");


        requestParams.put("userToken", userToken);
        requestParams.put("gameToken", "9a703ttf6-2354-80d-91fc-cbee617568ae");
        requestParams.put("positionX", 1);
        requestParams.put("positionY", 2);

        given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("play").then().log().body().
                spec(responseSpec).assertThat().statusCode(401).and().assertThat().
                body("errors.gameToken",equalTo("Invalid game token."));
    }

    @Test
    public void test_invalidUserToken(){

        /*******************************************************
         * send an invalid user token
         * use an valid game token
         ******************************************************/
        String invalidUserToken = "a94f61w45-7ad0-41b2-9b05-9b34c2f38ca2";
        String validGameToken = "a94f6145-7ad0-41b2-9b05-9b34c2f38ca2";

        requestParams.put("userToken", invalidUserToken);
        requestParams.put("gameToken", validGameToken);
        requestParams.put("positionX", 0);
        requestParams.put("positionY", 0);

        given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("play").then().log().body().
                spec(responseSpec).assertThat().statusCode(401).and().assertThat().
                body("errors.userToken",equalTo("Invalid user token."));

    }


}
