package misc;

import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import datastructures.priorityqueues.ArrayHeapPriorityQueue;
import datastructures.priorityqueues.IPriorityQueue;

import java.util.Iterator;

public class Sorter {
    /**
     * This method takes the input list and returns the greatest `k` elements in sorted order, from
     * least to greatest.
     *
     * If the input list contains fewer than `k` elements, return a list containing all
     * `input.length` elements in sorted order.
     *
     * Precondition: `input` does not contain `null`s or duplicate values (according to `equals`)
     * Postcondition: the input list has not been mutated
     *
     * @throws IllegalArgumentException  if k < 0
     * @throws IllegalArgumentException  if input is null
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        if (k < 0 || input == null) {
            throw new IllegalArgumentException();
        }

        IList<T> list = new DoubleLinkedList<T>();
        IPriorityQueue<T> heap = new ArrayHeapPriorityQueue<>();

        if (k == 0) {
            return list;
        }

        Iterator<T> iter = input.iterator();
        while (iter.hasNext()) {
            T item = iter.next();
            if (heap.size() < k) {
                heap.add(item);
            }
            else {
                if (heap.peekMin().compareTo(item) < 0) {
                    heap.add(item);
                    heap.removeMin();
                }
            }
        }
        while (heap.size() > 0) {
            list.add(heap.removeMin());
        }



        return list;
    }

}
