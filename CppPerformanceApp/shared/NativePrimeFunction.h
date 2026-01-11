#pragma once

#include <NativePrimeFunctionSpecJSI.h>
#include <jsi/jsi.h>

#include <memory>

namespace facebook::react {

class NativePrimeFunction
    : public NativePrimeFunctionCxxSpec<NativePrimeFunction> {
public:
  explicit NativePrimeFunction(std::shared_ptr<CallInvoker> jsInvoker);

  // Promise<PrimeProps> → jsi::Value
  facebook::jsi::Value getPrimeNumbers(
      facebook::jsi::Runtime& rt,
      double value);
};

} // namespace facebook::react
