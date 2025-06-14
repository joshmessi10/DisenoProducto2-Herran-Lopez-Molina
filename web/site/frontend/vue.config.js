const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/': {
        target: 'http://35.188.126.68', // Your Node.js backend server
        changeOrigin: true,
        secure: false, // Set this to true if you're using HTTPS
      },
    },
  },
});

