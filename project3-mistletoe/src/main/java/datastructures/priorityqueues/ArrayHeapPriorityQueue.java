package datastructures.priorityqueues;

import datastructures.EmptyContainerException;
import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.dictionaries.IDictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeapPriorityQueue<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int ARRAY_SIZE = 10;
    /*
    You MUST use this field to store the contents of your heap.
    You may NOT rename this field or change its type: we will be inspecting it in our secret tests.
    */
    T[] heap;
    IDictionary<T, Integer> indices;

    // stores the index of the first empty space in the array
    private int lastIndex;
    // Feel free to add more fields and constants.

    public ArrayHeapPriorityQueue() {
        heap = makeArrayOfT(ARRAY_SIZE);
        indices = new ChainedHashDictionary<>();
        lastIndex = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type `T`.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        /*
        This helper method is basically the same one we gave you in `ArrayDictionary` and
        `ChainedHashDictionary`.

        As before, you do not need to understand how this method works, and should not modify it in
         any way.
        */
        return (T[]) (new Comparable[arraySize]);
    }

    @Override
    public T removeMin() {
        if (indices.size() == 0) {
            throw new EmptyContainerException();
        }
        T item = heap[0];

        if (indices.size() > 1) {
            swap(lastIndex - 1, 0);
        }
        indices.remove(item);
        lastIndex--;

        // call percolate down if the size is greater than one
        if (indices.size() > 1) {
            percolateDown(0);
        }
        return item;
    }

    @Override
    public T peekMin() {
        if (indices.size() == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (indices.containsKey(item)) {
            throw new InvalidElementException();
        }

        // if the array is full resize
        if (lastIndex >= heap.length) {
            resize();
        }
        // add the item to the first empty index
        heap[lastIndex] = item;
        indices.put(item, lastIndex);

        // percolate up the new item if needed
        percolateUp(lastIndex);

        // update the index of the first empty index
        lastIndex++;
    }

    private void resize() {
        // create a new array with double the size
        T[] newHeap = makeArrayOfT(heap.length * 2);

        // copy all the items into the new heap
        for (int i = 0; i < heap.length; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        return indices.containsKey(item);
    }

    @Override
    public void remove(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (!indices.containsKey(item)) {
            throw new InvalidElementException();
        }

        int index = indices.get(item);
        if (indices.get(item) == 0) {
            removeMin();
        }
        else if (indices.size() > 1 && index != 0) {
            swap(index, lastIndex - 1);
            indices.remove(item);
            lastIndex--;
            if (4 * index < lastIndex) {
                percolateDown(index);
            }
            percolateUp(index);
        }
    }

    @Override
    public void replace(T oldItem, T newItem) {
        if (oldItem == null || newItem == null) {
            throw new IllegalArgumentException();
        }
        if (!indices.containsKey(oldItem) || indices.containsKey(newItem)) {
            throw new InvalidElementException();
        }

        int index = indices.get(oldItem);
        heap[index] = newItem;
        indices.remove(oldItem);
        indices.put(newItem, index);
        if (heap[index].compareTo(heap[parentIndex(index)]) < 0) {
            percolateUp(index);
        }
        else {
            percolateDown(index);
        }
    }

    @Override
    public int size() {
        return indices.size();
    }

    /**************************************************Helper methods**************************************************/
    private void percolateUp(int index) {
        // find the index of the parent of index
        int parent = parentIndex(index);
        // if the child has a higher priority swap with parent
        // call percolate up again (recursive)
        if (heap[index].compareTo(heap[parent]) < 0) {
            swap(index, parent);
            percolateUp(parent);
        }
    }

    private void percolateDown(int parentIndex) {
        // store the index of the item with the highest priority
        int minIndex = parentIndex;

        // compare the parent with all of its children
        for (int i = 1; i < 5; i++) {
            if (4 * parentIndex + i >= lastIndex || heap[minIndex] == null) {
                break;
            }
            if (heap[4 * parentIndex + i].compareTo(heap[minIndex]) < 0) {
                minIndex = 4 * parentIndex + i;
            }
        }
        // swap the elements at parentIndex and minIndex if a children has a higher priority
        // call percolate down again (recursive)
        if (minIndex != parentIndex) {
            swap(minIndex, parentIndex);
            if (4 * parentIndex < lastIndex) {
                percolateDown(minIndex);
            }
        }
    }

    private void swap(int index, int parentIndex) {
        T item = heap[index];
        T parentItem = heap[parentIndex];
        heap[index] = parentItem;
        heap[parentIndex] = item;

        // update dictionary
        indices.remove(item);
        indices.remove(parentItem);
        indices.put(item, parentIndex);
        indices.put(parentItem, index);
    }

    private int parentIndex(int index) {
        return (index - 1) / 4;
    }

    public IDictionary<T, Integer> getIndices() {
        return indices;
    }

    /**************************************************End**************************************************/

    @Override
    public String toString() {
        return IPriorityQueue.toString(this);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayHeapIterator<>(this.heap, this.size());
    }

    private static class ArrayHeapIterator<T extends Comparable<T>> implements Iterator<T> {
        private final T[] heap;
        private final int size;
        private int index;

        ArrayHeapIterator(T[] heap, int size) {
            this.heap = heap;
            this.size = size;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return this.index < this.size;
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            T output = heap[this.index];
            this.index++;
            return output;
        }
    }
}
