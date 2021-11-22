package datastructures.lists;

import datastructures.lists.DoubleLinkedList.Node;
import misc.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


import static datastructures.lists.TestDoubleLinkedList.makeBasicList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class should contain all the tests you implement to verify that your `delete` method behaves
 * as specified. Each test should run quickly; if your tests take longer than 1 second to run, they
 * may get timed out on the runners and during grading.
 */
@Tag("project1")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestDoubleLinkedListDelete extends BaseTest {

    @Test
    void testExample() {
        /*
        Feel free to modify or delete this dummy test.

        Below is an example of using casting, so that Java lets us access
        the specific fields of DoubleLinkedList. If you wish, you may
        use this syntax to access the internal pointers within
        DoubleLinkedList objects. Being able to check the internal pointers
        will more easily let you be thorough in your tests. For reference, our
        secret tests will be checking that the internal pointers are correct to more
        easily check your solution.
         */
        IList<String> list = makeBasicList();

        Node<String> front = ((DoubleLinkedList<String>) list).front;
        Node<String> back = ((DoubleLinkedList<String>) list).back;
        assertThat(front.next, is(back.prev));
        assertThat(front.prev, is(nullValue()));
         assertThat(back.next, is(nullValue()));
    }


    @Test
    void testDeleteEdge() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.delete(0);
        assertThat(list.isEmpty(), is(true));
    }

    @Test
    void nextNodeFront() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.delete(0);
        Node<Integer> front = ((DoubleLinkedList<Integer>) list).front;
        Node<Integer> back = ((DoubleLinkedList<Integer>) list).back;
        assertThat(back.prev, is(front));
    }

    @Test
    void nextNodeMiddle() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.delete(1);
        Node<Integer> front = ((DoubleLinkedList<Integer>) list).front;
        Node<Integer> back = ((DoubleLinkedList<Integer>) list).back;
        assertThat(back.prev, is(front));
    }

    @Test
    void testDeleteBasic() {
        IList<String> list = makeBasicList();
        list.delete(0);
        assertThat(list.get(0), is("b"));
        assertThat(list.get(1), is("c"));
        list.delete(1);
        assertThat(list.get(0), is("b"));
    }

    @Test
    void testDeleteWithMutators() {
        IList<String> list = makeBasicList();
        list.add("d");
        assertThat(list.get(3), is("d"));
        list.delete(3);
        assertThat(list.contains("d"), is(false));
        list.insert(0, "z");
        assertThat(list.get(0), is("z"));
        list.set(3, "t");
        assertThat(list.get(3), is("t"));
        list.delete(0);
        assertThat(list.contains("z"), is(false));
    }

    @Test
    void testDeleteTillEmptySameIndex() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        for (int i = 0; i < 20; i++) {
            list.delete(0);
        }

        assertThat(list.isEmpty(), is(true));
    }

    @Test
    void testDeleteInvalidIndex() {
        IList<String> list = makeBasicList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.delete(4));
        assertThrows(IndexOutOfBoundsException.class, () -> list.delete(-1));
    }
}
