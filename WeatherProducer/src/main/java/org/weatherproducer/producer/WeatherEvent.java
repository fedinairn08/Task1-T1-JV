package org.weatherproducer.producer;

import java.time.LocalDateTime;

public record WeatherEvent(
        String city,
        int temperature,
        String condition,
        LocalDateTime eventDate
) { }
