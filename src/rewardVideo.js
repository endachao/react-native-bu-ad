/**
 * Enda <endachao@gmail.com>
 */
import {NativeModules} from 'react-native';

const {RewardVideo} = NativeModules;

export const load = (option) => {
    return RewardVideo.loadRewardVideo(option);
};

export default {
    load,
};


