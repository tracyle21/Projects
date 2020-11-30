package com.example.teambeans;

import android.os.Build;
import android.os.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Config(sdk = {Build.VERSION_CODES.O_MR1})
@RunWith(MockitoJUnitRunner.class)
public class ChatEnvironmentTest {
    private ChatEnvironment chatEnvironmentFragment;

    @Mock
    private ChatEnvironmentRequest server;

    @Before
    public void setUp() {
        chatEnvironmentFragment = new ChatEnvironment();
    }

    @Test
    public void testAddMessage() {
        ChatEnvironment e = new ChatEnvironment();
        e.setModel(server);
        e.setCurrentUser("kjh04");


        ChatEnvironment.Message m = new ChatEnvironment.Message("Hello", "kjh04", "18:56:39 10-19-2020");
        e.messages = new ArrayList<>();
        e.addMessageToMessages(m);
        verify(server, times(1)).addMessage(null, "kjh04", "Hello",  e.displayTimeToDatabaseTime("18:56:39 10-19-2020"));
    }

    @Test
    public void testDoNotAddMessageIfFromIsNull() {
        ChatEnvironment e = new ChatEnvironment();
        e.setModel(server);
        e.setCurrentUser("kjh04");


        ChatEnvironment.Message m = new ChatEnvironment.Message("Hello", null, "18:56:39 10-19-2020");
        e.messages = new ArrayList<>();
        e.addMessageToMessages(m);
        verify(server, times(0)).addMessage(null, null, "Hello",  e.displayTimeToDatabaseTime("18:56:39 10-19-2020"));
    }

    @Test
    public void testDoNotAddMessageIfDateIsNull() {
        ChatEnvironment e = new ChatEnvironment();
        e.setModel(server);
        e.setCurrentUser("kjh04");


        ChatEnvironment.Message m = new ChatEnvironment.Message("Hello", "kjh04", null);
        e.messages = new ArrayList<>();
        e.addMessageToMessages(m);
        verify(server, times(0)).addMessage(null, "kjh04", "Hello",  null);
    }

    @Test
    public void testDoNotAddMessageIfMessageIsNull() {
        ChatEnvironment e = new ChatEnvironment();
        e.setModel(server);
        e.setCurrentUser("kjh04");


        ChatEnvironment.Message m = new ChatEnvironment.Message(null, "kjh04", "18:56:39 10-19-2020");
        e.messages = new ArrayList<>();
        e.addMessageToMessages(m);
        verify(server, times(0)).addMessage(null, "kjh04", null,  e.displayTimeToDatabaseTime("18:56:39 10-19-2020"));
    }

    @Test
    public void testCompareToSame() {
        ChatEnvironment.Message m1 = new ChatEnvironment.Message("Hello", "a", "21:29:24 11-12-2020");
        ChatEnvironment.Message m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        int result1 = m1.compareTo(m2);
        assertEquals(0, result1);

        int result2 = m2.compareTo(m1);
        assertEquals(0, result2);
    }

    @Test
    public void testCompareToLessThan() {
        ChatEnvironment.Message m1;
        ChatEnvironment.Message m2;
        int result;

        m1 = new ChatEnvironment.Message("Hello", "a", "21:29:24 11-12-2019");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m1.compareTo(m2);
        assertEquals(-1, result);

        m1 = new ChatEnvironment.Message("Hello", "a", "21:29:24 11-05-2020");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m1.compareTo(m2);
        assertEquals(-1, result);

        m1 = new ChatEnvironment.Message("Hello", "a", "21:29:24 10-12-2020");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m1.compareTo(m2);
        assertEquals(-1, result);

        m1 = new ChatEnvironment.Message("Hello", "a", "21:29:22 11-12-2020");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m1.compareTo(m2);
        assertEquals(-1, result);

        m1 = new ChatEnvironment.Message("Hello", "a", "21:18:24 11-12-2020");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m1.compareTo(m2);
        assertEquals(-1, result);

        m1 = new ChatEnvironment.Message("Hello", "a", "13:18:24 11-12-2020");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m1.compareTo(m2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareToGreaterThan() {
        ChatEnvironment.Message m1;
        ChatEnvironment.Message m2;
        int result;

        m1 = new ChatEnvironment.Message("Hello", "a", "21:29:24 11-12-2019");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m2.compareTo(m1);
        assertEquals(1, result);

        m1 = new ChatEnvironment.Message("Hello", "a", "21:29:24 11-05-2020");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m2.compareTo(m1);
        assertEquals(1, result);

        m1 = new ChatEnvironment.Message("Hello", "a", "21:29:24 10-12-2020");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m2.compareTo(m1);
        assertEquals(1, result);

        m1 = new ChatEnvironment.Message("Hello", "a", "21:29:22 11-12-2020");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m2.compareTo(m1);
        assertEquals(1, result);

        m1 = new ChatEnvironment.Message("Hello", "a", "21:18:24 11-12-2020");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m2.compareTo(m1);
        assertEquals(1, result);

        m1 = new ChatEnvironment.Message("Hello", "a", "13:18:24 11-12-2020");
        m2 = new ChatEnvironment.Message("World", "b", "21:29:24 11-12-2020");

        result = m2.compareTo(m1);
        assertEquals(1, result);
    }

    @Test
    public void testMessageSort() {
        ChatEnvironment.Message m1 = new ChatEnvironment.Message("Hello", "a", "21:29:24 03-12-2020");
        ChatEnvironment.Message m2 = new ChatEnvironment.Message("World", "b", "21:29:24 02-12-2020");
        ChatEnvironment.Message m3 = new ChatEnvironment.Message("World", "b", "21:29:24 01-12-2020");

        chatEnvironmentFragment.messages = new ArrayList<ChatEnvironment.Message>();
        chatEnvironmentFragment.messages.add(m3);
        chatEnvironmentFragment.messages.add(m1);
        chatEnvironmentFragment.messages.add(m2);

        chatEnvironmentFragment.sortByTime();

        assertEquals("21:29:24 01-12-2020", chatEnvironmentFragment.messages.get(2).getTime());
        assertEquals("21:29:24 02-12-2020", chatEnvironmentFragment.messages.get(1).getTime());
        assertEquals("21:29:24 03-12-2020", chatEnvironmentFragment.messages.get(0).getTime());
    }
}
