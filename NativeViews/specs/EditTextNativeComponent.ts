import { codegenNativeComponent, CodegenTypes, HostComponent, ViewProps } from "react-native";

export type textEvent = {
   text: string;
}
export interface NativeProps extends ViewProps {
   onChangeText?: CodegenTypes.DirectEventHandler<textEvent> | null
}

export default codegenNativeComponent<NativeProps>(
  'EditText',
) as HostComponent<NativeProps>;