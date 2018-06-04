package com.example.kutudp.tinhangngay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DetailActivity extends AppCompatActivity {

    private WebView webView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        webView = (WebView) findViewById(R.id.webView);
        toolbar = (Toolbar) findViewById(R.id.toolBarDetail);
        Intent intent = getIntent();
        String linkItem = intent.getStringExtra("linkItem");
        String title = intent.getStringExtra("nameItem");
        toolbar.setTitle(title);
        webView.loadUrl(linkItem);
        webView.setWebViewClient(new WebViewClient());
        actionToolBar();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notify, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.notify:
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
