const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');
const { wrapWithReanimatedMetroConfig } = require('react-native-reanimated/metro-config');

/**
 * Metro configuration
 * https://reactnative.dev/docs/metro
 *
 * @type {import('@react-native/metro-config').MetroConfig}
 */

const defaultConfig = getDefaultConfig(__dirname);

// You can extend config here if needed
const mergedConfig = mergeConfig(defaultConfig, {
  // custom overrides if needed
});

module.exports = wrapWithReanimatedMetroConfig(mergedConfig);
