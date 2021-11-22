package datastructures.lists;

import datastructures.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    /*
    Warning:
    You may not rename these fields or change their types.
    We will be inspecting these in our secret tests.
    You also may not add any additional fields.

    Note: The fields below intentionally omit the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" they become package-private, which means anything in
    the same package can access them. Since our tests are in the same package, they will be able
    to test these properties directly.
     */
    Node<T> front;
    Node<T> back;
    int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        Node<T> curr;
        if (front == null) {
            curr = new Node<T>(null, item, null);
            front = curr;
            back = curr;
        }
        else {
            curr = new Node<T>(back, item, null);
            back.next = curr;
            back = curr;
        }

        size++;
    }

    @Override
    public T remove() {
        if (front == null) {
            throw new EmptyContainerException();
        }

        Node<T> curr = back;
        if (front == back) {
            front = null;
            back = null;
        }
        else {
            curr = back;
            back = back.prev;
            back.next = null;
        }
        size--;
        return curr.data;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (getNode(index) != null) {
            return getNode(index).data;
        }
        return null;

    }

    private Node<T> getNode(int index) {
        Node<T> curr;
        if (size - index <= size / 2) {
             curr = back;
            for (int i = size - 1; i > index; i--) {
                curr = curr.prev;
            }
        }
        else {
            curr = front;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
        }
        return curr;
    }

    @Override
    public T set(int index, T item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> curr = getNode(index);
        Node<T> prev = null;
        Node<T> next = null;
        if (curr != null) {
            prev = curr.prev;
            next = curr.next;
        }
        else {
            return null;
        }
        Node<T> replace = new Node<T>(prev, item, next);
        if (prev != null) {
            prev.next = replace;
        }
        else {
            front = replace;
        }
        if (next != null) {
            next.prev = replace;
        }
        else {
            back = replace;
        }
        return curr.data;
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= size + 1) {
            throw new IndexOutOfBoundsException();
        }
        if (front == null || index == size) {
            add(item);
        }
        else {
            Node<T> curr = getNode(index);
            Node<T> insert;

            if (index == 0) {
                insert = new Node<T>(null, item, curr);
                front = insert;
                curr.prev = insert;
            } else {
                Node<T> prev = curr.prev;
                insert = new Node<T>(prev, item, curr);
                prev.next = insert;
                curr.prev = insert;
            }
            size++;
        }
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> curr = getNode(index);
        if (index == 0) {
            if (front.next != null) {
                front = front.next;
                front.prev = null;
            }
            else {
                front = null;
                back = null;
            }
        }
        else if (index != size - 1) {
            Node<T> prev = curr.prev;
            prev.next = curr.next;
            curr.next.prev = prev;
        }
        else {
            if (back.prev != null) {
                back = back.prev;
            }
            back.next = null;
        }
        size--;
        return curr.data;
    }

    @Override
    public int indexOf(T item) {
        return getItem(item);
    }

    private int getItem(T item) {
        Node<T> curr = front;

        for (int i = 0; i < size; i++) {
            if (Objects.equals(curr.data, item)) {
                return i;
            }
            curr = curr.next;
        }

        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        return getItem(other) > -1;
    }

    @Override
    public String toString() {
        //return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        return IList.toString(this);
    }

    @Override
    public Iterator<T> iterator() {
        /*
        Note: we have provided a part of the implementation of an iterator for you. You should
        complete the methods stubs in the DoubleLinkedListIterator inner class at the bottom of
        this file. You do not need to change this method.
        */
        return new DoubleLinkedListIterator<>(this.front);
    }

    static class Node<E> {
        // You may not change the fields in this class or add any new fields.
        final E data;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> next;

        public DoubleLinkedListIterator(Node<T> front) {
            // You do not need to make any changes to this constructor.
            this.next = front;
        }

        /**
         * Returns `true` if the iterator still has elements to look at;
         * returns `false` otherwise.
         */
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (next == null) {
                throw new NoSuchElementException();
            }

            Node<T> curr = next;
            next = next.next;
            return curr.data;
        }
    }
}
