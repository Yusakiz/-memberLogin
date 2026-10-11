import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import HomeView from '../views/HomeView.vue'
import ProfileView from '../views/ProfileView.vue'
import SearchView from '../views/SearchView.vue'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView,
    meta: { title: '會員登入' }
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterView,
    meta: { title: '會員註冊' }
  },
  {
    path: '/home',
    name: 'Home',
    component: HomeView,
    meta: { requiresAuth: true, title: '歡迎頁' }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: ProfileView,
    meta: { requiresAuth: true, title: '個人資料修改' }
  },
  {
    path: '/search',
    name: 'Search',
    component: SearchView,
    meta: { requiresAuth: true, title: '會員資料查詢' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守衛：未登入時攔截並轉跳至登入頁
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if ((to.name === 'Login' || to.name === 'Register') && token) {
    next({ name: 'Home' })
  } else {
    next()
  }
})

export default router
