import React, { useState, useTransition } from 'react';
import { SafeAreaView, TextInput, FlatList, Text, View, StyleSheet, ActivityIndicator } from 'react-native';

const DATA = Array.from({ length: 5000 }, (_, i) => `Item ${i + 1}`);

export default function App() {
  const [input, setInput] = useState('');
  const [filteredData, setFilteredData] = useState(DATA);
  const [isPending, startTransition] = useTransition();

  const handleSearch = (text: string) => {
    setInput(text);

    startTransition(() => {
      const lower = text.toLowerCase();
      const results = DATA.filter((item) => item.toLowerCase().includes(lower));
      setFilteredData(results);
    });
  };

  return (
    <SafeAreaView style={styles.container}>
      <TextInput
        placeholder="Search..."
        style={styles.input}
        value={input}
        onChangeText={handleSearch}
      />

      {isPending && (
        <View style={styles.loading}>
          <ActivityIndicator size="small" />
          <Text style={styles.loadingText}>Updating...</Text>
        </View>
      )}

      <FlatList
        data={filteredData}
        keyExtractor={(item, index) => `${item}-${index}`}
        renderItem={({ item }) => <Text style={styles.item}>{item}</Text>}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, paddingTop: 50, paddingHorizontal: 20 },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    padding: 12,
    borderRadius: 8,
    marginBottom: 12,
  },
  item: { fontSize: 16, paddingVertical: 4 },
  loading: { flexDirection: 'row', alignItems: 'center', marginBottom: 10 },
  loadingText: { marginLeft: 8 },
});
