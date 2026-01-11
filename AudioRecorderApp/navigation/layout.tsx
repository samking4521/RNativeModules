import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import RecordAudio from '../Screens/home';
import AudioGallery from '../Screens/audioGallery';
import MediaPlayer from '../Screens/mediaPlayer';

const Stack = createNativeStackNavigator();

export default function RootNavigator() {
  return (
    <NavigationContainer>
        <Stack.Navigator screenOptions={{
            headerShown: false,
            animation:"slide_from_right"
    
        }} initialRouteName='home'>
            <Stack.Screen name="home" component={RecordAudio}/>
             <Stack.Screen name="audioGallery" component={AudioGallery}/>
              <Stack.Screen name="mediaPlayer" component={MediaPlayer}/>
            </Stack.Navigator>
    </NavigationContainer>
    
  );
}

