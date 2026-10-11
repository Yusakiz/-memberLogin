<script setup>
import { ref, onMounted } from 'vue'
import { getMe } from './api'
import LoginPage from './components/LoginPage.vue'
import MemberPage from './components/MemberPage.vue'

// 1. 存放當前登入者資訊 (null 代表尚未登入)
const member = ref(null)

// 2. 記錄是否正在初始檢查登入中 (避免一進網頁閃爍)
const loading = ref(true)

// 網頁一打開時 (onMounted)，自動檢查瀏覽器保險箱有沒有 Token！
onMounted(checkLogin)

async function checkLogin() {
  const token = localStorage.getItem('token')
  if (!token) {
    member.value = null
    loading.value = false
    return
  }

  try {
    // 拿著保險箱裡的 Token，向後端確認「我是誰」
    member.value = await getMe()
  } catch (err) {
    // 若 Token 過期或失效，恢復未登入狀態
    member.value = null
  } finally {
    loading.value = false
  }
}

// 處理登出
function handleLoggedOut() {
  member.value = null
}
</script>

<template>
  <main>
    <!-- 檢查中提示 -->
    <div v-if="loading" style="text-align: center; margin-top: 100px; color: #64748b;">
      系統載入中...
    </div>

    <!-- 門鈴接線處！
         1. 當未登入 (!member) 時，展示 LoginPage。
         2. 當 LoginPage 按下 emit('logged-in') 時，觸發 @logged-in="checkLogin"！
    -->
    <LoginPage
      v-else-if="!member"
      @logged-in="checkLogin"
    />

    <!-- 3. 已登入時，展示 MemberPage 會員中心 -->
    <MemberPage
      v-else
      :member="member"
      @member-updated="member = $event"
      @logged-out="handleLoggedOut"
    />
  </main>
</template>
