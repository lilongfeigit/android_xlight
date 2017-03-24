package com.umarbhutta.xlightcompanion;

import android.app.Activity;
import android.app.Application;

import com.umarbhutta.xlightcompanion.imgloader.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guangbinw on 2017/3/13.
 */

public class App extends Application {

    private List<Activity> activityList;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化APP
        ImageLoaderUtils.initImageLoader(getApplicationContext());
    }

    public void setActivity(Activity activity){
        if(activityList==null){
            activityList = new ArrayList<Activity>();
        }
        if(!activityList.contains(activity)){
            activityList.add(activity);
        }
    }

    public void finishActivity(){
        if(activityList!=null){
            for(Activity activity:activityList){
                activity.finish();
            }
        }
    }
}
