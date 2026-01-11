import {
  StyleSheet,
  Text,
  View,
  Alert,
  EventSubscription,
  Pressable,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import NativeBatteryLevel from './specs/NativeBatteryLevel';
import { useEffect, useRef, useState } from 'react';
import BatteryUI from './components/batteryUI';

export type BatteryStateType = 'UNKNOWN' | 'UNPLUGGED' | 'CHARGING' | 'FULL';

export default function App() {
  const [batteryLevel, setBatteryLevel] = useState<number>(0);
  const [batteryState, setBatteryState] = useState<BatteryStateType>('UNKNOWN');
  const [lowPowerMode, setLowPowerMode] = useState<boolean>(false);
  const [notification, setNotification] = useState(false);

  const listenerSubscription = useRef<null | EventSubscription>(null);

  useEffect(() => {
    listenerSubscription.current = NativeBatteryLevel?.onBatteryEvent(value => {
      Alert.alert(
        `Battery Level: ${Math.round(value.level)}%\nStatus: ${
          value.state
        }\nLow Power Mode: ${value.isLowPowerMode ? 'ENABLED' : 'DISABLED'}`,
      );
      setBatteryLevel(value.level);
      setBatteryState(value.state);

      setLowPowerMode(value.isLowPowerMode ?? false);
    });

    return () => {
      listenerSubscription.current?.remove();
      listenerSubscription.current = null;
    };
  }, []);

  const getBatteryLevel = async () => {
    try {
      const batteryPercent = await NativeBatteryLevel.getBatteryLevel();
      console.log('Battery percent: ', batteryPercent);

      setBatteryLevel(batteryPercent);
    } catch (error) {
      console.error(error);
    }
  };

  const getBatteryState = async () => {
    try {
      const batteryStat = await NativeBatteryLevel.getBatteryState();
      console.log('Battery state: ', batteryStat);
      setBatteryState(batteryStat);
    } catch (error) {
      console.error(error);
    }
  };

  const getLowPowerMode = async () => {
    try {
      const lowMode = await NativeBatteryLevel.isLowPowerModeEnabled();
      console.log('low power mode: ', lowMode);
      setLowPowerMode(lowMode);
    } catch (error) {
      console.error(error);
    }
  };

  const isBatteryInfoAvailable = async () => {
    const isAvailable = await NativeBatteryLevel.isBatteryInfoAvailable();
    console.log('is info available : ', isAvailable);
    return isAvailable;
  };

  useEffect(() => {
    const isBatteryInfoAval = isBatteryInfoAvailable();
    if (!isBatteryInfoAval) {
      console.error('Battery Info is not available for this device');
      return;
    }
    getBatteryLevel();
    getBatteryState();
    getLowPowerMode();
  }, []);

  const addNotifications = (value: boolean) => {
    NativeBatteryLevel.addBatteryListener(!value);
    setNotification(!value);
  };

  return (
    <SafeAreaView style={styles.container}>
      <BatteryUI
        batteryLevel={batteryLevel}
        batteryState={batteryState}
        lowPowerMode={lowPowerMode}
      />
      <View style={styles.batteryDetails}>
        <Text style={styles.batteryLevelText}>{Math.round(batteryLevel)}%</Text>
      </View>
      <View style={styles.batteryDetails}>
        <Text style={styles.labelText}>Battery state: </Text>
        <Text style={styles.infoText}>{batteryState}</Text>
      </View>

      <View style={styles.batteryDetails}>
        <Text style={styles.labelText}>Battery low power mode: </Text>
        <Text style={styles.infoText}>
          {lowPowerMode ? 'ENABLED' : 'DISABLED'}
        </Text>
      </View>

      <Pressable
        onPress={() => addNotifications(notification)}
        style={[
          styles.btn,
          { backgroundColor: notification ? '#a93636ff' : '#4C4C4C' },
        ]}
      >
        <Text style={styles.textNotification}>
          Battery notification: {notification ? 'ON' : 'OFF'}
        </Text>
      </Pressable>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  batteryDetails: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 15,
  },
  labelText: {
    fontSize: 16,
    color: '#4C4C4C',
  },
  batteryLevelText: {
    fontSize: 35,
    fontWeight: '500',
    letterSpacing: 1,
  },
  textNotification: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: 600,
  },
  btn: {
    paddingHorizontal: 10,
    paddingVertical: 15,
    borderRadius: 5,
    marginTop: 20,
  },
  infoText: {
    fontSize: 18,
    fontWeight: '500',
  },
});
