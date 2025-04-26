import { createRouter, createWebHistory } from 'vue-router'
import system from '../views/system.vue'
import LoginPage from '../views/LoginPage.vue'
import PersonalCenter from '../views/PersonalCenter.vue' 
import GameRoom from '../views/GameRoom.vue'
import Matching from '../views/Matching.vue'
import History from '../views/History.vue'
const routes = [
  
  { path: '/system', name: 'system', component: system }, 
  { path:'/login',  name:'login', component:LoginPage},
  {path:'/personal-center',name:'personal-center',component:PersonalCenter},
  {path:'/matching',name:'matching',component:Matching},
  {path: '/game-room',name: 'game-room',component: GameRoom,meta: { requiresAuth: true }},
  {path:'/history',name:'history',component:History},
  {
    path: '/',
    redirect: '/login', // 默认重定向到登录页面
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true'; // 检查用户是否已登录

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next('/login'); // 如果需要认证但未登录，重定向到登录页面
    } else {
      next(); // 已登录，继续导航
    }
  } else {
    next(); // 不需要认证，继续导航
  }
});

export default router