package com.enda.buad.ad;

import android.app.Activity;

import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.facebook.react.bridge.Promise;

public class Config {
    public static String appId;
    public static String appName = "AppName";
    public static boolean isInit;
    public static String rewardName = "金币";
    public static int rewardAmount = 1;
    public static TTAdManager adManager;
    public static TTSplashAd splashAd;
    public static TTNativeExpressAd nativeBannerAd;

    // 激励视频的 active
    public static Activity rewardActivity;

    // 激励视频的 Promise
    public static Promise rewardPromise;
}
