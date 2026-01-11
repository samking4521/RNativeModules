import { codegenNativeComponent, CodegenTypes, HostComponent, ImageSource, ViewProps } from "react-native";

type PressEvent = {
    isClicked: boolean
}

export interface NativeProps extends ViewProps {
   source?: string | null;
   onPressEvent?: CodegenTypes.DirectEventHandler<PressEvent>;
}

export default codegenNativeComponent<NativeProps>(
  'ImageViews',
) as HostComponent<NativeProps>;