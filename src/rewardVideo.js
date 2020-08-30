/**
 * Enda <endachao@gmail.com>
 */
import {NativeModules, NativeEventEmitter} from 'react-native';

const {RewardVideo} = NativeModules;
const eventEmitter = new NativeEventEmitter(RewardVideo);

export const load = (option) => {
    return RewardVideo.loadRewardVideo(option);
};

const listenerCache = {};

interface EVENT_TYPE {
    onAdError: string; // 广告加载失败监听
    onAdLoaded: string; // 广告加载成功监听
    onAdClick: string; // 广告被点击监听
    onAdClose: string; // 广告关闭监听
}

export const show = () => {
    console.log('start ad show for js');
    let result = RewardVideo.showRewardVideo();
    const subscribe = (type, callback) => {
        if (listenerCache[type]) {
            listenerCache[type].remove();
        }

        return (listenerCache[type] = eventEmitter.addListener('RewardVideo-' + type, (event: any) => {
            callback(event);
        }));
    };

    return {
        result,
        subscribe,
    };
};


export default {
    load,
    show,
};


