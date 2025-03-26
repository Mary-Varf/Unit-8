package src;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import src.UI.StyledButton;
import src.UI.StyledTextField;
import src.UI.Switcher;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherApp extends Application {
    WeatherData weatherData;
    private String city;
    private Label cityLabel;
    private Label temperatureLabel;
    private Label humidityLabel;
    private Label windSpeedLabel;
    private ImageView weatherIcon;
    private StyledTextField cityInput;
    private Switcher switcherDegree;
    private Switcher switcherSpeed;
    private boolean isCelsius = true;
    private boolean isMH = true;
    private VBox layout;

    private WeatherService weatherService;
    private ForecastPanel forecastPanel;
    private HistoryPanel historyPanel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather App");

        cityLabel = new Label("Location: --");
        cityLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        
        temperatureLabel = new Label("--");
        temperatureLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");
        
        humidityLabel = new Label("Humidity: --");
        windSpeedLabel = new Label("Wind Speed: --");
        cityInput = new StyledTextField("Please enter a location");
        cityInput.setPromptText("Please enter a location");
        weatherIcon = new ImageView();

        weatherService = new WeatherService();
        forecastPanel = new ForecastPanel();
        historyPanel = new HistoryPanel();

        cityInput.setOnAction(e -> {
            String input = cityInput.getText().trim();
            if (!input.isEmpty()) {
                fetchWeather(input);
            } else {
                showPopup("Please enter a location.");
            }
        });
        
        StyledButton refreshButton = new StyledButton("Search");
        refreshButton.setOnAction(e -> {
            String input = cityInput.getText().trim();
            if (!input.isEmpty()) {
                fetchWeather(input);
            } else {
                showPopup("Please enter a location.");
            }
        });

        HBox inputBox = new HBox(10, cityInput, refreshButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));

        switcherDegree = new Switcher(isCelsius, "C", "F");
        switcherSpeed = new Switcher(isMH, "m/s", "mph");

        HBox switcherBox = new HBox(10, cityLabel, switcherDegree, switcherSpeed);
        switcherBox.setAlignment(Pos.CENTER);
        switcherBox.setPadding(new Insets(10));

        switcherDegree.setOnToggleAction(() -> {
            isCelsius = switcherDegree.isCelsius();
            String temperature = isCelsius ? weatherData.getTemperature() + " C"
                                           : convertToFahrenheit(weatherData.getTemperature()) + " F";
            temperatureLabel.setText(temperature);
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String historyEntry = timeStamp + " - " + city + " | " + weatherData.getWeatherCondition()
                                    + " | " + temperature;
            historyPanel.addEntry(historyEntry);
            forecastPanel.updateTemperatureUnit(isCelsius);
        });

        switcherSpeed.setOnToggleAction(() -> {
            isMH = switcherSpeed.isCelsius();
            windSpeedLabel.setText("Wind Speed: " + (isMH ? weatherData.getWindSpeed() + " m/s"
                                                         : convertToMph(weatherData.getWindSpeed()) + " mph"));
        });

        HBox weatherInfoBox = new HBox(20, 
            new VBox(5, temperatureLabel, humidityLabel, windSpeedLabel), 
            new VBox(weatherIcon));
        weatherInfoBox.setAlignment(Pos.CENTER);
        weatherInfoBox.setPadding(new Insets(10));

        VBox forecastBox = new VBox(10, forecastPanel.getForecastGrid());
        forecastBox.setPadding(new Insets(0, 20, 0, 20));
        
        VBox historyBox = new VBox(10, historyPanel.getHistoryArea());
        historyBox.setPadding(new Insets(0, 20, 20, 20));

        layout = new VBox(20, inputBox, switcherBox, weatherInfoBox, forecastBox, historyBox);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #87CEEB;");

        Scene scene = new Scene(layout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        fetchUserLocation();
    }

    private void setBackground(String imageUrl) {
        BackgroundImage backgroundImage = new BackgroundImage(
            new Image(imageUrl, 500, 600, false, true),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT
        );
        layout.setBackground(new Background(backgroundImage));
    }

    private void showPopup(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Location Not Found");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void fetchUserLocation() {
        new Thread(() -> {
            try {
                String urlString = "http://ip-api.com/json?lang=en";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Charset", "UTF-8");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(result.toString());
                city = jsonResponse.getString("city");

                Platform.runLater(() -> fetchWeather(city));

            } catch (Exception e) {
                Platform.runLater(() -> cityLabel.setText("Could not detect location"));
            }
        }).start();
    }

    private void fetchWeather(String input) {
        if (input.isEmpty()) {
            showPopup("Please enter a location.");
            return;
        }

        new Thread(() -> {
            if (input.matches("-?\\d+(\\.\\d+)?\\s*,\\s*-?\\d+(\\.\\d+)?")) {
                String[] parts = input.split(",");
                double lat = Double.parseDouble(parts[0].trim());
                double lon = Double.parseDouble(parts[1].trim());
                if (weatherService.getWeatherByCoordinates(lat, lon) != null) {
                    weatherData = weatherService.getWeatherByCoordinates(lat, lon);
                }
            } else if (weatherService.getWeather(input) != null) {
                weatherData = weatherService.getWeather(input);
            } else {
                showPopup("Invalid location.");
                return;
            }

            Platform.runLater(() -> {
                if (weatherData != null) {
                    String iconUrl = "http://openweathermap.org/img/wn/" + weatherData.getIconCode() + "@2x.png";
                    Image image = new Image(iconUrl);

                    LocalDateTime now = LocalDateTime.now();
                    String temperature = isCelsius ? weatherData.getTemperature() + " C"
                                                   : convertToFahrenheit(weatherData.getTemperature()) + " F";
                    temperatureLabel.setText(temperature);
                    String timeStamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    String historyEntry = timeStamp + " - " + input + " | " + weatherData.getWeatherCondition()
                                          + " | " + temperature;

                    cityLabel.setText(capitalizeWords(input));
                    humidityLabel.setText("Humidity: " + weatherData.getHumidity() + "%");
                    windSpeedLabel.setText("Wind Speed: " + (isMH ? weatherData.getWindSpeed() + " m/s"
                                                                  : convertToMph(weatherData.getWindSpeed()) + " mph"));
                    weatherIcon.setImage(image);

                    historyPanel.addEntry(historyEntry);
                    forecastPanel.fetchForecast(input);
                    setBackground(weatherData.getBgUrl());
                } else {
                    showPopup("Location not found. Please try again.");
                }
            });
        }).start();
    }

    public static String capitalizeWords(String input) {
        String[] words = input.split("\\s");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(Character.toTitleCase(word.charAt(0)))
                  .append(word.substring(1))
                  .append(" ");
        }
        return result.toString().trim();
    }

    private int convertToFahrenheit(int celsius) {
        return (int) (celsius * 9 / 5.0 + 32);
    }

    private int convertToMph(int windSpeedMps) {
        return (int) (windSpeedMps * 2.23694);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
