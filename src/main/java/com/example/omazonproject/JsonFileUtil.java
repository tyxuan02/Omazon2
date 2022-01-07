package com.example.omazonproject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
                cartItem.put("pricePerUnit", product.getProductPrice());
                cartItem.put("quantity", quantity);
                cartItem.put("cartImagePath", product.getProductImagePath());

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
            cartItem.put("pricePerUnit", product.getProductPrice());
            cartItem.put("quantity", quantity);
            cartItem.put("cartImagePath", product.getProductImagePath());

            JSONArray jsonArray = new JSONArray();
            jsonArray.add(cartItem);

            FileWriter file = new FileWriter("JsonFiles\\cart.json");
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();
        }
    }

    public List<CartItem> readCartFile() {
        List<CartItem> cartItemList = new ArrayList<>();
        if (new File("JsonFiles\\cart.json").exists()) {
            JSONParser jsonParser = new JSONParser();
            try {
                Object obj = jsonParser.parse(new FileReader("JsonFiles\\cart.json"));
                JSONArray jsonArray = (JSONArray) obj;

                // Iterate over jsonArray to load all the cart item in it
                for (Object object : jsonArray) {
                    if (object instanceof JSONObject) {
                        CartItem cartItem = new CartItem();
                        cartItem.setProductName((String) ((JSONObject) object).get("productName"));
                        cartItem.setQuantity(Math.toIntExact((Long) ((JSONObject) object).get("quantity")));
                        cartItem.setPricePerUnit((Double) ((JSONObject) object).get("pricePerUnit"));
                        cartItem.setCartImagePath("src/main/resources/images/" + ((JSONObject) object).get("cartImagePath") + ".png");
                        cartItemList.add(cartItem);
                    }
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        return cartItemList;
    }
}
