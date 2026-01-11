import type { CodegenTypes, ViewProps, HostComponent } from 'react-native';
import { codegenNativeComponent } from 'react-native';
import { Float, Int32 } from 'react-native/Libraries/Types/CodegenTypes';

export type PressEvent = {
    x: Int32,
    y: Int32,
    z: Int32
}

export type LongPressEvent = {
   isClicked: boolean
}

export interface NativeProps extends ViewProps {
  customText?: string;
  fontSize?: Float;
  fontWeight?: string;
  textAlign?: string;
  color?: string;
  onPress?: CodegenTypes.DirectEventHandler<PressEvent>;
  onLongPress?: CodegenTypes.BubblingEventHandler<LongPressEvent>;
}

export default codegenNativeComponent<NativeProps>('MyTextView') as HostComponent<NativeProps>