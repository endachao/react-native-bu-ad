/**
 * Enda <endachao@gmail.com>
 */
import {NativeModules} from 'react-native';

const {AdManager} = NativeModules;


export const init = (appInfo) => {
    console.log(appInfo);
    return AdManager.init(appInfo)
};


export default {
    init
}
