package io.nowave.api.repository;


import io.nowave.api.model.User;

public class UserDataProvider {


    public static String password = "toto";

    public static User userToBeSaved() {
        return new User("Bert", "Poller", "bpoller@ekito.fr", password);
    }

    public static String userToBeSavedJSON() {
        return "{\n" +
                "    \"firstName\":\"Bert\",\n" +
                "    \"lastName\" :\"Poller\",\n" +
                "    \"email\" : \"bpoller@ekito.fr\",\n" +
                "    \"password\":\""+password+"\"\n" +
                "}";
    }
}
