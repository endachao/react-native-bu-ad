/**
 * Enda <endachao@gmail.com>
 */
import {NativeModules, NativeEventEmitter} from 'react-native';

const {SplashAd} = NativeModules;
const eventEmitter = new NativeEventEmitter(SplashAd);

interface EVENT_TYPE {
    onAdError: string; // 广告加载失败监听
    onAdClick: string; // 广告被点击监听
    onAdClose: string; // 广告关闭
    onAdSkip: string; // 用户点击跳过广告监听
    onAdShow: string; // 开屏广告开始展示
}

const listenerCache = {};
const show = (options) => {


    console.log(SplashAd);

    SplashAd.showSplashAd(options);

    const subscribe = (type, callback) => {
        if (listenerCache[type]) {
            listenerCache[type].remove();
        }
        return (listenerCache[type] = eventEmitter.addListener('SplashAd-' + type, (event: any) => {
            callback(event);
        }));
    };

    return {
        subscribe,
    };
};

export default {
    show,
};
