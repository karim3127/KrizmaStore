package com.phonetact.phonetact.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.phonetact.R;
import com.phonetact.phonetact.Utils.Classe_Utils;
import com.phonetact.phonetact.Utils.ClickatellHttp;
import com.phonetact.phonetact.Utils.MaterialRippleLayout;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import java.util.List;
import java.util.Random;

/**
 * Created by hp on 28/05/2015.
 */
public class ValidateCodeActivity extends ActionBarActivity{

    //element layout
    EditText myCode;
    MaterialRippleLayout myValidButton;

    //variable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validatecode);

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
        myCode = (EditText) findViewById(R.id.myCode);
        myValidButton = (MaterialRippleLayout) findViewById(R.id.validatecode);

        //make a filter to the broadcast service
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
       // registerReceiver(receiver,filter);

        //click button
        myValidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberphone = myCode.getText().toString();
               // if(numberphone.equals(Classe_Utils.LoadPreferences("MYCODE",getApplicationContext()))){
                // success
                    final QBUser userr = new QBUser(Classe_Utils.LoadPreferences("MYNUMBER",getApplicationContext()), Classe_Utils.LoadPreferences("MYNUMBER",getApplicationContext()));
                    userr.setPhone(Classe_Utils.LoadPreferences("MYNUMBER",getApplicationContext()));
                    QBUsers.signUp(userr, new QBEntityCallbackImpl<QBUser>() {
                        @Override
                        public void onSuccess(QBUser user, Bundle args) {
                            ((com.phonetact.phonetact.Application.myApplication)getApplication()).currentuser = user;
                            QBUsers.signIn(userr, new QBEntityCallbackImpl<QBUser>() {
                                @Override
                                public void onSuccess(QBUser user, Bundle params) {
                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(myCode.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
                                    Intent myIntent = new Intent(getApplicationContext(),SetDataUserActivity.class);
                                    startActivity(myIntent);
                                    overridePendingTransition(R.anim.push_left,R.anim.no_change_x);
                                    finish();
                                }

                                @Override
                                public void onError(List<String> errors) {
                                    Toast.makeText(getApplicationContext(),"login    "+errors,Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onError(List<String> errors) {
                            Toast.makeText(getApplicationContext(),""+errors,Toast.LENGTH_SHORT).show();
                        }
                    });

            //    }

            }
        });

        //make listnner to the edittext
        myCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)&& (keyCode == KeyEvent.KEYCODE_ENTER)){
                    String numberphone = myCode.getText().toString();
                    if(numberphone.equals(Classe_Utils.LoadPreferences("MYCODE",getApplicationContext()))){
                        final  QBUser userr = new QBUser(Classe_Utils.LoadPreferences("MYNUMBER",getApplicationContext()), Classe_Utils.LoadPreferences("MYNUMBER",getApplicationContext()));
                        userr.setPhone(Classe_Utils.LoadPreferences("MYNUMBER", getApplicationContext()));
                        QBUsers.signUp(userr, new QBEntityCallbackImpl<QBUser>() {
                            @Override
                            public void onSuccess(QBUser user, Bundle args) {
                                ((com.phonetact.phonetact.Application.myApplication)getApplication()).currentuser = user;
                                QBUsers.signIn(userr, new QBEntityCallbackImpl<QBUser>() {
                                    @Override
                                    public void onSuccess(QBUser user, Bundle params) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(myCode.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                                        Intent myIntent = new Intent(getApplicationContext(), SetDataUserActivity.class);
                                        startActivity(myIntent);
                                        overridePendingTransition(R.anim.push_left, R.anim.no_change_x);
                                        finish();
                                    }

                                    @Override
                                    public void onError(List<String> errors) {

                                    }
                                });
                            }

                            @Override
                            public void onError(List<String> errors) {
                                Toast.makeText(getApplicationContext(), "" + errors, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        affichedialog();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_change_x, R.anim.push_right);
    }

    private final BroadcastReceiver receiver
            = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Do something
            Log.i("cs.fsu", "smsReceiver: SMS Received");

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Log.i("cs.fsu", "smsReceiver : Reading Bundle");

                Object[] pdus = (Object[])bundle.get("pdus");
                SmsMessage sms = SmsMessage.createFromPdu((byte[])pdus[0]);
                String message = sms.getMessageBody();
                while (message.contains("FLAG"))
                    message = message.replace("FLAG", "");

                myCode.setText(message);
                /*if(message.contains(Classe_Utils.LoadPreferences("MYCODE", context))){
                    Toast.makeText(getApplicationContext(), "the message is  " + message, Toast.LENGTH_SHORT).show();

                }*/
            }
        }
    };

    //affiche dialog when the user exist
    private void affichedialog(){
        Dialog.Builder   builder = new SimpleDialog.Builder(R.style.SimpleDialog){

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };
        ((SimpleDialog.Builder)builder).message("Code invalide")
                .positiveAction("Ok")
                .negativeAction("");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(),null);
    }
}
