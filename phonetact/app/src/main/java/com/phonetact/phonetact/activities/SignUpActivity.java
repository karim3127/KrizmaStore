package com.phonetact.phonetact.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.phonetact.R;
import com.phonetact.phonetact.Utils.Classe_Utils;
import com.phonetact.phonetact.Utils.ClickatellHttp;
import com.phonetact.phonetact.Utils.Country;
import com.phonetact.phonetact.Utils.MaterialRippleLayout;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by hp on 25/05/2015.
 */
public class SignUpActivity extends ActionBarActivity {

    //element layout
    TextView myCodePhone;
    TextView myCity;
    EditText myPhone;
    MaterialRippleLayout myNextButton;

    //variable
    ClickatellHttp httpApi;
    Date expirationDate = null;
    public static String a = "0";
    public static String theCity = "";
    public static String theCode = "";
    HashMap<String, Country> countryLookupMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //get the item layout
        myCodePhone = (TextView) findViewById(R.id.myCodePhone);
        myCity = (TextView) findViewById(R.id.myCity);
        myPhone = (EditText) findViewById(R.id.myPhoneNumber);
        myNextButton = (MaterialRippleLayout) findViewById(R.id.nextbutton);


        /*//set data to mycode and my city
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //String countryCode = (tm.getSimCountryIso());
        String country_Language = getApplicationContext().getResources().getConfiguration().locale.getDisplayLanguage();
        String Country_code= getApplicationContext().getResources().getConfiguration().locale.getCountry();*/


        myCodePhone.setText("+"+GetCountryZipCode());
        myCity.setText(SplashScreenActivity.codefromSplash);

