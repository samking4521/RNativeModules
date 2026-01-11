import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

type PrimeProps = {
    language: string;
    value: number;
    time: number;
}

export interface Spec extends TurboModule {
   getPrimeNumbers(value: number): Promise<PrimeProps>;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'NativePrimeFunction',
);