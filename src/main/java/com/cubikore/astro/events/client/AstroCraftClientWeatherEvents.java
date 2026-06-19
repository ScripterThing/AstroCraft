package com.cubikore.astro.events.client;

import com.cubikore.astro.weather.ClientWeatherManager;

@FunctionalInterface
public interface AstroCraftClientWeatherEvents {
    void onRegisteringClientWeather(ClientWeatherManager clientWeatherManager);

    AstroCraftEvent<AstroCraftClientWeatherEvents> WEATHER_REGISTERING = AstroCraftEvent.create(listeners -> clientWeatherManager -> {
        for(AstroCraftClientWeatherEvents listener : listeners) {
            listener.onRegisteringClientWeather(clientWeatherManager);
        }
    });
}
