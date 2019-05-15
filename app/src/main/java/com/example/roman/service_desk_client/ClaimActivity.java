package com.example.roman.service_desk_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.roman.service_desk_client.Classes.Claim;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class ClaimActivity extends AppCompatActivity {
    private Button btnSave;
    private EditText etTitle, etDescription, etInvNum;
    private TextView tvMaster, tvStatus, tvInvNum;
    private String mode = "create";
    private int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnSave = (Button)findViewById(R.id.btnSave);
        tvMaster = (TextView)findViewById(R.id.tvClaimsMaster);
        tvStatus = (TextView)findViewById(R.id.tvClaimStatus);
        tvInvNum = (TextView)findViewById(R.id.tvInvNum);
        etInvNum = (EditText)findViewById(R.id.etInvNum);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mode = extras.getString("mode");
            if (mode.compareTo("edit") == 0) {
                etTitle.setText(extras.getString("title"));
                etDescription.setText(extras.getString("description"));
                id = extras.getInt("id");
                extras.getInt("inv_id");
                etInvNum.setText("" + extras.getInt("inv_id"));
            } else if(mode.compareTo("show") == 0) {
                etTitle.setText(extras.getString("title"));
                etDescription.setText(extras.getString("description"));
                etTitle.setEnabled(false);
                etDescription.setEnabled(false);
                btnSave.setVisibility(View.INVISIBLE);
                tvStatus.setText(extras.getString("status"));
                tvMaster.setText(extras.getString("serviceman"));
                etInvNum.setText("" + extras.getInt("inv_id"));
                etInvNum.setEnabled(false);
            }
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "title=" + etTitle.getText() + "&description=" + etDescription.getText()
                        + "&user_id=1&id=" + id + "&inv_id=" + etInvNum.getText();
                String method = "";
                if(mode.compareTo("edit") == 0) {
                    method = "claim/edit";
                } else {
                    method = "claim/create";
                }
                SendData sendData = new SendData(params, method, getBaseContext());
                sendData.execute();
                // ToDo: надо возвращать пользователя на обратную страницу( user на user activity, admin на админ и тд)
                Intent userMainActivity = new Intent(ClaimActivity.this, UsersMainActivity.class);
                startActivity(userMainActivity);
                finish();
            }
        });
    }
}
