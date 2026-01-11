import { codegenNativeComponent, HostComponent, ViewProps } from "react-native";

export interface NativeProps extends ViewProps {

}

export default codegenNativeComponent<NativeProps>(
  'TextInputLayout',
) as HostComponent<NativeProps>;