package com.example.ejob.ui.login;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.widget.Button;

import com.example.ejob.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.internal.DoNotInstrument;

import static org.junit.Assert.*;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
@DoNotInstrument
public class LoginActivityTest {

    private LoginActivity loginActivity;
    private Button loginButton;

    @Before
    public void setUp() throws Exception {
       loginActivity = Robolectric.buildActivity(LoginActivity.class)
               .create()
               .get();

       loginButton = (Button) loginActivity.findViewById(R.id.btLogin);
    }

    @Test
    public void shouldNotBeNull(){
        loginActivity = Robolectric.buildActivity(LoginActivity.class)
                .create()
                .get();
        assertNotNull(loginActivity);
    }



//    @After
//    public void tearDown() throws Exception {
//
//
//    }
//
//    @Test
//    public void onCreate() {
//    }
//
//    @Test
//    public void onStart() {
//    }
//
//    @Test
//    public void saveData() {
//    }
//
//    @Test
//    public void handleError() {
//    }
//
//    @Test
//    public void login() {
//    }
//
//    @Test
//    public void openAdminActivity() {
//    }
//
//    @Test
//    public void openEmployerActivity() {
//    }
//
//    @Test
//    public void openUserActivity() {
//    }

//    @Test
//    public void testContext(){
//        Application application;
//        Context context = application.getApplicationContext();
//    }


//    @Test
//    public void clickingLogin_startLoginActivity() throws Exception{
//        loginButton.performClick();
//        assertNotNull();
//
//    }


}