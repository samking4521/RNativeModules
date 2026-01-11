import { StatusBar, StyleSheet, View } from 'react-native';
import RootNavigator from './navigation/layout';
import { useTheme } from './Theme';
import { GestureHandlerRootView } from 'react-native-gesture-handler';

function App() {
  const theme = useTheme();
  return (
    <GestureHandlerRootView style={styles.container}>
      <View style={styles.container}>
        <StatusBar
          barStyle={theme.id === 'light' ? 'dark-content' : 'light-content'}
          backgroundColor={theme.colors.statusBarColor}
        />
        <RootNavigator />
      </View>
    </GestureHandlerRootView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});

export default App;
