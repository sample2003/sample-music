const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    port: 8088,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 代理目标地址
        changeOrigin: true, // 允许跨域
      }
    }
  },
})
