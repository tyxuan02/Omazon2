package com.example.omazonproject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * This method is used to write and read .json file for different purposes
 *
 * @author XiangLun
 */
public class JsonFileUtil {

    /**
     * This method is used to write cart.json file, which stores the information of the cart items.
     *
     * @author XiangLun
     */
    @SuppressWarnings("unchecked")
    public void writeCartFile(Product product, int quantity) throws IOException {
        File cartFile = new File("JsonFiles\\cart.json");
        if (cartFile.exists()) {
            // if the cart file exists,
            // append the newly added cart item at the end of the .json file
            JSONParser jsonParser = new JSONParser();
            try {
                Object obj = jsonParser.parse(new FileReader(cartFile));
                JSONArray jsonArray = (JSONArray) obj;

                JSONObject cartItem = new JSONObject();
                cartItem.put("productName", product.getProductName());
                cartItem.put("sellerEmail", product.getSellerEmail());
                cartItem.put("quantity", quantity);

                jsonArray.add(cartItem);

                FileWriter file = new FileWriter("JsonFiles\\cart.json");
                file.write(jsonArray.toJSONString());
                file.flush();
                file.close();

            } catch (FileNotFoundException | ParseException e) {
                e.printStackTrace();
            }

        } else {
            // if cart.json file is not found,
            // write the information of the cart item into a new cart.json file
            JSONObject cartItem = new JSONObject();
            cartItem.put("productName", product.getProductName());
            cartItem.put("sellerEmail", product.getSellerEmail());
            cartItem.put("quantity", quantity);

            JSONArray jsonArray = new JSONArray();
            jsonArray.add(cartItem);

            FileWriter file = new FileWriter("JsonFiles\\cart.json");
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();
        }
    }

    public static void readCartFile() {

    }
}
