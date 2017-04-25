package com.jagod.goodbyehomework.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jagod.goodbyehomework.activities.LobbyActivity;
import com.jagod.goodbyhomework.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * Создано Юрием в 16.04.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class Client {

    public static final String SERVER_URL = "https://goodbye-homework.herokuapp.com";
    public static final String SIGN_IN = SERVER_URL + "/signin";
    public static final String SIGN_UP = SERVER_URL + "/register";

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String SEX = "sex";
    public static final String MAIL = "mail";
    public static final String PASSWORD = "password";

    private static boolean signIn(String mail, String password) {
        String response = post(SIGN_IN, String.format("%s=%s&%s=%s", MAIL, escapeHtml4(mail), PASSWORD, escapeHtml4(password)));
        response = response.trim();
        return (response.equals("0"));
    }

    public static boolean signInIfCan(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (preferences.contains(activity.getString(R.string.db_login)) && preferences.contains(activity.getString(R.string.db_password))) {
            return signIn(preferences.getString(activity.getString(R.string.db_login), ""), preferences.getString(activity.getString(R.string.db_login), ""), activity);
        }
        return false;
    }

    public static boolean signIn(String mail, String password, Activity activity) {

        if (signIn(mail, password)) {
            if (activity != null) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor editor = preferences.edit();
                if (preferences.contains(activity.getString(R.string.db_login))) {
                    editor.remove(activity.getString(R.string.db_login));
                }
                if (preferences.contains(activity.getString(R.string.db_password))) {
                    editor.remove(activity.getString(R.string.db_password));
                }
                editor.commit();
                editor.putString(activity.getString(R.string.db_login), mail);
                editor.putString(activity.getString(R.string.db_password), password);
                editor.commit();
                Intent i = new Intent(activity, LobbyActivity.class);
                activity.startActivity(i);
            }
            return true;
        }

        return false;
    }

    public static boolean signUp(String firstName, String lastName, String mail, String password, Activity activity) {
        String response = post(SIGN_UP, String.format("%s=%s&%s=%s&%s=%s&%s=%s", FIRST_NAME, escapeHtml4(firstName), LAST_NAME, escapeHtml4(lastName), MAIL, escapeHtml4(mail), PASSWORD, escapeHtml4(password)));

        if (response.equals("0")) {
            signIn(mail, password, activity);
            return true;
        } else return false;
    }

    private static String post(final String targetURL, final String urlParameters) {

        String response = excutePost(targetURL, urlParameters);

        return response.trim();


    }

    private static String excutePost(String targetURL, String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.write(urlParameters.getBytes("UTF-8"));
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return excutePost(targetURL, urlParameters);

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }


}
