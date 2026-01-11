import {useColorScheme } from 'react-native'
import { lightTheme } from './light'
import { darkTheme } from './dark'

export const useTheme = ()=> {
     const scheme = useColorScheme()
     return scheme === "light"? lightTheme : darkTheme
}