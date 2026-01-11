import {
  Pressable,
  StyleSheet,
  Text,
  View,
  NativeEventEmitter,
  NativeModules,
  TextInput,
} from 'react-native';
import React, { useEffect, useState } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import Back from 'react-native-vector-icons/Ionicons';
import Replay5 from 'react-native-vector-icons/MaterialIcons';
import Play from 'react-native-vector-icons/FontAwesome5';
import Forward5 from 'react-native-vector-icons/MaterialIcons';
import Pause from 'react-native-vector-icons/FontAwesome6';
import Animated, {
  useAnimatedProps,
  useSharedValue,
} from 'react-native-reanimated';
import { Slider } from 'react-native-awesome-slider';
import NativeAudioApi from '../specs/NativeAudioApi';

const AnimatedTextInput = Animated.createAnimatedComponent(TextInput);

export default function MediaPlayer({ route }: any) {
  const navigation = useNavigation();
  const progress = useSharedValue(0);
  const text = useSharedValue('0:00');
  const [speedValue, setSpeedValue] = useState('x 1.0');
  const { audioData } = route.params;
  const min = useSharedValue(0);
  const max = useSharedValue(audioData.duration);

  const [play, setPlay] = useState(false);

  useEffect(() => {
    NativeAudioApi.playAudio(audioData.filePath)
    setPlay(true)
    
    const nativeAudioEmitter = new NativeEventEmitter(
      NativeModules.NativeAudioApi,
    );

    let currentPositionListener = nativeAudioEmitter.addListener(
      'onGetPosition',
      event => {
        
        progress.value = event.currentPosition;
       
        text.value = msToDuration(event.currentPosition);
      },
    );

    let completeListener = nativeAudioEmitter.addListener(
      'onCompleteListener',
      event => {
        if (event.completePlay) {
          console.log('complete listener: ', event);
          progress.value = audioData.duration;
          text.value = msToDuration(audioData.duration);
        
          setPlay(false);
        }
      },
    );

    let seekListener = nativeAudioEmitter.addListener('onSeek', event => {
      console.log('seek: ', event);
      progress.value = event.seek;
      text.value = msToDuration(event.seek);
    });

    return () => {
      currentPositionListener.remove();
      completeListener.remove();
      seekListener.remove();
    };
  }, []);

  function msToDuration(ms: number) {
    const totalSeconds = Math.floor(ms / 1000);
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const seconds = totalSeconds % 60;

    const pad = (num: number) => String(num).padStart(2, '0');
    const pad1 = (num: number) => String(num).padStart(1, '0');

    if (hours > 0) {
      return `${pad1(hours)}:${pad1(minutes)}:${pad(seconds)}`;
    } else {
      return `${pad1(minutes)}:${pad(seconds)}`;
    }
  }

  async function playAudio() {
    if (play) {
      try {
        await NativeAudioApi.pauseAudio();
        console.log('Audio paused');
        setPlay(false);
      } catch (e) {
        console.warn('error playing', e);
      }
    } else {
      try {
        await NativeAudioApi.playAudio(audioData.filePath);
        console.log('Audio playing');
        setPlay(true);
      } catch (e) {
        console.warn('error playing', e);
      }
    }
  }

  async function goBack() {
    try {
      await NativeAudioApi.stopAudio();
      console.log('stopped succesfully');
      navigation.goBack();
    } catch (e) {
      console.warn('error stopping audio', e);
    }
  }

  const seekAudio = async (value: number, type: string) => {
    try {
      await NativeAudioApi.seekTo(value, type);
      console.log('seeked succesfully');
    } catch (e) {
      console.warn('error seeking audio forward', e);
    }
  };

  const offsetAnimatedProps = useAnimatedProps(() => {
    return {
      text: `${text.value}`,
      defaultValue: `${text.value}`,
    };
  });

  const controlAudioSpeed = () => {
    if (speedValue == 'x 1.0') {
      NativeAudioApi.playbackSpeed('mid');
      setSpeedValue('x 1.5');
    } else if (speedValue == 'x 1.5') {
      NativeAudioApi.playbackSpeed('high');
      setSpeedValue('x 2.0');
    } else {
      NativeAudioApi.playbackSpeed('normal');
      setSpeedValue('x 1.0');
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.headerTextCont}>
        <Back
          onPress={goBack}
          name="arrow-back"
          size={25}
          style={{ marginRight: 10 }}
        />
        <Text style={styles.headerText}>{audioData.fileName}</Text>
      </View>
      <View style={styles.body}>
        <View style={styles.controls}>
          <Pressable onPress={controlAudioSpeed} style={styles.seek}>
            <Text>{speedValue}</Text>
          </Pressable>

          <View>
            <Slider
              onSlidingComplete={(value: number) => seekAudio(value, 'normal')}
              progress={progress}
              minimumValue={min}
              maximumValue={max}
              containerStyle={styles.slider}
              bubbleContainerStyle={styles.bubbleCont}
              thumbWidth={12}
              theme={{
                maximumTrackTintColor: 'rgb(229, 229, 229)',
                minimumTrackTintColor: '#rgb(255, 0, 45)',
              }}
            />
            <View style={styles.duration}>
              <AnimatedTextInput
                animatedProps={offsetAnimatedProps}
                editable={false}
                style={styles.durationText}
              />

              <Text style={styles.durationText}>
                {msToDuration(audioData.duration)}
              </Text>
            </View>
            <Pressable style={styles.audioControls}>
              <Replay5
                onPress={() => seekAudio(progress.value, 'decrease')}
                name="replay-5"
                size={25}
                color={'rgb(255, 0, 45)'}
              />
              <Pressable onPress={playAudio} style={styles.playPause}>
                {play ? (
                  <Pause name="pause" size={20} color={'#FFFFFF'} />
                ) : (
                  <Play name="play" size={20} color={'#FFFFFF'} />
                )}
              </Pressable>
              <Forward5
                onPress={() => seekAudio(progress.value, 'increase')}
                name="forward-5"
                size={25}
                color={'rgb(255, 0, 45)'}
              />
            </Pressable>
          </View>
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },
  body: {
    flex: 1,
    padding: 10,
  },
  slider: {
    height: 2,
  },
  bubbleCont: {
    display: 'none',
  },
  headerTextCont: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 10,
  },
  headerText: {
    flex: 1,
    fontSize: 20,
    paddingHorizontal: 20,
    textAlign: 'center',
  },
  controls: {
    marginTop: 'auto',
    paddingBottom: 30,
  },
  seek: {
    alignSelf: 'center',
    paddingHorizontal: 15,
    paddingVertical: 10,
    borderRadius: 30,
    marginBottom: 35,
    backgroundColor: 'rgb(229, 229, 229)',
  },
  duration: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginTop: 5,
  },
  durationText: {
    fontSize: 12,
  },
  audioControls: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around',
    marginTop: 5,
  },
  playPause: {
    backgroundColor: 'rgb(255, 0, 45)',
    width: 60,
    height: 60,
    borderRadius: 60,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
