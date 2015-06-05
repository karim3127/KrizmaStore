package com.phonetact.phonetact.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.phonetact.R;
import com.phonetact.phonetact.Utils.Classe_Utils;
import com.phonetact.phonetact.Utils.MaterialRippleLayout;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by hp on 28/05/2015.
 */
public class SetDataUserActivity extends ActionBarActivity {

    //element layout
    MaterialRippleLayout mySaveButton;
    ImageView mimage;
    EditText mEmail;
    EditText mName;

    //variable
    private static final int REQUEST_IMAGE = 2;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    protected static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    public static final boolean etat = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdatauser);

        //variable and option
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.imageprofile)
                .showImageForEmptyUri(R.drawable.imageprofile)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnFail(R.drawable.imageprofile)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300)).build();

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
        mimage = (ImageView) findViewById(R.id.profilimage);
        mEmail = (EditText) findViewById(R.id.myEmail);
        mEmail.addTextChangedListener(valid_mail_inscription);
        mName = (EditText) findViewById(R.id.myName);
        mySaveButton = (MaterialRippleLayout) findViewById(R.id.validatecode);

        //click button
        mySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectPath.size() > 0){
                    uploadfile();
                }else{
                    // Connect image to user
                    QBUser user = new QBUser();
                    user.setId(((com.phonetact.phonetact.Application.myApplication)getApplication()).currentuser.getId());
                    user.setEmail(mEmail.getText().toString());
                    user.setFullName(mName.getText().toString());

                    QBUsers.updateUser(user, new QBEntityCallbackImpl<QBUser>(){
                        @Override
                        public void onSuccess(QBUser user, Bundle args) {
                            ((com.phonetact.phonetact.Application.myApplication)getApplication()).currentuser = user;
                            Intent myIntent = new Intent(getApplicationContext(),ChooceChanelActivity.class);
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.push_left,R.anim.no_change_x);
                            finish();
                        }

                        @Override
                        public void onError(List<String> errors) {

                        }
                    });
                }


            }
        });

        //click image profil
        mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedMode = MultiImageSelectorActivity.MODE_MULTI;

               // if(mChoiceMode.getCheckedRadioButtonId() == R.id.single){
                    selectedMode = MultiImageSelectorActivity.MODE_MULTI;
               /* }else{
                    selectedMode = MultiImageSelectorActivity.MODE_MULTI;
                }*/

                boolean showCamera = true;

                int maxNum = 1;
               /* if(!TextUtils.isEmpty(mRequestNum.getText())){
                    maxNum = Integer.valueOf(mRequestNum.getText().toString());
                }*/

                Intent intent = new Intent(SetDataUserActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
                // 默认选择
                if(mSelectPath != null && mSelectPath.size()>0){
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                }
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_change_x, R.anim.push_right);
    }


    private void uploadfile(){
       final  ProgressDialog proleg;
        proleg = new ProgressDialog(SetDataUserActivity.this);
        proleg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        proleg.setTitle("جاري التحميل....");
        proleg.setCancelable(false);
        proleg.show();
        proleg.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        proleg.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // just create any file
        File avatar = new File(mSelectPath.get(0));

        // Upload new avatar to Content module
        Boolean fileIsPublic = false;

        QBContent.uploadFileTask(avatar, fileIsPublic, null, new QBEntityCallbackImpl<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle params) {

                int uploadedFileID = qbFile.getId();
                LogInfuntion(uploadedFileID);
            }

            @Override
            public void onError(List<String> errors) {
                Toast.makeText(getApplicationContext(),"file    "+errors,Toast.LENGTH_SHORT).show();
            }
        }, new QBProgressCallback() {
            @Override
            public void onProgressUpdate(int progress) {
                proleg.setProgress(progress);
            }
        });
    }

    /**
     * login function
     */
    private void LogInfuntion(int uploadedFileID){
        // Connect image to user
        QBUser user = new QBUser();
        user.setId(((com.phonetact.phonetact.Application.myApplication)getApplication()).currentuser.getId());
        user.setEmail(mEmail.getText().toString());
        user.setFullName(mName.getText().toString());
        user.setFileId(uploadedFileID);

        QBUsers.updateUser(user, new QBEntityCallbackImpl<QBUser>(){
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                ((com.phonetact.phonetact.Application.myApplication)getApplication()).currentuser = user;
                Intent myIntent = new Intent(getApplicationContext(),ChooceChanelActivity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.push_left,R.anim.no_change_x);
                finish();
            }

            @Override
            public void onError(List<String> errors) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for(String p: mSelectPath){
                    sb.append(p);
                    //sb.append("\n");
                }
                imageLoader.displayImage("file://"+mSelectPath.get(0),mimage,options);
                Toast.makeText(getApplicationContext(),sb,Toast.LENGTH_SHORT).show();
            }
        }

    }


    /**
     * valid the mail
     *
     * @param email
     * @return
     */
    public boolean isValidEmailAddress(String email) {
        java.util.regex.Pattern p = java.util.regex.Pattern
                .compile(".+@.+\\.[a-z]+");
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    TextWatcher valid_mail_inscription = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            if (isValidEmailAddress(s.toString())) {
                mEmail.setTextColor(getResources().getColor(
                        R.color.color_bleu_S));
            } else {
                mEmail.setTextColor(getResources().getColor(
                        R.color.color_rouge));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };
}
