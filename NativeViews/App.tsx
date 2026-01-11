import { StyleSheet, Text, View } from 'react-native'
import React from 'react'
import { SafeAreaView } from 'react-native-safe-area-context'
import Home from './Screens/home'

export default function App() {
  return (
    <SafeAreaView style={{flex: 1, backgroundColor: "white"}}>
       <Home/>
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({})