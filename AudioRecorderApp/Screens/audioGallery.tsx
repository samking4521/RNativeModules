import { StyleSheet, Text, View, TextInput, FlatList, Pressable } from 'react-native'
import React, { useCallback, useEffect, useRef, useState } from 'react'
import NativeAudioApi from '../specs/NativeAudioApi'
import { SafeAreaView } from 'react-native-safe-area-context'
import { useTheme } from '../Theme'
import Search from 'react-native-vector-icons/Fontisto';
import Clear from 'react-native-vector-icons/Ionicons';
import Play from 'react-native-vector-icons/FontAwesome5';
import { AudioData } from '../Constants/DbAudio'
import { useNavigation } from '@react-navigation/native'

const AudioItem = React.memo(({item}: {item: AudioData})=>{
      const navigation = useNavigation<any>()

    function msToDuration(ms: number) {
  const totalSeconds = Math.floor(ms / 1000);
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;
 
  const pad = (num: number) => String(num).padStart(2, '0');

  if (hours > 0) {
    return `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`;
  } else {
    return `${pad(minutes)}:${pad(seconds)}`;
  }
}

function msToDate(ms: number) {
  const date = new Date(ms);
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed
  const year = date.getFullYear();

  return `${day}/${month}/${year}`;
}

function navToMediaPlayer(){
    navigation.navigate("mediaPlayer", {audioData: item})
}

    return(
        <Pressable onPress={navToMediaPlayer} style={styles.listItem}>
            <View style={styles.playCont}>
                <Play name='play' size={12} color={"rgb(141, 140, 140)"}/>
            </View>
            <View>
                <Text style={styles.filenameText}>{item.fileName}</Text>
                <View style={styles.audioStats}>
                    <Text style={styles.statsText}>{msToDuration(item.duration)}</Text>
                    <Text style={styles.statsText}>{msToDate(item.createdAt)}</Text>
                </View>
            </View>
        </Pressable>
    )
})

export default function AudioGallery() {
    const theme = useTheme()
    const [text, setText] = useState("")
    const [audioRecordList, setAudioRecordList] = useState<AudioData[]>([])

    useEffect(()=>{
       (async ()=>{
       
           const audioData = await NativeAudioApi.getAllAudio()
           setAudioRecordList(audioData)
        })()
        
    }, [])

    const renderItem = useCallback(({item, index}: {item: AudioData, index: number})=>{
          return (
               <AudioItem item={item}/>
          )
    }, [])

    
  return (
     <SafeAreaView style={styles.container}>
          <View style={styles.body}>
                <View>
                    <Text style={styles.headerText}>Recordings</Text>
                    <View style={styles.txtInputCont}>
                        <Search name='search' size={15} color={theme.colors.grayDark} style={{marginRight: 12}}/>
                        <TextInput
                        defaultValue={text}
                        onChangeText={setText}
                     selectionColor={"#84D4CD"}
          cursorColor={"#6E19EE"}
         selectionHandleColor={"#6E19EE"}
                      placeholder='Search audio record'
                      placeholderTextColor={"gray"}
                      style={[styles.txtInput]}
                    />
                    <Clear name='close-circle-sharp' size={28} color={"#5E5E5E"} style={{display: text.length >= 1? "flex": "none"}} />
                    </View>
                    
                </View>
                <FlatList
                showsVerticalScrollIndicator={false}
                contentContainerStyle={{paddingTop: 20}}
                  data={audioRecordList}
                  renderItem={renderItem}
                />
          </View>
     </SafeAreaView>
  )
}

const styles = StyleSheet.create({
    container: {
    flex: 1,
    backgroundColor: "#FFFFFF",
    padding: 20
    },
    body: {
        flex: 1
    },
    headerText: {
        fontSize: 40,
        marginBottom: 40,
        paddingTop: 30
    },
    txtInputCont: {
       
        width: "100%",
        height: 50,
        borderRadius: 50,
        backgroundColor: "rgb(236, 236, 236)",
        flexDirection: "row",
        alignItems: "center",
        paddingHorizontal: 10
    },
    txtInput: {
        fontSize: 16,
        flex: 1, 
        marginRight: "auto",

    },
    playCont: {
        width: 35,
        height: 35,
        marginRight: 10,
        borderRadius: 35,
          backgroundColor: "rgb(236, 236, 236)",
          justifyContent:"center",
          alignItems:"center",
       

    },
    listItem: {
        marginBottom: 15,
        padding: 10,
        flexDirection: "row",
        alignItems: "center",
        overflow: "visible"

    },
    filenameText: {
        fontSize: 17,
        fontWeight: "500",
        width: "90%"
    },
    audioStats: {
        flexDirection: "row",
        alignItems: "center"
    },
    statsText: {
        marginRight: 5,
        color: "#4C4C4C",
        fontSize: 12.5
    }

   

})