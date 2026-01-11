import {
  Pressable,
  StyleSheet,
  Text,
  View,
  PermissionsAndroid,
  ViewStyle,
  NativeEventEmitter,
  NativeModules,
  ToastAndroid,
} from 'react-native';
import React, { useEffect, useRef, useState } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import Cancel from 'react-native-vector-icons/Ionicons';
import Menu from 'react-native-vector-icons/Feather';
import { useTheme } from '../Theme';
import RecordBtnAnime from '../Components/recordBtnAnime';
import NativeAudioApi from '../specs/NativeAudioApi';
import Pause from 'react-native-vector-icons/FontAwesome6';
import Done from 'react-native-vector-icons/MaterialIcons';
import WaveFormView from '../Components/WaveFormView';
import SaveFileBottomSheet from '../Components/saveFileBottomSheet';
import { useNavigation } from '@react-navigation/native';

type btnTypes = 'cancel' | 'record' | 'menu' | null;

export default function RecordAudio() {
  const [rippleEffect, setRippleEffect] = useState<btnTypes | null>(null);
  const [permissionGranted, setPermissionGranted] = useState(false);
  const [isInitialized, setIsInitialized] = useState(false);
  const [isRecording, setIsRecording] = useState(false);
  const [amplitude, setAmplitude] = useState<number[]>([]);
  const [filename, setFilename] = useState('');
  const [showBottomSheet, setShowBottomSheet] = useState(false)
  const millis = useRef(0);
  const theme = useTheme();
  const navigation = useNavigation<any>()

  const startRecordStyle: ViewStyle = {
    backgroundColor: theme.colors.orange,
    justifyContent: 'center',
    alignItems: 'center',
    marginHorizontal: 0,
  };

  const getPermission = async () => {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        setPermissionGranted(true);
      } else {
        setPermissionGranted(false);
      }
    } catch (e) {
      console.warn(e);
    }
  };

  useEffect(() => {
    const nativeAudioEmitter = new NativeEventEmitter(
      NativeModules.NativeAudioApi,
    );

    let amplitudeListener = nativeAudioEmitter.addListener(
      'onAmplitude',
      event => {
        millis.current = millis.current + 100;
        setAmplitude(prev => [...prev, event.amplitude]);
      },
    );

    let deleteRecordListener = nativeAudioEmitter.addListener(
      'onDeleteRecord',
      event => {
        ToastAndroid.show(event.message, ToastAndroid.LONG);
      },
    );

    let completeRecordingListener = nativeAudioEmitter.addListener(
      'onSaveRecord',
      event => {
        ToastAndroid.show(event.message, ToastAndroid.LONG);
      },
    );

    // Remove the listeners once unmounted
    return () => {
      amplitudeListener.remove();
      deleteRecordListener.remove();
      completeRecordingListener.remove();
    };
  }, []);

  useEffect(() => {
    getPermission();
  }, []);

  function startAudioRecord() {
    if (permissionGranted) {
      startRecord();
      return;
    }
    getPermission();
  }

  function startRecord() {
    setRippleEffect('record');
    if (isInitialized) {
      if (isRecording) {
        NativeAudioApi?.pauseRecording();
        setIsRecording(false);
        return;
      }
      NativeAudioApi?.resumeRecording();
      setIsRecording(true);
    } else {
      setIsInitialized(true);
      setIsRecording(true);
      NativeAudioApi?.startRecording();
    }
  }

  function startTimerClock(milliseconds: number) {
    const ms = (milliseconds % 1000) / 10;
    const totalSeconds = Math.floor(milliseconds / 1000);
    const seconds = totalSeconds % 60;
    const totalMinutes = Math.floor(totalSeconds / 60);
    const minutes = totalMinutes % 60;
    const hours = Math.floor(totalMinutes / 60);

    // Pad with leading zeros
    const pad = (n: number, z = 2) => n.toString().padStart(z, '0');
    const padMs = (n: number) => n.toString().padStart(2, '0');

    if (hours > 0)
      return `${pad(hours)}:${pad(minutes)}:${pad(seconds)}:${padMs(ms)}`;
    else return `${pad(minutes)}:${pad(seconds)}:${padMs(ms)}`;
  }

  function deleteRecording() {
    if (!isInitialized) {
      return;
    }
    setRippleEffect('cancel');
    NativeAudioApi.deleteRecording();
    setAmplitude([]);
    millis.current = 0;
    setIsInitialized(false);
    setIsRecording(false);
  }

  function completeRecording() {
    if (!isInitialized ) {
        setRippleEffect('menu');
     navigation.navigate("audioGallery")
      return;
    }
   
     const thefilename = NativeAudioApi.stopRecording();
    setFilename(thefilename);
    setShowBottomSheet(true)
    setAmplitude([]);
    setIsInitialized(false);
    setIsRecording(false);
   
    
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.body}>
        <View style={styles.innerContainer}>
          <View>
            <Text style={styles.timerText}>
              {amplitude.length > 0
                ? startTimerClock(millis.current)
                : '00:00:00'}
            </Text>
          </View>
          <WaveFormView amplitude={amplitude} />
        </View>

        {!showBottomSheet && <View style={styles.audioBtnCont}>
          <Pressable onPress={deleteRecording}>
            <View
              style={[
                styles.recordBtnAction,
                { backgroundColor: theme.colors.gray },
              ]}
            >
              <Cancel
                name="close"
                size={30}
                color={
                  isInitialized ? theme.colors.black : theme.colors.grayDark
                }
              />
              {rippleEffect === 'cancel' && (
                <RecordBtnAnime setRippleEffect={setRippleEffect} />
              )}
            </View>
          </Pressable>

          <Pressable onPress={startAudioRecord} style={styles.recordBtn}>
            <View style={[styles.recordBtn, startRecordStyle]}>
              {isRecording && (
                <Pause name="pause" size={18} color={theme.colors.black} />
              )}
            </View>
            {rippleEffect === 'record' && (
              <RecordBtnAnime setRippleEffect={setRippleEffect} />
            )}
          </Pressable>

          <Pressable
            onPress={completeRecording}
            style={[
              styles.recordBtnAction,
              { backgroundColor: theme.colors.gray },
            ]}
          >
            {isInitialized ? (
              <Done name="done" size={28} color={theme.colors.black} />
            ) : (
              <Menu name="menu" size={25} color={theme.colors.black} />
            )}
            {rippleEffect === 'menu' && (
              <RecordBtnAnime setRippleEffect={setRippleEffect} />
            )}
          </Pressable>
        </View>}
      </View>
      {showBottomSheet && <SaveFileBottomSheet
        filename={filename}
        showBottomSheet={showBottomSheet}
        setShowBottomSheet={setShowBottomSheet}
        setFilename={setFilename}
        millis={millis}
      />}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: '#FFFFFF',
  },
  body: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  innerContainer: {
    flex: 1,
    marginBottom: 'auto',
    justifyContent: 'center',
    alignItems: 'center',
  },
  timerText: {
    fontSize: 60,
    fontWeight: '400',
  },
  recordBtnAction: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 60,
    height: 60,
    borderRadius: 60,
    overflow: 'hidden',
  },
  recordBtn: {
    width: 70,
    height: 70,
    borderRadius: 70,
    marginHorizontal: 30,
    overflow: 'hidden',
  },

  audioBtnCont: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 70,
    
   
   
  },
});
