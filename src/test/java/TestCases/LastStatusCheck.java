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

public class LastStatusCheck {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;
    public JSONObject requestParams;

    // constructor
    public LastStatusCheck(){

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
    public void test_checkGameStatus(){

        /*******************************************************
         * This test case used to test validation of game status
         * use valid user and game token
         ******************************************************/

        String validUserToken = "b401dbfe-27e8-4a86-8660-c176a620fed2";
        String validGameToken = "6ea1c0c7-2677-4959-8d76-6c5deaeb93ed";

        requestParams.put("userToken", validUserToken);
        requestParams.put("gameToken", validGameToken);

        given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("checkStatus").then().log().body().
                spec(responseSpec).assertThat().statusCode(200);
    }
    @Test
    public void test_checkGameStatusWithInvalidUserToken(){

        /*******************************************************
         * This test case used to test invalid user Token
         * use an invalid user token
         ******************************************************/

        String invalidUserToken = "a94f61w45-7ad0-41b2-9b05-9b34c2f38ca2";
        String validGameToken = "6ea1c0c7-2677-4959-8d76-6c5deaeb93ed";

        requestParams.put("userToken", invalidUserToken);
        requestParams.put("gameToken",validGameToken );

        given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("checkStatus").then().log().body().
                spec(responseSpec).assertThat().statusCode(401).
                and().assertThat().
                body("errors.userToken",equalTo("Invalid user token."));
    }

    @Test
    public void test_checkGameStatusWithInvalidGameToken(){

        /*******************************************************
         * This test case used to test invalid game Token
         * use an invalid game token
         ******************************************************/

        String validUserToken = "b401dbfe-27e8-4a86-8660-c176a620fed2";
        String invalidGameToken = "brrdbfe-27e8-4a86-8660-c176a620fed2";
        requestParams.put("userToken", validUserToken);
        requestParams.put("gameToken", invalidGameToken);

        given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("checkStatus").then().log().body().
                spec(responseSpec).assertThat().statusCode(401).
                and().assertThat().
                body("errors.gameToken",equalTo("Invalid game token."));
    }
}
