package org.weatherconsumer.consumer;

import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;

@Component
public class WeatherConsumer {

    private final WeatherStats stats = new WeatherStats();

    @KafkaListener(topics = "weather-topic", groupId = "weather-group")
    public void consume(WeatherEvent event) {
        stats.updateStats(
                event.city(),
                event.temperature(),
                event.condition()
        );

        if (stats.getTotalEvents() % 10 == 0) {
            System.out.println(stats.getSummary());
        }
    }
}
