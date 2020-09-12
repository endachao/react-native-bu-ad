package com.enda.buad.ad.rn;

import androidx.annotation.NonNull;

import com.enda.buad.ad.rn.view.FeedAdView;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

import java.util.Map;

public class FeedAdManage extends ViewGroupManager<FeedAdView> {
    public static ReactApplicationContext reactAppContext;

    public FeedAdManage(ReactApplicationContext context) {
        reactAppContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return "FeedAdManage";
    }


    // 视图是在 createViewInstance 方法中创建的
    @NonNull
    @Override
    protected FeedAdView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new FeedAdView(reactContext);
    }


    // 该方法告诉react-native当你添加一个react view时，会调用Android原生的layout方法
    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }


    // getExportedCustomBubblingEventTypeConstants 方法将事件通知映射到JavaScript端
    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put("onAdClick",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onAdClick")))
                .put("onAdError",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onAdError")))
                .put("onAdClose",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onAdClose")))
                .put("onAdLayout",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onAdLayout")))
                .build();
    }
}
