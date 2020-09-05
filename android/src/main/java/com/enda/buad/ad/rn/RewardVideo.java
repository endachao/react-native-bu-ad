package com.enda.buad.ad.rn;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.enda.buad.ad.Ad;
import com.enda.buad.ad.Config;
import com.enda.buad.ad.rn.activity.RewardActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RewardVideo extends ReactContextBaseJavaModule {
    public static String TAG = "RewardVideo";
    public static ReactApplicationContext reactAppContext;

    //激励视频类的状态
    public static boolean is_show = false;
    public static boolean is_click = false;
    public static boolean is_close = false;
    public static boolean is_reward = false;
    public static boolean is_download_idle = false;
    public static boolean is_download_active = false;
    public static boolean is_install = false;

    public RewardVideo(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        reactAppContext = reactContext;
    }

    // 发送事件到RN
    public static void sendEvent(String eventName, @Nullable WritableMap params) {
        reactAppContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(TAG + "-" + eventName, params);
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void loadRewardVideo(ReadableMap options, final Promise promise) {
        String extra = "";
        String userId = "";
        String codeId;

        if (!options.hasKey("codeId")) {
            promise.reject("400", "codeId is required");
        }
        codeId = options.getString("codeId");
        if (options.hasKey("userId")) {
            userId = options.getString("userId");
        }

        if (options.hasKey("extra")) {
            extra = options.getString("extra");
        }

        loadRewardVideo(codeId, userId, extra, promise);
    }

    @ReactMethod
    public void showRewardVideo(ReadableMap options, final Promise promise) {
        String codeId = options.getString("codeId");
        if (!options.hasKey("codeId")) {
            promise.reject("400", "codeId is required");
        }

        if (!Ad.rewardVideoAdMap.containsKey(codeId)) {
            promise.reject("400", codeId + " not load");
        }

        Config.rewardPromise = promise;
        Activity context = reactAppContext.getCurrentActivity();
        try {
            Intent intent = new Intent(reactAppContext, RewardActivity.class);
            // 传递 codeId
            intent.putExtra("codeId", codeId);

            // 不要过渡动画
            assert context != null;
            context.overridePendingTransition(0, 0);
            context.startActivityForResult(intent, 10000);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "start reward Activity error: ", e);
        }
    }

    public static void loadRewardVideo(final String codeId, String userId, String extra, final Promise promise) {
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setExpressViewAcceptedSize(500, 500)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName(Config.rewardName) //奖励的名称
                .setRewardAmount(Config.rewardAmount)   //奖励的数量
                .setUserID(userId)
                .setOrientation(TTAdConstant.VERTICAL)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
                .setMediaExtra(extra) //用户透传的信息，可不传
                .build();

        Ad.createAdNative(reactAppContext).loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                promise.reject(String.valueOf(code), code + ":loadRewardVideoAd ad error " + message);
            }

            // 视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Log.d("reward Cached ", "穿山甲激励视频缓存成功");
            }

            // 视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                // 支持缓存多个激励视频
                Ad.rewardVideoAdMap.put(codeId, ad);
                promise.resolve(true);
            }
        });
    }

    public static void returnShowRewardVideoResult() {
        if (Config.rewardPromise != null) {
            // 回调返回结果
            String json = "{\"is_show\":" + is_show + ",\"is_click\":" + is_click + ",\"is_install\":" + is_install + ",\"is_reward\":" + is_reward + "}";
            Config.rewardPromise.resolve(json);
        }

        if (Config.rewardActivity != null) {
            // 释放 Activity
            Config.rewardActivity.finish();
        }
    }
}
