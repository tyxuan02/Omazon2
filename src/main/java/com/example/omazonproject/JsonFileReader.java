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
    private String emailAddress;
    private String password;

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

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }
}
