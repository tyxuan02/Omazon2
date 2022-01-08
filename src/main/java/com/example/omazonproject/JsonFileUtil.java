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
    public void writeCartFile(Product product, int quantity) {
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


            } catch (ParseException | IOException e) {
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

            FileWriter file = null;
            try {
                file = new FileWriter("JsonFiles\\cart.json");
                file.write(jsonArray.toJSONString());
                file.flush();
                file.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to write favorite.json file, which stores the information of the favorite items.
     *
     * @author XiangLun
     */
    @SuppressWarnings("unchecked")
    public void writeFavoriteFile(Product product) {
        File favoriteFile = new File("JsonFiles\\favorite.json");
        if (favoriteFile.exists()) {
            // if the favorite.json file exists,
            // append the newly added favorite item at the end of the .json file
            JSONParser jsonParser = new JSONParser();
            try {
                Object obj = jsonParser.parse(new FileReader(favoriteFile));
                JSONArray jsonArray = (JSONArray) obj;

                JSONObject favoriteItem = new JSONObject();
                favoriteItem.put("productName", product.getProductName());
                favoriteItem.put("pricePerUnit", product.getProductPrice());
                favoriteItem.put("favoriteImagePath", product.getProductImagePath());

                jsonArray.add(favoriteItem);

                FileWriter file = new FileWriter("JsonFiles\\favorite.json");
                file.write(jsonArray.toJSONString());
                file.flush();
                file.close();

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } else {
            // if favorite.json file is not found,
            // write the information of the favorite item into a new favorite.json file
            JSONObject favoriteItem = new JSONObject();
            favoriteItem.put("productName", product.getProductName());
            favoriteItem.put("pricePerUnit", product.getProductPrice());
            favoriteItem.put("favoriteImagePath", product.getProductImagePath());

            JSONArray jsonArray = new JSONArray();
            jsonArray.add(favoriteItem);

            try {
                FileWriter file = new FileWriter("JsonFiles\\favorite.json");
                file.write(jsonArray.toJSONString());
                file.flush();
                file.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * This method is used to read the cart.json file
     *
     * @return a list of CartItem object
     * @author XiangLun
     */
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

    /**
     * This method is used to read the favorite.json file
     *
     * @return a list of FavoriteItem object
     * @author XiangLun
     */
    public List<FavoriteItem> readFavoriteFile() {
        List<FavoriteItem> favoriteItemList = new ArrayList<>();
        if (new File("JsonFiles\\favorite.json").exists()) {
            JSONParser jsonParser = new JSONParser();
            try {
                Object obj = jsonParser.parse(new FileReader("JsonFiles\\favorite.json"));
                JSONArray jsonArray = (JSONArray) obj;

                // Iterate over jsonArray to load all the favorite item in it
                for (Object object : jsonArray) {
                    if (object instanceof JSONObject) {
                        FavoriteItem favoriteItem = new FavoriteItem();
                        favoriteItem.setProductName((String) ((JSONObject) object).get("productName"));
                        favoriteItem.setPricePerUnit((Double) ((JSONObject) object).get("pricePerUnit"));
                        favoriteItem.setFavoriteImagePath("src/main/resources/images/" + ((JSONObject) object).get("favoriteImagePath") + ".png");
                        favoriteItemList.add(favoriteItem);
                    }
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        return favoriteItemList;
    }
}
