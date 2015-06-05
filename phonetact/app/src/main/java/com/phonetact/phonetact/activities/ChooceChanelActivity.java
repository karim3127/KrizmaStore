package com.phonetact.phonetact.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.phonetact.R;
import com.phonetact.phonetact.Utils.MaterialRippleLayout;
import com.rey.material.widget.CheckBox;

/**
 * Created by hp on 29/05/2015.
 */
public class ChooceChanelActivity extends ActionBarActivity {

    //element layout
    MaterialRippleLayout mySaveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosechanel);

        // intialiser the element layout
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_ab_up_compat);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                overridePendingTransition(R.anim.no_change_x, R.anim.push_right);
            }
        });
        mySaveButton = (MaterialRippleLayout) findViewById(R.id.Savebuuton);

        //click button
        mySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(),Dashbord_Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.push_left,R.anim.no_change_x);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_change_x, R.anim.push_right);
    }

}
