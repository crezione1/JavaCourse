package org.example;

import org.example.data.Accounts;

import java.time.LocalDate;
import java.util.Objects;

class Test {
    public static void main(String[] args) {
        var accounts = Accounts.generateAccountList(10);
        var emailToBirthdayTable = new HashTable<String, LocalDate>();
        accounts.forEach(a -> emailToBirthdayTable.put(a.getEmail(), a.getBirthday()));
        emailToBirthdayTable.printTable();
    }
}

public class HashTable<K, V> {
    private static final int INIT_CAPACITY = 14;
    private static final float LOAD_FACTOR = 0.5f;
    private int occupiedBucketsCount = 0;
    @SuppressWarnings("unchecked")
    private Node<K, V>[] store = new Node[INIT_CAPACITY];

    public HashTable() {
    }

    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        this.store = new Node[capacity];
    }

    public boolean put(K key, V element) {
        var index = hash(key) % store.length;
        Node<K, V> currentNode = store[index];
        if (currentNode != null) {
            while (currentNode.next != null) {
                if (currentNode.value.equals(element)) {
                    return false;
                } else {
                    currentNode.next = new Node<>(key, element);
                    currentNode.key = key;
                    resizeIfNeeded();
                    return true;
                }
            }
            currentNode.next = new Node<>(key, element);
        } else {
            store[index] = new Node<>(key, element);
            store[index].key = key;
            occupiedBucketsCount++;
            resizeIfNeeded();
            return true;
        }

        return false;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode());
    }

    public void resizeIfNeeded() {
        if (LOAD_FACTOR < (float) occupiedBucketsCount / (float) store.length) {
            Node<K, V>[] resizedStore = new Node[store.length * 2];
            for (int i = 0; i < store.length; i++) {
                if(!Objects.isNull(store[i])) {
                    var newIndex = hash(store[i].key) % resizedStore.length;
                    resizedStore[newIndex] = store[i];
                }
            }

            store = resizedStore;
        }
    }
    public void printTable() {
        for (int i = 0; i < store.length; i++) {
            System.out.print(i + ": ");
            var currentNode = store[i];
            if (currentNode != null) {
                System.out.print(store[i].key + ": " );
                while (currentNode.next != null) {
                    System.out.print(currentNode.value + " -> ");
                    currentNode = currentNode.next;
                }
                System.out.print(currentNode.value + "");
                System.out.println();
            } else {
                System.out.println();
            }

        }
    }

    public static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
