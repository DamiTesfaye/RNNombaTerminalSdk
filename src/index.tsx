import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'rn-nomba-terminal-actions' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const RnNombaTerminalActions = NativeModules.RnNombaTerminalActions
  ? NativeModules.RnNombaTerminalActions
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function handleTerminalRequest(args: Array<string>): Promise<any> {
  return RnNombaTerminalActions.handleTerminalRequest(args);
}
