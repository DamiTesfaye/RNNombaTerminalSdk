const {
  withAndroidManifest,
  createRunOncePlugin,
} = require('@expo/config-plugins');

const pkg = { name: 'rn-nomba-terminal-sdk', version: '0.1.3' };

function withNombaTerminal(config) {
  config = withAndroidManifest(config, (cfg) => {
    const manifest = cfg.modResults;
    const ensurePermission = (name) => {
      const arr = (manifest.manifest['uses-permission'] ||= []);
      if (!arr.some((p) => p.$['android:name'] === name)) {
        arr.push({ $: { 'android:name': name } });
      }
    };
    ensurePermission('android.permission.INTERNET');
    return cfg;
  });

  return config;
}

module.exports = createRunOncePlugin(withNombaTerminal, pkg.name, pkg.version);
