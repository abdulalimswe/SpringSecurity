package com.utin.oj.utils;

public class EmailUtils {
    public static String getEmailMessage(String name, String host, String key){
        return "Hello " + name + ",\n\nYour new account has been created.Please click on the link below to verify your account.\n\n" +
                getVerificationUrl(host,key) + "\n\nThe Support Team";
    }
    public static String getResetPasswordMessage(String name, String host, String token){
        return "Hello " + name + ",\n\nPlease click on the link below to reset your account.\n\n" +
                getResetPasswordUrl(host,token) + "\n\nThe Support Team";
    }

    public static String getVerificationUrl(String host, String key){
        return host + "/user/verify/account?key="+key;
    }

    public static String getResetPasswordUrl(String host, String token){
        return host + "/verify/password?token="+token;
    }
}
