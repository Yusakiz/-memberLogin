<template>
  <div class="container">
    <div v-if="loading" style="text-align: center; padding: 3rem;">
      <p>資料載入中...</p>
    </div>

    <template v-else-if="member">
      <!-- 平台歡迎頁 (條件 2) -->
      <div class="welcome-header">
        <h1 class="welcome-title">歡迎回來，{{ member.fullName }}！ 👋</h1>
        <p class="welcome-desc">您已成功登入會員平台。您可以在此查看個人資料、查詢其他會員、或是維護個人帳號設定。</p>
      </div>

      <div class="card" style="margin-bottom: 2rem;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
          <h2 class="card-title" style="margin: 0;">📋 我的會員基本資料</h2>
          <router-link to="/profile" class="btn btn-outline btn-sm">
            ✏️ 修改資料
          </router-link>
        </div>

        <div class="info-grid">
          <div class="info-item">
            <div class="info-label">會員編號 (ID)</div>
            <div class="info-value">#{{ member.id }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">會員帳號</div>
            <div class="info-value">{{ member.username }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">真實姓名</div>
            <div class="info-value">{{ member.fullName }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">電子郵件</div>
            <div class="info-value">{{ member.email || '尚未設定' }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">連絡電話</div>
            <div class="info-value">{{ member.phone || '尚未設定' }}</div>
          </div>
          <div class="info-item">
            <div class="info-label">註冊時間</div>
            <div class="info-value" style="font-size: 0.9rem;">{{ formatDate(member.createdAt) }}</div>
          </div>
        </div>
      </div>

      <!-- 快捷操作引導 -->
      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 1.5rem;">
        <div class="card">
          <h3 style="font-size: 1.15rem; margin-bottom: 0.5rem;">🔍 查詢其他會員</h3>
          <p style="font-size: 0.9rem; color: var(--text-muted); margin-bottom: 1rem;">
            透過會員帳號或姓名快速尋找平台上的其他註冊會員資訊。
          </p>
          <router-link to="/search" class="btn btn-primary btn-sm">
            前往查詢頁面
          </router-link>
        </div>

        <div class="card">
          <h3 style="font-size: 1.15rem; margin-bottom: 0.5rem;">⚙️ 帳號安全與修改</h3>
          <p style="font-size: 0.9rem; color: var(--text-muted); margin-bottom: 1rem;">
            隨時更新您的姓名、連絡電話、重設密碼，或刪除個人帳號。
          </p>
          <router-link to="/profile" class="btn btn-outline btn-sm">
            管理個人資料
          </router-link>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { memberApi } from '../api'

const member = ref(null)
const loading = ref(true)

const fetchProfile = async () => {
  try {
    const res = await memberApi.getCurrentMember()
    if (res.success) {
      member.value = res.data
      localStorage.setItem('member', JSON.stringify(res.data))
    }
  } catch (err) {
    console.error('獲取個人資料失敗', err)
  } finally {
    loading.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const d = new Date(dateStr)
  return d.toLocaleString('zh-TW', { hour12: false })
}

onMounted(() => {
  fetchProfile()
})
</script>
