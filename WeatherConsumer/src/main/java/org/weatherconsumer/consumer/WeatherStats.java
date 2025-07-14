package org.weatherconsumer.consumer;

import java.util.HashMap;
import java.util.Map;

public class WeatherStats {

    private int totalEvents;
    private final Map<String, Integer> sunnyDays = new HashMap<>();
    private final Map<String, Integer> rainyDays = new HashMap<>();
    private final Map<String, Double> avgTemperatures = new HashMap<>();
    private final Map<String, Integer> cityEventCount = new HashMap<>();
    private int maxRainyDays = 0;
    private String maxRainyCity = "";
    private int maxSunnyDays = 0;
    private String maxSunnyCity = "";
    private int maxTemperature = Integer.MIN_VALUE;
    private String hottestCity = "";
    private int minTemperature = Integer.MAX_VALUE;
    private String coldestCity = "";

    public void updateStats(String city, int temperature, String condition) {
        totalEvents++;

        if ("солнечно".equals(condition)) {
            sunnyDays.put(city, sunnyDays.getOrDefault(city, 0) + 1);
            if (sunnyDays.get(city) > maxSunnyDays) {
                maxSunnyDays = sunnyDays.get(city);
                maxSunnyCity = city;
            }
        }

        if ("дождь".equals(condition)) {
            rainyDays.put(city, rainyDays.getOrDefault(city, 0) + 1);
            if (rainyDays.get(city) > maxRainyDays) {
                maxRainyDays = rainyDays.get(city);
                maxRainyCity = city;
            }
        }

        int count = cityEventCount.getOrDefault(city, 0) + 1;
        cityEventCount.put(city, count);
        double avg = avgTemperatures.getOrDefault(city, 0.0);
        avgTemperatures.put(city, (avg * (count - 1) + temperature) / count);

        if (temperature > maxTemperature) {
            maxTemperature = temperature;
            hottestCity = city;
        }
        if (temperature < minTemperature) {
            minTemperature = temperature;
            coldestCity = city;
        }
    }

    public String getSummary() {
        return String.format("""
            === Статистика (обработано %d событий) ===
            Город с самыми солнечными днями: %s (%d дней)
            Город с самыми дождливыми днями: %s (%d дней)
            Самая высокая температура: %d°C (%s)
            Самая низкая температура: %d°C (%s)
            Средние температуры:
            %s
            ========================================
            """,
                totalEvents,
                maxSunnyCity, maxSunnyDays,
                maxRainyCity, maxRainyDays,
                maxTemperature, hottestCity,
                minTemperature, coldestCity,
                formatAverages()
        );
    }

    private String formatAverages() {
        StringBuilder sb = new StringBuilder();
        avgTemperatures.forEach((city, temp) ->
                sb.append(String.format("  %s: %.1f°C%n", city, temp)));
        return sb.toString();
    }
}
