package com.example.librarypass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    private TextInputLayout user,password;
    private TextView tvForgotpass,tvNewuser;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user=findViewById(R.id.user);
        password=findViewById(R.id.password);
        tvForgotpass=findViewById(R.id.tvForgotpass);
        tvNewuser=findViewById(R.id.tvNewuser);
        btLogin=findViewById(R.id.btLogin);


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowusertologin();
            }
        });

        tvNewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUsertoregactivity();
            }
        });

    }

    private void allowusertologin()
    {
        String user_id=user.getEditText().getText().toString().trim();
        String Password=password.getEditText().getText().toString().trim();
        if(user_id.isEmpty())
            user.setError("User ID is required");
        else if (Password.isEmpty())
            password.setError("User ID is required");
    }

    private void sendUsertoregactivity()
    {

    }
}
