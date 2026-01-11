import React, { useTransition } from 'react';
import { Button, Pressable, Text } from 'react-native';

type theProps = React.PropsWithChildren<{
  action: () => void,
  isActive: boolean
}>

export default function TabButton({ action, children, isActive }: theProps) {
  const [isPending, startTransition] = useTransition();

  if(isPending){
    return <Text>Loading...</Text>
  }
  
  return (
    <Pressable style={{paddingHorizontal: 30, paddingVertical: 15, backgroundColor: isActive? "magenta" : "cyan"}} onPress={async () => {
      startTransition(async () => {
        // await the action that's passed in.
        // This allows it to be either sync or async. 
        await action();
      });
    }}>
      {children}
    </Pressable>
  );
}
