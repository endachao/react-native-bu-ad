# react-native-bu-ad

## Getting started

`$ yarn add git+https://github.com/endachao/react-native-bu-ad.git`

### Update

`$ yarn upgrade react-native-bu-ad`

## Usage
```javascript

import React from 'react';
import {ad} from 'react-native-bu-ad';
import rewardVideo from 'react-native-bu-ad/src/rewardVideo';

import {
    SafeAreaView,
    StyleSheet,
    ScrollView,
    View,
    Text,
    StatusBar,
    Button,
} from 'react-native';


class App extends React.Component {
    constructor(props) {
        super(props);
        let options = {
            appId: 'App Id',
            appName: 'App Name',
        };
        ad.init(options).then(res => {
            console.log(res);
            rewardVideo.load({
                codeId: '代码位 ID',
                userId: '123',
                extra: '1233',
            }).then(res => {
                console.log('激励视频缓存成功', res);
            }).catch(err => {
                console.log(err);
            });
        });

        this.videoAd = this.videoAd.bind(this);
    }

    componentDidMount(): * {
    }

    videoAd() {
        console.log('videoAd');
        let res = rewardVideo.show();

        res.result.then((val) => {
            console.log('RewardVideo 回调结果', val);
        });

        res.subscribe('onAdLoaded', (event) => {
            console.log('广告加载成功监听', event);
        });

        res.subscribe('onAdError', (event) => {
            console.log('广告加载失败监听', event);
        });

        res.subscribe('onAdClose', (event) => {
            console.log('广告被关闭监听', event);
        });

        res.subscribe('onAdClick', (event) => {
            console.log('广告点击查看详情监听', event);
        });
    }

    render(): boolean | number | string | React$Element<*> | React$Portal | Iterable | null {
        return (
            <View>
                <Text>React Native BU AD</Text>
                <Button onPress={this.videoAd} title={'激励视频'}/>
            </View>
        );
    }
}

export default App;

```
