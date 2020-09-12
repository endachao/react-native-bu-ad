package com.enda.buad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.enda.buad.ad.rn.AdManage;
import com.enda.buad.ad.rn.FeedAdManage;
import com.enda.buad.ad.rn.RewardVideo;
import com.enda.buad.ad.rn.Splash;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

public class BUADPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new AdManage(reactContext));
        modules.add(new RewardVideo(reactContext));
        modules.add(new Splash(reactContext));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new FeedAdManage(reactContext)      // 注册 UI 组件
        );
    }
}
