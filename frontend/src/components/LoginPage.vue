<script setup>
import { ref, reactive } from 'vue'
import { ApiError, login, register } from '../api'

// 定義向父層 (App.vue) 發出事件的通知器
const emit = defineEmits(['logged-in'])

// 1. 響應式變數 (Reactive State)
// 當前模式：'login' (登入) 或 'register' (註冊)
const mode = ref('login')

// 表單輸入內容 (使用 reactive 包裝物件)
const form = reactive({
  username: '',
  password: '',
  fullName: ''
})

const error = ref('')
const success = ref('')
const submitting = ref(false)

// 切換頁籤 (登入 / 註冊)
function switchMode(nextMode) {
  mode.value = nextMode
  form.password = ''
  error.value = ''
  success.value = ''
}

// 送出表單 (登入或註冊)
async function handleSubmit() {
  error.value = ''
  success.value = ''
  submitting.value = true

  try {
    if (mode.value === 'login') {
      // 呼叫 api.js 的 login 方法 (條件 2)
      await login(form.username.trim(), form.password)
      // 登入成功！發送通知給 App.vue，讓它切換到會員首頁！
      emit('logged-in')
    } else {
      // 呼叫 api.js 的 register 方法 (條件 1)
      await register({
        username: form.username.trim(),
        password: form.password,
        fullName: form.fullName.trim()
      })
      // 註冊成功後，自動切換到登入頁籤，並顯示綠色提示
      switchMode('login')
      success.value = '註冊成功！請直接輸入密碼進行登入'
    }
  } catch (err) {
    // 捕捉後端丟回來的錯誤訊息 (例如「帳號或密碼錯誤」或「此帳號已被使用」)
    error.value = err instanceof ApiError ? err.message : '連線伺服器失敗，請稍後再試'
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="login-box">
    <h1 style="text-align: center; margin-top: 0; margin-bottom: 20px;">👤 簡易會員平台</h1>

    <!-- 頁籤切換按鈕 (登入 / 註冊) -->
    <div class="tabs">
      <button
        type="button"
        :class="{ secondary: mode !== 'login' }"
        @click="switchMode('login')"
      >
        登入
      </button>
      <button
        type="button"
        :class="{ secondary: mode !== 'register' }"
        @click="switchMode('register')"
      >
        註冊
      </button>
    </div>

    <!-- 成功與錯誤提示文字 -->
    <div v-if="success" class="success">{{ success }}</div>
    <div v-if="error" class="error">{{ error }}</div>

    <!-- 表單區塊 -->
    <form @submit.prevent="handleSubmit">
      <label>
        會員帳號
        <input
          v-model="form.username"
          type="text"
          placeholder="請輸入會員帳號"
          required
        />
      </label>

      <!-- 只有切換到「註冊」模式時，才會顯示姓名欄位 (v-if) -->
      <label v-if="mode === 'register'">
        會員真實姓名
        <input
          v-model="form.fullName"
          type="text"
          placeholder="請輸入您的真實姓名"
          required
        />
      </label>

      <label>
        會員密碼
        <input
          v-model="form.password"
          type="password"
          placeholder="請輸入密碼"
          required
        />
      </label>

      <button type="submit" :disabled="submitting">
        {{ submitting ? '處理中...' : (mode === 'login' ? '立即登入' : '確認註冊') }}
      </button>
    </form>

    <!-- 方便面試測試的提示文字 -->
    <div style="margin-top: 20px; font-size: 0.82rem; color: #64748b; background: #f8fafc; padding: 10px; border-radius: 6px; border: 1px dashed #cbd5e1;">
      💡 <strong>預設測試帳號：</strong><br />
      帳號：<code>test01</code> / 密碼：<code>123456</code><br />
      帳號：<code>alice</code> / 密碼：<code>123456</code>
    </div>
  </div>
</template>
