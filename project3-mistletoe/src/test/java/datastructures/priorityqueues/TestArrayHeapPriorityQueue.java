package datastructures.priorityqueues;

import datastructures.EmptyContainerException;
import misc.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import datastructures.dictionaries.IDictionary;


/**
 * See spec for details on what kinds of tests this class should include.
 */
@Tag("project3")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestArrayHeapPriorityQueue extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeapPriorityQueue<>();
    }

    /**
     * A helper method for accessing the private array inside an `ArrayHeapPriorityQueue`.
     */
    protected static <T extends Comparable<T>> Comparable<T>[] getArray(IPriorityQueue<T> heap) {
        return ((ArrayHeapPriorityQueue<T>) heap).heap;
    }

    /**
     * A helper method for accessing the IDictionary inside an `ArrayHeapPriorityQueue`.
     */
    protected static <T extends Comparable<T>> IDictionary<T, Integer> getDictionary(IPriorityQueue<T> heap) {
        return ((ArrayHeapPriorityQueue<T>) heap).indices;
    }

    @Test
    void testAddEmptyInternalArray() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        Comparable<Integer>[] array = getArray(heap);
        assertThat(array[0], is(3));
    }

    @Test
    void testUpdateDecrease() {
        IPriorityQueue<IntPair> heap = this.makeInstance();
        for (int i = 1; i <= 5; i++) {
            heap.add(new IntPair(i, i));
        }

        heap.replace(new IntPair(2, 2), new IntPair(0, 0));

        assertThat(heap.removeMin(), is(new IntPair(0, 0)));
        assertThat(heap.removeMin(), is(new IntPair(1, 1)));
    }

    @Test
    void testUpdateIncrease() {
        IntPair[] values = IntPair.createArray(new int[][]{{0, 0}, {2, 2}, {4, 4}, {6, 6}, {8, 8}});
        IPriorityQueue<IntPair> heap = this.makeInstance();

        for (IntPair value : values) {
            heap.add(value);
        }

        IntPair newValue = new IntPair(5, 5);
        heap.replace(values[0], newValue);

        assertThat(heap.removeMin(), is(values[1]));
        assertThat(heap.removeMin(), is(values[2]));
        assertThat(heap.removeMin(), is(newValue));
    }

    //********************************EMPTY TESTS******************************************//

    @Test
    void testRemoveMinEmpty() {
        IPriorityQueue<IntPair> heap = this.makeInstance();
        assertThrows(EmptyContainerException.class, () -> heap.removeMin());
    }

    @Test
    void testPeekMinEmpty() {
        IPriorityQueue<IntPair> heap = this.makeInstance();
        assertThrows(EmptyContainerException.class, () -> heap.peekMin());
    }

    @Test
    void testSizeEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertThat(heap.size(), is(0));
    }

    @Test
    void testAddEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        assertThat(heap.size(), is(1));
    }

    @Test
    void testContainsEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertThat(heap.contains(0), is(false));
    }

    @Test
    void testRemoveEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertThrows(InvalidElementException.class, () -> heap.remove(0));
    }

    @Test
    void testReplaceEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertThrows(InvalidElementException.class, () -> heap.replace(0, 1));
    }

    @Test
    void testIsEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertThat(heap.isEmpty(), is(true));
    }

    //********************************SIZE ONE TESTS*************************************//

    @Test
    void testRemoveMinSizeOne() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(0);
        heap.removeMin();
        assertThat(heap.isEmpty(), is(true));
    }

    @Test
    void testPeekMinSizeOne() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(0);
        assertThat(heap.peekMin(), is(0));
    }

    @Test
    void testSizeSizeOne() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(0);
        assertThat(heap.size(), is(1));
    }

    @Test
    void testAddSizeOne() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(0);
        heap.add(1);
        assertThat(heap.size(), is(2));
    }

    @Test
    void testContainsSizeOne() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(0);
        assertThat(heap.contains(0), is(true));
    }

    @Test
    void testRemoveSizeOne() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(0);
        heap.remove(0);
        assertThat(heap.isEmpty(), is(true));
    }

    @Test
    void testReplaceSizeOne() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(0);
        heap.replace(0, 1);
        assertThat(heap.peekMin(), is(1));
    }

    @Test
    void testIsEmptySizeOne() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(0);
        assertThat(heap.isEmpty(), is(false));
    }

    @Test
    void testgetIndicesSizeOne() {
        IPriorityQueue<Integer> heap = new ArrayHeapPriorityQueue<>();
        heap.add(3);

        Comparable<Integer>[] array = getArray(heap);

        assertThat(array[0], is(3));

        IDictionary<Integer, Integer> dict = getDictionary(heap);

        assertThat(dict.get(3), is(0));

    }


    //********************************INCREASING NUMBER OF ELEMENTS TESTS*************************************//


    @Test
    void testRemoveMinSizeMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 100; i++) {
            heap.add(i);
        }

        // Removed element 0
        assertThat(heap.removeMin(), is(0));

        // Removed element 1
        assertThat(heap.removeMin(), is(1));

        // Removed element 2
        assertThat(heap.removeMin(), is(2));

        // Remove all elements
        for (int i = 3; i < 100; i++) {
            heap.remove(i);
        }

        // Queue should contain no elements
        assertThrows(EmptyContainerException.class, () -> heap.removeMin());
    }

    @Test
    void testPeekMinSizeMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 100; i++) {
            heap.add(i);
        }

        // Smallest value is 0
        assertThat(heap.peekMin(), is(0));

        // Removed element 0
        heap.removeMin();

        // Smallest value is 1
        assertThat(heap.peekMin(), is(1));

        // Removed element 1
        heap.removeMin();

        // Smallest value is 2
        assertThat(heap.peekMin(), is(2));

        // Remove all elements
        for (int i = 2; i < 100; i++) {
            heap.remove(i);
        }

        // Queue should contain no elements
        assertThrows(EmptyContainerException.class, () -> heap.peekMin());

    }

    @Test
    void testSizeSizeMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 100; i++) {
            heap.add(i);
        }

        assertThat(heap.size(), is(100));
    }

    @Test
    void testAddSizeMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 100; i++) {
            heap.add(i);
        }

        // Add an item to the end
        heap.add(100);

        assertThat(heap.size(), is(101));

        // Add an item to the beginning
        heap.add(-1);
        assertThat(heap.peekMin(), is(-1));
        assertThat(heap.size(), is(102));


        // Add a null element
        assertThrows(IllegalArgumentException.class, () -> heap.add(null));

        // Add element already in queue
        assertThrows(InvalidElementException.class, () -> heap.add(1));

    }

    @Test
    void testContainsSizeMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 100; i++) {
            heap.add(i);
        }

        assertThat(heap.contains(0), is(true));

        heap.remove(50);
        assertThat(heap.contains(50), is(false));


        assertThat(heap.contains(100), is(false));

        heap.add(100);
        assertThat(heap.contains(100), is(true));

        // Check null item in queue
        assertThrows(IllegalArgumentException.class, () -> heap.contains(null));
    }

    @Test
    void testRemoveSizeMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 100; i++) {
            heap.add(i);
        }

        // Remove a null element
        assertThrows(IllegalArgumentException.class, () -> heap.remove(null));

        for (int i = 0; i < 100; i++) {
            heap.remove(i);
        }

        assertThat(heap.isEmpty(), is(true));


        for (int i = 0; i < 5; i++) {
            heap.add(i);
        }

        // Remove 0
        heap.remove(0);

        // Remove element not in queue
        assertThrows(InvalidElementException.class, () -> heap.remove(0));
    }

    @Test
    void testReplaceSizeMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 5; i++) {
            heap.add(i);
        }

        assertThrows(InvalidElementException.class, () -> heap.replace(3, 1));
        assertThat(heap.peekMin(), is(0));

        heap.replace(1, -1);
        assertThat(heap.peekMin(), is(-1));

        assertThat(heap.size(), is(5));
    }

    @Test
    void testIsEmptySizeMultiple() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 5; i++) {
            heap.add(i);
        }

        assertThat(heap.isEmpty(), is(false));
    }


    @Test
    void testgetIndicesSizeMultiple() {
        IPriorityQueue<Integer> heap = new ArrayHeapPriorityQueue<>();
        Comparable<Integer>[] array = getArray(heap);
        IDictionary<Integer, Integer> dict = getDictionary(heap);

        for (int i = 0; i < 10; i++) {
            heap.add(i);
        }

        for (int i = 0; i < 10; i++) {
            assertThat(array[i], is(dict.get(i)));
        }
    }

    @Test
    void testgetIndicesSizeMultipleNegative() {
        IPriorityQueue<Integer> heap = new ArrayHeapPriorityQueue<>();
        Comparable<Integer>[] array = getArray(heap);
        IDictionary<Integer, Integer> dict = getDictionary(heap);

        for (int i = 0; i < 5; i++) {
            heap.add(i * -1);
        }

        assertThat(array[0], is(-4));
        assertThat(array[1], is(0));
        assertThat(array[2], is(-1));
        assertThat(array[3], is(-2));
        assertThat(array[4], is(-3));


        assertThat(dict.get(-4), is(0));
        assertThat(dict.get(0), is(1));
        assertThat(dict.get(-1), is(2));
        assertThat(dict.get(-2), is(3));
        assertThat(dict.get(-3), is(4));
    }


    //********************************SPECIAL CASES CLEAR-BOX TESTS*************************************//
    /*
      Non-specific clear-box tests listed above
     */

    @Test
    void testSizeTwoIntPair() {
        IPriorityQueue<IntPair> heap = this.makeInstance();

        IntPair a = new IntPair(1, 2);
        IntPair b = new IntPair(0, 3);

        heap.add(a);
        heap.add(b);

        assertThat(heap.removeMin(), is(a));
        assertThat(heap.peekMin(), is(b));
    }

    @Test
    void testSizeMultipleIntPair() {
        IPriorityQueue<IntPair> heap = this.makeInstance();

        for (int i = 0; i < 1000; i++) {
            heap.add(new IntPair(i, 10));
        }

        for (int i = 0; i < 1000; i++) {
            assertThat(heap.removeMin(), is(new IntPair(i, 10)));
        }
    }


    @Test
    void testAddResize() {
        IPriorityQueue<Integer> heap = new ArrayHeapPriorityQueue<>();
        Comparable<Integer>[] array = getArray(heap);

        assertThat(array.length, is(10));

        for (int i = 0; i < 11; i++) {
            heap.add(i);
        }

        array = getArray(heap);

        assertThat(array.length, is(20));
    }


    @Test
    void testRemoveConditional() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Comparable<Integer>[] array = getArray(heap);
        IDictionary<Integer, Integer> dict = getDictionary(heap);

        heap.add(0);
        heap.add(10);
        heap.add(5);
        heap.add(12);
        heap.add(13);
        heap.add(14);
        heap.add(15);
        heap.add(16);
        heap.add(17);
        heap.add(6);

        heap.remove(16);

        assertThat(array[1], is(6));
        assertThat(array[7], is(10));

        assertThat(dict.get(6), is(1));
        assertThat(dict.get(10), is(7));
    }

    @Test
    void testReplaceConditionalPercUp() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Comparable<Integer>[] array = getArray(heap);
        IDictionary<Integer, Integer> dict = getDictionary(heap);

        heap.add(0);
        heap.add(5);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(6);
        heap.add(7);
        heap.add(8);
        heap.add(9);

        heap.replace(6, 1);

        assertThat(array[1], is(1));
        assertThat(array[5], is(5));

        assertThat(dict.get(1), is(1));
        assertThat(dict.get(5), is(5));
    }


    @Test
    void testReplaceConditionalPercDown() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Comparable<Integer>[] array = getArray(heap);
        IDictionary<Integer, Integer> dict = getDictionary(heap);

        heap.add(0);
        heap.add(5);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(6);
        heap.add(7);
        heap.add(8);
        heap.add(9);

        heap.replace(5, 10);

        assertThat(array[1], is(6));
        assertThat(array[5], is(10));

        assertThat(dict.get(6), is(1));
        assertThat(dict.get(10), is(5));
    }




}
