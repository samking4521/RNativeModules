import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  getPrimeNumbers(
    value: number,
  ): Promise<{ language: string; value: number; time: number }>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('NativePrimeFunction');
