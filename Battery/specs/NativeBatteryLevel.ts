import type {TurboModule, CodegenTypes} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

interface BatteryEvent {
  level: number;        
  state: 'UNKNOWN' | 'UNPLUGGED' | 'CHARGING' | 'FULL';
  isLowPowerMode?: boolean;
}

export interface Spec extends TurboModule {
   getBatteryLevel(): Promise<number>;
   getBatteryState(): Promise<"UNKNOWN" | "UNPLUGGED" | "CHARGING" | "FULL" >;
   isLowPowerModeEnabled(): Promise<boolean>;
   isBatteryInfoAvailable(): Promise<boolean>;
   isBatteryOptimizationEnabled(): Promise<boolean | null>;
   addBatteryListener(value: boolean): void;
   readonly onBatteryEvent: CodegenTypes.EventEmitter<BatteryEvent>;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'NativeBatteryLevel',
);