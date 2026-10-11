<template>
  <div id="app">
    <nav class="navbar">
      <div class="nav-brand">
        <span>👤 會員管理平台</span>
      </div>
      <div class="nav-links">
        <template v-if="isLoggedIn">
          <router-link to="/home" class="nav-link">🏠 歡迎首頁</router-link>
          <router-link to="/search" class="nav-link">🔍 查詢會員</router-link>
          <router-link to="/profile" class="nav-link">⚙️ 個人設定</router-link>
          <button @click="handleLogout" class="btn btn-outline btn-sm">🚪 登出</button>
        </template>
        <template v-else>
          <router-link to="/login" class="nav-link">登入</router-link>
          <router-link to="/register" class="nav-link">註冊</router-link>
        </template>
      </div>
    </nav>

    <main>
      <router-view @auth-change="checkAuth" />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isLoggedIn = ref(false)

const checkAuth = () => {
  isLoggedIn.value = !!localStorage.getItem('token')
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('member')
  isLoggedIn.value = false
  router.push('/login')
}

onMounted(() => {
  checkAuth()
})
</script>
