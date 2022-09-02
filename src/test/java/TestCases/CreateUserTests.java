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

public class CreateUserTests {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;
    public JSONObject requestParams;

    // constructor
    public CreateUserTests(){

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
    public void test_CreateValidUser(){

        /*******************************************************
         * This test case used to test create a new user
         * use new nickName and email
         ******************************************************/

        String name = getRandomString(3);
        String email =  getRandomString(4) +"@mail.com" ;
        requestParams.put("nickname", name);
        requestParams.put("email", email);

        given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("user").then().log().body().
                spec(responseSpec).assertThat().statusCode(201);

    }

    @Test
    public void test_User_alreadyRegistered(){

        /*******************************************************
         * This test case used to test create a new user that already created
         * use a nickName exist
         ******************************************************/

        String userName = "cc" ;
        String email = "dummi@mail.com";
        requestParams.put("nickname",userName);
        requestParams.put("email", email);

        given().spec(requestSpec).
                body(requestParams.toJSONString()).
                when().
                post("user").then().log().body().
                spec(responseSpec).assertThat().statusCode(400);

    }


}
