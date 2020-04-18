package mainFragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarypass.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mainFragments.MyCustomDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG="HomeFragment";

    private TextView pass_genrate;
    private CalendarView pass_container;
    private TextView name,roll,date,time;
    private TextView tvname,tvroll,tvdate,tvtime;
    private Models.mStudent mStudent = null;
    private FirebaseAuth mauth;
    private FirebaseFirestore fstore;
    private String currentuserid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        pass_genrate=view.findViewById(R.id.pass_genrate);
        pass_container=view.findViewById(R.id.pass_container);
        mauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();
        name=view.findViewById(R.id.tv_pname2);
        roll=view.findViewById(R.id.tv_proll2);
        date=view.findViewById(R.id.tv_pdate2);
        time=view.findViewById(R.id.tv_ptime2);
        tvname=view.findViewById(R.id.tv_pname1);
        tvroll=view.findViewById(R.id.tv_proll1);
        tvdate=view.findViewById(R.id.tv_pdate1);
        tvtime=view.findViewById(R.id.tv_ptime1);

        pass_genrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onclick: opening dialog");

                MyCustomDialog dialog=new MyCustomDialog();
                dialog.show(getFragmentManager(),"My custom Dialog");
                fetchInfo();
                pass_container.setVisibility(View.VISIBLE);
            }
        });

        return view;
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
}
