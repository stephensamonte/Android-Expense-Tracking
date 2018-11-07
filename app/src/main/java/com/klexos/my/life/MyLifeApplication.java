package com.klexos.my.life;

import com.firebase.client.Firebase;

/**
 * Created by steph on 7/5/2017.
 */

public class MyLifeApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Initialize Firebase */
        Firebase.setAndroidContext(this);
        /* Enable disk persistence. This is so that the application works offline */
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }

}