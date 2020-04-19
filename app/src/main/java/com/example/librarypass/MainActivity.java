package com.example.librarypass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import mainFragments.ViewPagerAdapter;
import studentAuth.Login;
import studentAuth.updateUser;

public class MainActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ViewPager mviewpager;
    private TabLayout mtablayout;
    private ViewPagerAdapter mviewpageradapter;
    private FirebaseAuth mauth;
    private FirebaseFirestore fstore;
    private String currentuserid;
    private FirebaseUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        currentuser=mauth.getCurrentUser();

        mtoolbar=(Toolbar) findViewById(R.id.main_page_toolbar);
        mtablayout=findViewById(R.id.main_tab);
        mviewpager=(ViewPager) findViewById(R.id.maintab_pager);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("LIBRARY PASS");

        mviewpageradapter=new ViewPagerAdapter(getSupportFragmentManager());
        mviewpager.setAdapter(mviewpageradapter);

        mtablayout.setupWithViewPager(mviewpager);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(currentuser==null) {
            gotoLoginScreen();
            finish();
        }
        else
            currentuserid=mauth.getCurrentUser().getUid();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.update:
            {
                Intent Registration_Intent=new Intent(MainActivity.this, updateUser.class);
                startActivity(Registration_Intent);
                break;
            }
            case R.id.Logout:
            {
                mauth.signOut();
               gotoLoginScreen();
                break;
            }
            case R.id.guide:
            {
                Intent Guideline_Intent=new Intent(MainActivity.this,Guideline.class);
                startActivity(Guideline_Intent);
                break;
            }
        }

        return true;
    }
    private void gotoLoginScreen() {
        Intent intent=new Intent(MainActivity.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
