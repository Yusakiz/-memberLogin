<template>
  <div class="auth-container">
    <div class="card">
      <h2 class="card-title">會員註冊</h2>
      <p class="card-subtitle">填寫以下基本資料以建立新會員帳號</p>

      <div v-if="errorMessage" class="alert alert-error">
        {{ errorMessage }}
      </div>

      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="username">會員帳號 <span style="color: var(--danger)">*</span></label>
          <input
            id="username"
            v-model="form.username"
            type="text"
            placeholder="請輸入 3~30 個字元之帳號"
            required
            autocomplete="username"
          />
        </div>

        <div class="form-group">
          <label for="password">會員密碼 <span style="color: var(--danger)">*</span></label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            placeholder="請輸入至少 4 碼密碼"
            required
            autocomplete="new-password"
          />
        </div>

        <div class="form-group">
          <label for="fullName">真實姓名 <span style="color: var(--danger)">*</span></label>
          <input
            id="fullName"
            v-model="form.fullName"
            type="text"
            placeholder="請輸入中文或英文姓名"
            required
          />
        </div>

        <div class="form-group">
          <label for="email">電子郵件</label>
          <input
            id="email"
            v-model="form.email"
            type="email"
            placeholder="example@mail.com"
          />
        </div>

        <div class="form-group">
          <label for="phone">連絡電話</label>
          <input
            id="phone"
            v-model="form.phone"
            type="tel"
            placeholder="例如: 0912-345-678"
          />
        </div>

        <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
          {{ loading ? '建立帳號中...' : '確認註冊' }}
        </button>
      </form>

      <div style="margin-top: 1.5rem; text-align: center; font-size: 0.9rem; color: var(--text-muted);">
        已經有帳號了？
        <router-link to="/login" style="color: var(--primary); font-weight: 600; text-decoration: none;">
          返回登入
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../api'

const router = useRouter()

const form = ref({
  username: '',
  password: '',
  fullName: '',
  email: '',
  phone: ''
})

const loading = ref(false)
const errorMessage = ref('')

const handleRegister = async () => {
  errorMessage.value = ''
  loading.value = true

  try {
    const res = await authApi.register(form.value)
    if (res.success) {
      // 註冊成功後導回登入頁
      router.push({ path: '/login', query: { registered: 'true' } })
    } else {
      errorMessage.value = res.message || '註冊失敗'
    }
  } catch (err) {
    errorMessage.value = err.message || '註冊發生錯誤'
  } finally {
    loading.value = false
  }
}
</script>
