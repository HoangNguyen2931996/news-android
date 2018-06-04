package com.example.kutudp.tinhangngay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

public class NotificationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayList<News> listNews;
    private NewsAdapter newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getWidgets();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            String str = MainActivity.getNotify();
            if("".equals(str)){
                CheckConnection.showToast(getApplicationContext(), "Không có tin mới nào");
            } else{
                int limit = Integer.parseInt(str);
                getNews(limit);
                actionListView();
                actionToolBar();
            }
            actionToolBar();
        } else {
            CheckConnection.showToast(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
            finish();
        }
    }
    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setNotify("");
                finish();
            }
        });
    }
    private void actionListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplication(), DetailActivity.class);
                intent.putExtra("linkItem", listNews.get(position).getLink());
                intent.putExtra("nameItem", listNews.get(position).getName());
                startActivity(intent);
            }
        });
    }

    private void getNews(int limit) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlServer + "/getnewslimit/"+limit, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null && response.length()>0) {

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

    private void getWidgets() {
        toolbar = (Toolbar) findViewById(R.id.toolBarNotify);
        listView = (ListView) findViewById(R.id.listViewNewsNotify);
        listNews = new ArrayList<>();
        newsAdapter = new NewsAdapter(listNews, getApplicationContext());
        listView.setAdapter(newsAdapter);
    }
}
