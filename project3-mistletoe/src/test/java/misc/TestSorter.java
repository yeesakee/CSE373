package misc;

import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import datastructures.priorityqueues.IntPair;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static datastructures.lists.IListMatcher.listContaining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * See spec for details on what kinds of tests this class should include.
 */
@Tag("project3")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestSorter extends BaseTest {
    @Test
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertThat(top, is(listContaining(15, 16, 17, 18, 19)));
    }


    //********************************EMPTY TESTS******************************************//

    @Test
    void testTopKSortEmptyInput() {
        IList<Integer> list = null;

        assertThrows(IllegalArgumentException.class, () -> Sorter.topKSort(5, list));

    }

    @Test
    void testTopKSortNegativeK() {
        IList<Integer> list = new DoubleLinkedList<>();

        assertThrows(IllegalArgumentException.class, () -> Sorter.topKSort(-1, list));

    }

    @Test
    void testTopKSortZeroK() {
        IList<Integer> list = new DoubleLinkedList<>();

        list.add(0);

        IList<Integer> top = Sorter.topKSort(0, list);

        assertThat(top.isEmpty(), is(true));
    }

    //********************************SIZE ONE TESTS*************************************//


    @Test
    void testTopKSizeOne() {
        IList<Integer> list = new DoubleLinkedList<>();

        list.add(0);

        IList<Integer> top = Sorter.topKSort(10, list);

        assertThat(top.size(), is(1));

        assertThat(top, is(listContaining(0)));
    }

    //********************************INCREASING NUMBER OF ELEMENTS TESTS*************************************//

    @Test
    void testTopKSizeMultipleNegative() {
        IList<Integer> list = new DoubleLinkedList<>();

        for (int i = 0; i < 100; i++) {
            list.add(i * -1);
        }


        IList<Integer> top = Sorter.topKSort(10, list);

        assertThat(top.size(), is(10));
        assertThat(top, is(listContaining(-9, -8, -7, -6, -5, -4, -3, -2, -1, 0)));
    }


    //********************************SPECIAL CASES CLEAR-BOX TESTS*************************************//

    @Test
    void testInputConditional() {
        IList<Integer> list = new DoubleLinkedList<>();

        for (int i = 0; i < 7; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(8, list);

        assertThat(top, is(listContaining(0, 1, 2, 3, 4, 5, 6)));
    }

    @Test
    void testSizeTwoIntPair() {
        IList<IntPair> list = new DoubleLinkedList<>();

        IntPair a = new IntPair(1, 2);
        IntPair b = new IntPair(0, 3);

        list.add(a);
        list.add(b);

        IList<IntPair> top = Sorter.topKSort(2, list);

        assertThat(top, is(listContaining(a, b)));


    }

    @Test
    void testSizeMultipleIntPair() {
        IList<IntPair> list = new DoubleLinkedList<>();

        for (int i = 0; i < 1000; i++) {
            list.add(new IntPair(i, 10));
        }

        IList<IntPair> top = Sorter.topKSort(10, list);

        //<[(990,10), (991,10), (992,10), (993,10), (994,10), (995,10), (996,10), (997,10), (998,10), (999,10)]>

        for (int i = 990; i < 1000; i++) {
            assertThat(list.get(i), is(new IntPair(i, 10)));
        }

    }


}
