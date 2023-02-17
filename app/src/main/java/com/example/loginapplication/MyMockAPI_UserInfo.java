package com.example.loginapplication;

import java.util.HashMap;
import java.util.regex.Pattern;

public class MyMockAPI_UserInfo {
    private static HashMap<String, String> usersInfo = new HashMap<>();

    public static String GET_UserInfo(String email, String token) {
        if (!isValidEmail(email)){
            return "KO|Invalid Email Format";
        }

        if (!MyMockAPI_Credentials.GET_EmailAndToken(email, token).equals("OK")) {
            return "KO|Token expired";
        }

        return usersInfo.get(email);

    }

    public static String POST_UserInfo(String email, String info, String token) {
        if (!isValidEmail(email)){
            return "KO|Invalid Email Format";
        }

        if (!MyMockAPI_Credentials.GET_EmailAndToken(email, token).equals("OK")) {
            return "KO|Token expired";
        }

        if (usersInfo.containsKey(email)) {
            return "KO|User Info already exists";
        }

        usersInfo.put(email, info);
        return "OK";
    }

    public static String PUT_UserInfo(String email, String info, String token) {
        if (!isValidEmail(email)) {
            return "KO|Invalid Email Format";
        }

        if (!MyMockAPI_Credentials.GET_EmailAndToken(email, token).equals("OK")) {
            return "KO|Token expired";
        }

        if (!usersInfo.containsKey(email)) {
            return "KO|Email not found";
        }

        usersInfo.remove(email);
        usersInfo.put(email, info);
        return "OK";
    }

    public static String DELETE_UserInfo(String email, String token) {
        if (!isValidEmail(email)){
            return "KO|Invalid Email Format";
        }

        if (!MyMockAPI_Credentials.GET_EmailAndToken(email, token).equals("OK")) {
            return "KO|Token expired";
        }

        if (!usersInfo.containsKey(email)) {
            return "KO|Email not found";
        }

        usersInfo.remove(email);
        return "OK";
    }


    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
