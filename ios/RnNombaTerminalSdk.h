
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNRnNombaTerminalSdkSpec.h"

@interface RnNombaTerminalSdk : NSObject <NativeRnNombaTerminalSdkSpec>
#else
#import <React/RCTBridgeModule.h>

@interface RnNombaTerminalSdk : NSObject <RCTBridgeModule>
#endif

@end
