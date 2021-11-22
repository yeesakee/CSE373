package datastructures.sets;

import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.dictionaries.IDictionary;
import datastructures.dictionaries.KVPair;

import java.util.Iterator;

/**
 * @see ISet for more details on what each method is supposed to do.
 */
public class ChainedHashSet<T> implements ISet<T> {
    // This should be the only field you need
    private IDictionary<T, Boolean> map;

    public ChainedHashSet() {
        // No need to change this method
        this.map = new ChainedHashDictionary<>();
    }

    @Override
    public boolean add(T item) {

        // Check if item is not in the set
        if (!this.contains(item)) {

            // Add the item
            map.put(item, Boolean.TRUE);

            // Return true if not found
            return Boolean.TRUE;
        }

        // Return false if found
        return Boolean.FALSE;
    }

    @Override
    public boolean remove(T item) {

        // Check if item is in the set
        if (this.contains(item)) {

            // Remove item
            map.remove(item);

            // Return true if found
            return Boolean.TRUE;
        }

        // Return false if not found
        return Boolean.FALSE;
    }

    @Override
    public boolean contains(T item) {

        // Variable to return
        boolean bool = Boolean.FALSE;

        // Check if item is in the set
        if (map.containsKey(item)) {

            // Return true if found
            bool = Boolean.TRUE;
        }

        // Return false if not found
        return bool;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }

    @Override
    public String toString() {
        return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        // return ISet.toString(this);
    }

    private static class SetIterator<T> implements Iterator<T> {
        // This should be the only field you need
        private Iterator<KVPair<T, Boolean>> iter;

        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            // No need to change this method.
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public T next() {
            return iter.next().getKey();
        }
    }
}