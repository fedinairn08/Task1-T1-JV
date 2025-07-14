package org.weatherproducer.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class WeatherProducer {

    private static final String TOPIC = "weather-topic";

    private final KafkaTemplate<String, WeatherEvent> kafkaTemplate;

    private final Random random = new Random();

    private final List<String> cities = List.of(
            "Москва", "Санкт-Петербург", "Рязань", "Тула", "Нижний Новгород"
    );

    private final List<String> conditions = List.of(
            "солнечно", "облачно", "дождь"
    );

    public WeatherProducer(KafkaTemplate<String, WeatherEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 2000)
    public void generateWeatherEvent() {
        String city = cities.get(random.nextInt(cities.size()));
        int temperature = random.nextInt(36);
        String condition = conditions.get(random.nextInt(conditions.size()));
        LocalDateTime eventDate = LocalDateTime.now().minusDays(random.nextInt(7));

        WeatherEvent event = new WeatherEvent(city, temperature, condition, eventDate);
        kafkaTemplate.send(TOPIC, city, event);
        System.out.printf("Отправлено: %s - %d°C, %s (%s)%n",
                city, temperature, condition, eventDate);
    }
}
