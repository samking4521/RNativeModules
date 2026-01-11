import {
  Dimensions,
  Platform,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import React, { useEffect, useState } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import Animated, {
  useAnimatedStyle,
  useSharedValue,
  withTiming,
} from 'react-native-reanimated';
const { width } = Dimensions.get('window');

const WIDTH = width;
const GRAPH_WIDTH = (70 / 100) * WIDTH;
const BAR_WIDTH = (10 / 100) * WIDTH;

type TimeDataProps = {
  language: string;
  value: number;
  time: number;
};

const defaultTimeData = [
  {
    language: 'C++',
    value: 0,
    time: 0,
  },
  Platform.OS === 'android'
    ? {
        language: 'Kotlin',
        value: 0,
        time: 0,
      }
    : {
        language: 'Obj-C',
        value: 0,
        time: 0,
      },

  {
    language: 'JS',
    value: 0,
    time: 0,
  },
];

const BarItem = ({ item }: { item: TimeDataProps }) => {
  const height = useSharedValue(item.value);

  useEffect(() => {
    height.value = withTiming((item.value / 100) * GRAPH_WIDTH, {
      duration: 300,
    });
  }, [item.value, height]);

  const animatedStyle = useAnimatedStyle(() => ({
    height: height.value,
  }));

  return (
    <Animated.View
      style={[
        {
          width: BAR_WIDTH,

          backgroundColor:
            item.language === 'C++'
              ? '#00599C'
              : item.language === 'Kotlin'
              ? '#A97BFF'
              : item.language === 'Objective-C'
              ? '#FF6600'
              : '#F7DF1E',
        },
        animatedStyle,
      ]}
    />
  );
};

export default function App() {
  const [timeData, setTimeData] = useState<TimeDataProps[]>(defaultTimeData);
  const [computing, setComputing] = useState(false);

  useEffect(() => {}, []);

  const startCompute = () => {
    setTimeData([
      {
        language: 'C++',
        value: 0,
        time: 0,
      },
      Platform.OS === 'android'
        ? {
            language: 'Kotlin',
            value: 0,
            time: 0,
          }
        : {
            language: 'Obj-C',
            value: 0,
            time: 0,
          },

      {
        language: 'JS',
        value: 90,
        time: 4,
      },
    ]);
    setComputing(!computing);
  };

  return (
    <SafeAreaView style={styles.container}>
      <View>
        <View style={styles.graphCard}>
          <View style={styles.speedScaleTextCont}>
            <Text style={styles.speedScaleText}>100</Text>
            <Text style={styles.speedScaleText}>75</Text>
            <Text style={styles.speedScaleText}>50</Text>
            <Text style={styles.speedScaleText}>25</Text>
            <Text style={styles.speedScaleText}>0</Text>
          </View>
          <View>
            <View style={styles.chartContainer}>
              <View style={styles.barCont}>
                {timeData.map((item, i) => {
                  return (
                    <View key={`item-${i}`} style={styles.bars}>
                      {item.value > 0 && (
                        <Text style={styles.duration}>{item.time} ms</Text>
                      )}
                      <BarItem item={item} />
                    </View>
                  );
                })}
              </View>
            </View>
            <View style={styles.langCont}>
              {timeData.map((item, i) => {
                return (
                  <View key={`item-${i}`} style={styles.langTextCont}>
                    <Text style={styles.langText}>{item.language}</Text>
                  </View>
                );
              })}
            </View>
          </View>
        </View>

        <View style={styles.startBtnCont}>
          <Pressable
            onPress={startCompute}
            style={[
              styles.startBtn,
              { backgroundColor: computing ? 'green' : 'red' },
            ]}
          >
            <Text style={styles.computeText}>
              {computing ? 'computing...' : 'Start Computation'}
            </Text>
          </Pressable>
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 15,
    backgroundColor: '#121212',
    alignItems: 'center',
  },
  graphCard: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 50,
  },
  chartContainer: {
    width: GRAPH_WIDTH,
    height: GRAPH_WIDTH,
    borderLeftWidth: 2,
    borderBottomWidth: 2,
    borderColor: '#F5F5F5',
  },
  barCont: {
    flexDirection: 'row',
  },
  bars: {
    marginLeft: (15 / 100) * GRAPH_WIDTH,
    height: '100%',
    justifyContent: 'flex-end',
  },
  speedScaleTextCont: {
    width: 'auto',
    height: GRAPH_WIDTH,
    justifyContent: 'space-between',
    marginRight: 10,
  },
  speedScaleText: {
    color: '#F5F5F5',
    fontWeight: '600',
    fontSize: 16,
    textAlign: 'right',
  },
  duration: {
    color: '#F5F5F5',
    fontWeight: '600',
    fontSize: 12,
    textAlign: 'center',
  },
  langCont: {
    position: 'absolute',
    top: GRAPH_WIDTH + 10,
    flexDirection: 'row',
    alignItems: 'center',

    flex: 1,
  },
  langTextCont: {
    marginLeft: (18 / 100) * GRAPH_WIDTH,
  },
  langText: {
    color: '#F5F5F5',
    fontWeight: '600',
    fontSize: 16,
    textAlign: 'left',
  },
  startBtnCont: {
    marginTop: 100,
    alignSelf: 'center',
  },
  startBtn: {
    paddingHorizontal: 15,
    paddingVertical: 10,
    borderRadius: 5,
    justifyContent: 'center',
    alignItems: 'center',
  },
  computeText: {
    fontSize: 16,
    fontWeight: '600',
    color: 'white',
  },
});
