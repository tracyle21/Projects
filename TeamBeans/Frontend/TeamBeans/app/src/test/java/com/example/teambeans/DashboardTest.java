package com.example.teambeans;

import android.graphics.Path;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.annotation.Config;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.annotation.Config;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@Config(sdk = {Build.VERSION_CODES.O_MR1})
@RunWith(MockitoJUnitRunner.class)
public class DashboardTest {
    private Dashboard dash;
    @Mock
    private OptionsAdapter ops1;
    @Mock
    private OptionsAdapter ops2;
    @Mock
    ProgressBar loading;
    @Mock
    RequestQueue queue;

    ArrayList<String> names1 = new ArrayList<String>();
    ArrayList<String> names2 = new ArrayList<String>();
    ArrayList<Boolean> filter1 = new ArrayList<Boolean>();
    ArrayList<Boolean> filter2 = new ArrayList<Boolean>();
    JsonObjectRequest request;
    String url;
    @Before
    public void setUp() {
        dash = new Dashboard();
        dash.filterAdapter1 = ops1;
        dash.filterAdapter2 = ops2;
        dash.loading = loading;
        dash.queue = queue;
        names1.add("Java");
        names1.add("a");
        names1.add("t");
        filter1.add(true);
        filter1.add(false);
        filter1.add(false);
        url = "http://10.24.226.25:8080/getUsersForLanguage?language=t";
        request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            dash.results.clear();
                            dash.results.add("k");
                        } catch (Exception e) {
                            System.out.println("Exception caught");
                            loading.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dash.results.clear();
                        dash.results.add("k");
                    }
                });
    }
    @Test
    public void mockFilterURLTest1() throws JSONException {
        url = "http://10.24.226.25:8080/getUsersForLanguage?language=Java";
        when(ops1.getThisFilter()).thenReturn(filter1);
        when(ops1.getThisNames()).thenReturn(names1);
        when(ops2.getThisFilter()).thenReturn(filter2);
        when(ops2.getThisNames()).thenReturn(names2);
        Mockito.doNothing().when(loading).setVisibility(View.GONE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            dash.results.clear();
                            dash.results.add("k");
                        } catch (Exception e) {
                            System.out.println("Exception caught");
                            loading.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dash.results.clear();
                        dash.results.add("k");
                    }
                });
        when(queue.add(request)).thenReturn(request);
        dash.filter2("k", new JSONArray());
        assertEquals(dash.url, url);
    }
    @Test
    public void mockFilterURLTest2() throws JSONException {
        url = "http://10.24.226.25:8080/getUsersForLanguage?language=a";
        filter1.clear();
        filter1.add(false);
        filter1.add(true);
        filter1.add(false);
        ArrayList<String> names2 = new ArrayList<String>();
        ArrayList<Boolean> filter2 = new ArrayList<Boolean>();
        when(ops1.getThisFilter()).thenReturn(filter1);
        when(ops1.getThisNames()).thenReturn(names1);
        when(ops2.getThisFilter()).thenReturn(filter2);
        when(ops2.getThisNames()).thenReturn(names2);
        Mockito.doNothing().when(loading).setVisibility(View.GONE);
        when(queue.add(request)).thenReturn(request);
        dash.filter2("k", new JSONArray());
        assertEquals(dash.url, url);
    }
    @Test
    public void mockFilterURLTest3() throws JSONException {
        url = "http://10.24.226.25:8080/getUsersForLanguage?language=t";
        filter1.clear();
        filter1.add(true);
        filter1.add(true);
        filter1.add(true);
        ArrayList<String> names2 = new ArrayList<String>();
        ArrayList<Boolean> filter2 = new ArrayList<Boolean>();
        when(ops1.getThisFilter()).thenReturn(filter1);
        when(ops1.getThisNames()).thenReturn(names1);
        when(ops2.getThisFilter()).thenReturn(filter2);
        when(ops2.getThisNames()).thenReturn(names2);
        Mockito.doNothing().when(loading).setVisibility(View.GONE);
        when(queue.add(request)).thenReturn(request);
        dash.filter2("k", new JSONArray());
        assertEquals(dash.url, url);
    }


    @Test
    public void mockFilterTestEmpty() throws JSONException {
        ArrayList<String> names1 = new ArrayList<String>();
        names1.add("k");
        names1.add("a");
        names1.add("t");
        ArrayList<Boolean> filter1 = new ArrayList<Boolean>();
        filter1.add(false);
        filter1.add(false);
        filter1.add(false);
        ArrayList<String> names2 = new ArrayList<String>();
        ArrayList<Boolean> filter2 = new ArrayList<Boolean>();
        when(ops1.getThisFilter()).thenReturn(filter1);
        when(ops1.getThisNames()).thenReturn(names1);
        when(ops2.getThisFilter()).thenReturn(filter2);
        when(ops2.getThisNames()).thenReturn(names2);
        Mockito.doNothing().when(loading).setVisibility(View.GONE);
        dash.filter2("k", new JSONArray());
        assertTrue(dash.results.size() == 0);
    }

    @Test
    public void populateLanguagesTest()
    {
        int l11 = dash.options1.size();
        dash.populate();
        int l12 = dash.options1.size();
        assertTrue(l11 == 0 && l12 == 14);
    }
    @Test
    public void populateClassesTest()
    {
        int l21 = dash.options2.size();
        dash.populate();
        int l22 = dash.options2.size();
        assertTrue(l21 == 0 && l22 == 10);
    }
}
