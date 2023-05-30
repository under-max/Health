import { fileURLToPath, URL } from 'node:url';
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
const Dotenv = require('dotenv-webpack');
import  envCompatible  from 'vite-plugin-env-compatible';

export default defineConfig({
  plugins: [
    vue(),
    envCompatible({
      path: '.env',
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8090',
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
});