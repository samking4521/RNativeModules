import React, { useEffect } from 'react';
import Animated, {
  withTiming,
  withSequence,
  runOnJS,
  useSharedValue,
  useAnimatedStyle,
} from 'react-native-reanimated';
import { useTheme } from '../Theme';

type btnTypes = 'cancel' | 'record' | 'menu' | null;

interface Props {
  setRippleEffect: React.Dispatch<React.SetStateAction<btnTypes>>;
}

export default function RecordBtnAnime({ setRippleEffect }: Props) {
  const theme = useTheme();
  const scale = useSharedValue(0);
  const opacity = useSharedValue(0);

  const stylez = useAnimatedStyle(() => ({
    transform: [{ scale: scale.value }],
    opacity: opacity.value,
    backgroundColor: theme.colors.disabledDarkGray,
  }));

  const randomPosition = () => {
    const padding = 7;
    const computeA = Math.random();
    const computeB = Math.random();
    const resultA = computeA >= 0.5 ? 30 : 0;
    const resultB = computeB >= 0.5 ? 30 : 0;
    return {
      top: resultA + padding,
      left: resultB + padding,
    };
  };

  const position = randomPosition();

  const btnStyles = [
    {
      width: 40,
      height: 40,
      borderRadius: 40,
      position: 'absolute' as 'absolute',
      top: position.top,
      left: position.left,
    },
    stylez,
  ];

  useEffect(() => {
    opacity.value = withSequence(
      withTiming(0.5, { duration: 300 }),
      withTiming(0, { duration: 0 }),
    );

    scale.value = withSequence(
      withTiming(4, { duration: 300 }),
      withTiming(0.5, { duration: 0 }, finished => {
        if (finished) {
          runOnJS(setRippleEffect)(null);
        }
      }),
    );
  }, [opacity, scale, setRippleEffect]);

  return <Animated.View style={btnStyles} />;
}
