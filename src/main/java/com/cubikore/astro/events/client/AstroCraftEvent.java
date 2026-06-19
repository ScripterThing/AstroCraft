package com.cubikore.astro.events.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AstroCraftEvent<T> {
    private final List<T> listeners = new ArrayList<>();
    private final Function<List<T>, T> invokerFactory;

    private T invoker;

    private AstroCraftEvent(Function<List<T>, T> invokerFactory) {
        this.invokerFactory = invokerFactory;
        this.invoker = invokerFactory.apply(listeners);
    }

    public static <T> AstroCraftEvent<T> create(Function<List<T>, T> invokerFactory) {
        return new AstroCraftEvent<>(invokerFactory);
    }

    public void register(T listener) {
        listeners.add(listener);
        invoker = invokerFactory.apply(List.copyOf(listeners));
    }

    public T invoker() {
        return invoker;
    }
}
