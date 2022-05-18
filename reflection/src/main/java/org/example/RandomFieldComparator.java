package org.example;

import lombok.SneakyThrows;
import org.example.data.Accounts;
import org.example.model.Account;
import org.example.model.Sex;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A generic comparator that is comparing a random field of the given class. The field is either primitive or
 * {@link Comparable}. It is chosen during comparator instance creation and is used for all comparisons.
 * <p>
 * By default it compares only accessible fields, but this can be configured via a constructor property. If no field is
 * available to compare, the constructor throws {@link IllegalArgumentException}
 *
 * @param <T> the type of the objects that may be compared by this comparator
 */
public class RandomFieldComparator<T> implements Comparator<T> {

    public static void main(String[] args) throws NoSuchMethodException {
        RandomFieldComparator<Account> accountComparator = new RandomFieldComparator<>(Account.class, false);
        System.out.println(accountComparator);
        List<Account> accounts = Accounts.generateAccountList(10);
        accounts.add(new Account(1L, "Test", "test", "email", LocalDate.now(), Sex.MALE, LocalDate.now(), new BigDecimal(1)));
        accounts.add(new Account(2L, "Test", "test", "email", LocalDate.now(), Sex.MALE, LocalDate.now(), new BigDecimal(1)));
        accounts.add(new Account(3L, "Test", "test", "email", LocalDate.now(), Sex.MALE, LocalDate.now(), new BigDecimal(1)));
        accounts.add(new Account(4L, "Test", "test", "email", LocalDate.now(), Sex.MALE, LocalDate.now(), new BigDecimal(1)));
        accounts.stream().sorted(accountComparator).forEach(System.out::println);
    }

    private final Field comparisonField;
    private final String type;

    public RandomFieldComparator(Class<T> targetType) throws NoSuchMethodException {
        this(targetType, true);
    }

    /**
     * A constructor that accepts a class and a property indicating which fields can be used for comparison. If property
     * value is true, then only public fields or fields with public getters can be used.
     *
     * @param targetType                  a type of objects that may be compared
     * @param compareOnlyAccessibleFields config property indicating if only publicly accessible fields can be used
     */
    public RandomFieldComparator(Class<T> targetType, boolean compareOnlyAccessibleFields) throws NoSuchMethodException {
        comparisonField = getRandomFiled(targetType, compareOnlyAccessibleFields);
        type = targetType.getTypeName();
    }

    private Field getRandomFiled(Class<T> clazz, boolean onlyAccessible) throws NoSuchMethodException {
        int index;
        Field[] fields;
        if (onlyAccessible) {
            fields = clazz.getFields();
            if (fields.length == 0) {
                throw new NoAccessibleFieldsException("There are no public fields inside" + clazz.getTypeName() + " for comparing change onlyAccessible to false");
            }
        } else {
            fields = clazz.getDeclaredFields();
            if (fields.length == 0) {
                throw new NoAccessibleFieldsException("There are no fields for comparing in the " + clazz.getTypeName());
            }
        }
        index = ThreadLocalRandom.current().nextInt(fields.length);
        fields[index].setAccessible(true);
        return fields[index];
    }

    /**
     * Compares two objects of the class T by the value of the field that was randomly chosen. It allows null values
     * for the fields, and it treats null value grater than a non-null value (nulls last).
     */
    @SneakyThrows
    @Override
    public int compare(T o1, T o2) {
        Object fieldFromO1 = this.comparisonField.get(o1);
        Object fieldFromO2 = this.comparisonField.get(o2);

        if (Objects.isNull(fieldFromO1) || Objects.isNull(fieldFromO2)) {
            if (Objects.isNull(fieldFromO1)) {
                return 1;
            } else {
                return -1;
            }
        }

        if (!Comparable.class.isAssignableFrom(fieldFromO1.getClass())) {
            throw new NotComparableFieldException("The selected field is not support comparable interface");
        }

        return ((Comparable) fieldFromO1).compareTo(fieldFromO2);
    }

    /**
     * Returns a statement "Random field comparator of class '%s' is comparing '%s'" where the first param is the name
     * of the type T, and the second parameter is the comparing field name.
     *
     * @return a predefined statement
     */
    @Override
    public String toString() {
        return "Random field comparator of class " + type + " is comparing " + comparisonField;
    }
}
