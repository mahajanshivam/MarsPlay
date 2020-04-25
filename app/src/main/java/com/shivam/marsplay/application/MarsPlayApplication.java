package com.shivam.marsplay.application;

import android.app.Application;

import com.shivam.marsplay.di.ApiComponent;
import com.shivam.marsplay.di.ApiHelper;
import com.shivam.marsplay.di.AppModule;
import com.shivam.marsplay.di.DaggerApiComponent;


public class MarsPlayApplication extends Application {

    private ApiComponent mApiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiHelper(new ApiHelper())
                .build();
    }


    public static MarsPlayApplication get(Application application) {
        return (MarsPlayApplication) application;
    }

    public ApiComponent getApiComponent() {
        return mApiComponent;
    }
}
