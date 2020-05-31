package studentAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarypass.MainActivity;
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

public class updateUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spStream;
    TextView tvName, tvRoll, tvContact, tvHoste, tvYear, tvStream, tvBranch, tvcnfpwd, tvpwd;
    EditText etName, etRoll, etContact, etHostel, etYear, etBranch, etpwd, etcnfpwd;
    Button btRegister,btUpdate;
    private FirebaseAuth mauth;
    private FirebaseFirestore fstore;
    private ProgressDialog loadingbar;
    private String roll, setpwd;
    private String currentuserid, name, conatct, hostel, year, branch, stream, rollno;
    Boolean existence = false;
    private mStudent mStudent = null;
    ArrayAdapter<String> adapterMonthspinner;
    ArrayList<String> spinnerMonthList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        loadingbar=new ProgressDialog(updateUser.this);


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
        btUpdate=findViewById(R.id.btupdate);
        spinnerMonthList=new ArrayList<String>(Arrays.asList(getApplication().getResources().getStringArray(R.array.stream)));
        spinnerMonthList.add(0,"Select Stream");
        adapterMonthspinner= new ArrayAdapter<String>(updateUser.this,android.R.layout.simple_spinner_dropdown_item,spinnerMonthList);
        spStream.setAdapter(adapterMonthspinner);

        FirebaseUser mFirebaseUser = mauth.getCurrentUser();
        if(mFirebaseUser != null)
            Retriveinfo();



        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=1;
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
                if (flag == 1)

                    updateuserinfo();


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
    private void Retriveinfo() {
        loadingbar.setTitle("Please Wait");
        loadingbar.setMessage("We Are Fetching Your Information...");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        btRegister.setVisibility(View.INVISIBLE);
        btUpdate.setVisibility(View.VISIBLE);
        tvcnfpwd.setVisibility(View.INVISIBLE);
        tvpwd.setVisibility(View.INVISIBLE);
        etcnfpwd.setVisibility(View.INVISIBLE);
        etpwd.setVisibility(View.INVISIBLE);
        fstore= FirebaseFirestore.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();
        DocumentReference docRef = fstore.collection("Student").document(currentuserid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        existence=true;
                        btRegister.setText("Update Profile");
                        mStudent = document.toObject(Models.mStudent.class);
                        setExistingData(mStudent);
                        //setLayoutWidgets(mBuyerPrsetExistingData(mStudent);
                    } else {
                        loadingbar.dismiss();
                        //Toast.makeText(BuyeProfileCreation.this,"No data history found",Toast.LENGTH_LONG).show();
                    }
                } else {
                    loadingbar.dismiss();
                    Toast.makeText(updateUser.this,"No data history found task failed",Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void setExistingData(Models.mStudent mStudent) {
        etName.setText(mStudent.getNm());
        etContact.setText(mStudent.getPhn());
        etBranch.setText(mStudent.getBr());
        //Toast.makeText(BuyeProfileCreation.this,mStudent.getStreet(),Toast.LENGTH_LONG).show();
        etHostel.setText(mStudent.getH());
        etRoll.setText(mStudent.getRoll());
        etYear.setText(mStudent.getYr());
        spStream.setSelection(adapterMonthspinner.getPosition(mStudent.getStrm()));
        //spStream.setAdapter(mStudent.getStrm());
        loadingbar.dismiss();


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
                Toast.makeText(updateUser.this, "Profile updated successfully", Toast.LENGTH_LONG).show();
                sendUsertoMain();
            }
        });

    }

    private void sendUsertoMain() {

        Intent mainintent=new Intent(updateUser.this, MainActivity.class);

        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

