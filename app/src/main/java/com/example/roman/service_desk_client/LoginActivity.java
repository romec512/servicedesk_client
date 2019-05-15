package com.example.roman.service_desk_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private String usersCredentials = "user:password";
    private String adminsCredentials = "admin:password";
    private TextView tvLoginError;
    private EditText etLogin, etPassword;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvLoginError = (TextView)findViewById(R.id.tvLoginError);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        loginBtn = (Button)findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String credentials = etLogin.getText() + ":" + etPassword.getText();
                if(credentials.compareTo(usersCredentials) == 0) {
                    Intent userActivity = new Intent(LoginActivity.this, UsersMainActivity.class);
                    startActivity(userActivity);
                    finish();
                } else if(credentials.compareTo(adminsCredentials) == 0) {
                    Intent adminActivity = new Intent(LoginActivity.this, AdminMainActivity.class);
                    startActivity(adminActivity);
                    finish();
                } else {
                    tvLoginError.setText("Неверный логин или пароль!");
                }
            }
        });
        // ToDo: remove after development
//        Intent userActivity = new Intent(LoginActivity.this, UsersMainActivity.class);
//        startActivity(userActivity);
//        Intent userActivity = new Intent(LoginActivity.this, AdminMainActivity.class);
//        startActivity(userActivity);
    }
}
