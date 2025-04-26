import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import { createPinia } from 'pinia'
import { useUserStore } from './stores/user'

// 创建 Axios 实例（带默认配置）
const http = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
    headers: {
      'Content-Type': 'application/json',
    },
    timeout: 10000, // 10秒超时
  })
  
  // 请求拦截器（可选）
  http.interceptors.request.use((config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  })
  
  // 响应拦截器（可选）
  http.interceptors.response.use(
    (response) => response.data, // 直接返回data字段
    (error) => {
      console.error('API Error:', error)
      return Promise.reject(error)
    }
  )
  
  router.beforeEach((to, from, next) => {
    // 确保没有阻止跳转的逻辑
    next();
  });
  
  const app = createApp(App)


  
  // 挂载到全局
  app.config.globalProperties.$http = http;
  
  // 类型声明
  declare module '@vue/runtime-core' {
    interface ComponentCustomProperties {
      $http: typeof http;
      $socket: WebSocket;
    }
  }
  
  app.use(createPinia());
  const userStore = useUserStore();
  userStore.initFromLocalStorage();
  
  app.use(router);
  app.mount('#app');
  
