package com.weather.weather.kafka.consumer;

import com.weather.weather.kafka.WeatherInfoEventListener;
import com.weather.weather.model.WeatherInfoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WeatherInfoEventProcessor {
    private static final Logger logger = LoggerFactory.getLogger(WeatherInfoEventProcessor.class);
    private WeatherInfoEventListener listener;

    public void register(WeatherInfoEventListener listener) {
        this.listener = listener;
    }

    public void onEvent(WeatherInfoEvent event) {
        if (listener != null) {
            listener.onData(event);
        }
    }

    public void onComplete() {
        if (listener != null) {
            listener.processComplete();
        }
    }

    @KafkaListener(topics = "weather", groupId = "group_id")
    public void consume(WeatherInfoEvent message) {
        logger.info(String.format("#### -> Consumed message -> %s", message));
        onEvent(message);
    }
}
