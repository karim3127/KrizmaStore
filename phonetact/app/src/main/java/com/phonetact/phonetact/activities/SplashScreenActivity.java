package com.phonetact.phonetact.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phonetact.R;
import com.phonetact.phonetact.Application.myApplication;
import com.phonetact.phonetact.Utils.Classe_Utils;
import com.phonetact.phonetact.Utils.ClickatellHttp;
import com.phonetact.phonetact.Utils.Country;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.rey.material.widget.SnackBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by hp on 25/05/2015.
 */
public class SplashScreenActivity extends ActionBarActivity implements Animation.AnimationListener{

    ImageView imgPoster;
    //ImageView imgPosterlogo;
    TextView txtlogo;
    SnackBar mSnackBar;

    RelativeLayout relative;
    // Animation
    Animation fade_in;
    Animation animzommfinal;
    Animation animSideDown;

    //variable
    HashMap<String, Country> countryLookupMap = null;
    public static String codefromSplash = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //intialised layout element
        mSnackBar =  (SnackBar)findViewById(R.id.main_sn);
        mSnackBar.text("You should verified your network!")
                .actionText("REFRESH")
                .duration(0)
                .actionClickListener(new SnackBar.OnActionClickListener() {
                    @Override
                    public void onActionClick(SnackBar snackBar, int i) {
                        //VerifiedNetwork();
                    }
                });
        imgPoster = (ImageView) findViewById(R.id.imagelogo);
        //imgPosterlogo = (ImageView) findViewById(R.id.imagelogo1);
        txtlogo = (TextView) findViewById(R.id.txt_logo);
        txtlogo.setVisibility(View.GONE);
      //  imgPosterlogo.setVisibility(View.GONE);
        relative = (RelativeLayout) findViewById(R.id.relative);

