/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This file uses the location and optional keywords that wre given as input by the user to make a
 * URL request to the Eventful API.
 */

package com.example.tirza.soutetirza_pset62;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class HttpRequest {

    /** Uses the URL that was given by the user to make a request to the Eventful API */
    static synchronized String downloadFromAPI(String... params) {
        String urlToApi = "http://api.eventful.com/rest/events/search?...";
        String key = "&app_key=4HnrhJhqgB996TGp";
        String location = "&location=" + params[0];
        String keywords = "";
        if (!(params[1].equals(""))) keywords = "&keywords=" + params[1];
        String urlRequest = urlToApi + key + location + keywords;
        URL url;
        String result = "";

        try {
            url = new URL(urlRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return result;
        }

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            Integer responseCode = connection.getResponseCode();
            if (200 >= responseCode && responseCode <= 299) {
                InputStreamReader input = new InputStreamReader(connection.getInputStream());
                BufferedReader br = new BufferedReader(input);
                String line;
                while ((line = br.readLine()) != null) {
                    result = result + line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}