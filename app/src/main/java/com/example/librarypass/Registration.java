package com.example.librarypass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Registration extends AppCompatActivity {

    Spinner spStream;
    TextView tvName,tvRoll,tvContact,tvHoste,tvYear,tvStream,tvBranch,tvcnfpwd,tvpwd;
    EditText etName,etRoll,etContact,etHostel,etYear,etBranch,etpwd,etcnfpwd;
    Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        tvName=findViewById(R.id.tvName);
        tvRoll=findViewById(R.id.tvRoll);
        tvContact=findViewById(R.id.tvContact);
        tvHoste=findViewById(R.id.tvHostel);
        tvYear=findViewById(R.id.tvYear);
        tvStream=findViewById(R.id.tvStream);
        tvBranch=findViewById(R.id.tvBranch);
        tvcnfpwd=findViewById(R.id.tvcnfpwd);
        tvpwd=findViewById(R.id.tvpwd);

        etcnfpwd=findViewById(R.id.etcnfpwd);
        etpwd=findViewById(R.id.etpwd);
        etName=findViewById(R.id.etName);
        etRoll=findViewById(R.id.etRoll);
        etContact=findViewById(R.id.etContact);
        etHostel=findViewById(R.id.etHostel);
        etYear=findViewById(R.id.etYear);
        etBranch=findViewById(R.id.etBranch);

        spStream=findViewById(R.id.spStream);

        btRegister=findViewById(R.id.btRegister);
    }
}
