package com.enda.buad.ad.rn.view;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import com.enda.buad.R;
import com.facebook.react.bridge.ReactContext;

public class FeedAdView extends RelativeLayout {

    private ReactContext reactContext;
    private Activity activity;
    private String _codeId;
    private int _adWidth;
    private boolean isDownload;

    final protected RelativeLayout relativeLayout;

    public FeedAdView(ReactContext context) {
        super(context);
        reactContext = context;
        activity = context.getCurrentActivity();

        // 根据布局id把这个布局加载成一个View并返回的
        inflate(context, R.layout.feed_view, this);

        relativeLayout = findViewById(R.id.feed_container);
    }

    public void setAdWidth(int width) {
        _adWidth = width;
    }

    public void setCodeId(String codeId) {
        _codeId = codeId;
    }


}
