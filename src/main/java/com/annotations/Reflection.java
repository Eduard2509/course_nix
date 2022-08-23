package com.annotations;

import com.repository.AutoRepository;
import com.repository.BusinessAutoRepository;
import com.service.AutoService;
import com.service.BusinessAutoService;
import com.service.SportAutoService;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class Reflection {
    HashMap<Class<?>, Object> cache = new HashMap<>();

    @SneakyThrows
    public void getSingleton() {
        Reflections reflection = new Reflections("com");
        Set<Class<?>> set = reflection.getTypesAnnotatedWith(Singleton.class);
        for (Class<?> cla : set) {
            Field[] instance = cla.getDeclaredFields();
            Constructor<?>[] declaredConstructors = cla.getDeclaredConstructors();
            for (Constructor<?> declaredConstructor : declaredConstructors) {
                createClassAndInstanceWithoutParameters(instance, declaredConstructor, cla);
                createClassAndInstanceWithParameters(declaredConstructor, instance, cla);
            }
        }
    }

    @SneakyThrows
    private void setInstance(Field[] instance, Class<?> cla, Object o) {
        for (Field field : instance) {
            if (field.getName().equals("instance")) {
                field.setAccessible(true);
                field.set(null, o);
                cache.put(cla, o);
            }
        }
    }

    @SneakyThrows
    private void createClassAndInstanceWithoutParameters(
            Field[] instance, Constructor<?> declaredConstructor, Class<?> cla) {
        if (declaredConstructor.getParameterCount() == 0) {
            for (Field field : instance) {
                if (field.getName().equals("instance")) {
                    field.setAccessible(true);
                    field.set(null, declaredConstructor.newInstance());
                    cache.put(cla, declaredConstructor.newInstance());
                }
            }
            cache.put(cla, declaredConstructor.newInstance());
        }
    }

    private void createClassAndInstanceWithParameters(
            Constructor<?> declaredConstructor, Field[] instance, Class<?> cla) {
        try {
            if (declaredConstructor.isAnnotationPresent(Autowired.class)
                    && declaredConstructor.getParameterCount() == 1) {
                if (AutoService.class.equals(cla)) {
                    Object o = declaredConstructor.newInstance(cache.get(AutoRepository.class));
                    setInstance(instance, cla, o);
                }
                if (BusinessAutoService.class.equals(cla)) {
                    Object o = declaredConstructor.newInstance(cache.get(BusinessAutoRepository.class));
                    setInstance(instance, cla, o);
                }
                if (SportAutoService.class.equals(cla)) {
                    Object o = declaredConstructor.newInstance(cache.get(SportAutoService.class));
                    setInstance(instance, cla, o);
                }
            }
        } catch (InvocationTargetException | InstantiationException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
