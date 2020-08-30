package com.enda.buad.ad.rn.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.enda.buad.ad.Ad;
import com.enda.buad.ad.Config;
import com.enda.buad.ad.rn.RewardVideo;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

public class RewardActivity extends Activity {

    public static String TAG = "RewardActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.rewardActivity = this;

        if (Ad.rewardVideoAdSinge == null) {
            Config.rewardPromise.reject("400", "广告未加载");
            return;
        }

        showAd(Ad.rewardVideoAdSinge);
    }

    private void showAd(TTRewardVideoAd ad) {
        final RewardActivity _this = this;
        ad.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
            @Override
            public void onAdShow() {
                String msg = "开始展示激励视频";
                Log.d(TAG, msg);
                fireEvent("onAdLoaded", 202, msg);
            }

            @Override
            public void onAdVideoBarClick() {
                RewardVideo.is_click = true;
                String msg = "点击了激励视频";
                Log.d(TAG, msg);
                fireEvent("onAdClick", 203, msg);
            }

            @Override
            public void onAdClose() {
                RewardVideo.is_close = true;
                fireEvent("onAdClose", 204, "关闭激励视频");
                RewardVideo.returnShowRewardVideoResult();
            }

            // 视频播放完成回调
            @Override
            public void onVideoComplete() {
                RewardVideo.is_show = true;
                String msg = "激励视频播放完成";
                Log.d(TAG, msg);
                fireEvent("onVideoComplete", 205, msg);
            }

            @Override
            public void onVideoError() {
                fireEvent("onAdError", 1004, "激励视频播放出错了");
            }

            // 视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励数量，rewardName：奖励名称
            @Override
            public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                if (rewardVerify) {
                    RewardVideo.is_reward = true;
                } else {
                    RewardVideo.is_reward = false;
                }
            }

            @Override
            public void onSkippedVideo() {
                //激励视频不允许跳过...
                RewardVideo.is_show = false;
            }
        });

        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                RewardVideo.is_download_idle = true;
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!RewardVideo.is_download_active) {
                    RewardVideo.is_download_active = true;
                    fireEvent("onDownloadActive", 300, "下载中，点击下载区域暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                fireEvent("onDownloadActive", 301, "下载暂停，点击下载区域继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                fireEvent("onDownloadActive", 304, "下载失败，点击下载区域重新下载");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                fireEvent("onDownloadActive", 302, "下载完成，点击下载区域重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                String msg = "安装完成，点击下载区域打开";
                Log.d(TAG, "onInstalled: " + msg);
                fireEvent("onDownloadActive", 303, msg);
                RewardVideo.is_install = true;
            }
        });

        // 开始显示广告,会铺满全屏...
        ad.showRewardVideoAd(_this);
    }

    // 二次封装发送到RN的事件函数
    public void fireEvent(String eventName, int startCode, String message) {
        WritableMap p = Arguments.createMap();
        p.putInt("code", startCode);
        p.putString("message", message);
        RewardVideo.sendEvent(eventName, p);
    }

}
