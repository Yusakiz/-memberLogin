import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    // 設定反向代理 (Proxy)：
    // 只要前端呼叫以 /api 開頭的網址，Vite 就自動幫忙轉發給後端 8080！
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
