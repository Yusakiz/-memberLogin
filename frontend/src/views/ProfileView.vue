<template>
  <div class="container">
    <div class="card" style="max-width: 600px; margin: 0 auto;">
      <h2 class="card-title">會員資料修改</h2>
      <p class="card-subtitle">您可以更新您的個人資訊或重設密碼</p>

      <div v-if="successMessage" class="alert alert-success">
        {{ successMessage }}
      </div>
      <div v-if="errorMessage" class="alert alert-error">
        {{ errorMessage }}
      </div>

      <form @submit.prevent="handleUpdate">
        <div class="form-group">
          <label>會員帳號</label>
          <input
            type="text"
            :value="member.username"
            disabled
            style="background-color: #f1f5f9; cursor: not-allowed; color: #64748b;"
          />
          <small style="color: var(--text-muted); font-size: 0.8rem;">帳號為系統唯一識別碼，不可變更</small>
        </div>

        <div class="form-group">
          <label for="fullName">真實姓名 <span style="color: var(--danger)">*</span></label>
          <input
            id="fullName"
            v-model="form.fullName"
            type="text"
            required
            placeholder="請輸入姓名"
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
            placeholder="0912-345-678"
          />
        </div>

        <div class="form-group">
          <label for="password">重設密碼 (若不修改請留空)</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            placeholder="若要更換密碼請輸入新密碼"
            autocomplete="new-password"
          />
        </div>

        <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
          {{ loading ? '儲存中...' : '💾 儲存修改' }}
        </button>
      </form>

      <!-- 危險操作區：刪除帳號 (條件 5 & 條件 6) -->
      <div style="margin-top: 3rem; padding-top: 2rem; border-top: 1px solid var(--border);">
        <h3 style="color: var(--danger); font-size: 1.1rem; margin-bottom: 0.5rem;">⚠️ 危險區域</h3>
        <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 1rem;">
          刪除您的會員帳號為不可逆的操作。刪除後您的所有資料將永久移除，且無法再次以此帳號登入。
        </p>

        <button @click="confirmDelete" class="btn btn-danger btn-sm" :disabled="deleteLoading">
          {{ deleteLoading ? '刪除中...' : '🗑️ 刪除我的會員帳號' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { memberApi } from '../api'

const router = useRouter()
const emit = defineEmits(['auth-change'])

const member = ref({})
const form = ref({
  fullName: '',
  email: '',
  phone: '',
  password: ''
})

const loading = ref(false)
const deleteLoading = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

const loadProfile = async () => {
  try {
    const res = await memberApi.getCurrentMember()
    if (res.success) {
      member.value = res.data
      form.value.fullName = res.data.fullName || ''
      form.value.email = res.data.email || ''
      form.value.phone = res.data.phone || ''
    }
  } catch (err) {
    errorMessage.value = err.message || '載入資料失敗'
  }
}

// 實作修改功能 (條件 3 & 條件 6)
const handleUpdate = async () => {
  errorMessage.value = ''
  successMessage.value = ''
  loading.value = true

  try {
    const updateData = {
      fullName: form.value.fullName,
      email: form.value.email,
      phone: form.value.phone
    }
    // 只有在填入新密碼時才傳送密碼欄位
    if (form.value.password.trim()) {
      updateData.password = form.value.password.trim()
    }

    const res = await memberApi.updateMember(member.value.id, updateData)
    if (res.success) {
      successMessage.value = '會員資料修改成功！'
      member.value = res.data
      localStorage.setItem('member', JSON.stringify(res.data))
      form.value.password = ''
    } else {
      errorMessage.value = res.message || '修改失敗'
    }
  } catch (err) {
    errorMessage.value = err.message || '修改失敗'
  } finally {
    loading.value = false
  }
}

// 實作刪除功能 (條件 5 & 條件 6)
const confirmDelete = async () => {
  const confirmed = window.confirm(
    `確定要永久刪除帳號「${member.value.username}」嗎？此操作無法還原！`
  )
  if (!confirmed) return

  deleteLoading.value = true
  errorMessage.value = ''

  try {
    const res = await memberApi.deleteMember(member.value.id)
    if (res.success) {
      alert('您的會員帳號已成功刪除。')
      localStorage.removeItem('token')
      localStorage.removeItem('member')
      emit('auth-change')
      router.push('/login')
    } else {
      errorMessage.value = res.message || '刪除失敗'
    }
  } catch (err) {
    errorMessage.value = err.message || '刪除操作失敗'
  } finally {
    deleteLoading.value = false
  }
}

onMounted(() => {
  loadProfile()
})
</script>
