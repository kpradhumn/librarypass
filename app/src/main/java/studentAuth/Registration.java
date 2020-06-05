package studentAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Models.mStudent;

public class Registration extends AppCompatActivity  {

    ArrayAdapter<String> adapterMonthspinner,adapterMonthspinners,adapteryears,adapterHostelSpinner;
    ArrayList<String> spinnerMonthList;
    Spinner spStream,spBranch,spyear,spHostel;
    TextView tvName, tvRoll, tvContact, tvHoste, tvYear, tvStream, tvBranch, tvcnfpwd, tvpwd;
    EditText etName, etRoll, etContact, etpwd, etcnfpwd;
    Button btRegister;
    private int status=0;
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
        tvBranch=findViewById(R.id.tvBranch);
        tvName = findViewById(R.id.tvName);
        tvRoll = findViewById(R.id.tvRoll);
        tvContact = findViewById(R.id.tvContact);
        tvHoste = findViewById(R.id.tvHostel);
        tvYear = findViewById(R.id.tvYear);
        tvStream = findViewById(R.id.tvStream);
        spBranch = findViewById(R.id.Spbranch);
        tvcnfpwd = findViewById(R.id.tvcnfpwd);
        tvpwd = findViewById(R.id.tvpwd);
        etcnfpwd = findViewById(R.id.etcnfpwd);
        etpwd = findViewById(R.id.etpwd);
        etName = findViewById(R.id.etName);
        etRoll = findViewById(R.id.etRoll);
        etContact = findViewById(R.id.etContact);
        spHostel = findViewById(R.id.etHostel);
        spyear = findViewById(R.id.etYear);
        spStream = findViewById(R.id.spStream);
        btRegister = findViewById(R.id.btRegister);
        spinnerMonthList=new ArrayList<String>(Arrays.asList(getApplication().getResources().getStringArray(R.array.stream)));
        adapterMonthspinner= new ArrayAdapter<String>(Registration.this,android.R.layout.simple_spinner_dropdown_item,spinnerMonthList);
        spStream.setAdapter(adapterMonthspinner);
        spinnerMonthList = new ArrayList<String>(Arrays.asList(getApplication().getResources().getStringArray(R.array.branch)));
        adapterMonthspinners = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_dropdown_item, spinnerMonthList);
        spBranch.setAdapter(adapterMonthspinners);
        spinnerMonthList = new ArrayList<String>(Arrays.asList(getApplication().getResources().getStringArray(R.array.year)));
        adapteryears = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_dropdown_item, spinnerMonthList);
        spyear.setAdapter(adapteryears);
        spinnerMonthList = new ArrayList<String>(Arrays.asList(getApplication().getResources().getStringArray(R.array.hostel)));
        adapterHostelSpinner = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_dropdown_item, spinnerMonthList);
        spHostel.setAdapter(adapterHostelSpinner);




        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    int flag = 0;
                    name = etName.getText().toString();
                    conatct = etContact.getText().toString();
                    rollno = etRoll.getText().toString();
                    hostel=spHostel.getSelectedItem().toString();
                    year=spyear.getSelectedItem().toString();
                    stream=spStream.getSelectedItem().toString();
                    branch=spBranch.getSelectedItem().toString();

                    if (etName.getText().toString().trim().length() == 0)
                        etName.setError("Name is required");
                    else if (etRoll.getText().toString().trim().length() == 0) {
                        etRoll.setError("Roll no. is required");
                        flag = 0;
                    } else {
                        roll = etRoll.getText().toString();
                        roll = roll.concat("@kiit.ac.in");
                    }
                    // Toast.makeText(registration.this,roll,Toast.LENGTH_LONG).show();
                    if(hostel.length()==0 || hostel.equals("Select Hostel")){
                    ((TextView)spHostel.getSelectedView()).setError("Hostel name is required");
                    flag=0;
                    }
                    if(year.length()==0 || year.equals("Select Year")){
                    ((TextView)spyear.getSelectedView()).setError("Year is required");
                    flag=0;
                    }
                    if(stream.length()==0 || stream.equals("Choose Stream")){
                    ((TextView)spStream.getSelectedView()).setError("Stream is required");
                    flag=0;
                    }
                    if(branch.length()==0 || branch.equals("Choose Branch")){
                    ((TextView)spBranch.getSelectedView()).setError("Branch is required");
                    flag=0;
                    }



                if (etContact.getText().toString().trim().length() == 0) {
                        etContact.setError("Contact No. is required");
                        flag = 0;
                    }
                    if (etContact.getText().toString().trim().length() != 10) {
                        etContact.setError("Enter correct contact no.");
                        flag = 0;
                    }
                    if (etpwd.getText().toString().trim().length() == 0) {
                        etpwd.setError("Please set password");
                        flag = 0;
                    }
                    if (etpwd.getText().toString().equals(etcnfpwd.getText().toString())) {
                        if (etcnfpwd.getText().toString().isEmpty())
                            flag = 0;
                        else {
                            setpwd = etcnfpwd.getText().toString();

                            flag = 1;
                        }

                    } else {
                        etcnfpwd.setError("Password does not match");
                        flag = 0;

                    }
                    if (flag == 1) {
                        createaccunt(roll, setpwd);
                    }
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
        spHostel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    hostel= parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView)spHostel.getSelectedView()).setError("Hostel Name is required");

            }
        });
        spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    branch= parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView)spBranch.getSelectedView()).setError("Stream is required");

            }
        });
        spyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    year= parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView)spyear.getSelectedView()).setError("Year is required");
            }
        });
        spStream.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stream = parent.getItemAtPosition(position).toString();
                if(position>0) {
                    if (stream.equals("B.Tech") || stream.equals("M.Tech")) {
                        spBranch.setVisibility(View.VISIBLE);
                        branch = spBranch.getItemAtPosition(position).toString();

                    } else {
                        tvBranch.setVisibility(View.INVISIBLE);
                        spBranch.setVisibility(View.INVISIBLE);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.addRule(RelativeLayout.BELOW, R.id.spStream);
                        tvpwd.setLayoutParams(params);
                        branch = parent.getItemAtPosition(position).toString();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView)spStream.getSelectedView()).setError("Stream is required");
            }
        });
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

