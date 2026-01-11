import { Dimensions, View, StyleSheet } from 'react-native';
import React from 'react';
import { useTheme } from '../Theme';

type AmpsType = {
  amplitude: number[];
};

const { width } = Dimensions.get('screen');
console.log(width)
const AMPS_WIDTH = 0.015 * width;
const AMPS_RADIUS = 0.01 * width;
const AMPS_DISTANCE = 0.009 * width
const AMPS_LENGTH = AMPS_WIDTH + AMPS_DISTANCE

function WaveFormView({ amplitude }: AmpsType) {
  const theme = useTheme();

  return (
    <View style={[styles.waveFormView, {marginTop: 20}]}>
      {amplitude.slice(-(width/AMPS_LENGTH)).map((amps, index) => {
        return (
          <View
            style={{
              marginRight: AMPS_DISTANCE,
              width: AMPS_WIDTH,
              height: amps,
              borderRadius: AMPS_RADIUS,
              backgroundColor: theme.colors.orange,
            }}
            key={`amps-${index}`}
          />
        );
      })}
    </View>
  );
}

const styles = StyleSheet.create({
  waveFormView: {
    marginTop: 20,
    width: width,
    height: 200,
    flexDirection: "row-reverse",
    alignItems: "center"
  },
});

export default React.memo(WaveFormView);
