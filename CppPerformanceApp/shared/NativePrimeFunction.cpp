// #include "NativePrimeFunction.h"
// #include <iostream>
// #include <vector>
// #include <cmath>
// #include <jsi/jsi.h>
// #include <chrono>

// using namespace facebook::react;
// namespace jsi = facebook::jsi;

// /* =======================
//    Shared Logic
//    ======================= */

// int getPrimeNumbers(int n) {
//     if (n < 2) return 0;

//     std::vector<uint8_t> isPrime(n + 1, 1);
//     isPrime[0] = isPrime[1] = 0;

//     int limit = static_cast<int>(std::sqrt(n));

//     for (int i = 2; i <= limit; ++i) {
//         if (isPrime[i]) {
//             for (int j = i * i; j <= n; j += i) {
//                 isPrime[j] = 0;
//             }
//         }
//     }

//     int count = 0;
//     for (int i = 2; i <= n; ++i) {
//         count += isPrime[i];
//     }

//     return count;
// }


// /* =======================
//    TurboModule
//    ======================= */

// NativePrimeFunction::NativePrimeFunction(
//     std::shared_ptr<CallInvoker> jsInvoker)
//     : NativePrimeFunctionCxxSpec(std::move(jsInvoker)) {}

// jsi::Value NativePrimeFunction::getPrimeNumbers(
//     jsi::Runtime& rt,
//     double value) {

//   int n = static_cast<int>(value); // safe conversion

//   auto start = std::chrono::high_resolution_clock::now();

//   // Explicitly call the global function
//   int primeCount = ::getPrimeNumbers(n);

//   auto end = std::chrono::high_resolution_clock::now();
//   double durationMs =
//       std::chrono::duration<double, std::milli>(end - start).count();

//   jsi::Object result(rt);
//   result.setProperty(rt, "language", "C++");
//   result.setProperty(rt, "value", primeCount);
//   result.setProperty(rt, "time", durationMs);

//   return result;
// }

#include "NativePrimeFunction.h"
#include <jsi/jsi.h>
#include <chrono>
#include <thread>
#include <vector>
#include <memory>

using namespace facebook::react;
namespace jsi = facebook::jsi;

/* =======================
   Bit-packed Sieve
   ======================= */
int getPrimeNumbers(int n) {
    if (n < 2) return 0;

    // Each bit represents a number; 0 = non-prime, 1 = prime
    std::vector<uint8_t> sieve((n + 7) / 8, 0xFF);

    // 0 and 1 are not prime
    sieve[0] &= ~0x03;

    int limit = static_cast<int>(std::sqrt(n));

    auto isBitSet = [&](int index) {
        return (sieve[index / 8] >> (index % 8)) & 1;
    };

    auto clearBit = [&](int index) {
        sieve[index / 8] &= ~(1 << (index % 8));
    };

    for (int i = 2; i <= limit; ++i) {
        if (isBitSet(i)) {
            for (int j = i * i; j <= n; j += i) {
                clearBit(j);
            }
        }
    }

    int count = 0;
    for (int i = 2; i <= n; ++i) {
        if (isBitSet(i)) ++count;
    }

    return count;
}

/* =======================
   TurboModule
   ======================= */

NativePrimeFunction::NativePrimeFunction(
    std::shared_ptr<CallInvoker> jsInvoker)
    : NativePrimeFunctionCxxSpec(std::move(jsInvoker)) {}

jsi::Value NativePrimeFunction::getPrimeNumbers(
    jsi::Runtime& rt,
    double value) {

    int n = static_cast<int>(value);

    // Create JS Promise
    jsi::Function resolve, reject;
    jsi::Value promise = facebook::react::createPromise(rt, resolve, reject);

    auto jsInvoker = this->jsInvoker_;

    // Launch heavy computation on a background thread
    std::thread([n, resolve = std::move(resolve), reject = std::move(reject), jsInvoker]() mutable {

        try {
            auto start = std::chrono::high_resolution_clock::now();
            int primeCount = ::getPrimeNumbers(n);
            auto end = std::chrono::high_resolution_clock::now();

            double durationMs =
                std::chrono::duration<double, std::milli>(end - start).count();

            // Post result back to JS thread
            jsInvoker->invokeAsync([resolve = std::move(resolve), primeCount, durationMs]() mutable {
                jsi::Runtime& rt = resolve.getRuntime();

                jsi::Object result(rt);
                result.setProperty(rt, "language", "C++");
                result.setProperty(rt, "value", primeCount);
                result.setProperty(rt, "time", durationMs);

                resolve.call(rt, result);
            });

        } catch (const std::exception& e) {
            jsInvoker->invokeAsync([reject = std::move(reject), e]() mutable {
                jsi::Runtime& rt = reject.getRuntime();
                reject.call(rt, jsi::String::createFromUtf8(rt, e.what()));
            });
        }

    }).detach();

    return promise;
}

