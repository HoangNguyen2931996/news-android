package com.example.kutudp.tinhangngay;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutudp.tinhangngay.adapters.ResourceAdapter;
import com.example.kutudp.tinhangngay.adapters.ViewPagerAdapter;
import com.example.kutudp.tinhangngay.fragments.CategoryFragment;
import com.example.kutudp.tinhangngay.fragments.HomeFragment;
import com.example.kutudp.tinhangngay.models.Resource;
import com.example.kutudp.tinhangngay.utils.CheckConnection;
import com.example.kutudp.tinhangngay.utils.Server;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Fragment> listFragment;
    private ArrayList<String> listTitle;
    private ViewPagerAdapter viewPagerAdapter;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ListView listView;
    private ArrayList<Resource> listResource;
    private ResourceAdapter resourceAdapter;
    private static String notify;


    public static String getNotify() {
        return notify;
    }

    public static void setNotify(String notify) {
        MainActivity.notify = notify;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWidgets();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            FirebaseMessaging.getInstance().subscribeToTopic("news");
            actionBar();
            getCat();
            getResource();
            catchOnItemListView();
            catchOnItemTabLayout();
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

    private void catchOnItemTabLayout(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int possition = tabLayout.getSelectedTabPosition();
                try {
                    CategoryFragment categoryFragment = (CategoryFragment) listFragment.get(possition);
                    toolbar.setTitle(listTitle.get(possition));
                    Log.e("possion", String.valueOf(possition));
                    categoryFragment.setCategoryName(listTitle.get(possition));
                } catch (Exception e) {
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_reorder_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void catchOnItemListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                    Intent intent = new Intent(MainActivity.this, ResourceActivity.class);
                    intent.putExtra("idResource", listResource.get(position).getId());
                    intent.putExtra("nameResource", listResource.get(position).getName());
                    startActivity(intent);
                } else {
                    CheckConnection.showToast(getApplicationContext(), "Bạn hãy kiểm tra kết nối");
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void getResource() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlServer + "/getresource", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("name");
                            String link = jsonObject.getString("link");
                            listResource.add(new Resource(id, name, link));
                            resourceAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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

    private void getCat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlServer + "/getcat", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("name");
                            listFragment.add(new CategoryFragment());
                            listTitle.add(name);
                            CategoryFragment categoryFragment = (CategoryFragment) listFragment.get(i+1);
                            categoryFragment.setCategoryName(name);
                            viewPagerAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
        if(notify == null){
            notify = "";
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        listFragment = new ArrayList<>();
        listFragment.add(new HomeFragment());
        listTitle = new ArrayList<>();
        listTitle.add("Trang chủ");
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), listFragment, listTitle);
        viewPager.setAdapter(viewPagerAdapter);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        listView = (ListView) findViewById(R.id.listView);
        listResource = new ArrayList<>();
        resourceAdapter = new ResourceAdapter(listResource, getApplicationContext());
        listView.setAdapter(resourceAdapter);


    }

}
