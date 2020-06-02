package studentAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarypass.ForgetPassword;
import com.example.librarypass.MainActivity;
import com.example.librarypass.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Models.mStudent;
import mainFragments.HomeFragment;

public class Login extends AppCompatActivity {

    private TextInputLayout user,password;
    private TextView tvForgotpass,tvNewuser;
    private Button btLogin;
    private FirebaseAuth mauth;
    private ProgressDialog loadingbar;
    private Models.mStudent mStudent = null;


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
        tvForgotpass.setOnClickListener(v -> {
           startActivity(new Intent(Login.this, ForgetPassword.class));
            finish();
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
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        String currentuserid = mauth.getUid();
        DocumentReference docRef = fstore.collection("Student").document(currentuserid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mStudent = document.toObject(Models.mStudent.class);
                        setMain(mStudent);
                        //setLayoutWidgets(mBuyerPrsetExistingData(mStudent);
                    } else {

                        //Toast.makeText(BuyeProfileCreation.this,"No data history found",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Login.this, "No data history found task failed", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private void allowlogin()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("hostel_pref", MODE_PRIVATE);
        String hostelName = sharedPreferences.getString("hostel", "");
        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        String day = dayFormat.format(calfordate.getTime());
        String currentuserid = mauth.getCurrentUser().getUid();
        String uid = day.concat(currentuserid);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Hostel").document(hostelName).collection("studentList").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String status = (String) document.get("status");
                        if (status.equals("0")) {
                            new AlertDialog.Builder(Login.this).setTitle("Cannot Login").setMessage(Html.fromHtml("Seems a user is <b>logged in</b> from another device.<br>Since a pass is yet to be <b>returned</b> from this user hence <b>login</b> for now is not possible.<br><b>Return</b> the pass first by scanning the QR code from logged in device "))
                                    .setPositiveButton("OK", null).show();
                        }
                        else
                        {Intent mainintent=new Intent(Login.this, MainActivity.class);
                            mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainintent);
                            finish();}}
                    else
                    {Intent mainintent=new Intent(Login.this, MainActivity.class);
                        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainintent);
                        finish();}
                }
            }
        });
    }


    private void setMain(Models.mStudent mStudent) {
        String hostel=mStudent.getH();
        SharedPreferences sharedPreferences = getSharedPreferences("hostel_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("hostel", hostel);
        editor.apply();
        allowlogin();
    }

}
