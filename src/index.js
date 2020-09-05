/**
 * Enda <endachao@gmail.com>
 */
import {NativeModules} from 'react-native';

const {AdManager} = NativeModules;

import rewardVideo from 'react-native-bu-ad/src/rewardVideo';

export const init = (appInfo) => {
	console.log(NativeModules);
    return AdManager.init(appInfo);
};


export default {
    init,
};
