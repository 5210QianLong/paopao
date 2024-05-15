import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// 会自动导入对应的 Vant API 以及样式。
import AutoImport from 'unplugin-auto-import/vite';
// unplugin-vue-components 会解析模板并自动注册对应的组件, @vant/auto-import-resolver 会自动引入对应的组件样式。
import Components from 'unplugin-vue-components/vite';
import { VantResolver } from '@vant/auto-import-resolver';

// https://vitejs.dev/config/
export default defineConfig({
  // server:{
  //   proxy:{
  //     '/a':{
  //       target:'http://localhost:8080',
  //       changeOrigin: true, // 是否改变源地址
  //       rewrite: (path) => path.replace(/^\/a/, '') // 重写路径
  //     }
  //   }
  // },
  plugins: [
    vue(),
    AutoImport({
      resolvers: [VantResolver()],
    }),
    Components({
      resolvers: [VantResolver()],
    }),
  ],

});

