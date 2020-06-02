package com.example.librarypass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hotchemi.android.rate.AppRate;
import mainFragments.ViewPagerAdapter;
import studentAuth.Login;
import studentAuth.updateUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mtoolbar;
    private ViewPager mviewpager;
    private TabLayout mtablayout;
    private ViewPagerAdapter mviewpageradapter;
    private FirebaseAuth mauth;
    private FirebaseFirestore fstore;
    private String currentuserid, hostelName, uid;
    private FirebaseUser currentuser;
    private DrawerLayout drawerLayout;
    private static NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        mtoolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        mtablayout = findViewById(R.id.main_tab);
        mviewpager = (ViewPager) findViewById(R.id.maintab_pager);
        currentuser = mauth.getCurrentUser();

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("LIBRARY PASS");

        mviewpageradapter = new ViewPagerAdapter(getSupportFragmentManager());
        mviewpager.setAdapter(mviewpageradapter);
        //DrawerController.setIdentity("MainActivity");
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mtablayout.setupWithViewPager(mviewpager);
        SharedPreferences sharedPreferences = getSharedPreferences("hostel_pref", MODE_PRIVATE);
        hostelName = sharedPreferences.getString("hostel", "");


        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

    }

    /*  @Override
      public boolean onCreateOptionsMenu(Menu menu) {
          MenuInflater inflater=getMenuInflater();
          inflater.inflate(R.menu.main_menu,menu);
          return true;
      }

     */
    @Override
    public void onStart() {
        super.onStart();
        if (currentuser == null) {
            gotoLogin();
            finish();
        } else
            currentuserid = mauth.getCurrentUser().getUid();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.nav_profile: {
                Intent Registration_Intent = new Intent(MainActivity.this, updateUser.class);
                startActivity(Registration_Intent);
                break;
            }
            case R.id.nav_logout: {

                gotoLoginScreen();
                break;
            }
            case R.id.nav_guidlines: {
                Intent Guideline_Intent = new Intent(MainActivity.this, Guideline.class);
                startActivity(Guideline_Intent);
                break;
            }
            case R.id.nav_quspaper: {
                String url = "http://library.kiit.ac.in/index.html";
                Uri u = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW, u);
                startActivity(i);
            }
            case R.id.nav_rate:
            {

                break;
            }
            case R.id.nav_gallery:
            {
                Intent Gallery_Intent = new Intent(MainActivity.this, Gallery.class);
                startActivity(Gallery_Intent);
                break;
            }
            case R.id.nav_aboutus:
            {
                Intent Gallery_Intent = new Intent(MainActivity.this, AboutUs.class);
                startActivity(Gallery_Intent);
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gotoLoginScreen() {
        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        String day = dayFormat.format(calfordate.getTime());
        currentuserid = mauth.getCurrentUser().getUid();
        uid = day.concat(currentuserid);
        DocumentReference docRef = fstore.collection("Hostel").document(hostelName).collection("studentList").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String status = (String) document.get("status");
                        if (status.equals("1")) {
                            mauth.signOut();
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else if (status.equals("0"))
                            new AlertDialog.Builder(MainActivity.this).setTitle("Cannot Logout").setMessage(Html.fromHtml("As per our guidlines <br>to Logout you need to <b>return</b> the pass to the hostel by scanning the QR code.<br>Once the pass is <b>returned</b> the user can login or logout anytime.<br>"))
                                    .setPositiveButton("OK", null).show();
                    } else {
                        mauth.signOut();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private void gotoLogin() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // or finish();
    }
}

