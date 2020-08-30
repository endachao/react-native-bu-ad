package com.enda.buad.ad.rn;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.enda.buad.ad.Ad;
import com.enda.buad.ad.Config;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

public class RewardVideo extends ReactContextBaseJavaModule {
    public static String TAG = "RewardVideo";
    public static ReactApplicationContext reactAppContext;

    public RewardVideo(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        reactAppContext = reactContext;
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

    public static void loadRewardVideo(String codeId, String userId, String extra, final Promise promise) {
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setExpressViewAcceptedSize(500, 500)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(100)   //奖励的数量
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
                Ad.rewardVideoAdSinge = ad;
                promise.resolve(true);
            }
        });
    }
}
