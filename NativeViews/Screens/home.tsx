import { Alert, ImageSource, LayoutChangeEvent, NativeSyntheticEvent, Pressable, StyleSheet, Text, View } from 'react-native';
import React, { useRef, useState } from 'react';
import TextInputLayout from '../specs/TextInputLayoutNativeComponent';
import EditText, { textEvent } from '../specs/EditTextNativeComponent'
import ImageView from '../specs/ImageViewNativeComponent';
import CameraIcon from 'react-native-vector-icons/AntDesign';
import AddIcon from 'react-native-vector-icons/AntDesign';
import NativeImagePicker from '../specs/NativeImagePicker';
import RecyclerViewNativeComponent, { Item } from '../specs/RecyclerViewNativeComponent';

export default function Home() {
  const [imageUri, setImageUri] = useState<string | null>(null)
  const [items, setItems] = useState<Item[] | null>(null)
  const textRef = useRef("")

  const selectImage = async()=> {
     try {
 const theUri = await NativeImagePicker.getImageUri()
      console.log("theUri: ", theUri)
      if(theUri){
          setImageUri(theUri)
      }
     }catch(e : any) {
        console.log("Error : ", e.message)
     }
     
  }

  const receiveText = (e: NativeSyntheticEvent<textEvent>)=> {
        console.log("onChangeText: ", e.nativeEvent.text)
        textRef.current = e.nativeEvent.text
  }

  console.log("items : ", items)

  const updateList = ()=> {
    if(!items){
        if(imageUri){
            setItems([{uri: imageUri, text: textRef.current}])
        }
    }else {
        if(imageUri){
           setItems([...items, {uri: imageUri, text: textRef.current}])
        }
    } 
      
  }

  return (
  <View style={styles.container}>
        <Text style={{fontSize: 20, fontWeight: "600", color:"red"}}>Todo App</Text>
         <ImageView
          
           source={imageUri}
           onPressEvent={(e)=> {
               if(e.nativeEvent.isClicked){
                    console.log("event : ", e.nativeEvent.isClicked)
               }
           }}
        />
      <View style={{backgroundColor: "red", width: 50, height: 50}} />
        {/* <View style={{marginVertical: 50}}>
        <Pressable onPress={selectImage} style={{position:"absolute", alignSelf: "flex-end", top: 170, right: -25, backgroundColor: "gray", width: 50, height: 50, borderRadius: 50, justifyContent: "center", alignItems: "center"}}>
            <CameraIcon name='camera' color={"white"} size={24}/>
        </Pressable>
        </View> */}
       
        
<TextInputLayout style={{width: "90%", minHeight: 50, marginBottom: 20}}>
            <EditText onChangeText={receiveText} style={{width: "100%"}}/>
        </TextInputLayout>
       
        

        <Pressable onPress={updateList} style={{flexDirection: "row", alignItems: "center", paddingHorizontal: 20, paddingVertical: 10, borderRadius: 10, backgroundColor: "green", marginBottom: 10}}>
            <AddIcon name='plus' size={24} color={"white"} style={{marginRight: 10}}/>
            <Text style={{color: "white", fontSize: 16, fontWeight: "600"}}>Add task</Text>
        </Pressable>

        <RecyclerViewNativeComponent
            style={{width: "90%", height: 100, marginTop: 20}}
            items={items}
        />
       
  </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
