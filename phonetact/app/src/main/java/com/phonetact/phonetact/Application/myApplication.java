package com.phonetact.phonetact.Application;

import android.app.Application;
import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;

/**
 * Created by hp on 25/05/2015.
 */
@ReportsCrashes(formKey = "",  mailTo = "abdelkrim1906@hotmail.fr")
public class myApplication extends Application {

    //variable
    public static QBUser currentuser;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressWarnings("unused")
    @Override
    public void onCreate() {

        // Initialize the Parse SDK.
        // FacebookSdk.sdkInitialize(this);
        QBSettings.getInstance().fastConfigInit("23613", "yHA4ut99y2wn2J-", "QFVr5nfHQFQ77Ou");
        super.onCreate();
        ACRA.init(this);
        initImageLoader(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}