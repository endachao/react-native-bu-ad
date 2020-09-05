package com.enda.buad.ad;

import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

import java.util.HashMap;
import java.util.Map;

public class Ad {
    private static String TAG = "AD";
    // 多广告位缓存激励视频
    public static Map<String, TTRewardVideoAd> rewardVideoAdMap = new HashMap<>();

    public static TTAdManager get() {
        if (!Config.isInit) {
            throw new RuntimeException("SDK Not Init");
        }
        return Config.adManager;
    }

    public static TTAdNative createAdNative(Context context) {
        return Config.adManager.createAdNative(context);
    }

    /**
     * 初始化穿山甲的 SDK
     *
     * @param context
     */
    public static boolean adInit(final Context context) {
        TTAdSdk.init(context,
                new TTAdConfig.Builder()
                        .appId(Config.appId)
                        .appName(Config.appName)
                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                        .supportMultiProcess(true) //是否支持多进程，true支持
                        .asyncInit(true) //异步初始化sdk，开启可减少初始化耗时
                        .build());
        Log.d(TAG, "sdk 初始化完成" + Config.appId + "--" + Config.appName);
        Config.isInit = true;
        Config.adManager = TTAdSdk.getAdManager();
        return true;
    }
}
