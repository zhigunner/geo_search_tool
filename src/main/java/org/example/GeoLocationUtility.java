package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class GeoLocationUtility {

    private static final String API_KEY = "f897a99d971b5eef57be6fafa0d83239";
    private static final String BASE_URL = "http://api.openweathermap.org/geo/1.0/";

    private final OkHttpClient client = new OkHttpClient();

    public String getLocationData(String query) throws IOException {
        String url = BASE_URL + "direct?q=" + query + ",US&limit=1&appid=" + API_KEY;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();

            if (jsonArray.size() > 0) {
                JsonObject locationData = jsonArray.get(0).getAsJsonObject();
                String name = locationData.get("name").getAsString();
                double lat = locationData.get("lat").getAsDouble();
                double lon = locationData.get("lon").getAsDouble();
                String country = locationData.get("country").getAsString();
                String state = locationData.has("state") ? locationData.get("state").getAsString() : "N/A";

                return String.format("Location: %s, State: %s, Country: %s, Lat: %.4f, Lon: %.4f",
                        name, state, country, lat, lon);
            } else {
                return "No results found for the given location.";
            }
        }
    }

    public String getZipData(String zip) throws IOException {
        String url = BASE_URL + "zip?zip=" + zip + ",US&appid=" + API_KEY;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JsonObject locationData = JsonParser.parseString(responseBody).getAsJsonObject();

            String name = locationData.get("name").getAsString();
            double lat = locationData.get("lat").getAsDouble();
            double lon = locationData.get("lon").getAsDouble();
            String country = locationData.get("country").getAsString();

            return String.format("Location: %s, Country: %s, Lat: %.4f, Lon: %.4f",
                    name, country, lat, lon);
        }
    }

    public static void main(String[] args) {
        GeoLocationUtility util = new GeoLocationUtility();

        for (String location : args) {
            try {
                if (location.matches("\\d{5}")) {
                    System.out.println(util.getZipData(location));
                } else {
                    System.out.println(util.getLocationData(location));
                }
            } catch (IOException e) {
                System.out.println("Error fetching data for: " + location);
                e.printStackTrace();
            }
        }
    }
}
