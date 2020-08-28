package com.enda.buad.ad;

import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;

public class Ad {
    private static String TAG = "AD";
    private static boolean isInit;

    public static TTAdManager get() {
        if (!isInit) {
            throw new RuntimeException("SDK 未执行初始化");
        }
        return TTAdSdk.getAdManager();
    }

    /**
     * 初始化穿山甲的 SDK
     * @param context
     * @param appId
     * @param appName
     */
    public static void adInit(final Context context, String appId, String appName) {
        if (isInit) return;
        TTAdSdk.init(context,
                new TTAdConfig.Builder()
                        .appId(appId)
                        .appName(appName)
                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                        .supportMultiProcess(true) //是否支持多进程，true支持
                        .asyncInit(true) //异步初始化sdk，开启可减少初始化耗时
                        .build());
        Log.d(TAG, "sdk 初始化完成" + appId + "--" + appName);
        isInit = true;
    }
}
