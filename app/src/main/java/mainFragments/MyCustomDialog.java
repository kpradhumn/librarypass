package mainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.librarypass.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Models.mStudent;
import studentAuth.Registration;

public class MyCustomDialog extends DialogFragment {
    private static  final String TAG="MyCustomDialog";
    private  Button btn_dgenrate_pass;
    private Models.mStudent mStudent = null;
    private FirebaseAuth mauth;
    private FirebaseFirestore fstore;
    private String currentuserid;
    private TextView tv_dpass,tv_dname1,tv_dname2,tv_droll1,tv_droll2,tv_ddate1,tv_ddate2,tv_dtime1,tv_dtime2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_genrate_pass,container,false);
        btn_dgenrate_pass=view.findViewById(R.id.btn_dgenrate_pass);
        mauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();

           tv_dpass =view.findViewById(R.id.tv_dpass);
           tv_dname1 = view.findViewById(R.id.tv_dname1);
           tv_dname2 = view.findViewById(R.id.tv_dname2);
           tv_droll1 = view.findViewById(R.id.tv_droll1);
           tv_droll2 = view.findViewById(R.id.tv_droll2);
           tv_ddate1 = view.findViewById(R.id.tv_ddate1);
           tv_ddate2 = view.findViewById(R.id.tv_ddate2);
           tv_dtime1 = view.findViewById(R.id.tv_dtime1);
           tv_dtime2 = view.findViewById(R.id.tv_dtime2);

        btn_dgenrate_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retriveinfo();
               getDialog().dismiss();
            }
        });


        return view;
    }
    private void Retriveinfo() {
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
