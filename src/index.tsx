import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'rn-nomba-terminal-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const RnNombaTerminalSdk = NativeModules.RnNombaTerminalSdk
  ? NativeModules.RnNombaTerminalSdk
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function handleTerminalRequest(args: Array<string>): Promise<any> {
  return RnNombaTerminalSdk.handleTerminalRequest(args);
}
