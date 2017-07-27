package com.anashidayat.kirimsms;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.util.ServerRunner;

public class MainActivity extends AppCompatActivity {
   private NanoHTTPD server;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent myintent=new Intent(MainActivity.this,KirimsmsService.class);

        stopService(myintent);
        FragmentManager fragmentManager=getSupportFragmentManager();
        mSectionsPagerAdapter=new SectionsPagerAdapter(fragmentManager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        FloatingActionButton floatingActionButton=(FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent=new Intent(MainActivity.this,KirimsmsService.class);
                startService(myintent);
                bindservice();
                Toast.makeText(MainActivity.this,"Seting berhasil.  Server HTTP Running",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        /*
        Button button=(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent=new Intent(MainActivity.this,KirimsmsService.class);
                startService(myintent);
                bindservice();
                Toast.makeText(MainActivity.this,"Seting berhasil",Toast.LENGTH_LONG).show();
            }
        });
        */

    }
    private void bindservice(){

    KirimSmsConnection.getInstance(this).doBindService();
    }
    protected void onStart() {
        super.onStart();

        Log.i(MyStatic.TAG, "onStart: ");

    }
 protected void onStop(){
     super.onStop();
     Log.i(MyStatic.TAG, "onStop: ");

 }
    void doUnbindService() {
      KirimSmsConnection.getInstance(this).doUnbindService();
    }
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position){
                case 0:
                    fragment=new ComputerSetFragment();
                    break;
                case 1:
                    fragment=new AndroidSetFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TERIMA SMS";
                case 1:
                    return "KIRIM SMS";

            }
            return null;
        }
    }
    /*
    protected void onResume(){
        super.onResume();

        server = new MyServer();


    }
    */
    /*
    protected void onPause() {
        super.onPause();
        if (server != null)
            server.stop();
    }
    */

}
