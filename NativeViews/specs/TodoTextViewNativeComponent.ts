import { codegenNativeComponent, HostComponent, ViewProps } from "react-native";
import { Int32 } from "react-native/Libraries/Types/CodegenTypes";


export interface NativeProps extends ViewProps {
   fontSize?: Int32,
   color?: string,
   fontStyle?: string
}

export default codegenNativeComponent<NativeProps>(
  'TodoTextView',
) as HostComponent<NativeProps>;