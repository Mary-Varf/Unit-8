package src;

public class WeatherData {
    private int temperature;
    private int humidity;
    private int windSpeed;
    private String weatherCondition;
    private String iconCode;
    private String bgUrl;

    public WeatherData(int temperature, int humidity, int windSpeed, String weatherCondition, String iconCode, String bgUrl) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.weatherCondition = weatherCondition;
        this.iconCode = iconCode;
        this.bgUrl = bgUrl;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public String getIconCode() {
        return iconCode;
    }

    public String getBgUrl() {
        return bgUrl;
    }
}