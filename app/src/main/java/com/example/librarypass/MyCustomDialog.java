package com.example.librarypass;

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

public class MyCustomDialog extends DialogFragment {
    private static final String TAG="MyCustomDialog";
    private  Button btn_dgenrate_pass;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_genrate_pass,container,false);
        btn_dgenrate_pass=view.findViewById(R.id.btn_dgenrate_pass);

        final TextView  tv_dpass =(TextView) view.findViewById(R.id.tv_dpass);
        final TextView  tv_dname1 =(TextView) view.findViewById(R.id.tv_dname1);
        final TextView  tv_dname2 =(TextView) view.findViewById(R.id.tv_dname2);
        final TextView  tv_droll1 =(TextView) view.findViewById(R.id.tv_droll1);
        final TextView  tv_droll2 =(TextView) view.findViewById(R.id.tv_droll2);
        final TextView  tv_ddate1 =(TextView) view.findViewById(R.id.tv_ddate1);
        final TextView  tv_ddate2 =(TextView) view.findViewById(R.id.tv_ddate2);
        final TextView  tv_dtime1 =(TextView) view.findViewById(R.id.tv_dtime1);
        final TextView  tv_dtime2 =(TextView) view.findViewById(R.id.tv_dtime2);

        btn_dgenrate_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getDialog().dismiss();
            }
        });


        return view;
    }
}
