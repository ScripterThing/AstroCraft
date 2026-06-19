package com.cubikore.astro.events.client;

import com.cubikore.astro.weather.WeatherManager;

@FunctionalInterface
public interface AstroCraftPlanetRegisterEvents {
    void onPlanetRegistering(WeatherManager weatherManager);

    AstroCraftEvent<AstroCraftPlanetRegisterEvents> PLANET_REGISTER = AstroCraftEvent.create(listeners -> weatherManager -> {
        for(AstroCraftPlanetRegisterEvents listener : listeners) {
            listener.onPlanetRegistering(weatherManager);
        }
    });
}
