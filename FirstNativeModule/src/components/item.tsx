import { useState } from "react";
import { StyleSheet, Text, TextInput, View } from "react-native";

type props = {
    action: (newQuantity: string)=> void
}

export default function Item({ action }: props) {
   const [value, setValue] = useState("1")

  async function handleChange(val: string) {
    // To expose an action prop, await the callback in startTransition.
     setValue(val)
      await action(val);
     
        
   

   
    
  }

  return (
    <View style={styles.cont}>
      <Text style={styles.text}>Eras Tour Tickets</Text>
      <View style={styles.viewCont}>
 <Text style={styles.textVal}>Quantity: </Text>
      <TextInput
        defaultValue={value}
        keyboardType="number-pad"
        onChangeText={handleChange}
       
        style={styles.txtInput}
      />
      </View>
     
    </View>
  );
}

const styles = StyleSheet.create({
  cont: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingHorizontal: 10
  },
  viewCont: {
 flexDirection: "row",
    alignItems: "center",
  },
  text: {
    fontSize: 18,
    fontWeight: "bold",
  },
  textVal: {
    fontSize: 16,
    fontWeight: "bold",
  },
  txtInput: {
    width: 70,
    height: 40,
    borderWidth: 1
  },
});
