import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react';
import svgr from 'vite-plugin-svgr';

// // https://vite.dev/config/
// export default defineConfig({
//   plugins: [react()],
// })

// import { defineConfig } from 'vite';
// import react from '@vitejs/plugin-react';


import path from 'path';

export default defineConfig({
  plugins: [react(), svgr()],
  resolve: {
    alias: {
      react: path.resolve(__dirname, 'node_modules/react'),
      'react-dom': path.resolve(__dirname, 'node_modules/react-dom'),
    },
  },
});

//?? может пригодиться

// export default {
//   server: {
//     proxy: {
//       '/api': {
//         target: 'http://127.0.0.1:4040',
//         changeOrigin: true,
//         rewrite: (path) => path.replace(/^\/api/, '/v1'),
//       },
//     },
//   },
// };