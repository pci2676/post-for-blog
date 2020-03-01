package com.javabom.di;

import com.javabom.di.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class DiContainer {

    public static <T> T getObjectByConstructor(Class<T> type) {
        T instance = createInstance(type);
        autowired(instance);

        return instance;
    }

    private static <T> void autowired(T instance) {
        Arrays.stream(instance.getClass().getDeclaredFields())
                .forEach(field -> {
                    if (field.getAnnotation(Autowired.class) == null) {
                        return;
                    }
                    autowired(instance, field);
                });
    }

    private static <T> void autowired(T instance, Field field) {
        try {
            Object filedInstance = createInstance(field.getType());
            field.setAccessible(true);
            field.set(instance, filedInstance);
        } catch (IllegalAccessException e) {
            throw new NoSuchElementException();
        }
    }

    private static <T> T createInstance(Class<T> type) {
        Constructor<T> constructor = null;
        try {
            constructor = type.getConstructor(null);
            return constructor.newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }
}
