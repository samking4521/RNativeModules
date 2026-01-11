import { StyleSheet, Text, View } from "react-native";

const intl = new Intl.NumberFormat("en-US", {
  style: "currency",
  currency: "USD"
});

type theProps = {
    quantity: number,
    isPending: boolean
}

export default function Total({quantity, isPending}: theProps) {
  return (
    <View style={styles.cont}>
      <Text style={styles.text}>Total:</Text>
      <Text style={styles.text}>
        {isPending ? "🌀 Updating..." : `${intl.format(quantity * 9999)}`}
      </Text>
    </View>
  )
}

const styles = StyleSheet.create({
    cont: {
        flexDirection:"row",
        alignItems: "center",
        justifyContent: "space-between",
        paddingHorizontal: 10
    },
    text: {
        fontSize: 16
    }
})
