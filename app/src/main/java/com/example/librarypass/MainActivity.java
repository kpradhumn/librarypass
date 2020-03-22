package com.example.librarypass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ViewPager mviewpager;
    private TabLayout mtablayout;
    private ViewPagerAdapter mviewpageradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.update:
            {
                Intent Registration_Intent=new Intent(MainActivity.this,Registration.class);
                startActivity(Registration_Intent);
                break;
            }
            case R.id.Logout:
            {
                Intent Login_Intent=new Intent(MainActivity.this,Login.class);
                startActivity(Login_Intent);
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
}
