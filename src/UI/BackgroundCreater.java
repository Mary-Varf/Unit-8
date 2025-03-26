package src.UI;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class BackgroundCreater {
    private Long sunrise;
    private Long sunset;
    final String urlSummerDay = "file:src/img/day.jpg";
    final String urlNight = "file:src/img/night.jpg";
    final String urlMorning = "file:src/img/sunrise.jpg";
    final String urlSunSet = "file:src/img/sunset.png";

    public BackgroundCreater (Long sunrise, Long sunset) {
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getBackground(long timezoneOffset) {
        final String url;

        ZonedDateTime gmtTime = ZonedDateTime.now(ZoneId.of("GMT"));
        long gmtTimeInSeconds = gmtTime.toEpochSecond();
        long timeInLocationInSeconds = gmtTimeInSeconds + timezoneOffset;

        LocalDateTime nowTimeZone = LocalDateTime.ofEpochSecond(timeInLocationInSeconds, 0, ZoneOffset.UTC); 
        int hour = nowTimeZone.getHour();
        LocalDateTime sunriseDate = LocalDateTime.ofEpochSecond(sunrise, 0, ZoneOffset.UTC);
        LocalDateTime sunsetDate = LocalDateTime.ofEpochSecond(sunset, 0, ZoneOffset.UTC);
        int sunriseHour = sunriseDate.getHour();
        int sunsetHour = sunsetDate.getHour();
        
        if (hour < sunriseHour) {
            url = urlNight;
        }
        else if (hour < sunriseHour + 1) {
            url = urlMorning;
        } else if (hour < sunsetHour - 1) {
            url = urlSummerDay;
        }
        else if (hour < sunsetHour) {
            url = urlSunSet;
        }
        else {
            url = urlNight;
        }

        return url;
    }
}
