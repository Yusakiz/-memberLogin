<template>
  <div class="auth-container">
    <div class="card">
      <h2 class="card-title">會員登入</h2>
      <p class="card-subtitle">請輸入您的帳號與密碼進入平台</p>

      <div v-if="errorMessage" class="alert alert-error">
        {{ errorMessage }}
      </div>

      <div v-if="successMessage" class="alert alert-success">
        {{ successMessage }}
      </div>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">會員帳號</label>
          <input
            id="username"
            v-model="form.username"
            type="text"
            placeholder="請輸入會員帳號"
            required
            autocomplete="username"
          />
        </div>

        <div class="form-group">
          <label for="password">會員密碼</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            placeholder="請輸入密碼"
            required
            autocomplete="current-password"
          />
        </div>

        <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
          {{ loading ? '登入中...' : '立即登入' }}
        </button>
      </form>

      <div style="margin-top: 1.5rem; text-align: center; font-size: 0.9rem; color: var(--text-muted);">
        還沒有帳號嗎？
        <router-link to="/register" style="color: var(--primary); font-weight: 600; text-decoration: none;">
          註冊新帳號
        </router-link>
      </div>

      <!-- 面試快速測試提示 -->
      <div style="margin-top: 1.5rem; padding: 0.75rem; background: #f8fafc; border-radius: 6px; border: 1px dashed #cbd5e1; font-size: 0.82rem; color: #64748b;">
        <strong>💡 測試帳號提示：</strong><br />
        帳號：<code>test01</code> / 密碼：<code>123456</code><br />
        帳號：<code>alice</code> / 密碼：<code>123456</code>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { authApi } from '../api'

const router = useRouter()
const route = useRoute()
const emit = defineEmits(['auth-change'])

const form = ref({
  username: '',
  password: ''
})

const loading = ref(false)
const errorMessage = ref('')
const successMessage = ref(route.query.registered ? '註冊成功！請直接登入' : '')

const handleLogin = async () => {
  errorMessage.value = ''
  loading.value = true

  try {
    const res = await authApi.login({
      username: form.value.username,
      password: form.value.password
    })

    if (res.success) {
      // 儲存 Token 與基本資訊至 localStorage
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('member', JSON.stringify(res.data.member))

      emit('auth-change')

      // 登入成功後，進入平台的歡迎頁 (條件 2)
      router.push('/home')
    } else {
      errorMessage.value = res.message || '登入失敗'
    }
  } catch (err) {
    errorMessage.value = err.message || '登入發生錯誤，請確認帳號密碼'
  } finally {
    loading.value = false
  }
}
</script>
