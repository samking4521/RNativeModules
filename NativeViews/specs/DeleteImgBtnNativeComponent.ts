import { codegenNativeComponent, HostComponent, ViewProps } from "react-native";
import { Int32 } from "react-native/Libraries/Types/CodegenTypes";


export interface NativeProps extends ViewProps {
  width?: Int32;
  height?: Int32;
}

export default codegenNativeComponent<NativeProps>(
  'DeleteTask',
) as HostComponent<NativeProps>;