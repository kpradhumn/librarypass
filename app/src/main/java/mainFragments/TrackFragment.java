package mainFragments;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import com.example.librarypass.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrackFragment extends Fragment {


    public TrackFragment() {
        // Required empty public constructor
    }
    TextView passdate,tvpassid,passid,reqtime,reqdot,dot1,dot2;
    TextView gentime,gendot,dot3,dot4;
    TextView vertime,verdot,dot5,dot6;
    TextView retime,redot;
    TextView req,gen,ver,ret;
    TextView notgen;
    ImageView emoji;
    Calendar calfordate = Calendar.getInstance();
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
   String day = dayFormat.format(calfordate.getTime());


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track, container, false);
       final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefersh);
       swipeRefreshLayout.setColorSchemeColors(R.color.refresh,R.color.refresh1,R.color.refresh2);
       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               swipeRefreshLayout.setRefreshing(true);
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       swipeRefreshLayout.setRefreshing(false);
                       /*SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pass" + day, MODE_PRIVATE);
                       SharedPreferences sharedPreference = getActivity().getSharedPreferences("gen", MODE_PRIVATE);
                       SharedPreferences sharedPreferenced = getActivity().getSharedPreferences("date", MODE_PRIVATE);
                       String genTime = sharedPreference.getString("time", "");
                       sharedPreference=getActivity().getSharedPreferences("ver", MODE_PRIVATE);
                       String verTime = sharedPreference.getString("time", "");
                       sharedPreference=getActivity().getSharedPreferences("ret", MODE_PRIVATE);
                       String retTime = sharedPreference.getString("time", "");
                       sharedPreference=getActivity().getSharedPreferences("cancel", MODE_PRIVATE);
                       String canTime = sharedPreference.getString("time", "");
                       String passDate = sharedPreferenced.getString("date", "");
                       String passStatus = sharedPreferences.getString("status", "");
                       Toast.makeText(getContext(),canTime,Toast.LENGTH_LONG).show();*/
                   }
               },3000);

           }
       });
        emoji = view.findViewById(R.id.emoji);
        notgen = view.findViewById(R.id.tv_not_genrated);
        passdate = view.findViewById(R.id.tv_pass_date);
        tvpassid = view.findViewById(R.id.tv_pass_id1);
        passid = view.findViewById(R.id.tv_pass_id2);
        reqtime = view.findViewById(R.id.tv_reqtime);
        reqdot = view.findViewById(R.id.pass_req);
        dot1 = view.findViewById(R.id.dot_1);
        dot2 = view.findViewById(R.id.dot_2);
        gentime = view.findViewById(R.id.tv_gentime);
        gendot = view.findViewById(R.id.dot_passgenerated);
        dot3 = view.findViewById(R.id.dot_3);
        dot4 = view.findViewById(R.id.dot_4);
        vertime = view.findViewById(R.id.tv_vertime);
        verdot = view.findViewById(R.id.dot_passverified);
        dot5 = view.findViewById(R.id.dot_5);
        dot6 = view.findViewById(R.id.dot_6);
        retime = view.findViewById(R.id.tv_rtime);
        redot = view.findViewById(R.id.dot_passreturn);
        req = view.findViewById(R.id.tv_passreq);
        gen = view.findViewById(R.id.tv_passgenerated);
        ver = view.findViewById(R.id.tv_passverified);
        ret = view.findViewById(R.id.tv_passreturn);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pass" + day, MODE_PRIVATE);
        SharedPreferences sharedPreference = getActivity().getSharedPreferences("gen", MODE_PRIVATE);
        SharedPreferences sharedPreferenced = getActivity().getSharedPreferences("date", MODE_PRIVATE);
        String genTime = sharedPreference.getString("time", "");
        sharedPreference=getActivity().getSharedPreferences("ver", MODE_PRIVATE);
        String verTime = sharedPreference.getString("time", "");
        sharedPreference=getActivity().getSharedPreferences("ret", MODE_PRIVATE);
        String retTime = sharedPreference.getString("time", "");
        sharedPreference=getActivity().getSharedPreferences("cancel", MODE_PRIVATE);
        String canTime = sharedPreference.getString("time", "");
        String passDate = sharedPreferenced.getString("date", "");
        String passStatus = sharedPreferences.getString("status", "");
        Toast.makeText(getContext(), passDate, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), genTime, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), passStatus, Toast.LENGTH_LONG).show();
        if (passStatus.equals("0")) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            reqtime.setText(genTime);
            gentime.setText(genTime);
            passdate.setText(passDate);
            // passid.setText((int) timestamp.getTime());
            emoji.setVisibility(View.INVISIBLE);
            notgen.setVisibility(View.INVISIBLE);
            passdate.setVisibility(View.VISIBLE);
            tvpassid.setVisibility(View.VISIBLE);
            passid.setVisibility(View.VISIBLE);
            reqtime.setVisibility(View.VISIBLE);
            reqdot.setVisibility(View.VISIBLE);
            dot1.setVisibility(View.VISIBLE);
            dot2.setVisibility(View.VISIBLE);
            gentime.setVisibility(View.VISIBLE);
            gendot.setVisibility(View.VISIBLE);
            dot3.setVisibility(View.VISIBLE);
            dot4.setVisibility(View.VISIBLE);
            req.setVisibility(View.VISIBLE);
            gen.setVisibility(View.VISIBLE);

        }
        if (passStatus.equals("1")) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            reqtime.setText(genTime);
            gentime.setText(genTime);
            passdate.setText(passDate);
            // passid.setText((int) timestamp.getTime());
            emoji.setVisibility(View.INVISIBLE);
            notgen.setVisibility(View.INVISIBLE);
            passdate.setVisibility(View.VISIBLE);
            tvpassid.setVisibility(View.VISIBLE);
            passid.setVisibility(View.VISIBLE);
            reqtime.setVisibility(View.VISIBLE);
            reqdot.setVisibility(View.VISIBLE);
            dot1.setVisibility(View.VISIBLE);
            dot2.setVisibility(View.VISIBLE);
            gentime.setVisibility(View.VISIBLE);
            gendot.setVisibility(View.VISIBLE);
            dot3.setVisibility(View.VISIBLE);
            dot4.setVisibility(View.VISIBLE);
            req.setVisibility(View.VISIBLE);
            gen.setVisibility(View.VISIBLE);
            vertime.setText(genTime);
            vertime.setVisibility(View.VISIBLE);
            verdot.setVisibility(View.VISIBLE);
            dot5.setVisibility(View.VISIBLE);
            dot6.setVisibility(View.VISIBLE);
            ver.setVisibility(View.VISIBLE);

        }
        if (passStatus.equals("2")) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            reqtime.setText(genTime);
            gentime.setText(genTime);
            passdate.setText(passDate);
            // passid.setText((int) timestamp.getTime());
            emoji.setVisibility(View.INVISIBLE);
            notgen.setVisibility(View.INVISIBLE);
            passdate.setVisibility(View.VISIBLE);
            tvpassid.setVisibility(View.VISIBLE);
            passid.setVisibility(View.VISIBLE);
            reqtime.setVisibility(View.VISIBLE);
            reqdot.setVisibility(View.VISIBLE);
            dot1.setVisibility(View.VISIBLE);
            dot2.setVisibility(View.VISIBLE);
            gentime.setVisibility(View.VISIBLE);
            gendot.setVisibility(View.VISIBLE);
            dot3.setVisibility(View.VISIBLE);
            dot4.setVisibility(View.VISIBLE);
            req.setVisibility(View.VISIBLE);
            gen.setVisibility(View.VISIBLE);
            ver.setVisibility(View.VISIBLE);
            vertime.setText(verTime);
            vertime.setVisibility(View.VISIBLE);
            verdot.setVisibility(View.VISIBLE);
            ver.setVisibility(View.VISIBLE);
            retime.setText(retTime);
            retime.setVisibility(View.VISIBLE);
            ret.setVisibility(View.VISIBLE);


        }
        if (passStatus.equals("3"))
        {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            reqtime.setText(genTime);
            gentime.setText(genTime);
            passdate.setText(passDate);
            // passid.setText((int) timestamp.getTime());
            emoji.setVisibility(View.INVISIBLE);
            notgen.setVisibility(View.INVISIBLE);
            passdate.setVisibility(View.VISIBLE);
            tvpassid.setVisibility(View.VISIBLE);
            passid.setVisibility(View.VISIBLE);
            reqtime.setVisibility(View.VISIBLE);
            reqdot.setVisibility(View.VISIBLE);
            dot1.setVisibility(View.VISIBLE);
            dot2.setVisibility(View.VISIBLE);
            gentime.setVisibility(View.VISIBLE);
            gendot.setVisibility(View.VISIBLE);
            dot3.setVisibility(View.VISIBLE);
            dot4.setVisibility(View.VISIBLE);
            req.setVisibility(View.VISIBLE);
            gen.setVisibility(View.VISIBLE);
            vertime.setText(canTime);
            vertime.setVisibility(View.VISIBLE);
            verdot.setVisibility(View.VISIBLE);
            ver.setText("Pass Cancelled");
            ver.setVisibility(View.VISIBLE);
        }

return  view;
    }

}
