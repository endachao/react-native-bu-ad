package com.enda.buad.ad.rn.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.enda.buad.R;
import com.enda.buad.ad.Ad;
import com.enda.buad.ad.rn.Splash;
import com.enda.buad.ad.rn.WeakHandler;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import static com.enda.buad.ad.rn.Splash.sendEvent;

public class SplashActivity extends Activity implements WeakHandler.IHandler {
    public static String TAG = "SplashActivity";
    private static String codeId;

    private FrameLayout mSplashContainer;

    private static final int AD_TIME_OUT = 2000;
    private static final int MSG_GO_MAIN = 1;
    private final WeakHandler mHandler = new WeakHandler(this);
    // 开屏广告是否已经加载
    private boolean adHasLoad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置 View
        setContentView(R.layout.activity_splash);
        mSplashContainer = (FrameLayout) findViewById(R.id.splash_container);

        // 定时，AD_TIME_OUT时间到时执行，如果开屏广告没有加载则跳转到主页面
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT);

        initExtras();
        loadSplashAd();
    }

    private void initExtras() {
        // 读取 code id
        Bundle extras = getIntent().getExtras();
        codeId = extras.getString("codeId");
    }

    private void loadSplashAd() {
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
                adHasLoad = true;
                fireEvent("onAdError", code, message);
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onTimeout() {
                adHasLoad = true;
                fireEvent("onAdError", 400, "加载超时");
                // 关闭开屏广告
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                adHasLoad = true;
                mHandler.removeCallbacksAndMessages(null);
                if (ad == null) {

                    fireEvent("onAdError", 400, "未拉取到开屏广告");
                    // 未知错误获取到的广告对象为空，关闭广告
                    goToMainActivity();
                    return;
                }

                // 获取SplashView
                View view = ad.getSplashView();
                mSplashContainer.removeAllViews();

                // 把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                mSplashContainer.addView(view);

                // 设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {

                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClick");
                        fireEvent("onAdClick", 0, "true");

                        goToMainActivity();
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow");
                        fireEvent("onAdShow", 0, "true");
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip");
                        fireEvent("onAdSkip", 0, "true");
                        goToMainActivity();
                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver");
                        fireEvent("onAdClose", 0, "开屏广告倒计时结束");
                        goToMainActivity();
                    }
                });
            }
        }, AD_TIME_OUT);

    }

    // 关闭开屏广告方法
    private void goToMainActivity() {
        if (mSplashContainer != null) {
            mSplashContainer.removeAllViews();
        }
        this.overridePendingTransition(0, 0); // 不要过渡动画
        this.finish();
    }

    @Override
    public void handleMsg(Message msg) {
        // 监听发出的事件，如果是 MSG_GO_MAIN 则去首页
        if (msg.what == MSG_GO_MAIN) {
            if (!adHasLoad) {
                goToMainActivity();
            }
        }
    }

    // 二次封装发送到RN的事件函数
    public void fireEvent(String eventName, int startCode, String message) {
        WritableMap p = Arguments.createMap();
        p.putInt("code", startCode);
        p.putString("message", message);
        sendEvent(eventName, p);
    }
}
