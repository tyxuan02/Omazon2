package com.example.omazonproject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to deal with .json file for different purposes
 *
 * @author XiangLun
 */
public class JsonFileUtil {

    /**
     * This method is used to write ordered items into orders file from a list of cart Items
     *
     * @param list a list of cart item
     * @author XiangLun
     */
    @SuppressWarnings("unchecked")
    public void writeOrdersFile(List<CartItem> list) {
        File orderFile = new File("JsonFiles\\orders.json");
        if (orderFile.exists()) {
            // if the orders file exists,
            // add the newly added ordered item at the end of the .json file
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader("JsonFiles\\orders.json"));
                JSONArray jsonArray = (JSONArray) obj;

                for (CartItem cartItem : list) {
                    JSONObject orderedItem = new JSONObject();
                    orderedItem.put("productName", cartItem.getProductName());
                    orderedItem.put("quantity", cartItem.getQuantity());
                    orderedItem.put("ordersImagePath", cartItem.getCartImagePath());
                    jsonArray.add(orderedItem);
                }

                FileWriter writer = new FileWriter("JsonFiles\\orders.json");
                writer.write(jsonArray.toJSONString());
                writer.flush();
                writer.close();

            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }

        } else {
            // if it doesn't exist,
            // write a new orders file
            JSONArray jsonArray = new JSONArray();

            for (CartItem cartItem : list) {
                JSONObject orderedItem = new JSONObject();
                orderedItem.put("productName", cartItem.getProductName());
                orderedItem.put("quantity", cartItem.getQuantity());
                orderedItem.put("ordersImagePath", cartItem.getCartImagePath());
                jsonArray.add(orderedItem);
            }

            try {
                FileWriter writer = new FileWriter("JsonFiles\\orders.json");
                writer.write(jsonArray.toJSONString());
                writer.flush();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
                cartItem.put("sellerEmail", product.getSellerEmail());

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
            cartItem.put("sellerEmail", product.getSellerEmail());

            JSONArray jsonArray = new JSONArray();
            jsonArray.add(cartItem);

            try {
                FileWriter file = new FileWriter("JsonFiles\\cart.json");
                file.write(jsonArray.toJSONString());
                file.flush();
                file.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to write an order into the orders.json file, which stores the information of the ordered items.
     *
     * @author XiangLun
     */
    @SuppressWarnings("unchecked")
    public void writeOrdersFile(Product product, int quantity) {
        File ordersFile = new File("JsonFiles\\orders.json");
        if (ordersFile.exists()) {
            // if the orders file exists,
            // append the newly added ordered item at the end of the .json file
            JSONParser jsonParser = new JSONParser();
            try {
                Object obj = jsonParser.parse(new FileReader(ordersFile));
                JSONArray jsonArray = (JSONArray) obj;

                JSONObject orderedItem = new JSONObject();
                orderedItem.put("productName", product.getProductName());
                orderedItem.put("quantity", quantity);
                orderedItem.put("ordersImagePath", product.getProductImagePath());

                jsonArray.add(orderedItem);

                FileWriter file = new FileWriter("JsonFiles\\orders.json");
                file.write(jsonArray.toJSONString());
                file.flush();
                file.close();


            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }

        } else {
            // if orders.json file is not found,
            // write the information of the ordered item into a new orders.json file
            JSONObject orderedItem = new JSONObject();
            orderedItem.put("productName", product.getProductName());
            orderedItem.put("quantity", quantity);
            orderedItem.put("ordersImagePath", product.getProductImagePath());

            JSONArray jsonArray = new JSONArray();
            jsonArray.add(orderedItem);

            try {
                FileWriter file = new FileWriter("JsonFiles\\orders.json");
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
     * This method is used to read the orders.json file
     *
     * @return a list of OrderedItem object which presence in the file
     * @author XiangLun
     */
    public List<OrderedItem> readOrdersFile() {
        List<OrderedItem> orderedItemsList = new ArrayList<>();
        if (new File("JsonFiles\\orders.json").exists()) {
            JSONParser jsonParser = new JSONParser();
            try {
                Object obj = jsonParser.parse(new FileReader("JsonFiles\\orders.json"));
                JSONArray jsonArray = (JSONArray) obj;

                // Iterate over jsonArray to load all the ordered item in it
                for (Object object : jsonArray) {
                    if (object instanceof JSONObject) {
                        OrderedItem orderedItem = new OrderedItem();
                        orderedItem.setProductName((String) ((JSONObject) object).get("productName"));
                        orderedItem.setQuantity(Math.toIntExact((Long) ((JSONObject) object).get("quantity")));
                        orderedItem.setOrderedImagePath("src/main/resources/images/" + ((JSONObject) object).get("ordersImagePath") + ".png");
                        orderedItemsList.add(orderedItem);
                    }
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        return orderedItemsList;
    }

    /**
     * This method is used to read the orderHistory.json file
     *
     * @return a list of OrderHistory object which presence in the file
     * @author XiangLun
     */
    public List<OrderHistoryItem> readOrderHistoryFile() {
        List<OrderHistoryItem> orderHistoryList = new ArrayList<>();
        if (new File("JsonFiles\\orderHistory.json").exists()) {
            JSONParser jsonParser = new JSONParser();
            try {
                Object obj = jsonParser.parse(new FileReader("JsonFiles\\orderHistory.json"));
                JSONArray jsonArray = (JSONArray) obj;

                // Iterate over jsonArray to load all the order history item in it
                for (Object object : jsonArray) {
                    if (object instanceof JSONObject) {
                        OrderHistoryItem orderHistoryItem = new OrderHistoryItem();
                        orderHistoryItem.setProductName((String) ((JSONObject) object).get("productName"));
                        orderHistoryItem.setQuantity(Math.toIntExact((Long) ((JSONObject) object).get("quantity")));
                        orderHistoryItem.setPricePerUnit((Double) ((JSONObject) object).get("pricePerUnit"));
                        orderHistoryItem.setOrderHistoryItemImagePath("src/main/resources/images/" + ((JSONObject) object).get("orderHistoryItemImagePath") + ".png");
                        orderHistoryList.add(orderHistoryItem);
                    }
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        return orderHistoryList;
    }

    /**
     * This method is used to read the cart.json file
     *
     * @return a list of CartItem object which presence in the file
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
                        cartItem.setSellerEmail((String) ((JSONObject) object).get("sellerEmail"));
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
     * @return a list of FavoriteItem object which presence in the file
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

    /**
     * This method is used to delete one record from the orders tab in the purchase page
     * and add that particular record to orderHistory.json
     *
     * @param item an ordered item to be deleted from the .json file
     * @author XiangLun
     */
    @SuppressWarnings("unchecked")
    public void deleteOrderRecord(OrderedItem item) {
        int removedQuantity = 0;
        String removedImagePath = null;
        String removedProductName = null;

        List<OrderedItem> orderedItemList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            // load the contents from tbe orders.json and store it in the json Array
            Object obj = parser.parse(new FileReader("JsonFiles\\orders.json"));
            JSONArray jsonArray = (JSONArray) obj;

            // Iterate over jsonArray
            for (Object object : jsonArray) {
                if (object instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) object;
                    if (!("src/main/resources/images/" + jsonObject.get("ordersImagePath") + ".png").equals(item.getOrderedImagePath())) {
                        // add all ordered item into the orderedItemList except the particular item that want to be removed
                        OrderedItem orderedItem = new OrderedItem();
                        orderedItem.setQuantity(Math.toIntExact((Long) ((JSONObject) object).get("quantity")));
                        orderedItem.setOrderedImagePath((String) ((JSONObject) object).get("ordersImagePath"));
                        orderedItem.setProductName((String) ((JSONObject) object).get("productName"));
                        orderedItemList.add(orderedItem);
                    } else {
                        // store the information of the removed object
                        removedImagePath = (String) ((JSONObject) object).get("ordersImagePath");
                        removedQuantity = Math.toIntExact((Long) ((JSONObject) object).get("quantity"));
                        removedProductName = (String) ((JSONObject) object).get("productName");
                    }
                }
            }

            // use the list to overwrite the JSON file
            JSONArray jsonArray1 = new JSONArray();
            for (OrderedItem items : orderedItemList) {
                JSONObject orderedItem = new JSONObject();
                orderedItem.put("quantity", items.getQuantity());
                orderedItem.put("ordersImagePath", items.getOrderedImagePath());
                orderedItem.put("productName", items.getProductName());
                jsonArray1.add(orderedItem);
            }
            FileWriter file = new FileWriter("JsonFiles\\orders.json");
            file.write(jsonArray1.toJSONString());
            file.flush();
            file.close();

            // connect to database to get the price per unit of the removed item
            double pricePerUnit = 0;
            Connection connection = null;
            PreparedStatement psGetPrice = null;
            ResultSet resultSet = null;

            try {
                DatabaseConnection db = new DatabaseConnection();
                connection = db.getConnection();
                psGetPrice = connection.prepareStatement("SELECT * FROM product_info WHERE imageName = ?");
                psGetPrice.setString(1, removedImagePath);
                resultSet = psGetPrice.executeQuery();

                if (resultSet.next()) {
                    pricePerUnit = Double.parseDouble(resultSet.getString("price"));
                }

            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (psGetPrice != null) {
                    try {
                        psGetPrice.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            // add the removed item into orderHistory.json
            File file1 = new File("JsonFiles\\orderHistory.json");
            if (file1.exists()) {
                // if the order history file exists,

                // append the newly added item at the end of the .json file
                JSONParser jsonParser = new JSONParser();
                try {
                    Object object = jsonParser.parse(new FileReader(file1));
                    JSONArray jsonArray2 = (JSONArray) object;

                    // fill the newly created json object with its information
                    JSONObject orderHistoryItem = new JSONObject();
                    orderHistoryItem.put("productName", removedProductName);
                    orderHistoryItem.put("quantity", removedQuantity);
                    orderHistoryItem.put("pricePerUnit", pricePerUnit);
                    orderHistoryItem.put("orderHistoryItemImagePath", removedImagePath);

                    // add the newly created json object at the end of the json array
                    jsonArray2.add(orderHistoryItem);

                    // convert the json array into json string and write it out
                    FileWriter orderHistory = new FileWriter("JsonFiles\\orderHistory.json");
                    orderHistory.write(jsonArray2.toJSONString());
                    orderHistory.flush();
                    orderHistory.close();


                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }

            } else {
                // if orderHistory.json file is not found,
                // write the information of the cart item into a new cart.json file
                JSONObject orderHistoryItem = new JSONObject();
                orderHistoryItem.put("productName", removedProductName);
                orderHistoryItem.put("quantity", removedQuantity);
                orderHistoryItem.put("pricePerUnit", pricePerUnit);
                orderHistoryItem.put("orderHistoryItemImagePath", removedImagePath);

                JSONArray jsonArray2 = new JSONArray();
                jsonArray2.add(orderHistoryItem);

                FileWriter orderHistory = new FileWriter("JsonFiles\\orderHistory.json");
                orderHistory.write(jsonArray2.toJSONString());
                orderHistory.flush();
                orderHistory.close();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method read through the cart.json file and return the total amount
     * required to purchase those items
     *
     * @return the total amount of price in double
     * @author XiangLun
     */
    public double getTotalAmountFromCart() {
        double sum = 0;
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("JsonFiles\\cart.json"));
            JSONArray jsonArray = (JSONArray) obj;

            for (Object object : jsonArray) {
                if (object instanceof JSONObject) {
                    int quantity = Math.toIntExact((Long) ((JSONObject) object).get("quantity"));
                    double pricePerUnit = (double) ((JSONObject) object).get("pricePerUnit");
                    sum += quantity * pricePerUnit;
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return sum;
    }
}
