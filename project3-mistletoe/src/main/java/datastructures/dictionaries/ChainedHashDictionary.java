package datastructures.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You'll need to define reasonable default values for each of the following three fields
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 1;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 1;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 1;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.

    Note: The field below intentionally omits the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" it becomes package-private, which means anything in
    the same package can access it. Since our tests are in the same package, they will be able
    to test this property directly.
     */
    IDictionary<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private double resizingThreshold = DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD;
    private int chainCount = DEFAULT_INITIAL_CHAIN_COUNT;
    private int chainCapacity = DEFAULT_INITIAL_CHAIN_CAPACITY;
    private int numOfPairValues = 0;

    public ChainedHashDictionary() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    public ChainedHashDictionary(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        resizingThreshold = resizingLoadFactorThreshold;
        chainCapacity = chainInitialCapacity;
        chains = makeArrayOfChains(initialChainCount);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * `IDictionary<K, V>` objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        //
        //Note: You do not need to modify this method. See `ArrayDictionary`'s `makeArrayOfPairs`
        //method for more background on why we need this method.
        //
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    @Override
    public V get(K key) {
        int index = hash(key, chains.length);
        if (chains == null || chains[index] == null) {
            throw new NoSuchKeyException();
        }

        return chains[index].get(key);
    }

    @Override
    public V put(K key, V value) {
        V oldValue = null;
        int index = hash(key, chains.length);
        if (chains[index] == null) {
            chains[index] = new ArrayDictionary<>(chainCapacity);
        }
        if (chains[index].containsKey((key))){
            oldValue = chains[index].get(key);
            numOfPairValues--;
        }
        chains[index].put(key, value);
        numOfPairValues++;

        // Check if the chains array needs to be resized
        // n keys = numOfPairValues, c buckets or hash table length= chains.length, load factor = n/c
        // Resize if the load factor is greater than or equal to the RESIZING_LOAD_FACTOR_THRESHOLD
        if (numOfPairValues / chains.length == resizingThreshold) {
            resize();
        }

        return oldValue;
    }

    /**
     * hash - Calculates hash function from a given key and the hash table size
     * @param key - key of KV pair
     * @param chainNum - hash table size (chains.length NOT chainCapacity)
     * @return - int of the index in the array for where to store the key
     */
    private int hash(K key, int chainNum) {
        //chainNum =
        if (key == null) {
            return 0;
        }
        int keyHash = key.hashCode();
        if (keyHash < 0) {
            keyHash *= -1;
        }
        return keyHash % chainNum;
    }

    private void resize() {

        // Create new chain array by doubling the size
        IDictionary<K, V>[] newChain = makeArrayOfChains(chains.length * 2);

        // Iterate through the current chain array
        for (IDictionary<K, V> chain : chains) {

            // Store variable to the current ArrayDictionary at the chain[i]
            // If not null
            if (chain != null) {

                // Iterate through each KV pair at the chain[i]
                for (KVPair<K, V> pair : chain) {

                    // Get the key of the KV pair
                    K key = pair.getKey();

                    // Get the index hash
                    int index = hash(key, chains.length * 2);

                    // If the new chain array at the index is null
                    if (newChain[index] == null) {

                        // Create and store a new ArrayDictionary in the new chain array at index
                        newChain[index] = new ArrayDictionary<>(chainCapacity);
                    }

                    // If the new chain array at the index is not null, store KV pair in the new chain array at index
                    newChain[index].put(key, pair.getValue());
                }
            }
        }
        // Update the old chain array to the newly created chain array
        chains = newChain;

    }


    @Override
    public V remove(K key) {
        int index = hash(key, chains.length);
        if (chains[index] == null || !chains[index].containsKey(key)) {
            return null;
        }
        V value = chains[index].get(key);
        chains[index].remove(key);
        numOfPairValues--;
        return value;
    }

    @Override
    public boolean containsKey(K key) {
        int index = hash(key, chains.length);
        if (chains[index] == null) {
            return false;
        }
        return chains[index].containsKey(key);
    }

    @Override
    public int size() {
        return numOfPairValues;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    @Override
    public String toString() {
        return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        // return IDictionary.toString(this);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int chainLength;
        private int chainIndex = 0;
        private int arrIndex = 0;
        private int arrSize = 0;
        private boolean lastChain;
        private int lastChainElements = 0;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            chainLength = chains.length;
            lastChain = chains[chainLength - 1] != null;
            if (chains[chainLength - 1] != null) {
                countLastChain();
            }
        }

        @Override
        public boolean hasNext() {
            if (chainIndex == 0) {
                findChainIndex();
            }
            if (arrSize > 0 && arrIndex == arrSize && chainIndex != chainLength - 1) {
                chainIndex = chainIndex + 1;
                arrIndex = 0;
                findChainIndex();
            }
            if (chainIndex == chainLength - 1 && lastChain && lastChainElements != 0) {
                return true;
            }
            return chainIndex < chainLength - 1;
        }

        @Override
        public KVPair<K, V> next() {
            findChainIndex();
            KVPair<K, V> pair = findPair(arrIndex);
            if (lastChain && chainIndex == chainLength - 1) {
                lastChainElements = lastChainElements - 1;
            }
            if (pair == null) {
                chainIndex = chainIndex + 1;
                findChainIndex();
                pair = findPair(arrIndex);
            }
            // should never return null
            return pair;
        }

        private void findChainIndex() {
            if (chainIndex >= chainLength) {
                throw new NoSuchElementException();
            }
            while ((chains[chainIndex] == null || chains[chainIndex].size() == 0) && chainIndex != chainLength - 1) {
                chainIndex = chainIndex + 1;
            }
        }

        private KVPair<K, V> findPair(int arrIndexCount) {
            if (chains[chainIndex] == null) {
                throw new NoSuchElementException();
            }
            arrSize = chains[chainIndex].size();
            for (KVPair<K, V> pair : chains[chainIndex]) {
                if (arrIndexCount == 0) {
                    arrIndex = arrIndex + 1;
                    return pair;
                }
                arrIndexCount--;
            }
            arrIndex = 0;
            return null;
        }

        private void countLastChain() {
            for (KVPair<K, V> pair : chains[chainLength - 1]) {
                lastChainElements = lastChainElements + 1;
            }
        }
    }
}
