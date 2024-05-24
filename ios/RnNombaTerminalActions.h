
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNRnNombaTerminalActionsSpec.h"

@interface RnNombaTerminalActions : NSObject <NativeRnNombaTerminalActionsSpec>
#else
#import <React/RCTBridgeModule.h>

@interface RnNombaTerminalActions : NSObject <RCTBridgeModule>
#endif

@end
