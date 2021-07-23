package com.weather.weather.controller;

import com.weather.weather.kafka.WeatherInfoEventListener;
import com.weather.weather.kafka.consumer.WeatherInfoEventProcessor;
import com.weather.weather.model.WeatherInfoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class WeatherInfoController {

    @Autowired
    private WeatherInfoEventProcessor processor;

    private final Flux<WeatherInfoEvent> bridge;

    public WeatherInfoController() {
        this.bridge = createBridge().publish().autoConnect().cache(10).log();
    }

    @GetMapping(value = "/weather", produces = "text/event-stream;charset=UTF-8")
    public Flux<WeatherInfoEvent> getWeatherInfo() {
        return bridge;
    }

    private Flux<WeatherInfoEvent> createBridge() {
        return Flux.create(sink -> {
            processor.register(new WeatherInfoEventListener() {

                @Override
                public void processComplete() {
                    sink.complete();
                }

                @Override
                public void onData(WeatherInfoEvent data) {
                    sink.next(data);
                }
            });
        });
    }
}