        //click mycity to choose a city
        myCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChooseCityActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left,R.anim.no_change_x);
            }
        });

        //click button
        myNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String numberphone = myPhone.getText().toString();
                if(! numberphone.equals("")){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myPhone.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    Dialog.Builder   builder = new SimpleDialog.Builder(R.style.SimpleDialog){

                        @Override
                        public void onPositiveActionClicked(DialogFragment fragment) {
                            /*Random rand = new Random();
                            int  n1 = rand.nextInt(10) + 0; int  n2 = rand.nextInt(10) + 0; int  n3 = rand.nextInt(10) + 0; int  n4 = rand.nextInt(10) + 0;
                            httpApi = new ClickatellHttp("khidma", "3470226", "tI6hVMd8");
                            a = n1+""+n2+""+n3+""+n4;
                            SendSingleMessage(numberphone,a);
                            Classe_Utils.savePreferences("MYCODE",a,getApplicationContext());*/
                            QBUsers.getUserByLogin(numberphone, new QBEntityCallbackImpl<QBUser>() {
                                @Override
                                public void onSuccess(QBUser result, Bundle params) {
                                    super.onSuccess(result, params);
                                    Toast.makeText(getApplicationContext(), "two  " + result.getLogin(), Toast.LENGTH_SHORT).show();
                                    affichedialog();
                                }

                                @Override
                                public void onError(List<String> errors) {
                                    super.onError(errors);
                                    Classe_Utils.savePreferences("MYNUMBER",numberphone,getApplicationContext());
                                    Classe_Utils.savePreferences("VERIFCODE","true",getApplicationContext());
                                    Intent myIntent = new Intent(getApplicationContext(),ValidateCodeActivity.class);
                                    startActivity(myIntent);
                                    overridePendingTransition(R.anim.push_left,R.anim.no_change_x);
                                    finish();
                                }
                            });

                            super.onPositiveActionClicked(fragment);
                        }

                        @Override
                        public void onNegativeActionClicked(DialogFragment fragment) {
                            super.onNegativeActionClicked(fragment);
                        }
                    };
                    ((SimpleDialog.Builder)builder).message("Ce numéro est-il le votre? "+numberphone+" .Un SMS avec votre code d'accès sera envoyé à ce numéro.")
                            .positiveAction("Ok")
                            .negativeAction("Modifier");
                    DialogFragment fragment = DialogFragment.newInstance(builder);
                    fragment.show(getSupportFragmentManager(),null);

                }

            }
        });

        //make listnner to the edittext
        myPhone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)&& (keyCode == KeyEvent.KEYCODE_ENTER)){
                    final String numberphone = myPhone.getText().toString();
                    if(! numberphone.equals("")){
                        Dialog.Builder   builder = new SimpleDialog.Builder(R.style.SimpleDialog){

                            @Override
                            public void onPositiveActionClicked(DialogFragment fragment) {
                                QBUsers.getUserByLogin(numberphone, new QBEntityCallbackImpl<QBUser>() {
                                    @Override
                                    public void onSuccess(QBUser result, Bundle params) {
                                        super.onSuccess(result, params);
                                        Toast.makeText(getApplicationContext(), "two  " + result.getLogin(), Toast.LENGTH_SHORT).show();
                                        affichedialog();
                                    }

                                    @Override
                                    public void onError(List<String> errors) {
                                        super.onError(errors);
                                        Random rand = new Random();
                                        int  n1 = rand.nextInt(10) + 0; int  n2 = rand.nextInt(10) + 0; int  n3 = rand.nextInt(10) + 0; int  n4 = rand.nextInt(10) + 0;
                                        httpApi = new ClickatellHttp("khidma", "3470226", "tI6hVMd8");
                                        a = n1+""+n2+""+n3+""+n4;
                                        SendSingleMessage(numberphone,a);
                                        Classe_Utils.savePreferences("MYCODE",a,getApplicationContext());
                                        Classe_Utils.savePreferences("MYNUMBER",numberphone,getApplicationContext());
                                        Classe_Utils.savePreferences("VERIFCODE","true",getApplicationContext());
                                        Intent myIntent = new Intent(getApplicationContext(),ValidateCodeActivity.class);
                                        startActivity(myIntent);
                                        overridePendingTransition(R.anim.push_left,R.anim.no_change_x);
                                        finish();
                                    }
                                });

                                super.onPositiveActionClicked(fragment);
                            }

                            @Override
                            public void onNegativeActionClicked(DialogFragment fragment) {
                                super.onNegativeActionClicked(fragment);
                            }
                        };
                        ((SimpleDialog.Builder)builder).message("Ce numéro est-il le votre? "+numberphone+" .Un SMS avec votre code d'accès sera envoyé à ce numéro.")
                                .positiveAction("Ok")
                                .negativeAction("Modifier");
                        DialogFragment fragment = DialogFragment.newInstance(builder);
                        fragment.show(getSupportFragmentManager(),null);

                    }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!theCity.isEmpty()){
            myCity.setText(theCity);
            myCodePhone.setText("+"+theCode);
        }
        Creatsession();
    }

    /**
     * This sends the given message and displays the output. The output is also shown via a toast.
     *
     * @param number  The number to send to. Should be in international format.
     * @param content The message the will be sent.
     */
    private void SendSingleMessage(final String number, final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ClickatellHttp.Message result = httpApi.sendMessage(number, content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "le resultat est  "+result.message_id, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    ShowException(e);
                }
            }
        }).start();
    }

    /**
     * This shows the given exception's message as a toast.
     *
     * @param exception The exception to show.
     */
    private void ShowException(final Exception exception) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void Creatsession(){
        try {
            expirationDate =  BaseService.getBaseService().getTokenExpirationDate();
        }catch (BaseServiceException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"one  "+expirationDate,Toast.LENGTH_SHORT).show();

        if(expirationDate == null){
           // final QBUser qbUser = new QBUser("karizma", "054206///");

            QBAuth.createSession( new QBEntityCallbackImpl<QBSession>() {
                @Override
                public void onSuccess(QBSession session, Bundle params) {


                }

                @Override
                public void onError(List<String> errors) {
                    // errors
                    Toast.makeText(getApplicationContext(), "errors " + errors, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    //affiche dialog when the user exist
    private void affichedialog(){
        Dialog.Builder   builder = new SimpleDialog.Builder(R.style.SimpleDialog){

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                Intent myIntent = new Intent(getApplicationContext(),Dashbord_Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.push_left,R.anim.no_change_x);
                finish();
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };
        ((SimpleDialog.Builder)builder).message("Ce numéro exite déja")
                .positiveAction("Ok")
                .negativeAction("");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(),null);
    }
    //Get my contry code
    public String GetCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    /*private GeoPoint getLocation() {
        try {
            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locationManager.getBestProvider(criteria,true);
//In order to make sure the device is getting the location, request updates.
// this requests updates every hour or if the user moves 1 kilometer
            Location curLocation = locationManager.getLastKnownLocation(provider);
            GeoPoint curGeoPoint = new GeoPoint((int)(curLocation.getLatitude() ), (int)(curLocation.getLongitude()));
            return curGeoPoint;
        } catch (NullPointerException e) {
            Log.e("your app name here","Log your error here      "+e);
        }
        return null;
    }


    public  String getCountryName(Context context, GeoPoint curLocal) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(curLocal.getLatitude() ,curLocal.getLongitude(),1);
            Iterator myVeryOwnIterator = countryLookupMap.keySet().iterator();
            String resultat = countryLookupMap.get(addresses.get(0).getCountryCode()).getName();


            //return resultat;
        } catch (IOException ignored) {
            ///Address result;
           // if (addresses != null && !addresses.isEmpty()) {

            //}
            return null;
        }
        return addresses.get(0).getCountryName();
    }*/



}
