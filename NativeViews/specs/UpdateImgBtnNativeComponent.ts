import { codegenNativeComponent, CodegenTypes, HostComponent, ViewProps } from "react-native";
import { Int32 } from "react-native/Libraries/Types/CodegenTypes";

type PressEvent = {
    isPressed: boolean
}

export interface NativeProps extends ViewProps {
    width?: Int32;
    height?: Int32;
    onPress?: CodegenTypes.DirectEventHandler<PressEvent>
}

export default codegenNativeComponent<NativeProps>(
  'UpdateTask',
) as HostComponent<NativeProps>;