//
//  NativeReverseStringModuleProvider.m
//  ReverseStringCpp
//
//  Created by Abidoye Samuel on 13/12/2025.
//


#import "NativeReverseStringModuleProvider.h"
#import <ReactCommon/CallInvoker.h>
#import <ReactCommon/TurboModule.h>
#import "NativeReverseStringModule.h"

@implementation NativeReverseStringModuleProvider

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<facebook::react::NativeReverseStringModule>(params.jsInvoker);
}

@end
