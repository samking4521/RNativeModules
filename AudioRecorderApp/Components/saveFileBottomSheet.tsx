import {
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  ToastAndroid,
  View,
  Keyboard
} from 'react-native';
import React, { useMemo, useEffect, useState } from 'react';
import BottomSheet, {
  BottomSheetTextInput,
  BottomSheetView,
} from '@gorhom/bottom-sheet';
import { useTheme } from '../Theme';
import NativeAudioApi from '../specs/NativeAudioApi';


type saveFileTypes = {
  filename: string,
  setFilename: React.Dispatch<React.SetStateAction<string>>,
  showBottomSheet: boolean,
  setShowBottomSheet: React.Dispatch<React.SetStateAction<boolean>>,
  millis: React.RefObject<number>
};

function SaveFileBottomSheet({
  filename,
  setFilename,
  showBottomSheet,
  setShowBottomSheet,
  millis
}: saveFileTypes) {
    const [index, setIndex] = useState(0)
    const [focus, setFocus] = useState(false)
    const [inputHeight, setInputHeight] = useState(40); // default height
  const theme = useTheme();
  const snapPoints = useMemo(() => ['5%', index>0? '60%' : '40%'], [index]);
  
  useEffect(() => {
  const showSub = Keyboard.addListener('keyboardDidShow', () => {
    setIndex(1)
  });

  const hideSub = Keyboard.addListener('keyboardDidHide', () => {
    setIndex(0)
  });

  return () => {
    showSub.remove();
    hideSub.remove();
  };
}, []);

  async function saveRecording() {
    try{
if (filename.length == 0) {
      ToastAndroid.show('Set a filename', ToastAndroid.LONG);
      return;
    }

    NativeAudioApi.saveRecording(filename);
    await NativeAudioApi.insertAudio(filename, millis.current, Date.now())
    console.log("record saved to room db")
    millis.current = 0
    setFilename("")
    setShowBottomSheet(false)
    }catch(e){
        console.warn("error creating audio record : ", e)

    }
    
   
  }

    function deleteRecord() {
    NativeAudioApi.deleteRecording();
    setFilename('');
     setShowBottomSheet(false)
  }

  return (
    <BottomSheet  
      index={1}
      snapPoints={snapPoints}
      enablePanDownToClose
      onClose={deleteRecord}
      containerStyle={{
        backgroundColor: showBottomSheet ? 'rgba(0,0,0,0.3)' : undefined,
      }}

      handleIndicatorStyle={{ display: 'none' }}
      backgroundStyle={{ borderTopLeftRadius: 30, borderTopRightRadius: 30 }}
    >
      <BottomSheetView style={styles.bottomSheetCont}>
        <Text style={styles.text}>Save Record!</Text>

        <TextInput
          style={[styles.bottomSheetTxtInput, {height: inputHeight, borderColor: focus? theme.colors.customBlue : "rgb(160, 160, 160)", borderWidth: focus? 2 : 1}]}
          defaultValue={filename}
          onFocus={()=>setFocus(true)}
          selectionColor={"#84D4CD"}
          cursorColor={"#6E19EE"}
         selectionHandleColor={"#6E19EE"}
          onChangeText={setFilename}
          multiline={true}
          autoCorrect={false}
          onContentSizeChange={(event) => {
               
                setInputHeight(event.nativeEvent.contentSize.height)
           
        }}
        />

        <View style={styles.pressableCont}>
          <Pressable
            onPress={deleteRecord}
            style={[styles.pressable, { backgroundColor: 'rgb(236,236,236)' }]}
          >
            <Text style={[styles.pressableText, { color: theme.colors.black }]}>
              CANCEL
            </Text>
          </Pressable>
          <Pressable
            onPress={saveRecording}
            style={[
              styles.pressable,
              { backgroundColor: theme.colors.customBlue },
            ]}
          >
            <Text
              style={[styles.pressableText, { color: theme.colors.background }]}
            >
              OK
            </Text>
          </Pressable>
        </View>
      </BottomSheetView>
    </BottomSheet>
  );
}

export default React.memo(SaveFileBottomSheet)
const styles = StyleSheet.create({
  bottomSheetCont: {
    height: '100%',
    alignItems: 'center',
    paddingHorizontal: 20,
  },

  text: {
    fontSize: 26,
    marginBottom: 20,
  },
  bottomSheetTxtInput: {
     minHeight: 40,
    width: '100%',
    fontSize: 15,
    padding: 20,
    borderRadius: 40,
    backgroundColor: 'rgb(236, 236, 236)',
  
    textAlignVertical: 'top', // Very important
    borderWidth: 1,
    borderColor: 'rgb(160, 160, 160)',
    marginBottom: 40,
  },
  pressableCont: {
    width: '100%',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around',
    
  },
  pressable: {
    width: '40%',
    padding: 14,
    elevation: 2,
    borderRadius: 30,
    justifyContent: 'center',
    alignItems: 'center',
  },
  pressableText: {
    fontSize: 14,
    fontWeight: '500',
  },
});
