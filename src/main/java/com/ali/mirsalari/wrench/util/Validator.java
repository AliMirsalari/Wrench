package com.ali.mirsalari.wrench.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean isValidPassword(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%^&+=])(?=\\S+$).{8,}$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(password);
        return matcher.matches();
    }
    public static boolean isValidEmail(String email) {
        String pattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidImage(File file) {
        String filename = file.getName();
        if (!filename.toLowerCase().endsWith(".jpg")) {
            return false;
        }
        long fileSize = file.length();
        int maxSizeInKB = 300;
        return fileSize <= (maxSizeInKB * 1024); // Convert maxSizeInKB to bytes
    }


}