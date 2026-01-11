#include "NativeReverseStringModule.h"

namespace facebook::react {

NativeReverseStringModule::NativeReverseStringModule(std::shared_ptr<CallInvoker> jsInvoker)
    : NativeReverseStringModuleCxxSpec(std::move(jsInvoker)) {}

std::string NativeReverseStringModule::reverseString(jsi::Runtime& rt, std::string input) {
  return std::string(input.rbegin(), input.rend());
}

} // namespace facebook::react