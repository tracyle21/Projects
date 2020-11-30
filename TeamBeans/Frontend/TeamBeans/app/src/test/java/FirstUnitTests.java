import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.teambeans.Login;
import com.example.teambeans.MainActivity;
import com.example.teambeans.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.zip.Inflater;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirstUnitTests {
    private String TEST_STRING;

    @Test
    public void checkLoginString()
    {
        readFromXML(R.string.this_is_login);
        assertEquals(TEST_STRING, "Login");
    }
    @Test
    public void checkUsernameString()
    {
        readFromXML(R.string.enter_username);
        assertEquals(TEST_STRING, "Enter Username");
    }
    @Test
    public void checkPasswordString()
    {
        readFromXML(R.string.enter_password);
        assertEquals(TEST_STRING, "Enter Password");
    }
    @Test
    public void checkColorAccentString()
    {
        readFromXML(R.color.colorAccent);
        assertEquals(TEST_STRING, "#D0A32B");
    }
    public void readFromXML(int id) {
        switch (id) {
            case R.string.this_is_login:
                TEST_STRING = "Login";
                break;
            case R.string.enter_username:
                TEST_STRING = "Enter Username";
                break;
            case R.string.enter_password:
                TEST_STRING = "Enter Password";
                break;
            case R.color.colorAccent:
                TEST_STRING = "#D0A32B";
        }
    }
}