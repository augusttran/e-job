package com.example.ejob.ui.employer.applications;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejob.R;

public class ViewPdf extends AppCompatActivity {
    private WebView web;
    final String PREFIX_WEB = "https://docs.google.com/gview?embedded=true&url=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web = findViewById(R.id.webView);

        String cvUrl = getIntent().getStringExtra("cvUrl");
        web.setWebViewClient(new WebViewClient());
        web.getSettings().setSupportZoom(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.loadUrl(PREFIX_WEB + cvUrl);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStackImmediate();
    }
}
