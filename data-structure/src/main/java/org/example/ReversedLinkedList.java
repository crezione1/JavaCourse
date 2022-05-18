package org.example;

import java.util.Objects;
import java.util.Stack;

public class ReversedLinkedList {

    public static void main(String[] args) {
        var head = createLinkedList(4, 3, 9, 1);
        printReversed(head);
        printReversedUsingStack(head);
    }

    /**
     * Creates a list of linked {@link Node} objects based on the given array of elements and returns a head of the list.
     *
     * @param elements an array of elements that should be added to the list
     * @param <T>      elements type
     * @return head of the list
     */
    public static <T> Node<T> createLinkedList(T... elements) {
        Node<T> head = new Node<>(elements[0]);
        Node<T> current = head;

        for (int i = 1; i < elements.length; i++) {
            current.next = new Node<>(elements[i]);
            current = current.next;
        }

        return head;
    }

    /**
     * Prints a list in a reserved order using a recursion technique. Please note that it should not change the list,
     * just print its elements.
     * <p>
     * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
     * 1 -> 9 -> 3 -> 4
     *
     * @param head the first node of the list
     * @param <T>  elements type
     */
    public static <T> void printReversed(Node<T> head) {
        Objects.requireNonNull(head);
        printRecursively(head.next);
        System.out.println(head.value);
    }

    private static void printRecursively(Node<?> node) {
        if (node != null) {
            printRecursively(node.next);
            System.out.print(node.value + " -> ");
        }
    }

    /**
     * Prints a list in a reserved order using a {@link java.util.Stack} instance. Please note that it should not change
     * the list, just print its elements.
     * <p>
     * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
     * 1 -> 9 -> 3 -> 4
     *
     * @param head the first node of the list
     * @param <T>  elements type
     */
    public static <T> void printReversedUsingStack(Node<T> head) {
        var stack = new Stack<T>();
        var current = head;
        while (!Objects.isNull(current)) {
            stack.add(current.value);
            current = current.next;
        }

        while (!stack.empty()) {
            System.out.print(stack.pop());
            if(stack.size() > 0) {
                System.out.print(" -> ");
            }
        }

    }

    private static class Node<T> {
        T value;
        Node<T> next;

        public Node(T value) {
            this.value = value;
        }
    }
}