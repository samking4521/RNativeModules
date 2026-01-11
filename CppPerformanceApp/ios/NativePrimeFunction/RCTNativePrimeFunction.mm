//
//  RCTNativePrimeFunction.m
//  CppPerformanceApp
//
//  Created by Abidoye Samuel on 17/12/2025.
//

#import "RCTNativePrimeFunction.h"

@implementation RCTNativePrimeFunction

+ (NSString *)moduleName { 
  return @"NativePrimeFunction";
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params { 
  return std::make_shared<facebook::react::NativePrimeFunctionSpecJSI>(params);
}

int countPrimes(int n) {
    if (n < 2) return 0;

    std::vector<bool> isPrime(n + 1, true);
    isPrime[0] = false;
    isPrime[1] = false;

    int limit = static_cast<int>(std::sqrt(n));
    for (int i = 2; i <= limit; ++i) {
        if (isPrime[i]) {
            for (int j = i * i; j <= n; j += i) {
                isPrime[j] = false;
            }
        }
    }

    int count = 0;
    for (int i = 2; i <= n; ++i) {
        if (isPrime[i]) count++;
    }

    return count;
}


- (void)getPrimeNumbers:(double)value resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject { 
  try {
    NSDate *start = [NSDate date];
    int primeCount = countPrimes(value);
    NSTimeInterval duration = [[NSDate date] timeIntervalSinceDate:start];
    resolve(@{
                   @"language": @("Obj-C"),
                   @"value": @(primeCount),
                   @"time": @(duration * 1000),
               });
  } catch (NSException *exception) {
    reject(@"prime_error", @"Failed to compute primes", nil);
  }
}

@end
