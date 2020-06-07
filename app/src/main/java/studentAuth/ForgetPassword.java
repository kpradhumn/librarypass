package studentAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.librarypass.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private EditText etfpwd;
    private Button sendmail;
    FirebaseAuth mauth;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        etfpwd=findViewById(R.id.etpwd);
        sendmail=findViewById(R.id.btn);
        mauth=FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(ForgetPassword.this);
        sendmail.setOnClickListener(v -> {

            String userEmail=etfpwd.getText().toString().trim();
            if (userEmail.isEmpty())
            {
                etfpwd.setError("Email is required");
            }
            else
            {
                loadingbar.setTitle("Sending E-mail");
                loadingbar.setMessage("Please wait while we are sending you recovery mail");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                mauth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        loadingbar.dismiss();
                        Toast.makeText(getApplicationContext(),"Please check your E-mail if you want to reset the password",Toast.LENGTH_LONG).show();startActivity(new Intent(ForgetPassword.this, Login.class));
                        finish();
                    }
                    else
                    {
                        loadingbar.dismiss();
                        String msg=task.getException().toString();
                        Toast.makeText(getApplicationContext(),"Error occured"+msg,Toast.LENGTH_LONG).show();
                    }

                });
            }

        });


    }
}
