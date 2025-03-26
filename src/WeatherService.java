package src;

import org.json.JSONObject;

import src.UI.BackgroundCreater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;


public class WeatherService {
    private static final String API_KEY = Config.getApiKey();

    public String normalizeCityName(String city) {
        String normalized = Normalizer.normalize(city, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{M}");
        city = pattern.matcher(normalized).replaceAll("");
        return city.replaceAll("ñ", "%C3%B1");
    }

    public WeatherData getWeather(String city) {
        try {
            String normalizedCity = normalizeCityName(city);
            String encodedCity = URLEncoder.encode(normalizedCity, StandardCharsets.UTF_8.toString());

            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            JSONObject jsonResponse = new JSONObject(result.toString());
            long timezoneOffset = jsonResponse.getLong("timezone"); 
            long sunriseTimestamp = jsonResponse.getJSONObject("sys").getLong("sunrise") + timezoneOffset;
            long sunsetTimestamp = jsonResponse.getJSONObject("sys").getLong("sunset") + timezoneOffset;
            String weatherCondition = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("main");
            String iconCode = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("icon");
            int temperature = (int) jsonResponse.getJSONObject("main").getDouble("temp");
            int humidity = jsonResponse.getJSONObject("main").getInt("humidity");
            int windSpeed = (int) jsonResponse.getJSONObject("wind").getDouble("speed");
            BackgroundCreater background = new BackgroundCreater(sunriseTimestamp, sunsetTimestamp);
            String backgroundUrl = background.getBackground(timezoneOffset);

            return new WeatherData(temperature, humidity, windSpeed, weatherCondition, iconCode, backgroundUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public WeatherData getWeatherByCoordinates(double lat, double lon) {
        try {
            String urlString = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon
                             + "&appid=" + API_KEY + "&units=metric";
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
    
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
    
            JSONObject jsonResponse = new JSONObject(result.toString());
            String weatherCondition = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("main");
            String iconCode = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("icon");
            int temperature = (int) jsonResponse.getJSONObject("main").getDouble("temp");
            int humidity = jsonResponse.getJSONObject("main").getInt("humidity");
            int windSpeed = (int) jsonResponse.getJSONObject("wind").getDouble("speed");
            long timezoneOffset = jsonResponse.getLong("timezone"); 
            long sunriseTimestamp = jsonResponse.getJSONObject("sys").getLong("sunrise") + timezoneOffset;
            long sunsetTimestamp = jsonResponse.getJSONObject("sys").getLong("sunset") + timezoneOffset;
            BackgroundCreater background = new BackgroundCreater(sunriseTimestamp, sunsetTimestamp);
            String backgroundUrl = background.getBackground(timezoneOffset);
    
            return new WeatherData(temperature, humidity, windSpeed, weatherCondition, iconCode, backgroundUrl);
    
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
