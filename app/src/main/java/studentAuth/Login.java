package studentAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarypass.MainActivity;
import com.example.librarypass.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mainFragments.HomeFragment;

public class Login extends AppCompatActivity {

    private TextInputLayout user,password;
    private TextView tvForgotpass,tvNewuser;
    private Button btLogin;
    private FirebaseAuth mauth;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mauth= FirebaseAuth.getInstance();
        user=findViewById(R.id.user);
        password=findViewById(R.id.password);
        tvForgotpass=findViewById(R.id.tvForgotpass);
        tvNewuser=findViewById(R.id.tvNewuser);
        btLogin=findViewById(R.id.btLogin);
        loadingbar=new ProgressDialog(this);


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
        user_id=user_id.concat("@kiit.ac.in");
        String Password=password.getEditText().getText().toString().trim();
        if(user_id.isEmpty())
            user.setError("User ID is required");
        else if (Password.isEmpty())
            password.setError("User ID is required");
        else {

            loadingbar.setTitle("Signing in");
            loadingbar.setMessage("Please wait while we are signing you in");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();



            mauth.signInWithEmailAndPassword(user_id,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mauth.getCurrentUser();
                        if (user.isEmailVerified()) {
                            Toast.makeText(Login.this, "Logged in successfuly", Toast.LENGTH_LONG).show();
                            loadingbar.dismiss();
                            sendUsertoMainactivity();
                        }
                        else {
                            loadingbar.dismiss();
                            Toast.makeText(Login.this, "Please verify your email", Toast.LENGTH_LONG).show();
                        }
                        //finish();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(Login.this, "Invalid Credentials"+message, Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }


                }
            });

        }

    }


    private void sendUsertoregactivity() {

        Intent logininten=new Intent(Login.this,Registration.class);
        startActivity(logininten);


    }
    private void sendUsertoMainactivity() {

        Intent mainintent=new Intent(Login.this, MainActivity.class);

        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();


    }

}
