package com.example.roman.service_desk_client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.roman.service_desk_client.Classes.Claim;
import com.example.roman.service_desk_client.Classes.ClaimsAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutionException;

public class ServicemanActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        recyclerView = (RecyclerView)findViewById(R.id.rvClaims);

        SendData sender = new SendData("serviceman_id=1", "claim/getServicemans", getBaseContext());
        Claim[] claims = null;
        try {
            sender.execute().get();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            claims = gson.fromJson(sender.resultString, Claim[].class);
            ClaimsAdapter adapter = new ClaimsAdapter(claims, "ServicemanActivity");
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recyclerView.setAdapter(adapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
