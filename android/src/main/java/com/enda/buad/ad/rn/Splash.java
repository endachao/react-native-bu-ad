package com.enda.buad.ad.rn;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.enda.buad.ad.rn.activity.SplashActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class Splash extends ReactContextBaseJavaModule {
    public static String TAG = "Splash";
    public static ReactApplicationContext reactAppContext;

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    public Splash(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        reactAppContext = reactContext;
    }

    @ReactMethod
    public void showSplashAd(ReadableMap options) {

        Intent intent = new Intent(reactAppContext, SplashActivity.class);
        try {
            intent.putExtra("codeId", options.getString("codeId"));
            final Activity context = getCurrentActivity();
            assert context != null;
            context.overridePendingTransition(0, 0);
            context.startActivityForResult(intent, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送事件到RN
    public static void sendEvent(String eventName, @Nullable WritableMap params) {
        reactAppContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(TAG + "-" + eventName, params);
    }

}
