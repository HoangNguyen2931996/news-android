package com.example.kutudp.tinhangngay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutudp.tinhangngay.adapters.NewsAdapter;
import com.example.kutudp.tinhangngay.models.News;
import com.example.kutudp.tinhangngay.utils.CheckConnection;
import com.example.kutudp.tinhangngay.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResourceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayList<News> listNews;
    private NewsAdapter newsAdapter;
    private int idResource = 0;
    private String nameResource;
    private int page = 1;
    private View footerView;
    private boolean isLoading = false;
    private boolean limitData = false;
    private mHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        getWidgets();

        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            idResource = getIdResource();
            nameResource = getNameResource();
            toolbar.setTitle(nameResource);
            actionToolBar();
            getNews(page);
            loadMoreData();
        } else {
            CheckConnection.showToast(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
            finish();
        }
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
    private String getNameResource() {
        return getIntent().getStringExtra("nameResource");
    }

    private void loadMoreData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplication(), DetailActivity.class);
                intent.putExtra("linkItem", listNews.get(position).getLink());
                intent.putExtra("nameItem", listNews.get(position).getName());
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if((firstVisibleItem+visibleItemCount)==totalItemCount && totalItemCount !=0 && isLoading == false && limitData==false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }
    private void getNews(int pageNum) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlServer + "/getnewsbyidresource/"+idResource+"?page="+pageNum, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null && response.length()>0) {

                    listView.removeFooterView(footerView);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("name");
                            String preview = jsonObject.getString("preview");
                            String link = jsonObject.getString("link");
                            String picture = jsonObject.getString("picture");
                            String dateCreated = jsonObject.getString("date_created");
                            int idCategory = jsonObject.getInt("id_category");
                            int idResource = jsonObject.getInt("id_resource");
                            listNews.add(new News(id, name, preview, link, picture, dateCreated, idCategory, idResource));
                            newsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else{
                    limitData = true;
                    listView.removeFooterView(footerView);
                    CheckConnection.showToast(getApplicationContext(),"Đã hết dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.showToast(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
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

    private int getIdResource() {
        return getIntent().getIntExtra("idResource", -1);
    }




    private void getWidgets() {
        toolbar = (Toolbar) findViewById(R.id.toolBarResource);
        listView = (ListView) findViewById(R.id.listViewNewsResource);
        listNews = new ArrayList<>();
        newsAdapter = new NewsAdapter(listNews, getApplicationContext());
        listView.setAdapter(newsAdapter);
        LayoutInflater inflater =(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView =(View) inflater.inflate(R.layout.progress_bar, null);
        handler = new mHandler();

    }
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    listView.addFooterView(footerView);
                    break;
                case 1:
                    getNews(++page);
                    isLoading = false;
                    break;
            }

        }
    }
    public class ThreadData extends Thread{
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = handler.obtainMessage(1);
            handler.sendMessage(message);

        }
    }

}
