//
//  RCTNativeBatteryLevel.m
//  Battery
//
//  Created by Abidoye Samuel on 05/12/2025.
//

#import "RCTNativeBatteryLevel.h"

@implementation RCTNativeBatteryLevel

+ (NSString *)moduleName { 
  return @"NativeBatteryLevel";
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params { 
  return std::make_shared<facebook::react::NativeBatteryLevelSpecJSI>(params);
}

- (void)getBatteryLevel:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject { 
  float batteryLevel = [UIDevice currentDevice].batteryLevel;
  if(batteryLevel < 0.0){
    reject(@"E_BATTERY_LEVEL_UNAVAILABLE", @"Battery level error", nil);
  }else {
    resolve(@(batteryLevel * 100));
  }
}

- (void)getBatteryState:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  if([UIDevice currentDevice].batteryState == UIDeviceBatteryStateUnknown){
    resolve(@"UNKNOWN");
  }else if([UIDevice currentDevice].batteryState == UIDeviceBatteryStateFull){
    resolve(@"FULL");
  }else if([UIDevice currentDevice].batteryState == UIDeviceBatteryStateCharging){
    resolve(@"CHARGING");
  }else if([UIDevice currentDevice].batteryState == UIDeviceBatteryStateUnplugged){
    resolve(@"UNPLUGGED");
  }else{
    reject(@"E_BATTERY_STATE_UNAVAILABLE", @"Battery state is not available on this device", nil);
  }
}

- (void)isBatteryInfoAvailable:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject { 
  // Enable battery monitoring
      [UIDevice currentDevice].batteryMonitoringEnabled = YES;
  BOOL isInfoAvailable = [UIDevice currentDevice].batteryLevel >= 0.0 &&
  [UIDevice currentDevice].batteryState != UIDeviceBatteryStateUnknown;
    
  if(isInfoAvailable){
    resolve(@(YES)); // wrap BOOL in NSNumber
  }else{
    reject(@"E_BATTERY_UNAVAILABLE", @"Battery info not available on this device", nil);
  }
}

- (void)isLowPowerModeEnabled:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  
  BOOL isLowPowerModeEnabled = [[NSProcessInfo processInfo] isLowPowerModeEnabled];
  if(isLowPowerModeEnabled){
    resolve(@(YES));
  }else{
    resolve(@(NO));
  }
}

- (void)isBatteryOptimizationEnabled:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject { 
  resolve(nil);
}

- (void) batteryStatChange: (NSNotification *)notification{
  UIDevice *device = [UIDevice currentDevice];
  float level = device.batteryLevel * 100;  // 0.0 → 1.0
  NSNumber *batteryLevel = @(level);
  UIDeviceBatteryState state = device.batteryState;
  BOOL isLowPowerModeEnabled = [[NSProcessInfo processInfo] isLowPowerModeEnabled];
  switch (state) {
          case UIDeviceBatteryStateCharging:
      [self emitOnBatteryEvent:@{@"level": batteryLevel, @"state": @"CHARGING", @"isLowPowerMode": @(isLowPowerModeEnabled)}];
              break;
          case UIDeviceBatteryStateFull:
      [self emitOnBatteryEvent:@{@"level": batteryLevel, @"state": @"FULL", @"isLowPowerMode": @(isLowPowerModeEnabled)}];
              break;
          case UIDeviceBatteryStateUnplugged:
      [self emitOnBatteryEvent:@{@"level": batteryLevel, @"state": @"UNPLUGGED", @"isLowPowerMode": @(isLowPowerModeEnabled)}];
              break;
          default:
      [self emitOnBatteryEvent:@{@"level": batteryLevel, @"state": @"UNKNOWN", @"isLowPowerMode": @(isLowPowerModeEnabled)}];
      }
}

- (void)addBatteryListener:(BOOL)value { 
  if(value){
    [[NSNotificationCenter defaultCenter] addObserver:self
                                                selector:@selector(batteryStatChange:)
                                                    name:UIDeviceBatteryLevelDidChangeNotification
                                                  object:nil];
    
    // Listen for battery state changes (charging, unplugged, full)
       [[NSNotificationCenter defaultCenter]
           addObserver:self
           selector:@selector(batteryStatChange:)
           name:UIDeviceBatteryStateDidChangeNotification
           object:nil];
    
    [[NSNotificationCenter defaultCenter]
           addObserver:self
           selector:@selector(batteryStatChange:)
           name:NSProcessInfoPowerStateDidChangeNotification
           object:nil];
  }else{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
  }
}

@end
