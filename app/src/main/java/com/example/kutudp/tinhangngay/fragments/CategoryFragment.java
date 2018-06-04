package com.example.kutudp.tinhangngay.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutudp.tinhangngay.DetailActivity;
import com.example.kutudp.tinhangngay.R;
import com.example.kutudp.tinhangngay.adapters.NewsAdapter;
import com.example.kutudp.tinhangngay.models.News;
import com.example.kutudp.tinhangngay.utils.CheckConnection;
import com.example.kutudp.tinhangngay.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CategoryFragment extends Fragment {
    private View view;
    private ListView listView;
    private ArrayList<News> listNews;
    private NewsAdapter newsAdapter;
    private int page = 1;
    private View footerView;
    private boolean isLoading = false;
    private boolean limitData = false;
    private mHandler handler;
    private String categoryName;
    public CategoryFragment() {
        categoryName = "";
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_fragment, null);
        getWidgets();

        if (CheckConnection.haveNetworkConnection(getActivity().getApplicationContext())) {
            getNews(page);
            loadMoreData();
        } else {
            CheckConnection.showToast(getActivity().getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
            getActivity().finish();
        }
        return view;
    }
    private void loadMoreData() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new  Intent(getActivity().getApplication(), DetailActivity.class);
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
    private void getWidgets() {
        listView = (ListView) view.findViewById(R.id.listViewNewsCat);
        listNews = new ArrayList<>();
        newsAdapter = new NewsAdapter(listNews, getActivity().getApplicationContext());
        listView.setAdapter(newsAdapter);
        LayoutInflater inflater =(LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView =(View) inflater.inflate(R.layout.progress_bar, null);
        handler = new mHandler();

    }
    private void getNews(int pageNum) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlServer + "/getnewsbynamecat/"+categoryName+"?page="+pageNum, new Response.Listener<JSONArray>() {
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
                    CheckConnection.showToast(getActivity().getApplicationContext(),"Đã hết dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.showToast(getActivity().getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
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
