package mainFragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarypass.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import Models.mStudent;
import studentAuth.Login;
import studentAuth.Registration;

import static android.content.Context.MODE_PRIVATE;
import static mainFragments.BarcodeScanner.Shared_pref;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private AlertDialog.Builder dialogbuilder;
    Calendar calfortimenew = Calendar.getInstance();
    SimpleDateFormat currenttimeformatnew = new SimpleDateFormat("HH");
    int finalhr = Integer.parseInt(currenttimeformatnew.format(calfortimenew.getTime()));
    static final int MY_REQUEST_CODE = 1000;
    ArrayAdapter<String> adapterLibspinner;
    ArrayList<String> SpinnerLibList;
    private TextView pass_genrate, pass_status;
    private CardView pass_container;
    private ImageView imgVerified, imgCancelled, scannerImg, returnScan;
    private TextView name, roll, date, time, cancel_pass, newpass,libname;
    private TextView tvname, tvroll, tvdate, tvtime;
    private Models.mStudent mStudent = null;
    private FirebaseAuth mauth;
    private FirebaseFirestore fstore;
    private String currentuserid, uid, hostelName,day,selectedLib;
    private FirebaseUser currentuser;
    private AlertDialog dialog;
    private Button btn_dgenrate_pass;
    private Spinner spLib;
    private String libName;
    private android.app.AlertDialog.Builder alertdialogbuilder;
    private android.app.AlertDialog alertDialog;
    private LayoutInflater inflater;
    private TextView tv_dpass, tv_dname1, tv_dname2, tv_droll1, tv_droll2, tv_ddate1, tv_ddate2, tv_dtime1, tv_dtime2;
    Calendar calfordate = Calendar.getInstance();
    SimpleDateFormat currentdateformat = new SimpleDateFormat(" dd MMM,yyyy");
    String currentdate = currentdateformat.format(calfordate.getTime());
    Calendar calfortime = Calendar.getInstance();
    SimpleDateFormat currenttimeformat = new SimpleDateFormat("hh:mm a");
    String currenttime = currenttimeformat.format(calfortime.getTime());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        pass_genrate = view.findViewById(R.id.pass_genrate);
        newpass = view.findViewById(R.id.new_pass);
        imgCancelled = view.findViewById(R.id.crossed);
        imgVerified = view.findViewById(R.id.imgVerified);
        pass_container = view.findViewById(R.id.pass_container);
        mauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        currentuser = mauth.getCurrentUser();
        pass_status = view.findViewById(R.id.pass_status);
        name = view.findViewById(R.id.tv_pname2);
        roll = view.findViewById(R.id.tv_proll2);
        date = view.findViewById(R.id.tv_pdate2);
        time = view.findViewById(R.id.tv_ptime2);
        tvname = view.findViewById(R.id.tv_pname1);
        tvroll = view.findViewById(R.id.tv_proll1);
        tvdate = view.findViewById(R.id.tv_pdate1);
        tvtime = view.findViewById(R.id.tv_ptime1);
        libname=view.findViewById(R.id.tv_lib2);
        scannerImg = view.findViewById(R.id.scannerImg);
        returnScan = view.findViewById(R.id.returnScannerImg);
        currentuserid = mauth.getCurrentUser().getUid();
        cancel_pass = view.findViewById(R.id.cancel_pass);
        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
         day = dayFormat.format(calfordate.getTime());
        uid = day.concat(currentuserid);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("hostel_pref", MODE_PRIVATE);
        hostelName = sharedPreferences.getString("hostel", "");
        //Toast.makeText(getContext(), String.valueOf(finalhr), Toast.LENGTH_LONG).show();
        scannerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass_status.getText().toString().equals("Generate Pass") || pass_status.getText().toString().equals("Cannot Generate Pass!!"))
                    Toast.makeText(getContext(), "Please get a pass first", Toast.LENGTH_SHORT).show();
                else if (pass_status.getText().toString().equals("Pass Verified"))
                    Toast.makeText(getContext(), "Already Verified", Toast.LENGTH_SHORT).show();
                else {
                    if (checkPerms()) {
                        onPause();
                        Intent intent = new Intent(getContext(), BarcodeScanner.class);
                        intent.putExtra("task", "verification");
                        intent.putExtra("hostel", hostelName);
                        intent.putExtra("libName",selectedLib);
                        // Toast.makeText(getContext(),hostelName,Toast.LENGTH_LONG).show();
                        startActivity(intent);

                    }
                }
            }
        });
        returnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass_status.getText().toString().equals("Generate Pass") || pass_status.getText().toString().equals("Cannot Generate Pass!!"))
                    Toast.makeText(getContext(), "Please get a pass first", Toast.LENGTH_SHORT).show();
                else if (pass_status.getText().toString().equals("Pass Verified")) {
                    if (checkPerms()) {
                        onPause();
                        Intent intent = new Intent(getContext(), BarcodeScanner.class);
                        intent.putExtra("task", "return");
                        intent.putExtra("hostel", hostelName);
                        intent.putExtra("libName",selectedLib);
                        //Toast.makeText(getContext(),hostelName,Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        onResume();


                    }
                } else
                    Toast.makeText(getContext(), "Pass can only be returned if verified by Library", Toast.LENGTH_SHORT).show();

            }
        });

        cancel_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialogbuilder = new android.app.AlertDialog.Builder(getContext());
                final View view = getLayoutInflater().inflate(R.layout.conformationdialog, null);
                Button nobtn = (Button) view.findViewById(R.id.nobtn);
                Button yesbtn = (Button) view.findViewById(R.id.yesbtn);
                alertdialogbuilder.setView(view);
                alertDialog = alertdialogbuilder.create();
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
                        currentuserid = mauth.getCurrentUser().getUid();
                        alertDialog.dismiss();

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pass"+day, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", "3");
                        editor.apply();
                        editor.commit();
                        sharedPreferences = getActivity().getSharedPreferences("cancel", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("time",currenttime);
                        editor.apply();
                        char nameHostel[] = hostelName.toCharArray();
                        if (nameHostel[0] == 'q' || nameHostel[0] == 'Q') {
                            if (finalhr >= 00 && finalhr <= 19) {
                                imgCancelled.setVisibility(View.VISIBLE);
                                newpass.setVisibility(view.VISIBLE);
                                pass_status.setText("GENERATE A NEW PASS");
                                cancel_pass.setVisibility(View.INVISIBLE);
                                DocumentReference IdRef = FirebaseFirestore.getInstance().collection("Hostel").document(hostelName)
                                        .collection("studentList").document(uid);
                                IdRef.delete().addOnSuccessListener(aVoid -> {
                                });
                                DocumentReference IdRefLib=FirebaseFirestore.getInstance().collection("Library").document(uid);
                                IdRefLib.delete().addOnSuccessListener(aVoid -> new android.app.AlertDialog.Builder(getContext()).setTitle("Pass Cancelled!").setMessage(Html.fromHtml("Your Pass has been successfully Cancelled.<br> You can only regenerate it till 07:00pm.<br>"))
                                        .setPositiveButton("Ok", null).show());
                            } else {
                                Toast.makeText(getContext(), "Cannot Cancel Pass after time Exceeds!!", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (nameHostel[0] == 'k' || nameHostel[0] == 'K') {
                            if (finalhr >= 18 && finalhr <= 20) {
                                imgCancelled.setVisibility(View.VISIBLE);
                                newpass.setVisibility(view.VISIBLE);
                                pass_status.setText("GENERATE A NEW PASS");
                                cancel_pass.setVisibility(View.INVISIBLE);
                                DocumentReference IdRef = FirebaseFirestore.getInstance().collection("Hostel").document(hostelName)
                                        .collection("studentList").document(uid);
                                IdRef.delete().addOnSuccessListener(aVoid -> {
                                });
                                IdRef.collection("Library").document(uid);
                                IdRef.delete().addOnSuccessListener(aVoid -> new android.app.AlertDialog.Builder(getContext()).setTitle("Pass Cancelled!").setMessage(Html.fromHtml("Your Pass has been successfully Cancelled.<br> You can only regenerate it till 07:00pm.<br>"))
                                        .setPositiveButton("Ok", null).show());
                            } else {
                                Toast.makeText(getContext(), "Cannot Cancel Pass after time Exceeds!!", Toast.LENGTH_LONG).show();
                            }
                        }


                    }
                });


            }
        });
        newpass.setOnClickListener(v -> {
            DocumentReference docRef = fstore.collection("Hostel").document(hostelName).collection("studentList").document(uid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            new android.app.AlertDialog.Builder(getContext()).setTitle("Pass Returned!").setMessage(Html.fromHtml("Your Pass has been succesfully returned to hostel.You can now Logout or a get a new pass next day.<br> Good Night!<br>"))
                                    .setPositiveButton("Ok", null).show();
                        } else {
                            getPass();
                        }

                    }
                    else {
                        getPass();
                    }

                }
            });
        });


        pass_genrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = fstore.collection("Hostel").document(hostelName).collection("studentList").document(uid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                SharedPreferences preferences = getActivity().getSharedPreferences("pass"+day, MODE_PRIVATE);
                                preferences.edit().remove("status").commit();
                                new android.app.AlertDialog.Builder(getContext()).setTitle("No Pass for the day!").setMessage(Html.fromHtml("You can only issue one pass for a day.<br>Comeback again tomorrow to get a new pass."))
                                        .setPositiveButton("Ok", null).show();
                            } else {

                                getPass();
                            }


                        }
                        else {
                            getPass();
                        }

                    }
                });
            }
        });


        return view;
    }


    private void getPass() {
        int prevdate=Integer.parseInt(day);
        prevdate-=1;
        String prevdates= String.valueOf(prevdate);
        SharedPreferences preferences = getActivity().getSharedPreferences("pass"+prevdates, MODE_PRIVATE);
        preferences.edit().remove("status").commit();
        char nameHostel[] = hostelName.toCharArray();
       // Toast.makeText(getContext(), Arrays.toString(nameHostel), Toast.LENGTH_LONG).show();
        if (nameHostel[0] == 'q' || nameHostel[0] == 'Q') {
            if (finalhr >= 00 && finalhr <= 19) {
                Log.d(TAG, "onclick: opening dialog");
                createpoupdialog();
                //fetchInfo();


            } else {
                pass_status.setText("Cannot Generate Pass!!");
                pass_status.setTextColor(getResources().getColor(R.color.textcolor));
                Toast.makeText(getContext(), "Pass cannot be Generated any further!!", Toast.LENGTH_LONG).show();


            }
        } else if (nameHostel[0] == 'k' || nameHostel[0] == 'K') {
            if (finalhr >= 18 && finalhr <= 20) {
                Log.d(TAG, "onclick: opening dialog");
                newpass.setVisibility(View.INVISIBLE);
                createpoupdialog();
                //fetchInfo();

            } else {
                pass_status.setText("Cannot Generate Pass!!");
                pass_status.setTextColor(getResources().getColor(R.color.textcolor));
                Toast.makeText(getContext(), "Pass cannot be Generated any further!!", Toast.LENGTH_LONG).show();


            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchInfo();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("hostel_return", MODE_PRIVATE);
        String hostelstatus = sharedPreferences.getString("return", "");
        if (hostelstatus.equals("unread")) {
            new android.app.AlertDialog.Builder(getContext()).setTitle("Pass Returned!").setMessage(Html.fromHtml("Your Pass has been succesfully returned to hostel.You can now Logout or a get a new pass next day.<br> Good Night!<br>"))
                    .setPositiveButton("Ok", null).show();
            //sharedPreferences.edit().remove("return").commit();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("return", "read");
            editor.apply();
            editor.commit();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if (currentuser == null) {
            gotoLoginScreen();
        } else
            fetchInfo();


    }

    private void gotoLoginScreen() {
        Intent intent = new Intent(getContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void fetchInfo() {
        fstore = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("hostel_pref", MODE_PRIVATE);
        String hostelName = sharedPreferences.getString("hostel", "");
        DocumentReference docRef = fstore.collection("Hostel").document(hostelName).collection("studentList").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String statuslib = (String) document.get("libStatus");
                        String status = (String) document.get("status");

                        if (status.equals("1") && statuslib.equals("1")) {
                            pass_genrate.setVisibility(View.VISIBLE);
                            pass_container.setVisibility(View.INVISIBLE);
                            pass_status.setText("Generate Pass");
                        } else if (status.equals("0") && statuslib.equals("1")) {
                            pass_status.setText("Pass Verified");
                            imgVerified.setVisibility(View.VISIBLE);
                            cancel_pass.setVisibility(View.INVISIBLE);
                            pass_genrate.setVisibility(View.INVISIBLE);
                            pass_container.setVisibility(View.VISIBLE);
                        } else if (status.equals("0")) {
                            pass_status.setText("Pass Generated");
                            cancel_pass.setVisibility(View.VISIBLE);
                            pass_genrate.setVisibility(View.INVISIBLE);
                            pass_container.setVisibility(View.VISIBLE);
                        }

                        mStudent = document.toObject(Models.mStudent.class);
                        setExistingData(mStudent,document.getString("Library"));
                    }
                    //setLayoutWidgets(mBuyerPrsetExistingData(mStudent);
                    else {

                        //Toast.makeText(BuyeProfileCreation.this,"No data history found",Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Toast.makeText(getContext(), "No data history found task failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setExistingData(Models.mStudent mStudent, String library) {
        name.setText(mStudent.getNm());
        roll.setText(mStudent.getRoll());
        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentdateformat = new SimpleDateFormat(" dd MMM,yyyy");
        String currentdate = currentdateformat.format(calfordate.getTime());
        date.setText(currentdate);
        time.setText(mStudent.getTime());
        libname.setText(library);

    }

    private void createpoupdialog() {

        dialogbuilder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_genrate_pass, null);
        tv_dpass = view.findViewById(R.id.tv_dpass);
        tv_dname1 = view.findViewById(R.id.tv_dname1);
        tv_dname2 = view.findViewById(R.id.tv_dname2);
        tv_droll1 = view.findViewById(R.id.tv_droll1);
        tv_droll2 = view.findViewById(R.id.tv_droll2);
        tv_ddate1 = view.findViewById(R.id.tv_ddate1);
        tv_ddate2 = view.findViewById(R.id.tv_ddate2);
        tv_dtime1 = view.findViewById(R.id.tv_dtime1);
        tv_dtime2 = view.findViewById(R.id.tv_dtime2);
        spLib=view.findViewById(R.id.spLib);
        btn_dgenrate_pass = view.findViewById(R.id.btn_dgenrate_pass);
        dialogbuilder.setView(view);
        dialog = dialogbuilder.create();
        Retriveinfo();
        dialog.show();
        btn_dgenrate_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save to database
                //go to next screen
                pass_genrate.setVisibility(View.INVISIBLE);
                pass_container.setVisibility(View.VISIBLE);
                cancel_pass.setVisibility(View.VISIBLE);
                pass_status.setText("Pass Generated");
                fetchInfo();
                setDataToDb();
                dialog.dismiss();
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
        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentdateformat = new SimpleDateFormat(" dd MMM,yyyy");
        String currentdate = currentdateformat.format(calfordate.getTime());
        Calendar calfortime = Calendar.getInstance();
        SimpleDateFormat currenttimeformat = new SimpleDateFormat("hh:mm a");
        String currenttime = currenttimeformat.format(calfortime.getTime());
        newpass.setVisibility(View.INVISIBLE);
        imgCancelled.setVisibility(View.INVISIBLE);
        tv_dname2.setText(mStudent.getNm());
        tv_droll2.setText(mStudent.getRoll());
        tv_ddate2.setText(currentdate);
        tv_dtime2.setText(currenttime);
        SpinnerLibList=new ArrayList<String>();
        SpinnerLibList.add(0,mStudent.getCourse()+" Lib");
        SpinnerLibList.add(1,"Central Lib");
        adapterLibspinner= new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,SpinnerLibList);
        adapterLibspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLib.setAdapter(adapterLibspinner);

        spLib.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                libName= parent.getItemAtPosition(position).toString();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Library Name", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("lib", libName);
                editor.apply();
                if(position==0)
                    selectedLib="Lib"+mStudent.getCourse();
                else if(position==1)
                    selectedLib="Central Lib";


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView)spLib.getSelectedView()).setError("Year is required");
            }
        });
    }

    private void setDataToDb() {
        //Setting data to hostel and Library
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pass"+day, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("status", "0");
        editor.apply();
        sharedPreferences = getActivity().getSharedPreferences("date", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("date",currentdate);
        editor.apply();
        sharedPreferences = getActivity().getSharedPreferences("gen", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("time",currenttime);
        editor.apply();
        currentuserid = mauth.getCurrentUser().getUid();
        DocumentReference documentReference = fstore.collection("Hostel").document(mStudent.getH()).collection("studentList").document(uid);
        HashMap<String, String> profilemap = new HashMap<>();
        profilemap.put("uid", currentuserid);
        profilemap.put("nm", mStudent.getNm());
        profilemap.put("roll", mStudent.getRoll());
        profilemap.put("yr", mStudent.getYr());
        profilemap.put("course", mStudent.getCourse());
        profilemap.put("phn", mStudent.getPhn());
        profilemap.put("date", currentdate);
        profilemap.put("Library",selectedLib);
        profilemap.put("time", currenttime);
        profilemap.put("status", "0");
        profilemap.put("libStatus", "0");
        documentReference.set(profilemap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("set up", "Success");



            }
        });
        profilemap.put("hostel", mStudent.getH());

        DocumentReference documentReferenceLib = fstore.collection("Library").document(selectedLib).collection("studentList").document(uid);
        documentReferenceLib.set(profilemap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("set up", "Success");

            }
        });


    }
    private boolean checkPerms(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CODE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_REQUEST_CODE){
            if(permissions[0].equals(Manifest.permission.CAMERA)){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    onResume();
                }else{
                    new AlertDialog.Builder(getContext()).setTitle("Needs Camera Permission")
                            .setMessage("To scan the QR Code")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface button, int which) {
                                    if (which == AlertDialog.BUTTON_POSITIVE) {
                                        HomeFragment.this.checkPerms();
                                    }
                                }
                            }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface button, int which) {
                            if (which == AlertDialog.BUTTON_NEGATIVE) {
                                Toast.makeText(getContext(),"Permission not given",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).show();
                }
            }
        }
    }



}

