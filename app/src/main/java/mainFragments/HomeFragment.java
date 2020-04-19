package mainFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarypass.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import studentAuth.Login;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG="HomeFragment";
    private AlertDialog.Builder dialogbuilder;
    Date date1 = new Date();
    String strDateFormat = "hh:mm:ss a";
    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
    String formattedDate= dateFormat.format(date1);
    String onlyhr=formattedDate.substring(0,2);
    int finalhr=Integer.parseInt(onlyhr);

    private TextView pass_genrate,pass_status;
    private CardView pass_container;
    private TextView name,roll,date,time,cancel_pass;
    private TextView tvname,tvroll,tvdate,tvtime;
    private Models.mStudent mStudent = null;
    private FirebaseAuth mauth;
    private FirebaseFirestore fstore;
    private String currentuserid;
    private FirebaseUser currentuser;
    private AlertDialog dialog;
    private Button btn_dgenrate_pass;
    private android.app.AlertDialog.Builder alertdialogbuilder;
    private android.app.AlertDialog alertDialog;
    private LayoutInflater inflater;
    private TextView tv_dpass, tv_dname1, tv_dname2, tv_droll1, tv_droll2, tv_ddate1, tv_ddate2, tv_dtime1, tv_dtime2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        pass_genrate=view.findViewById(R.id.pass_genrate);
        pass_container=view.findViewById(R.id.pass_container);
        mauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        currentuser=mauth.getCurrentUser();
        pass_status=view.findViewById(R.id.pass_status);
        name=view.findViewById(R.id.tv_pname2);
        roll=view.findViewById(R.id.tv_proll2);
        date=view.findViewById(R.id.tv_pdate2);
        time=view.findViewById(R.id.tv_ptime2);
        tvname=view.findViewById(R.id.tv_pname1);
        tvroll=view.findViewById(R.id.tv_proll1);
        tvdate=view.findViewById(R.id.tv_pdate1);
        tvtime=view.findViewById(R.id.tv_ptime1);
        currentuserid=mauth.getCurrentUser().getUid();
        cancel_pass=view.findViewById(R.id.cancel_pass);
        fetchInfo();
        cancel_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialogbuilder =new android.app.AlertDialog.Builder(getContext());
                final View view=getLayoutInflater().inflate(R.layout.conformationdialog,null);
                Button nobtn=(Button) view.findViewById(R.id.nobtn);
                Button yesbtn=(Button) view.findViewById(R.id.yesbtn);
                alertdialogbuilder.setView(view);
                alertDialog=alertdialogbuilder.create();
                alertDialog.show();
                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentuserid=mauth.getCurrentUser().getUid();
                        alertDialog.dismiss();
                       if(finalhr<=7) {
                           pass_container.setVisibility(View.INVISIBLE);
                           pass_genrate.setVisibility(view.VISIBLE);
                           pass_status.setText("GENERATE PASS");
                           cancel_pass.setVisibility(View.INVISIBLE);
                       }
                       else
                       {
                           Toast.makeText(getContext(),"Cannot Cancel Pass after time Exceeds!!",Toast.LENGTH_LONG).show();
                       }


                    }
                });


            }
        });




        pass_genrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalhr>=7)
                {
                    pass_status.setText("Cannot Generate Pass!!");
                    pass_status.setTextColor(getResources().getColor(R.color.textcolor));
                    Toast.makeText(getContext(),"Pass cannot be Generated any further!!",Toast.LENGTH_LONG).show();
                }
                else {

                    Log.d(TAG, "onclick: opening dialog");
                    createpoupdialog();
                }


            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(currentuser==null) {
            gotoLoginScreen();
        }


    }

    private void gotoLoginScreen() {
        Intent intent=new Intent(getContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void fetchInfo() {



        fstore= FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Student").document(currentuserid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mStudent = document.toObject(Models.mStudent.class);
                        setExistingData(mStudent);
                        //setLayoutWidgets(mBuyerPrsetExistingData(mStudent);
                    } else {

                        //Toast.makeText(BuyeProfileCreation.this,"No data history found",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(),"No data history found task failed",Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    private void setExistingData(Models.mStudent mStudent) {
        name.setText(mStudent.getNm());
        roll.setText(mStudent.getRoll());
        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentdateformat = new SimpleDateFormat(" dd MMM,yyyy");
        String currentdate = currentdateformat.format(calfordate.getTime());
        Calendar calfortime = Calendar.getInstance();
        SimpleDateFormat currenttimeformat = new SimpleDateFormat("hh:mm a");
        String currenttime = currenttimeformat.format(calfortime.getTime());
        date.setText(currentdate);
        time.setText(currenttime);
    }
    private void  createpoupdialog(){

        dialogbuilder = new AlertDialog.Builder(getContext());
        View view =getLayoutInflater().inflate(R.layout.dialog_genrate_pass,null);
        tv_dpass = view.findViewById(R.id.tv_dpass);
        tv_dname1 = view.findViewById(R.id.tv_dname1);
        tv_dname2 = view.findViewById(R.id.tv_dname2);
        tv_droll1 = view.findViewById(R.id.tv_droll1);
        tv_droll2 = view.findViewById(R.id.tv_droll2);
        tv_ddate1 = view.findViewById(R.id.tv_ddate1);
        tv_ddate2 = view.findViewById(R.id.tv_ddate2);
        tv_dtime1 = view.findViewById(R.id.tv_dtime1);
        tv_dtime2 = view.findViewById(R.id.tv_dtime2);
        btn_dgenrate_pass = view.findViewById(R.id.btn_dgenrate_pass);
        dialogbuilder.setView(view);
        dialog=dialogbuilder.create();
        Retriveinfo();
        dialog.show();
        btn_dgenrate_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save to database
                //go to next screen
                dialog.dismiss();
                pass_container.setVisibility(View.VISIBLE);
                cancel_pass.setVisibility(View.VISIBLE);
                pass_status.setText("Pass Generated");
            }
        });

    }
    private void Retriveinfo() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Student").document(currentuserid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mStudent = document.toObject(Models.mStudent.class);
                        setExistingDatatoDialog(mStudent);
                        //setLayoutWidgets(mBuyerPrsetExistingData(mStudent);
                    } else {

                        //Toast.makeText(BuyeProfileCreation.this,"No data history found",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No data history found task failed", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void setExistingDatatoDialog(Models.mStudent mStudent) {
        tv_dname2.setText(mStudent.getNm());
        tv_droll2.setText(mStudent.getRoll());
        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentdateformat = new SimpleDateFormat(" dd MMM,yyyy");
        String currentdate = currentdateformat.format(calfordate.getTime());
        Calendar calfortime = Calendar.getInstance();
        SimpleDateFormat currenttimeformat = new SimpleDateFormat("hh:mm a");
        String currenttime = currenttimeformat.format(calfortime.getTime());
        tv_ddate2.setText(currentdate);
        tv_dtime2.setText(currenttime);


    }

}

