package com.example.omazonproject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * This class is solely responsible to read the credentials of our Omazon email account from a .json file
 *
 * @author XiangLun
 */
public class JsonFileReader {

    /**
     * A field named emailAddress with string datatype
     */
    private String emailAddress;

    /**
     * A field named password with string datatype
     */
    private String password;

    /**
     * This constructor will read our account's credentials from a .json file and initialize
     * the password and the email address field in this class
     */
    public JsonFileReader() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("JsonFiles/credentials.json")) {
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            this.emailAddress = (String) jsonObject.get("email address");
            this.password = (String) jsonObject.get("password");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to return emailAddress in String
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * A method to return password in String
     */
    public String getPassword() {
        return password;
    }
}
