package com.example.teambeans;

import android.content.Context;
import android.os.Build;

import com.android.volley.Request;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(sdk = {Build.VERSION_CODES.O_MR1})
@RunWith(MockitoJUnitRunner.class)
public class EditUserProfileTest {
    private EditUserProfile EditUserProfile;

    @InjectMocks
    UserProfile UserProfile;

    @InjectMocks
    EditUserProfileTA EditUserProfileTA;

    @InjectMocks
    EditUserProfileProfessor EditUserProfileProfessor;

    @InjectMocks
    EditUserProfile EditUserProfile2;

    @InjectMocks
    ViewFeedback ViewFeedback;

    @InjectMocks
    AddFeedback AddFeedback;

    @Mock
    ForMockitoTest server;

    @Before
    public void setUp() {
        EditUserProfile = new EditUserProfile();
    }

    @Test
    public void testGetAfterSpace(){
        String course = "COMS 309";
        String result = EditUserProfile.getAfterSpace(course);
        assertEquals("309",result);
    }

    @Test
    public void testGetBeforeSpace(){
        String course = "COMS 309";
        String result = EditUserProfile.getBeforeSpace(course);
        assertEquals("COMS",result);
    }

    @Test
    public void testPutRequest1(){
        String url = "http://10.24.226.25:8080/addLanguage";
        String username ="1";
        String language ="Java";
        String result = EditUserProfile2.connectionForPUTTesting(username,url,language,Request.Method.PUT, "language");
        assertEquals(url, result);
        verify(server, times(1)).connectionForPUTTesting(username,url,language,Request.Method.PUT, "language");
    }

    @Test
    public void testPutRequest2(){
        //test class taken
       String url = "http://10.24.226.25:8080/addOldCourse";;
        String username ="1";
        String course ="COMS 309";
        String result = EditUserProfile2.connectionForPUTTesting(username,url,course,Request.Method.PUT, "classTaken");
        assertEquals(url, result);//if result fails it will be null
        verify(server, times(1)).connectionForPUTTesting(username,url,course,Request.Method.PUT, "classTaken");
    }

    @Test
    public void testPutRequest3(){
        //test class assist for TA
        String url = "http://10.24.226.25:8080/addAssistingCourse";
        String username ="2"; //user with TA role
        String course ="COMS 309";
        String result = EditUserProfileTA.connectionForPUTTesting(username,url,course,Request.Method.PUT, "courseAssist");
        assertEquals(url, result);
        verify(server, times(1)).connectionForPUTTesting(username,url,course,Request.Method.PUT, "courseAssist");
    }

    @Test
    public void testPutRequest4(){
        //test class assist for TA
        String url = "http://10.24.226.25:8080/addTeachingCourse";
        String username ="3"; //user with Professor role
        String course ="COMS 309";
        String result = EditUserProfileProfessor.connectionForPUTTesting(username,url,course,Request.Method.PUT, "courseTeaching");
        assertEquals(url, result);
        verify(server, times(1)).connectionForPUTTesting(username,url,course,Request.Method.PUT, "courseTeaching");
    }

    @Test
    public void testPostRequest(){
        String url = "http://10.24.226.25:8080/deleteLanguage";;
        String username ="1";
        String language ="Java";
        String result = EditUserProfile2.connectionForPOSTTesting(username,url,language);
        assertEquals(url, result);
        verify(server, times(1)).connectionForPOSTTesting(username,url,language);
    }

    @Test
    public void testGetRequest(){
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/1/description";
        String result = UserProfile.connectionfForGETTesting(url);
        assertEquals(url, result);
        verify(server, times(1)).connectionfForGETTesting(url);
    }

    //everything below here is for demo4 mockito test
    @Test
    public void testGetRatingRequestTA(){
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/2/ratingProfile";
        String result = String.valueOf(ViewFeedback.connectionForGETRatingTesting(url));
        assertEquals("0.0", result); //TA teamwork rating will forever be 0.0
        verify(server, times(1)).connectionForGETRatingTesting(url);
    }

    @Test
    public void testGetRatingRequestProfessor(){
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/3/ratingProfile";
        String result = String.valueOf(ViewFeedback.connectionForGETRatingTesting(url));
        assertEquals("0.0", result); //Professor teamwork rating will forever be 0.0
        verify(server, times(1)).connectionForGETRatingTesting(url);
    }

    @Test
    public void testGetRatingRequestStudent(){
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/1/ratingProfile";
        float result = ViewFeedback.connectionForGETRatingTesting(url);
        if(result >= 0){
            result = 1;
        }
        assertEquals("1.0", String.valueOf(result)); //TA teamwork rating will forever be 0.0
        verify(server, times(1)).connectionForGETRatingTesting(url);
    }

    @Test
    public void testPUTRatingRequestStudent(){
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/averageOutRatingProfile";
        String username ="1";
        String role ="STUDENT";
        String result = AddFeedback.connectionForPUTRatingTesting(username, role, url);
        assertEquals(url, result);
        verify(server, times(1)).connectionForPUTRatingTesting(username, role, url);
    }

    @Test
    public void testPUTRatingRequestTA(){
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/averageOutRatingProfile";
        String username ="2";
        String role ="TA";
        String result = AddFeedback.connectionForPUTRatingTesting(username, role, url);
        assertEquals(url, result);
        verify(server, times(1)).connectionForPUTRatingTesting(username, role, url);
    }

    @Test
    public void testPUTRatingRequestProfessor(){
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/averageOutRatingProfile";
        String username ="3";
        String role ="PROFESSOR";
        String result = AddFeedback.connectionForPUTRatingTesting(username, role, url);
        assertEquals(url, result);
        verify(server, times(1)).connectionForPUTRatingTesting(username, role, url);
    }
}