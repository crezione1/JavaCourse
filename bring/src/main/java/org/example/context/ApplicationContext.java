package org.example.context;

import org.example.exception.NoSuchBeanException;
import org.example.exception.NoUniqueBeanException;

import java.util.Map;

public interface ApplicationContext {
    <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException;

    <T> Map<String, T> getAllBeans(Class<T> beanType);
}
