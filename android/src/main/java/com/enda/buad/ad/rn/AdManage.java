package com.enda.buad.ad.rn;

import androidx.annotation.NonNull;

import com.enda.buad.ad.Ad;
import com.enda.buad.ad.Config;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

public class AdManage extends ReactContextBaseJavaModule {
    public static ReactApplicationContext reactAppContext;
    final public static String TAG = "AdManager";

    public AdManage(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        reactAppContext = reactContext;
    }

    @ReactMethod
    public void init(ReadableMap options, final Promise promise) {
        String appId = options.getString("appId");
        if (appId == null) {
            promise.reject("400", "appId is Require");
        }
        if (options.hasKey("appName")) {
            Config.appName = options.getString("appName");
        }
        Config.appId = appId;
        Ad.adInit(reactAppContext);
        promise.resolve(true);
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }
}
