package studentAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarypass.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import Models.mStudent;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spStream;
    TextView tvName, tvRoll, tvContact, tvHoste, tvYear, tvStream, tvBranch, tvcnfpwd, tvpwd;
    EditText etName, etRoll, etContact, etHostel, etYear, etBranch, etpwd, etcnfpwd;
    Button btRegister;
    private FirebaseAuth mauth;
    private FirebaseFirestore fstore;
    private ProgressDialog loadingbar;
    private String roll, setpwd;
    private String currentuserid, name, conatct, hostel, year, branch, stream, rollno;
    Boolean existence = false;
    private mStudent mStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        loadingbar=new ProgressDialog(Registration.this);


        tvName = findViewById(R.id.tvName);
        tvRoll = findViewById(R.id.tvRoll);
        tvContact = findViewById(R.id.tvContact);
        tvHoste = findViewById(R.id.tvHostel);
        tvYear = findViewById(R.id.tvYear);
        tvStream = findViewById(R.id.tvStream);
        tvBranch = findViewById(R.id.tvBranch);
        tvcnfpwd = findViewById(R.id.tvcnfpwd);
        tvpwd = findViewById(R.id.tvpwd);

        etcnfpwd = findViewById(R.id.etcnfpwd);
        etpwd = findViewById(R.id.etpwd);
        etName = findViewById(R.id.etName);
        etRoll = findViewById(R.id.etRoll);
        etContact = findViewById(R.id.etContact);
        etHostel = findViewById(R.id.etHostel);
        etYear = findViewById(R.id.etYear);
        etBranch = findViewById(R.id.etBranch);

        spStream = findViewById(R.id.spStream);
        spStream.setOnItemSelectedListener(this);
        btRegister = findViewById(R.id.btRegister);




        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag;
                name = etName.getText().toString();
                conatct = etContact.getText().toString();
                hostel = etHostel.getText().toString();
                year = etYear.getText().toString();
                branch = etBranch.getText().toString();
                rollno = etRoll.getText().toString();
                if (etName.getText().toString().trim().length() == 0)
                    etName.setError("Name is required");
                else if (etRoll.getText().toString().trim().length() == 0)
                    etRoll.setError("Roll no. is required");
                else {
                    roll = etRoll.getText().toString();
                    roll = roll.concat("@kiit.ac.in");
                }
                // Toast.makeText(registration.this,roll,Toast.LENGTH_LONG).show();
                if (etContact.getText().toString().trim().length() == 0) {
                    etContact.setError("Contact No. is required");
                    flag = 0;
                }
                if (etContact.getText().toString().trim().length() != 10) {
                    etContact.setError("Enter correct contact no.");
                    flag = 0;
                }
                if (etHostel.getText().toString().trim().length() == 0) {
                    etHostel.setError("Hostel namae is required");
                    flag = 0;
                }
                if (etYear.getText().toString().trim().length() == 0) {
                    etYear.setError("Year is required");
                    flag = 0;
                }


                if (etBranch.getText().toString().trim().length() == 0) {
                    etBranch.setError("Branch is required");
                    flag = 0;
                }
                if (etpwd.getText().toString().trim().length() == 0) {
                    etpwd.setError("Please set password");
                    flag = 0;
                }
                if (etpwd.getText().toString().equals(etcnfpwd.getText().toString())) {
                    setpwd = etcnfpwd.getText().toString();

                    flag = 1;

                } else {
                    etcnfpwd.setError("Password does not match");
                    flag = 0;

                }
                if (flag == 1)

                    createaccunt(roll, setpwd);


            }
        });

    }


    private void createaccunt(String roll, String setpwd) {

        loadingbar.setTitle("Creating new Account");
        loadingbar.setMessage("Please wait while we are creating account for you");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();

        mauth.createUserWithEmailAndPassword(roll, setpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mauth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Registration.this, "email verification sent", Toast.LENGTH_LONG).show();

                        }
                    });
                    String currentuserid = mauth.getCurrentUser().getUid();
                    Toast.makeText(Registration.this, "Account created suuccsefuly", Toast.LENGTH_LONG).show();
                    loadingbar.dismiss();
                    updateuserinfo();
                    sendUsertologin();
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(Registration.this, message, Toast.LENGTH_LONG).show();
                    loadingbar.dismiss();
                }

            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        stream = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + stream,Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> parent) {

        ((TextView)spStream.getSelectedView()).setError("Stream is required");

    }
    private void updateuserinfo()
    {
        currentuserid=mauth.getCurrentUser().getUid();
        DocumentReference documentReference=fstore.collection("Student").document(currentuserid);
        HashMap<String,String> profilemap=new HashMap<>();
        profilemap.put("uid",currentuserid);
        profilemap.put("nm",name);
        profilemap.put("roll",rollno);
        profilemap.put("yr",year);
        profilemap.put("strm",stream);
        profilemap.put("br",branch);
        profilemap.put("h",hostel);
        profilemap.put("phn",conatct);

        documentReference.set(profilemap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Registration.this, "Profile updated successfully", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void sendUsertologin() {

        Intent mainintent=new Intent(Registration.this, Login.class);

        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
