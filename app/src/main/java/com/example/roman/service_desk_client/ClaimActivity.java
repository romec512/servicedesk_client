package com.example.roman.service_desk_client;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class ClaimActivity extends AppCompatActivity {
    private Button btnSave;
    private EditText etTitle, etDescription, etInvNum;
    private TextView tvMaster, tvStatus, tvInvNum;
    private Spinner spinner;
    private String mode = "create";
    private int previousActivity = 0;
    private int id = -1;
    private String[] servicemans = {"Петров Павел Иванович", "Алексеев Михаил Петрович"};

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
        spinner = (Spinner)findViewById(R.id.spinner);


        final Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mode = extras.getString("mode");
            id = extras.getInt("id");
            if (mode.compareTo("edit") == 0) {
                etTitle.setText(extras.getString("title"));
                etDescription.setText(extras.getString("description"));
                id = extras.getInt("id");
                extras.getInt("inv_id");
                etInvNum.setText("" + extras.getInt("inv_id"));
                if(extras.getString("previous_activity").compareTo("AdminMainActivity") == 0){
                    previousActivity = 1;
                    mode = "attachServiceman";
                    spinner.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, servicemans);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    btnSave.setText("Назначить техника");
                    spinner.setVisibility(View.VISIBLE);
                }
                else if(extras.getString("previous_activity").compareTo("ServicemanActivity") == 0) {
                    previousActivity = 2;
                    mode = "closeClaim";
                    btnSave.setText("Закрыть заявку");
                    tvMaster.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    btnSave.setBackground(ContextCompat.getDrawable(this.getBaseContext(), R.drawable.buttoncreate));
                }
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
                spinner.setVisibility(View.INVISIBLE);
                if(extras.getString("previous_activity").compareTo("ServicemanActivity") == 0) {
                    previousActivity = 2;
                    mode = "closeClaim";
                    btnSave.setText("Закрыть заявку");
                    tvMaster.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    btnSave.setBackground(ContextCompat.getDrawable(this.getBaseContext(), R.drawable.buttoncreate));
                }
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
                } else if(mode.compareTo("attachServiceman") == 0) {
                    method = "claim/attachServiceman";
                    params += "&serviceman_id=" + spinner.getSelectedItemPosition();
                } else if(mode.compareTo("closeClaim") == 0) {
                    method = "claim/updateStatus";
                    params += "&status=2";
                } else {
                    method = "claim/create";
                }
                SendData sendData = new SendData(params, method, getBaseContext());
                try {
                    sendData.execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent userMainActivity = null;
                switch (previousActivity) {
                    case 0: userMainActivity = new Intent(ClaimActivity.this, UsersMainActivity.class); break;
                    case 1: userMainActivity = new Intent(ClaimActivity.this, AdminMainActivity.class); break;
                    case 2: userMainActivity = new Intent(ClaimActivity.this, ServicemanActivity.class); break;
                }
                startActivity(userMainActivity);
                finish();
            }
        });
    }
}
