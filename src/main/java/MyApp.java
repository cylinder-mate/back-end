import com.google.gson.Gson;
import io.javalin.Javalin;
import model.Order;
import model.User;

import java.util.List;

public class MyApp {
    public static void main(String[] args) {

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.setup();

        Javalin app = Javalin.create().start(7000);

        app.post("/user", ctx -> {
            Gson gson = new Gson();
            User newUser = gson.fromJson(ctx.body(), User.class);

            User dbUser = databaseManager.getUser(newUser.getEmail());

            if (dbUser.getPassword().equals(newUser.getPassword())) {
                ctx.result("Success");
            } else if (dbUser == null) {
                ctx.result("User does not exist");
            } else {
                ctx.result("Incorrect Password.");
            }
        });

        app.post("/register", ctx -> {
            Gson gson = new Gson();
            User newUser = gson.fromJson(ctx.body(), User.class);
            databaseManager.saveUser(newUser);
        });

        app.get("/order", ctx -> {
            String phoneNumber = ctx.header("phoneNumber");

            List<Order> orders = databaseManager.getAllOrders(phoneNumber);

            if (orders.isEmpty()) {
                ctx.result("null");
            } else {
                Gson gson = new Gson();
                ctx.result(gson.toJson(orders));
            }
        });

        app.get("/order/date", ctx -> {
            String phoneNumber = ctx.header("phoneNumber");
            String date = ctx.header("date");

            List<Order> orders = databaseManager.getOrdersByDate(phoneNumber, date);

            if (orders.isEmpty()) {
                ctx.result("null");
            } else {
                Gson gson = new Gson();
                ctx.result(gson.toJson(orders));
            }
        });

        app.post("/order", ctx -> {
            Gson gson = new Gson();
            Order newOrder = gson.fromJson(ctx.body(), Order.class);
            databaseManager.saveOrder(newOrder);
            ctx.result("Success");
        });
    }
}