import { Alert, Image, NativeSyntheticEvent, StyleSheet, Text, useColorScheme, View } from 'react-native'
import React, { useState } from 'react'
import { SafeAreaView } from 'react-native-safe-area-context'
import { LightTheme, DarkTheme, Colors } from './Constants'
import MyTextView, { LongPressEvent, PressEvent } from './specs/TextViewNativeComponent'

export default function App() {
  const [backgroundColor, setBackgroundColor] = useState("rgb(255, 0, 0)")
  const isDarkMode = useColorScheme()
  const theme = isDarkMode == "light"? LightTheme : DarkTheme
  

   const changeBgColor = (event: NativeSyntheticEvent<PressEvent>)=>{
       console.log("Native event : ", event.nativeEvent.x)
       setBackgroundColor(`rgb(${event.nativeEvent.x}, ${event.nativeEvent.y}, ${event.nativeEvent.z})`)
   }

   const showAlert = (event: NativeSyntheticEvent<LongPressEvent>)=> {
        const isClicked = event.nativeEvent.isClicked
        console.log("Long press : ", isClicked)
        if(isClicked){
           Alert.alert("This is an alert", `A very cool event returns ${isClicked}`)
        }
   }

  return (
    <SafeAreaView style={[styles.container, {backgroundColor: theme.background}]}>
         <View style={[styles.body, {backgroundColor: backgroundColor}]}>
          <Text style={{marginBottom: 10}}>Physics</Text>
            <MyTextView
            style={{width: "100%", height: 39, backgroundColor:"green", padding: 100}}
              customText='Tesla is an electric car company'
              fontSize={30}
              fontWeight='italic'
              textAlign='center'
              color='#FFFFFF'
              onPress={changeBgColor}
              onLongPress={showAlert}
            />
            <Text style={{marginTop: 10}}>Tesla</Text>
         </View>
         <View style={styles.colorBoxContainer}>
            <View style={[styles.colorBox,{backgroundColor: Colors.black}]}/>
             <View style={[styles.colorBox, {backgroundColor: Colors.blue}]}/>
              <View style={[styles.colorBox, {backgroundColor: Colors.yellow}]}/>
               <View style={[styles.colorBox, {backgroundColor: Colors.brown}]}/>
                <View style={[styles.colorBox, {backgroundColor: Colors.red}]}/>
         </View>
         <View style={styles.iconContainer}>
            <View style={styles.iconViewCont}>
 <Image source={require("./assets/images/paint_ic.jpeg")} style={styles.iconStyle}/>
            </View>
            <View style={styles.iconViewCont}>
 <Image source={require("./assets/images/save_ic.png")} style={styles.iconStyle}/>
            </View>
            <View style={styles.iconViewCont}>
               <Image source={require("./assets/images/gallery_ic.png")} style={styles.iconStyle}/>
              </View> 
            <View style={styles.iconViewCont}>
 <Image source={require("./assets/images/undo_ic.jpeg")} style={styles.iconStyle}/>
              </View>  
              <View style={styles.iconViewCont}>
                <Image source={require("./assets/images/color_palette_ic.png")} style={styles.iconStyle}/>
                </View> 
                 
         </View>
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  container: {
     flex: 1,
    
  },
  body: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "red"
  },
  colorBoxContainer: {
     flexDirection: "row",
     alignItems: "center",
     justifyContent: "center"
  },
  colorBox: {
    width: 35,
    height: 35,
    borderRadius: 10,
    marginHorizontal: 10
  },
  iconContainer: {
    marginTop: 20,
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center"

  },
  iconStyle: {
    width: 35,
    height: 35
  },
  iconViewCont: {
    backgroundColor: "lightgray", 
    justifyContent: "center", 
    alignItems: "center", 
    width: 45, 
    height: 45,
    marginHorizontal: 12
  },
  xmlText: {
    fontSize: 20,
    color: "red",
    fontWeight: "600"
  }
})