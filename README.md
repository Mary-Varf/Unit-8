# WeatherApp

**WeatherApp** is a JavaFX-based desktop application that provides real-time weather information for any location. It uses the **OpenWeatherMap API** to fetch current weather data and forecasts.

[![Watch the video](https://github.com/Mary-Varf/Unit-8/blob/main/Screenshot_2.png)](https://jmp.sh/s/nzZgXBBfILFur4ExY6Pw)

---

## Features

- **Location Detection:** Automatically detects the user's location based on IP address.
- **Search Functionality:** Search for weather by city name or geographic coordinates.
- **Temperature Units:** Toggle between Celsius and Fahrenheit.
- **Wind Speed Units:** Switch between meters/second and miles/hour.
- **Forecast Display:** Shows a short-term weather forecast.
- **Search History:** Maintains a history of recent searches.
- **Dynamic Background:** Changes the background image based on the time of day.

---

## Technologies Used

- **JavaFX** for GUI components.
- **OpenWeatherMap API** for fetching weather data.
- **IP-API** for location detection.
- **JSON** for data parsing.
- **HTTP requests** for API calls.

---

## Setup and Installation

### Prerequisites
- JDK 11+ installed.
- Internet connection for fetching weather data.

### Clone the Repository

git clone <repository-url>
cd WeatherApp

---

### API Key
1. Register at OpenWeatherMap to get a free API key.

2. Replace the placeholder in config.properties with your API key: API_KEY=YOUR_API_KEY

---
### Compile and Run
- javac -d bin -cp "lib/*" src/WeatherApp.java src/UI/Switcher.java src/ForecastPanel.java src/HistoryPanel.java src/WeatherData.java src/WeatherService.java src/UI/BackgroundCreater.java src/Config.java src/UI/StyledButton.java src/UI/StyledTextField.java
- java --module-path "C:/Program Files/Java/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "bin;lib/*" src.WeatherApp



