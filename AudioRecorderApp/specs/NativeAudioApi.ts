import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';
import { AudioData } from '../Constants/DbAudio';

export interface Spec extends TurboModule {
    startRecording(): void;
    stopRecording(): string;
    resumeRecording(): void;
    pauseRecording(): void;
    deleteRecording(): void;
    saveRecording(path: string): void
    addListener(eventName: string): void
    removeListeners(count: number): void
    insertAudio(filename: string, duration: number, createdAt: number): Promise<void>
    deleteAudio(filename: string, filepath: number, duration: number, createdAt: number): Promise<void>
    getAllAudio(): Promise<AudioData[]>;
    deleteAllAudio(): Promise<void>;
    playAudio(path: string): Promise<void>;
    pauseAudio(): Promise<void>;
    stopAudio(): Promise<void>;
    seekTo(position: number, type: string): Promise<void>;
    playbackSpeed(speed: string): void
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'NativeAudioApi',
);