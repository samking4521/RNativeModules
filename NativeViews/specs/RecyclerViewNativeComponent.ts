import { codegenNativeComponent, HostComponent, ViewProps } from "react-native";

export interface Item {
  text: string;
  uri: string;
}

export interface NativeProps extends ViewProps {
   items: Item[] | null;
}

export default codegenNativeComponent<NativeProps>(
  'RecyclerView',
) as HostComponent<NativeProps>;