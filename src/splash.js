/**
 * Enda <endachao@gmail.com>
 */
import {NativeModules, NativeEventEmitter} from 'react-native';

const {Splash} = NativeModules;
const eventEmitter = new NativeEventEmitter(Splash);

interface EVENT_TYPE {
    onAdError: string; // 广告加载失败监听
    onAdClick: string; // 广告被点击监听
    onAdClose: string; // 广告关闭
    onAdSkip: string; // 用户点击跳过广告监听
    onAdShow: string; // 开屏广告开始展示
}

interface SHOW_EVENT_TYPE {
    onLoadAdError: string; // 广告加载失败监听
    onLoadAdSuccess: string; // 广告加载失败监听
}

const listenerCache = {};
const listenerLoadCache = {};
const show = (options) => {


    console.log(Splash);

    Splash.showSplashAd(options);

    const subscribe = (type, callback) => {
        if (listenerCache[type]) {
            listenerCache[type].remove();
        }
        return (listenerCache[type] = eventEmitter.addListener('Splash-' + type, (event: any) => {
            callback(event);
        }));
    };

    return {
        subscribe,
    };
};

const load = (options) => {

    Splash.loadSplashAd(options);

    const subscribe = (type, callback) => {
        if (listenerLoadCache[type]) {
            listenerLoadCache[type].remove();
        }
        return (listenerLoadCache[type] = eventEmitter.addListener('Splash-' + type, (event: any) => {
            callback(event);
        }));
    };

    return {
        subscribe,
    };
};

export default {
    show,
    load
};
