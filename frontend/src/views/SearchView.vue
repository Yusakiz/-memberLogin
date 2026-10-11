<template>
  <div class="container">
    <div class="card">
      <h2 class="card-title">🔍 會員資料查詢</h2>
      <p class="card-subtitle">
        透過輸入「會員姓名」或「會員帳號」搜尋平台上的會員資訊 (條件 4)
      </p>

      <!-- 搜尋列 -->
      <div style="display: flex; gap: 0.75rem; margin-bottom: 1.5rem;">
        <input
          v-model="keyword"
          type="text"
          placeholder="請輸入會員姓名或會員帳號關鍵字..."
          @keyup.enter="handleSearch"
          style="flex: 1;"
        />
        <button @click="handleSearch" class="btn btn-primary" :disabled="loading">
          {{ loading ? '查詢中...' : '🔍 查詢' }}
        </button>
        <button v-if="keyword" @click="handleClear" class="btn btn-outline">
          清除
        </button>
      </div>

      <div v-if="errorMessage" class="alert alert-error">
        {{ errorMessage }}
      </div>

      <!-- 查詢結果表格 -->
      <div v-if="loading" style="text-align: center; padding: 2rem; color: var(--text-muted);">
        資料檢索中...
      </div>

      <div v-else-if="members.length === 0" style="text-align: center; padding: 3rem; color: var(--text-muted);">
        <p style="font-size: 1.1rem; margin-bottom: 0.5rem;">查無符合條件的會員資料</p>
        <p style="font-size: 0.85rem;">請嘗試更換搜尋關鍵字，或清空搜尋以檢視全部會員。</p>
      </div>

      <div v-else class="table-container">
        <table>
          <thead>
            <tr>
              <th style="width: 70px;">ID</th>
              <th>會員帳號</th>
              <th>真實姓名</th>
              <th>電子郵件</th>
              <th>連絡電話</th>
              <th>註冊時間</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="m in members" :key="m.id">
              <td><strong>#{{ m.id }}</strong></td>
              <td>
                {{ m.username }}
                <span v-if="currentMember && currentMember.id === m.id" class="badge badge-self">
                  本人
                </span>
              </td>
              <td>{{ m.fullName }}</td>
              <td>{{ m.email || '-' }}</td>
              <td>{{ m.phone || '-' }}</td>
              <td style="font-size: 0.85rem; color: var(--text-muted);">
                {{ formatDate(m.createdAt) }}
              </td>
            </tr>
          </tbody>
        </table>

        <div style="margin-top: 1rem; font-size: 0.85rem; color: var(--text-muted); text-align: right;">
          共找到 <strong>{{ members.length }}</strong> 筆會員資料
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { memberApi } from '../api'

const keyword = ref('')
const members = ref([])
const loading = ref(false)
const errorMessage = ref('')
const currentMember = ref(null)

const handleSearch = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const res = await memberApi.searchMembers(keyword.value)
    if (res.success) {
      members.value = res.data
    } else {
      errorMessage.value = res.message || '查詢失敗'
    }
  } catch (err) {
    errorMessage.value = err.message || '搜尋發生錯誤'
  } finally {
    loading.value = false
  }
}

const handleClear = () => {
  keyword.value = ''
  handleSearch()
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const d = new Date(dateStr)
  return d.toLocaleDateString('zh-TW')
}

onMounted(() => {
  const cached = localStorage.getItem('member')
  if (cached) {
    currentMember.value = JSON.parse(cached)
  }
  handleSearch()
})
</script>
