const fs = require("fs")
const TerserPlugin = require('terser-webpack-plugin');

module.exports = {
  publicPath: "/",
  outputDir: "../src/main/resources/static",
  indexPath: "index.html",
  configureWebpack: {
    devServer: {
      disableHostCheck: true,
      proxy: {
        '^/scheduler': {
          target: 'http://localhost:8080',
          changeOrigin: true
        },
        '^/attestation': {
          target: 'http://localhost:8080',
          changeOrigin: true
        },
      }
    }
  }
}