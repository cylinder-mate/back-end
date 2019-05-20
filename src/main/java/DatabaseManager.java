import com.mongodb.*;
import model.Order;
import model.User;

import java.util.ArrayList;
import java.util.List;

class DatabaseManager {

    DB database;

    void setup() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        database = mongoClient.getDB("cylinder-mate");
    }

    User getUser(String email) {
        DBCollection collection = database.getCollection("users");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("email", email);
        DBCursor cursor = collection.find(searchQuery);

        if (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            String phoneNumber = (String) dbObject.get("phoneNumber");
            String password = (String) dbObject.get("password");
            return new User(phoneNumber, email, password);
        }

        return null;
    }

    void saveUser(User user) {
        DBCollection collection = database.getCollection("users");
        BasicDBObject document = new BasicDBObject();
        document.put("email", user.getEmail());
        document.put("password", user.getPassword());
        document.put("phoneNumber", user.getPhoneNumber());
        collection.insert(document);
    }

    List<Order> getAllOrders(String phoneNumber) {
        DBCollection collection = database.getCollection("orders");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("phoneNumber", phoneNumber);
        DBCursor cursor = collection.find(searchQuery);

        List<Order> orders = new ArrayList<>();

        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            String date = (String) dbObject.get("date");
            int amount = (Integer) dbObject.get("amount");
            int size = (Integer) dbObject.get("size");
            String deliveryAddress = (String) dbObject.get("deliveryAddress");
            String company = (String) dbObject.get("company");
            orders.add(new Order(date, amount, size, company, deliveryAddress, phoneNumber));
        }

        return orders;
    }

    List<Order> getOrdersByDate(String phoneNumber, String date) {
        DBCollection collection = database.getCollection("orders");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("phoneNumber", phoneNumber);
        searchQuery.put("date", date);
        DBCursor cursor = collection.find(searchQuery);

        List<Order> orders = new ArrayList<>();

        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            int amount = (Integer) dbObject.get("amount");
            int size = (Integer) dbObject.get("size");
            String deliveryAddress = (String) dbObject.get("deliveryAddress");
            String company = (String) dbObject.get("company");
            orders.add(new Order(date, amount, size, company, deliveryAddress, phoneNumber));
        }

        return orders;
    }

    void saveOrder(Order order) {
        DBCollection collection = database.getCollection("orders");
        BasicDBObject document = new BasicDBObject();
        document.put("date", order.getDate());
        document.put("amount", order.getAmount());
        document.put("size", order.getSize());
        document.put("deliveryAddress", order.getDeliveryAddress());
        document.put("company", order.getCompany());
        document.put("phoneNumber", order.getPhoneNumber());
        collection.insert(document);
    }
}
