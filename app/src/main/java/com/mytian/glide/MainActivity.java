package com.mytian.glide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
private RecyclerView recyclerView;
    private GlideAdapter adapter;
    private ArrayList<String> list=new ArrayList<>();
    private OkHttpClient mClient;
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mClient = new OkHttpClient();
        adapter = new GlideAdapter(this,list);
        recyclerView.setAdapter(adapter);
        loadApi(index);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(true){
                    index += 1;
                    loadApi(index);
                }
            }

        });
    }
    private void loadApi(int page){
        Request request = new Request.Builder().url("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/"+page).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag","loading failure ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    try {
                        JSONObject json = new JSONObject(result);
                        JSONArray array = new JSONArray(json.getString("results"));
                        for(int i = 0;i<array.length();i++){
                            JSONObject ob = array.getJSONObject(i);
                            list.add(ob.getString("url"));
                            Log.e("tag","========== url: "+ob.getString("url"));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                        adapter.notifyChange(list);
                            }
                        });

                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
