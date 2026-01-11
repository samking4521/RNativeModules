import { Dimensions, StyleSheet, View, Text } from 'react-native';
import React, { memo } from 'react';
import Flash from 'react-native-vector-icons/Entypo';
import { BatteryStateType } from '../App';

const WIDTH = Dimensions.get('window').width;
const BATTERY_WIDTH = (50 / 100) * WIDTH;
const BATTERY_HEIGHT = 100;
const BATTERY_EDGE_HEIGHT = 50;
const ARR_LENGTH = 100;
const BATTERY_COUNT_WIDTH = BATTERY_WIDTH / ARR_LENGTH;
const BACKGROUND_COLOR = '#008000';
const BACKGROUND_WHITE = '#FFFFFF';
const LOW_POWER_MODE_COLOR = '#FF9500'

type batteryStatProps = {
    batteryLevel: number,
    batteryState: BatteryStateType,
    lowPowerMode: boolean
}

const BATTERY_COUNT_ARR = Array.from({ length: ARR_LENGTH }, (_, index) => {
  return {
    width: BATTERY_COUNT_WIDTH,
    index: index,
  };
});




const BatteryUI = ({ batteryLevel, batteryState, lowPowerMode }: batteryStatProps)=> {
  
function mapRange(value: number) {
    return (
      BATTERY_COUNT_WIDTH +
      (value * (BATTERY_COUNT_WIDTH * ARR_LENGTH - BATTERY_COUNT_WIDTH)) /
        ARR_LENGTH
    );
  }

  return (
    <View>
      <View style={styles.batteryContainer}>
        <View style={styles.batteryOutlineContainer}>
          <View style={styles.batteryBox}>
            {BATTERY_COUNT_ARR.map((item, _) => {
              return (
                <Text
                  key={`item-${item.index}`}
                  style={[styles.countStyle, {
                    backgroundColor:
                      mapRange(batteryLevel) >= item.width * item.index
                        ? lowPowerMode? LOW_POWER_MODE_COLOR : BACKGROUND_COLOR
                        : BACKGROUND_WHITE,
                  }]}
                />
              );
            })}
          </View>
          { batteryState === "CHARGING" && <View style={styles.flashCont}>
            <Flash name="flash" size={50} color="#000000" />
          </View>}
        </View>
        <View style={styles.batteryEdge} />
      </View>
    </View>
  );
}

export default memo(BatteryUI) 

const styles = StyleSheet.create({
  batteryContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 5,
  },
  batteryOutlineContainer: {
    width: BATTERY_WIDTH,
    height: BATTERY_HEIGHT,
    padding: 4,
    borderRadius: 10,
    borderWidth: 2,
  },
  batteryBox: {
    flex: 1,
    flexDirection: 'row',
    borderRadius: 5,
    overflow: 'hidden',
  },
  batteryEdge: {
    width: 1,
    height: BATTERY_EDGE_HEIGHT,
    borderWidth: 2,
  },
  flashCont: {
    ...StyleSheet.absoluteFillObject,
    justifyContent: 'center',
    alignItems: 'center',
  },
  countStyle: {
      flex: 1,
                    height: '100%',
  }
});