        animzommfinal = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.totop);
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        // load the animation
        animSideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fab_scale_up);

        // set animation listener
        animSideDown.setAnimationListener(this);

        fade_in.setAnimationListener(this);
        animzommfinal.setAnimationListener(this);
        StartAnimations();
        // imgPoster.startAnimation(animZoomIn);
        initialisermarquedevice();

    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.on);
        anim.reset();
        relative.clearAnimation();
        relative.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.off);
        anim.reset();
        imgPoster.clearAnimation();
        imgPoster.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

               // imgPosterlogo.setVisibility(View.VISIBLE);
               // imgPosterlogo.startAnimation(animSideDown);
                txtlogo.setVisibility(View.VISIBLE);
                txtlogo.startAnimation(fade_in);
            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();
    }


    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        /*if(animation == animSideDown){
            txtlogo.setVisibility(View.VISIBLE);
            txtlogo.startAnimation(fade_in);
        }*/
        if (animation == fade_in) {
            // ici pour passer vers une autre activity
            SplashHandler mHandler = new SplashHandler();
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessageDelayed(msg, 100);

        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    private class SplashHandler extends Handler {

        /**
         * This method is used to handle received messages
         *
         * @param msg
         *            <code>Message</code> to handle
         */
        public void handleMessage(Message msg) {
            // switch to identify the message by its code
            switch (msg.what) {
                default:
                case 0:
                    super.handleMessage(msg);
                    VerifiedNetwork();
            }
        }
    }

    //verified data network
    private void VerifiedNetwork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if ((ni != null) && (ni.isConnected())) {
            //verifed if existed a user in the device
            try{
                if(!(Classe_Utils.LoadPreferences("MYNUMBER",getApplicationContext()).equals(""))){
                    /**
                     * existe a user so he should login and creat a session
                     */
                    QBAuth.createSession(new QBUser(Classe_Utils.LoadPreferences("MYNUMBER",getApplicationContext()), Classe_Utils.LoadPreferences("MYNUMBER",getApplicationContext())), new QBEntityCallbackImpl<QBSession>() {
                        @Override
                        public void onSuccess(QBSession session, Bundle params) {
                            // success
                            //login now
                            QBUsers.getUserByLogin(Classe_Utils.LoadPreferences("MYNUMBER",getApplicationContext()), new QBEntityCallbackImpl<QBUser>() {
                                @Override
                                public void onSuccess(QBUser result, Bundle params) {
                                    super.onSuccess(result, params);
                                    ((myApplication)getApplication()).currentuser = result;
                                    Intent intent = new Intent(getApplicationContext(),
                                            Dashbord_Activity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fadd_in, R.anim.fade_out);
                                    finish();
                                }

                                @Override
                                public void onError(List<String> errors) {
                                    super.onError(errors);
                                }
                            });

                        }

                        @Override
                        public void onError(List<String> errors) {
                            // errors
                            //verfied if the user have set his number before
                            VerifSetNumber();
                        }
                    });

                }else{
                    //verfied if the user have set his number before
                    VerifSetNumber();
                }
            }catch(Exception e){
                Classe_Utils.savePreferences("MYNUMBER","",getApplicationContext());

                //verfied if the user have set his number before
                VerifSetNumber();
            }
        }else{
            mSnackBar.show();
        }
    }

    @SuppressWarnings("deprecation")
    private void initialisermarquedevice() {

        //list of favored
        try{
            Set<String> set = Classe_Utils.LoadLisPreferences("listfavory", getApplicationContext());
        }catch(Exception e){
            Classe_Utils.saveListPreferences("listfavory", new ArrayList<String>(), getApplicationContext());
        }
        //get my Contry Name
        save();
        codefromSplash = (String)countryLookupMap.get(getUserCountry(getApplicationContext())).getName();
    }

    /**
     * verfied if a user have set his number and he dont set his verifed code
     */
    private void VerifSetNumber(){
        try{
            if(Classe_Utils.LoadPreferences("VERIFCODE",getApplicationContext()).equals("true")){
                Intent intent = new Intent(getApplicationContext(),
                        ValidateCodeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadd_in, R.anim.fade_out);
                finish();
            }else{
                Intent intent = new Intent(getApplicationContext(),
                        SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadd_in, R.anim.fade_out);
                finish();
            }
        }catch(Exception e){
            Classe_Utils.savePreferences("VERIFCODE","false",getApplicationContext());
            Intent intent = new Intent(getApplicationContext(),
                    SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadd_in, R.anim.fade_out);
            finish();
        }
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toUpperCase();
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toUpperCase();
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

    //set the arraylist of contry code
    private void  save(){
        countryLookupMap = new HashMap<String, Country>();

        countryLookupMap.put("AD",new Country("376","Andorra"));//
        countryLookupMap.put("AE",new Country("971","United Arab Emirates"));//
        countryLookupMap.put("AF",new Country("93","Afghanistan"));
        countryLookupMap.put("AG",new Country("1","Antigua and Barbuda"));//
        countryLookupMap.put("AI",new Country("","Anguilla"));
        countryLookupMap.put("AL",new Country("355","Albania"));
        countryLookupMap.put("AM",new Country("374","Armenia"));
        countryLookupMap.put("AN",new Country("599","Netherlands Antilles"));
        countryLookupMap.put("AO",new Country("244","Angola"));
        countryLookupMap.put("AQ",new Country("672","Antarctica"));
        countryLookupMap.put("AR",new Country("54","Argentina"));
        countryLookupMap.put("AS",new Country("1","American Samoa"));//
        countryLookupMap.put("AT",new Country("43","Austria"));
        countryLookupMap.put("AU",new Country("61","Australia"));
        countryLookupMap.put("AW",new Country("297","Aruba"));
        countryLookupMap.put("AZ",new Country("994","Azerbaijan"));
        countryLookupMap.put("BA",new Country("387","Bosnia and Herzegovina"));
        countryLookupMap.put("BB",new Country("1","Barbados"));//
        countryLookupMap.put("BD",new Country("880","Bangladesh"));
        countryLookupMap.put("BE",new Country("32","Belgium"));
        countryLookupMap.put("BF",new Country("226","Burkina Faso"));
        countryLookupMap.put("BG",new Country("359","Bulgaria"));
        countryLookupMap.put("BH",new Country("973","Bahrain"));
        countryLookupMap.put("BI",new Country("257","Burundi"));
        countryLookupMap.put("BJ",new Country("229","Benin"));
        countryLookupMap.put("BM",new Country("1","Bermuda"));//
        countryLookupMap.put("BN",new Country("673","Brunei"));
        countryLookupMap.put("BO",new Country("591","Bolivia"));
        countryLookupMap.put("BR",new Country("55","Brazil"));
        countryLookupMap.put("BS",new Country("1","Bahamas"));//
        countryLookupMap.put("BT",new Country("975","Bhutan"));
        //countryLookupMap.put("BV",new Country("","Bouvet Island"));//
        countryLookupMap.put("BW",new Country("267","Botswana"));
        countryLookupMap.put("BY",new Country("375","Belarus"));
        countryLookupMap.put("BZ",new Country("501","Belize"));
        countryLookupMap.put("CA",new Country("1","Canada"));
        countryLookupMap.put("CC",new Country("61","Cocos (Keeling) Islands"));
        countryLookupMap.put("CD",new Country("243","Congo, The Democratic Republic of the"));
        countryLookupMap.put("CF",new Country("236","Central African Republic"));
        countryLookupMap.put("CG",new Country("242","Congo"));
        countryLookupMap.put("CH",new Country("41","Switzerland"));
        countryLookupMap.put("CI",new Country("225","Côte d?Ivoire"));
        countryLookupMap.put("CK",new Country("682","Cook Islands"));
        countryLookupMap.put("CL",new Country("56","Chile"));
        countryLookupMap.put("CM",new Country("237","Cameroon"));
        countryLookupMap.put("CN",new Country("86","China"));
        countryLookupMap.put("CO",new Country("57","Colombia"));
        countryLookupMap.put("CR",new Country("506","Costa Rica"));
        countryLookupMap.put("CU",new Country("53","Cuba"));
        countryLookupMap.put("CV",new Country("238","Cape Verde"));
        countryLookupMap.put("CX",new Country("61","Christmas Island"));
        countryLookupMap.put("CY",new Country("357","Cyprus"));
        countryLookupMap.put("CZ",new Country("420","Czech Republic"));
        countryLookupMap.put("DE",new Country("49","Germany"));
        countryLookupMap.put("DJ",new Country("253","Djibouti"));
        countryLookupMap.put("DK",new Country("45","Denmark"));
        countryLookupMap.put("DM",new Country("1","Dominica"));//
        countryLookupMap.put("DO",new Country("1","Dominican Republic"));//
        countryLookupMap.put("DZ",new Country("213","Algeria"));
        countryLookupMap.put("EC",new Country("593","Ecuador"));
        countryLookupMap.put("EE",new Country("372","Estonia"));
        countryLookupMap.put("EG",new Country("20","Egypt"));
        countryLookupMap.put("EH",new Country("212","Western Sahara"));//
        countryLookupMap.put("ER",new Country("291","Eritrea"));
        countryLookupMap.put("ES",new Country("34","Spain"));
        countryLookupMap.put("ET",new Country("251","Ethiopia"));
        countryLookupMap.put("FI",new Country("358","Finland"));
        countryLookupMap.put("FJ",new Country("679","Fiji Islands"));
        countryLookupMap.put("FK",new Country("500","Falkland Islands"));
        countryLookupMap.put("FM",new Country("691","Micronesia, Federated States of"));
        countryLookupMap.put("FO",new Country("298","Faroe Islands"));
        countryLookupMap.put("FR",new Country("33","France"));
        countryLookupMap.put("GA",new Country("241","Gabon"));
        countryLookupMap.put("GB",new Country("44","United Kingdom"));
        countryLookupMap.put("GD",new Country("1","Grenada"));//
        countryLookupMap.put("GE",new Country("995","Georgia"));
        //countryLookupMap.put("GF",new Country("","French Guiana"));//
        countryLookupMap.put("GH",new Country("233","Ghana"));
        countryLookupMap.put("GI",new Country("350","Gibraltar"));
        countryLookupMap.put("GL",new Country("299","Greenland"));
        countryLookupMap.put("GM",new Country("220","Gambia"));
        countryLookupMap.put("GN",new Country("224","Guinea"));
        //countryLookupMap.put("GP",new Country("","Guadeloupe"));//
        countryLookupMap.put("GQ",new Country("240","Equatorial Guinea"));
        countryLookupMap.put("GR",new Country("30","Greece"));
        //countryLookupMap.put("GS",new Country("","South Georgia and the South Sandwich Islands"));//
        countryLookupMap.put("GT",new Country("502","Guatemala"));
        countryLookupMap.put("GU",new Country("1","Guam"));//
        countryLookupMap.put("GW",new Country("245","Guinea-Bissau"));
        countryLookupMap.put("GY",new Country("592","Guyana"));
        countryLookupMap.put("HK",new Country("852","Hong Kong"));
        //countryLookupMap.put("HM",new Country("","Heard Island and McDonald Islands"));//
        countryLookupMap.put("HN",new Country("504","Honduras"));
        countryLookupMap.put("HR",new Country("385","Croatia"));
        countryLookupMap.put("HT",new Country("509","Haiti"));
        countryLookupMap.put("HU",new Country("36","Hungary"));
        countryLookupMap.put("ID",new Country("62","Indonesia"));
        countryLookupMap.put("IE",new Country("353","Ireland"));
        countryLookupMap.put("IL",new Country("972","Israel"));
        countryLookupMap.put("IN",new Country("91","India"));
        countryLookupMap.put("IO",new Country("246","British Indian Ocean Territory"));//
        countryLookupMap.put("IQ",new Country("964","Iraq"));
        countryLookupMap.put("IR",new Country("98","Iran"));
        countryLookupMap.put("IS",new Country("354","Iceland"));//
        countryLookupMap.put("IT",new Country("39","Italy"));
        countryLookupMap.put("JM",new Country("1","Jamaica"));//
        countryLookupMap.put("JO",new Country("962","Jordan"));
        countryLookupMap.put("JP",new Country("81","Japan"));
        countryLookupMap.put("KE",new Country("254","Kenya"));
        countryLookupMap.put("KG",new Country("996","Kyrgyzstan"));
        countryLookupMap.put("KH",new Country("855","Cambodia"));
        countryLookupMap.put("KI",new Country("686","Kiribati"));
        countryLookupMap.put("KM",new Country("269","Comoros"));
        countryLookupMap.put("KN",new Country("1","Saint Kitts and Nevis"));//
        countryLookupMap.put("KP",new Country("850","North Korea"));
        countryLookupMap.put("KR",new Country("82","South Korea"));
        countryLookupMap.put("KW",new Country("965","Kuwait"));
        countryLookupMap.put("KY",new Country("1","Cayman Islands"));//
        countryLookupMap.put("KZ",new Country("7","Kazakstan"));
        countryLookupMap.put("LA",new Country("856","Laos"));
        countryLookupMap.put("LB",new Country("961","Lebanon"));
        countryLookupMap.put("LC",new Country("1","Saint Lucia"));//
        countryLookupMap.put("LI",new Country("423","Liechtenstein"));
        countryLookupMap.put("LK",new Country("94","Sri Lanka"));
        countryLookupMap.put("LR",new Country("231","Liberia"));
        countryLookupMap.put("LS",new Country("266","Lesotho"));
        countryLookupMap.put("LT",new Country("370","Lithuania"));
        countryLookupMap.put("LU",new Country("352","Luxembourg"));
        countryLookupMap.put("LV",new Country("371","Latvia"));
        countryLookupMap.put("LY",new Country("218","Libyan Arab Jamahiriya"));
        countryLookupMap.put("MA",new Country("212","Morocco"));
        countryLookupMap.put("MC",new Country("377","Monaco"));
        countryLookupMap.put("MD",new Country("373","Moldova"));
        countryLookupMap.put("MG",new Country("261","Madagascar"));
        countryLookupMap.put("MH",new Country("692","Marshall Islands"));
        countryLookupMap.put("MK",new Country("389","Macedonia"));
        countryLookupMap.put("ML",new Country("223","Mali"));
        countryLookupMap.put("MM",new Country("95","Myanmar"));
        countryLookupMap.put("MN",new Country("976","Mongolia"));
        countryLookupMap.put("MO",new Country("853","Macao"));
        countryLookupMap.put("MP",new Country("1","Northern Mariana Islands"));//
        //countryLookupMap.put("MQ",new Country("","Martinique"));//
        countryLookupMap.put("MR",new Country("222","Mauritania"));
        countryLookupMap.put("MS",new Country("1","Montserrat"));//
        countryLookupMap.put("MT",new Country("356","Malta"));
        countryLookupMap.put("MU",new Country("230","Mauritius"));
        countryLookupMap.put("MV",new Country("960","Maldives"));
        countryLookupMap.put("MW",new Country("265","Malawi"));
        countryLookupMap.put("MX",new Country("52","Mexico"));
        countryLookupMap.put("MY",new Country("60","Malaysia"));
        countryLookupMap.put("MZ",new Country("258","Mozambique"));
        countryLookupMap.put("NA",new Country("264","Namibia"));
        countryLookupMap.put("NC",new Country("687","New Caledonia"));
        countryLookupMap.put("NE",new Country("227","Niger"));
        //countryLookupMap.put("NF",new Country("","Norfolk Island"));//
        countryLookupMap.put("NG",new Country("234","Nigeria"));
        countryLookupMap.put("NI",new Country("505","Nicaragua"));
        countryLookupMap.put("NL",new Country("31","Netherlands"));
        countryLookupMap.put("NO",new Country("","Norway"));
        countryLookupMap.put("NP",new Country("977","Nepal"));
        countryLookupMap.put("NR",new Country("674","Nauru"));
        countryLookupMap.put("NU",new Country("683","Niue"));
        countryLookupMap.put("NZ",new Country("64","New Zealand"));
        countryLookupMap.put("OM",new Country("968","Oman"));
        countryLookupMap.put("PA",new Country("507","Panama"));
        countryLookupMap.put("PE",new Country("51","Peru"));
        countryLookupMap.put("PF",new Country("689","French Polynesia"));
        countryLookupMap.put("PG",new Country("675","Papua New Guinea"));
        countryLookupMap.put("PH",new Country("63","Philippines"));
        countryLookupMap.put("PK",new Country("92","Pakistan"));
        countryLookupMap.put("PL",new Country("48","Poland"));
        countryLookupMap.put("PM",new Country("508","Saint Pierre and Miquelon"));
        countryLookupMap.put("PN",new Country("870","Pitcairn"));
        countryLookupMap.put("PR",new Country("1","Puerto Rico"));
        countryLookupMap.put("PS",new Country("970","Palestine"));//
        countryLookupMap.put("PT",new Country("351","Portugal"));
        countryLookupMap.put("PW",new Country("680","Palau"));
        countryLookupMap.put("PY",new Country("595","Paraguay"));
        countryLookupMap.put("QA",new Country("974","Qatar"));
        //countryLookupMap.put("RE",new Country("","Réunion"));//
        countryLookupMap.put("RO",new Country("40","Romania"));
        countryLookupMap.put("RU",new Country("7","Russian Federation"));
        countryLookupMap.put("RW",new Country("250","Rwanda"));
        countryLookupMap.put("SA",new Country("966","Saudi Arabia"));
        countryLookupMap.put("SB",new Country("677","Solomon Islands"));
        countryLookupMap.put("SC",new Country("248","Seychelles"));
        countryLookupMap.put("SD",new Country("249","Sudan"));
        countryLookupMap.put("SE",new Country("46","Sweden"));
        countryLookupMap.put("SG",new Country("65","Singapore"));
        countryLookupMap.put("SH",new Country("290","Saint Helena"));
        countryLookupMap.put("SI",new Country("386","Slovenia"));
        countryLookupMap.put("SJ",new Country("47","Svalbard and Jan Mayen"));//
        countryLookupMap.put("SK",new Country("421","Slovakia"));
        countryLookupMap.put("SL",new Country("232","Sierra Leone"));
        countryLookupMap.put("SM",new Country("378","San Marino"));
        countryLookupMap.put("SN",new Country("221","Senegal"));
        countryLookupMap.put("SO",new Country("252","Somalia"));
        countryLookupMap.put("SR",new Country("597","Suriname"));
        countryLookupMap.put("ST",new Country("239","Sao Tome and Principe"));
        countryLookupMap.put("SV",new Country("503","El Salvador"));
        countryLookupMap.put("SY",new Country("963","Syria"));
        countryLookupMap.put("SZ",new Country("268","Swaziland"));
        countryLookupMap.put("TC",new Country("1","Turks and Caicos Islands"));//
        countryLookupMap.put("TD",new Country("235","Chad"));
        // countryLookupMap.put("TF",new Country("","French Southern territories"));//
        countryLookupMap.put("TG",new Country("228","Togo"));
        countryLookupMap.put("TH",new Country("66","Thailand"));
        countryLookupMap.put("TJ",new Country("992","Tajikistan"));
        countryLookupMap.put("TK",new Country("690","Tokelau"));
        countryLookupMap.put("TM",new Country("993","Turkmenistan"));
        countryLookupMap.put("TN",new Country("216","Tunisia"));
        countryLookupMap.put("TO",new Country("676","Tonga"));
        countryLookupMap.put("TP",new Country("670","East Timor"));//
        countryLookupMap.put("TR",new Country("90","Turkey"));
        countryLookupMap.put("TT",new Country("1","Trinidad and Tobago"));//
        countryLookupMap.put("TV",new Country("688","Tuvalu"));
        countryLookupMap.put("TW",new Country("886","Taiwan"));
        countryLookupMap.put("TZ",new Country("255","Tanzania"));
        countryLookupMap.put("UA",new Country("380","Ukraine"));
        countryLookupMap.put("UG",new Country("256","Uganda"));
        //countryLookupMap.put("UM",new Country("","United States Minor Outlying Islands"));//
        countryLookupMap.put("US",new Country("1","United States"));
        countryLookupMap.put("UY",new Country("598","Uruguay"));
        countryLookupMap.put("UZ",new Country("998","Uzbekistan"));
        countryLookupMap.put("VA",new Country("39","Holy See (Vatican City State)"));
        countryLookupMap.put("VC",new Country("1","Saint Vincent and the Grenadines"));//
        countryLookupMap.put("VE",new Country("58","Venezuela"));
        //countryLookupMap.put("VG",new Country("","Virgin Islands, British"));
        //countryLookupMap.put("VI",new Country("","Virgin Islands, U.S."));//
        countryLookupMap.put("VN",new Country("84","Vietnam"));
        countryLookupMap.put("VU",new Country("678","Vanuatu"));
        countryLookupMap.put("WF",new Country("681","Wallis and Futuna"));
        countryLookupMap.put("WS",new Country("685","Samoa"));
        countryLookupMap.put("YE",new Country("967","Yemen"));
        countryLookupMap.put("YT",new Country("262","Mayotte"));
        //countryLookupMap.put("YU",new Country("","Yugoslavia"));//
        countryLookupMap.put("ZA",new Country("27","South Africa"));
        countryLookupMap.put("ZM",new Country("260","Zambia"));
        countryLookupMap.put("ZW",new Country("263","Zimbabwe"));
    }

}

