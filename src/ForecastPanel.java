package src;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ForecastPanel {
    private static final String API_KEY = Config.getApiKey();
    private GridPane forecastGrid;
    private boolean isCelcius = true;
    JSONArray forecastList;

    public ForecastPanel() {
        forecastGrid = new GridPane();
        forecastGrid.setHgap(10);
        forecastGrid.setVgap(5);
        forecastGrid.setPadding(new Insets(10));
        forecastGrid.setAlignment(Pos.CENTER);
        forecastGrid.setStyle("-fx-background-color: white; -fx-padding: 5px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }

    public GridPane getForecastGrid() {
        return forecastGrid;
    }

    public String normalizeCityName(String city) {
        String normalized = Normalizer.normalize(city, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{M}");
        city = pattern.matcher(normalized).replaceAll("");
        return city.replaceAll("ñ", "%C3%B1");
    }

    public void updateTemperatureUnit(boolean isCelsius) {
        this.isCelcius = isCelsius;
        updateForecastGrid(forecastList, isCelcius);
    }

    public void fetchForecast(String city) {
        new Thread(() -> {
            try {
                String normalizedCity = normalizeCityName(city);
                String encodedCity = URLEncoder.encode(normalizedCity, StandardCharsets.UTF_8.toString());
                String urlString = "http://api.openweathermap.org/data/2.5/forecast?q=" + encodedCity + "&appid=" + API_KEY + "&units=metric";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(result.toString());
                forecastList = jsonResponse.getJSONArray("list");

                Platform.runLater(() -> updateForecastGrid(forecastList, isCelcius));
            } catch (Exception e) {
                System.out.println(e);
            }
        }).start();
    }

    private void updateForecastGrid(JSONArray forecastList, boolean isCelsius) {
        forecastGrid.getChildren().clear();
        
        forecastGrid.add(new Label("Time"), 0, 0);
        for (int i = 0; i < 5; i++) {
            Label label = new Label((i + 1) * 3 + "h");
            label.setStyle("-fx-padding: 0 0 0 10;");
            forecastGrid.add(label, i + 1, 0);

        }

        forecastGrid.add(new Label("Weather"), 0, 1);
        for (int i = 0; i < 5; i++) {
            JSONObject entry = forecastList.getJSONObject(i);
            String iconCode = entry.getJSONArray("weather").getJSONObject(0).getString("icon");
            String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";
            ImageView icon = new ImageView(new Image(iconUrl));
            icon.setFitWidth(40);
            icon.setFitHeight(40);
            
            forecastGrid.add(icon, i + 1, 1);
        }

        forecastGrid.add(new Label("Temp"), 0, 2);
        for (int i = 0; i < 5; i++) {
            JSONObject entry = forecastList.getJSONObject(i);
            int temp = (int) entry.getJSONObject("main").getDouble("temp");
            String temperature = isCelsius ? temp + " C" : convertToFahrenheit(temp) + " F";
            Label label = new Label(temperature);
            label.setStyle("-fx-padding: 0 0 0 5;");
            
            forecastGrid.add(label, i + 1, 2);
        }
    }
    private int convertToFahrenheit(int celsius) {
        return (int) (celsius * 9 / 5.0 + 32);
    }
}