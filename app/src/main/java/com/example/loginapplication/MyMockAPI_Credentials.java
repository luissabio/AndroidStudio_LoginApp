package com.example.loginapplication;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;


public class MyMockAPI_Credentials {

    private static HashMap<String, String> users = new HashMap<>();
    private static HashMap<String, String> usersToken = new HashMap<>();

    public static String POST_EmailAndPassword(String email, String password) {
        if (!isValidEmail(email)) {
            return "KO|Invalid Email Format";
        }

        if (users.containsKey(email)) {
            return "KO|Email already exists";
        }

        users.put(email, password);

        String token = generateRandomString();
        usersToken.put(email, token);
        MyMockAPI_UserInfo.POST_UserInfo(email, "", token);
        return "OK";
    }

    public static String GET_EmailAndPassword(String email, String password) {
        if (!isValidEmail(email)) {
            return "KO|Invalid Email Format";
        }

        String storedPassword = users.get(email);

        if (storedPassword == null) {
            return "KO|Email not found";
        }

        if (storedPassword.equals(password)) {
            DELETE_EmailAndToken(email);
            String token = generateRandomString();
            usersToken.put(email, token);
            return "OK|" + token;
        } else {
            return "KO|Incorrect password";
        }
    }

    public static String PUT_EmailAndPassword(String email, String password) {
        if (!isValidEmail(email)) {
            return "KO|Invalid Email Format";
        }

        if (!users.containsKey(email)) {
            return "KO|Email doesn't exists";
        }

        users.remove(email);
        users.put(email, password);
        return "OK";
    }

    public static String DELETE_EmailAndPassword(String email, String password) {
        if (!isValidEmail(email)) {
            return "KO|Invalid Email Format";
        }

        if (!users.containsKey(email)) {
            return "KO|Email doesn't exists";
        }

        users.remove(email);
        return "OK";
    }

    public static String GET_EmailAndToken(String email, String token) {
        if (!isValidEmail(email)) {
            return "KO|Invalid Email Format";
        }

        String storedToken = usersToken.get(email);

        if (storedToken == null) {
            return "KO|Email not logged";
        }

        if (storedToken.equals(token)) {
            return "OK";
        } else {
            return "KO|Incorrect token";
        }
    }

    public static String DELETE_EmailAndToken(String email) {
        if (!isValidEmail(email)) {
            return "KO|Invalid Email Format";
        }

        if(usersToken.get(email) == null) {
            return "KO|No active token";
        }

        usersToken.remove(email);
        return "OK";
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private static String generateRandomString() {
        Random random = new Random();
        int length = random.nextInt(21) + 10;
        return UUID.randomUUID().toString().substring(0, length);
    }
}


