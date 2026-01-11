import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

interface BatteryEvent {
  level: number;        
  state: 'unknown' | 'unplugged' | 'charging' | 'full';
  isLowPowerMode?: boolean;
}

export interface Spec extends TurboModule {
   getBatteryLevel(): Promise<number>;
   getBatteryState(): Promise<"UNKNOWN" | "UNPLUGGED" | "CHARGING" | "FULL" >;
   isLowPowerModeEnabled(): boolean;
   isBatteryInfoAvailable(): Promise<boolean>;
   isBatteryOptimizationEnabled(): Promise<boolean>;
   useBatteryEventListener(event: BatteryEvent): ()=> void;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'NativeBatteryLevel.ts',
);