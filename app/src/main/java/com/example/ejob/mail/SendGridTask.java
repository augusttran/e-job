package com.example.ejob.mail;

import android.os.AsyncTask;

import com.sendgrid.SendGrid;

import java.util.Hashtable;

public class SendGridTask extends AsyncTask<Hashtable<String, String>, Void, String> {

    @Override
    protected String doInBackground(Hashtable<String, String>... hashtables) {
        Hashtable<String, String> hash = hashtables[0];
        SendGridCredentials sendGridCredentials = new SendGridCredentials();
        // SendGrid sendGrid = new SendGrid(sendGridCredentials.getUsername(),
        // sendGridCredentials.getPassword());
        return null;
    }
}
