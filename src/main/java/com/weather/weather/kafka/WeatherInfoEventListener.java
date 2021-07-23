package com.weather.weather.kafka;

import com.weather.weather.model.WeatherInfoEvent;

public interface WeatherInfoEventListener {
    void onData(WeatherInfoEvent event);
    void processComplete();
}
