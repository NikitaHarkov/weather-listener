package com.weather.weather.service;

import com.weather.weather.model.WeatherInfoEvent;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class WeatherInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherInfoService.class);

    @Autowired
    private KafkaTemplate<String, WeatherInfoEvent> kafkaTemplate;

    public ListenableFuture<SendResult<String, WeatherInfoEvent>> sendMessage(String topic, WeatherInfoEvent message) {
        LOGGER.info(String.format("#### -> Producing message -> %s", message));
        return this.kafkaTemplate.send(topic, message);
    }

    @Scheduled(fixedDelay = 5000)
    public void getWeatherInfoJob() {
        LOGGER.info("generate fake weather event");
        // fake event
        WeatherInfoEvent event = new WeatherInfoEvent(RandomUtils.nextLong(0,100), RandomUtils.nextInt(16, 30));
        sendMessage("weather", event);
    }
}
