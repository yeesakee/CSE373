package datastructures.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * @see IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.

    Note: The field below intentionally omits the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" it becomes package-private, which means anything in
    the same package can access it. Since our tests are in the same package, they will be able
    to test this property directly.
     */
    Pair<K, V>[] pairs;
    int size;

    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
        this(1);
        /*this.pairs = makeArrayOfPairs(1);
        this.size = 0;*/
    }

    //new constructor to be used when implementing ChainedHashDictionary
    public ArrayDictionary(int initialCapacity) {
        pairs = makeArrayOfPairs(initialCapacity);
    }

    /**
     * This method will return a new, empty array of the given size that can contain `Pair<K, V>`
     * objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
         arrays and generics interact. Do not modify this method in any way.
        */
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    /**
     * scanArray - Searches array for given key and returns given index if found
     *             returns -1 if not found
     * @param key - K key
     * @return int
     */
    private int scanArray(K key) {

        // Iterate through the array
        for (int index = 0; index < size(); index++) {

            // Check if both keys are null at the index
            if (pairs[index].key == null && key == null) {
                return index;
            }

            // Check if key is in the array at the index
            if (key != null) {
                if (Objects.equals(pairs[index].key, key)) {
                    return index;
                }
            }
        }
        return -1;
    }

    @Override
    public V get(K key) {

        // Get the index of key
        int pairIndex = scanArray(key);

        // If found return the value at that index
        if (pairIndex != -1) {
            return pairs[pairIndex].value;
        }

        // If not found throw exception
        throw new NoSuchKeyException();

    }

    @Override
    public V put(K key, V value) {
        // Get the index of key in the array
        int pairIndex = scanArray(key);

        // Variable to store and return old value
        V priorValue;

        // Check to see if key already exists
        if (pairIndex != -1) {

            // If it exits, replace value with the given one
            priorValue = pairs[pairIndex].value;
            pairs[pairIndex].value = value;

            // Return the old value
            return priorValue;
        }

        // Check to see if array if full
        if (pairs.length == size) {

            // Create a new array that is double the size of the old one
            Pair<K, V>[] newArray = makeArrayOfPairs(2 * size);

            // Copy over the old elements
            for (int i = 0; i < size; i++) {
                newArray[i] = pairs[i];
            }

            // Assign the old array to the new one
            pairs = newArray;
        }

        // Insert key and value pair into the array
        Pair<K, V> kvPair = new Pair<>(key, value);
        pairs[size] = kvPair;

        // Update size
        size++;

        return null;

    }

    @Override
    public V remove(K key) {

        int pairIndex = scanArray(key);
        V deletedValue;

        // Check to see if key already exists in dictionary
        if (pairIndex != -1) {
            // Get value
            deletedValue = pairs[pairIndex].value;

            // Replace the value to be the last pair currently in the ArrayDictionary
            pairs[pairIndex] = pairs[size - 1];
            pairs[size - 1] = null;

            // Update size
            size--;

            // Return the value of the deleted pair
            return deletedValue;
            }

        return null;
        }


    @Override
    public boolean containsKey(K key) {

        // Check to see if key already exists in dictionary
        return scanArray(key) != -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(pairs, size);
    }

    @Override
    public String toString() {
        //return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        return IDictionary.toString(this);
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s=%s", this.key, this.value);
        }
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int pairIndex;
        private int size;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
            this.pairIndex = 0;

        }

        @Override
        public boolean hasNext() {

            // Check if there is another element in the list
            return pairIndex < size;
        }

        @Override
        public KVPair<K, V> next() {

            // Check if there is another element in the list
            if (hasNext()) {

                // Instantiate new KVPair object from the Pair<K, V> pairs key and value
                KVPair<K, V> pair = new KVPair<>(pairs[pairIndex].key, pairs[pairIndex].value);

                // Update the index
                pairIndex++;

                // Return the pair
                return pair;
            }
            else {

                // No other item in the array
                throw new NoSuchElementException();
            }
        }

    }
}