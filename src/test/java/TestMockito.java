import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TestMockito {
    List mockedList = mock(List.class);
    LinkedList mockLinkedList = mock(LinkedList.class);

    /*
    *  1. mockito test interface, we don't even have a Collection object here.
    *  2. black box, focus on behaviour!!
    *  3. no real stuff is save into the mock object
    * */

    @Test
    public void testAddToMock() throws Exception {
        mockedList.add("one");
        mockedList.add("two");
        verify(mockedList, times(1)).add("one");
        verify(mockedList, times(1)).add("two");
        verify(mockedList, times(2)).add(any());
    }

    @Test
    public void testClear() throws Exception {
        mockedList.add("one");
        System.out.println(">>> " + mockedList.size()); // 0
        System.out.println(">>> " + mockedList.contains("one")); // false
        System.out.println(">>> " + mockedList.contains("two")); // false
        mockedList.clear();
        System.out.println(">>> " + mockedList.size()); // 0
        verify(mockedList, times(1)).clear();
        verify(mockedList, times(1)).add("one");
    }

    @Test
    public void testStub() {
        when(mockLinkedList.get(0)).thenReturn("first");
        mockLinkedList.add("first");
        System.out.println("*** " + mockLinkedList.size()); // 0
        System.out.println("*** " + mockLinkedList.contains("first")); // false
        System.out.println("*** " + mockLinkedList.contains("two")); // false
        assertThat(mockLinkedList.get(0)).isEqualTo("first");
        assertThat(mockLinkedList.get(1)).isEqualTo(null);
    }

    @Test(expected = RuntimeException.class)
    public void testException(){
        when(mockLinkedList.get(0)).thenReturn("first");
        when(mockLinkedList.get(1)).thenThrow(new RuntimeException());
        System.out.println("4 >>> " + mockLinkedList.get(0));
        System.out.println(mockLinkedList.get(1));
    }

    @Test
    public void testWithoutStub(){
        /*
        *   if you don't care what get(0) returns, then it should not be stubbed.
        * */
        mockLinkedList.get(0);
        verify(mockLinkedList).get(0);
    }

    @Test
    public void testArgumentMatchers(){
        when(mockedList.get(anyInt())).thenReturn("element");
        System.out.println(mockedList.get(1000));
        verify(mockedList).get(anyInt());
        verify(mockedList).get(anyInt());

        /*
        *   verify(mock).someMethod(anyInt(), anyString(), eq("hahah"));  // true
        *   verify(mock).someMethod(anyInt(), anyString(), "hahah");  // false
        * */

    }

    @Test
    public void testVerifyingExactNumber(){
        mockedList.add("one");
        mockedList.add("two");
        mockedList.add("two");
        mockedList.add("three");
        mockedList.add("three");
        mockedList.add("three");

        verify(mockedList).add("one");
        verify(mockedList, times(1)).add("one");

        verify(mockedList, times(2)).add("two");

        verify(mockedList, times(3)).add("three");

        verify(mockedList, never()).add("four");

        verify(mockedList, atMostOnce()).add("one");

        verify(mockedList, atLeastOnce()).add("two");

        verify(mockedList, atLeast(2)).add("two");

        verify(mockedList, atMost(5)).add("two");
    }



    List<Integer> list = new LinkedList<>();
    List spy = spy(list);

    @Test
    public void testSpy(){
        /*
        * when(spy.get(0)).thenReturn("foo");
        * Impossible: real method is called so spy.get(0) throws IOB Exception. (the list is yet empty)
        * */
        doReturn("foo").when(spy).get(0);
        System.out.println("spy get(0) >>> " + spy.get(0));

        when(spy.size()).thenReturn(100);
        spy.add("one");
        spy.add("two");
        System.out.println("spy size >>> " + spy.size());
        System.out.println("spy get(1) >>> " + spy.get(1));

        verify(spy).add("one");
        verify(spy).add("two");

        System.out.println("real size >> " + list.size());
    }
}
