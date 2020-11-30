package com.example.teambeans;

import android.os.Build;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Config(sdk = {Build.VERSION_CODES.O_MR1})
@RunWith(MockitoJUnitRunner.class)
public class InboxTest {
    private Inbox inboxFragment;

    @Mock
    private Inbox view;

    @Mock
    Inbox.InboxElement inboxElementMock;

    @Mock
    private InboxRequest server;

    @Before
    public void setUp() {
        inboxFragment = new Inbox();
    }

    @Test
    public void testDatabaseToDisplayDateFormat() {
        String databaseFormat = "2020-10-19T18:56:39.000+00:00";
        String displayFormat = "18:56:39 10-19-2020";
        String result = inboxFragment.databaseToDisplayTime(databaseFormat);
        assertEquals(displayFormat, result);
    }

    @Test
    public void testGetCurrentDisplayTime() {
        String displayFormat = "18:56:39 10-19-2020";
        String result = inboxFragment.getCurrentDisplayTime();
        String resultFormatted = "hh" + result.substring(2,3) + "mm" + result.substring(5,6) + "ss mm" + result.substring(11,12) + "dd" +  result.substring(14,15) + "yyyy";
        String expected = "hh:mm:ss mm-dd-yyyy";
        assertEquals(expected, resultFormatted);
    }

    @Test
    public void testDisplayToDatabaseDateFormat() {
        String databaseFormat = "2020-10-19T18:56:39.000+00:00";
        String displayFormat = "18:56:39 10-19-2020";
        String result = inboxFragment.displayTimeToDatabaseTime(displayFormat);
        assertEquals(databaseFormat, result);
    }

    @Test
    public void testConvoSort() {
        Inbox inbox = new Inbox();
        ArrayList<String> names = new ArrayList<String>();
        names.add("hoopink");
        String message = "hello world";
        String displayDate2 = "18:56:39 10-19-2020";
        String displayDate3 = "18:52:39 10-19-2020";
        String displayDate1 = "18:54:39 10-19-2020";
        String user1 = "kjh04";
        String user2 = "hoopink";

        Inbox.InboxElement element1 = new Inbox.InboxElement(names, message, displayDate1);
        Inbox.InboxElement element2 = new Inbox.InboxElement(names, message, displayDate2);
        Inbox.InboxElement element3 = new Inbox.InboxElement(names, message, displayDate3);


        inboxFragment.inbox = new ArrayList<Inbox.InboxElement>();
        inboxFragment.inbox.add(element1);
        inboxFragment.inbox.add(element2);
        inboxFragment.inbox.add(element3);

        inboxFragment.sortByTime();

        for(Inbox.InboxElement i: inboxFragment.inbox){
            System.out.println(i.getTimeDisplay());
        }

        assertEquals("18:52:39 10-19-2020", inboxFragment.inbox.get(0).getTimeDisplay());
        assertEquals("18:54:39 10-19-2020", inboxFragment.inbox.get(1).getTimeDisplay());
        assertEquals("18:56:39 10-19-2020", inboxFragment.inbox.get(2).getTimeDisplay());
    }

//    @Test
//    public void testAddTwoUserConversation() {
//        Inbox inbox = new Inbox();
//        ArrayList<String> names = new ArrayList<String>();
//        names.add("hoopink");
//        String message = "hello world";
//        String displayDate = "18:56:39 10-19-2020";
//        String user1 = "kjh04";
//        String user2 = "hoopink";
//
//        Inbox.InboxElement element = new Inbox.InboxElement(names, message, displayDate);
//
//        inbox.setModel(server);
//        inbox.setCurrentUser("kjh04");
//
//        inbox.addToInbox(element);
//
//        assertEquals(inbox.getInbox().size(), 0);
//        verify(server, times(1)).addTwoUserConversation(user1,  names.get(0), inbox.displayTimeToDatabaseTime(displayDate), element.getMessage());
//    }

//    @Test
//    public void testAddThreeUserConversation() {
//        Inbox inbox = new Inbox();
//        ArrayList<String> names = new ArrayList<String>();
//        names.add("kathyrn");
//        names.add("tracy");
//        String message = "hello world";
//        String displayDate = "18:56:39 10-19-2020";
//        String user1 = "kjh04";
//
//        Inbox.InboxElement element = new Inbox.InboxElement(names, message, displayDate);
//
//        inbox.setModel(server);
//        inbox.setCurrentUser("kjh04");
//
//        inbox.addToInbox(element);
//
//        assertEquals(inbox.getInbox().size(), 0);
//        verify(server, times(1)).addThreeUserConversation(user1,  names.get(0), names.get(1), inbox.displayTimeToDatabaseTime(displayDate), element.getMessage());
//    }

//    @Test
//    public void testAddFourUserConversation() {
//        Inbox inbox = new Inbox();
//        ArrayList<String> names = new ArrayList<String>();
//        names.add("kathyrn");
//        names.add("tracy");
//        names.add("renee");
//        String message = "hello world";
//        String displayDate = "18:56:39 10-19-2020";
//        String user1 = "kjh04";
//
//        Inbox.InboxElement element = new Inbox.InboxElement(names, message, displayDate);
//
//        inbox.setModel(server);
//        inbox.setCurrentUser("kjh04");
//
//        inbox.addToInbox(element);
//
//        assertEquals(inbox.getInbox().size(), 0);
//        verify(server, times(1)).addFourUserConversation(user1,  names.get(0), names.get(1), names.get(2), inbox.displayTimeToDatabaseTime(displayDate), element.getMessage());
//    }

    @Test
    public void failToReachRequestMethodIfDateIncorrect() {
        Inbox inbox = new Inbox();
        ArrayList<String> names = new ArrayList<String>();
        names.add("kathyrn");
        names.add("tracy");
        names.add("renee");
        String message = "hello world";
        String displayDate = "18:56:39 10-19-2020";
        String user1 = "kjh04";

        inbox.setModel(server);
        inbox.setCurrentUser("kjh04");

        inbox.addTwoUserConversation(user1, names.get(0), displayDate, message);
        inbox.addThreeUserConversation(user1, names.get(0), names.get(1), displayDate, message);
        inbox.addFourUserConversation(user1, names.get(0), names.get(1), names.get(2), displayDate, message);

        verify(server, times(0)).addTwoUserConversation(user1,  names.get(0), displayDate, message);
        verify(server, times(0)).addThreeUserConversation(user1,  names.get(0), names.get(1), displayDate, message);
        verify(server, times(0)).addFourUserConversation(user1,  names.get(0), names.get(1), names.get(2), displayDate, message);
    }
}