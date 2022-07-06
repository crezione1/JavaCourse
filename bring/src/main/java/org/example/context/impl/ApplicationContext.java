package org.example.context.impl;

import org.example.annotation.BBean;
import org.example.exception.NoSuchBeanException;
import org.example.exception.NoUniqueBeanException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class ApplicationContext implements org.example.context.ApplicationContext {
    Map<String, Object> context = new HashMap<>();

    public ApplicationContext(String packageName) {
        initContext(packageName);
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        List<Object> bbeans = context.values().stream()
                .filter(o -> o.getClass().isAssignableFrom(beanType)).toList();

        if(bbeans.size() < 1) {
            throw new NoSuchBeanException();
        }
        if(bbeans.size() > 1) {
            throw new NoUniqueBeanException();
        }

         return (T) bbeans.get(0);
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException {
        return (T) context.entrySet().stream()
                .filter(obj -> obj.getKey().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanException())
                .getValue();
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return context.entrySet().stream()
                .filter(obj -> obj.getValue().getClass().isAssignableFrom(beanType))
                .collect(toMap(Map.Entry::getKey, e -> beanType.cast(e.getValue())));
    }

    private void initContext(String packageToScan) {
        Set<Class<?>> bBeans = searchForAllBBeans(packageToScan);

        for (var bBean : bBeans) {
            try {
                Object bbeanInstance = instantiateBBean(bBean);
                context.put(resolveBBeanName(bBean), bbeanInstance);
            } catch (NoSuchMethodException
                     | InvocationTargetException
                     | InstantiationException
                     | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private Set<Class<?>> searchForAllBBeans(String packageToScan) {
        Reflections reflections = new Reflections(packageToScan);
        return reflections.getTypesAnnotatedWith(BBean.class);
    }

    private Object instantiateBBean(Class<?> bBean) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> constructor = bBean.getConstructor();
        return constructor.newInstance();
    }

    private String resolveBBeanName(Class<?> bbean) {
        final BBean annotation = bbean.getAnnotation(BBean.class);
        final String providedName = annotation.bbeanName();
        String className = bbean.getSimpleName();
        String classNameCamelCaseFormatted = className.substring(0, 1).toLowerCase() + className.substring(1);
        return providedName.isEmpty() ? classNameCamelCaseFormatted : providedName;
    }
}
