package com.enda.buad.ad.rn;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.enda.buad.ad.Ad;
import com.enda.buad.ad.Config;
import com.enda.buad.ad.rn.activity.SplashActivity;
import com.facebook.react.bridge.Arguments;
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


    @ReactMethod
    public void loadSplashAd(ReadableMap options) {

        String codeId = options.getString("codeId");
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();

        // 请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        Ad.createAdNative(Splash.reactAppContext).loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {

            @Override
            @MainThread
            public void onError(int code, String message) {
                Log.d(TAG, message);
                fireEvent("onLoadAdError", code, message);
            }

            @Override
            @MainThread
            public void onTimeout() {
                fireEvent("onLoadAdError", 400, "加载超时");
                // 关闭开屏广告
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                if (ad == null) {
                    fireEvent("onLoadAdError", 400, "未拉取到开屏广告");
                    return;

                }
                fireEvent("onLoadAdSuccess", 400, "开屏广告缓存成功");
                Config.splashAd = ad;
            }
        }, 3000);
    }

    // 发送事件到RN
    public static void sendEvent(String eventName, @Nullable WritableMap params) {
        reactAppContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(TAG + "-" + eventName, params);
    }


    // 二次封装发送到RN的事件函数
    public static void fireEvent(String eventName, int startCode, String message) {
        WritableMap p = Arguments.createMap();
        p.putInt("code", startCode);
        p.putString("message", message);
        sendEvent(eventName, p);
    }
}
