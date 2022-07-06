package org.example;

import org.example.context.impl.ApplicationContext;
import org.example.demo.bbeans.DemoBBeanWithName;
import org.example.demo.bbeans.DemoBBeanWithoutName;
import org.example.exception.NoSuchBeanException;
import org.example.exception.NoUniqueBeanException;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        ApplicationContext applicationContextDemo = new ApplicationContext("org.example.demo.bbeans");

        try {
            System.out.println("All bbeans" + applicationContextDemo.getAllBeans(DemoBBeanWithName.class));
            System.out.println("getBeanByType" + applicationContextDemo.getBean("DemoBBeanWithNameCustomNameProvided", DemoBBeanWithName.class));
            System.out.println("getBeanByType" + applicationContextDemo.getBean(DemoBBeanWithoutName.class));
        } catch (NoSuchBeanException | NoUniqueBeanException e) {
            throw new RuntimeException(e);
        }
    }
}