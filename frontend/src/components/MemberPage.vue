<script setup>
import { ref, reactive } from 'vue'
import { ApiError, searchMembers, updateMember, deleteMember, logout } from '../api'

// 1. 接收父層 (App.vue) 傳過來的當前登入者資訊 (Props)
const props = defineProps({
  member: {
    type: Object,
    required: true
  }
})

// 2. 定義向父層通報的門鈴 (Emits)
const emit = defineEmits(['logged-out', 'member-updated'])

// =================== 功能一：查詢其他會員 (條件 4) ===================
const searchKeyword = ref('')
const searchResults = ref(null) // null 代表尚未搜尋，[] 代表查無資料
const searchLoading = ref(false)

async function handleSearch() {
  searchLoading.value = true
  try {
    // 呼叫 api.js 的 searchMembers
    searchResults.value = await searchMembers(searchKeyword.value.trim())
  } catch (err) {
    alert(err.message || '查詢失敗')
  } finally {
    searchLoading.value = false
  }
}

// =================== 功能二：修改個人資料 (條件 3) ===================
const editForm = reactive({
  fullName: props.member.fullName,
  password: '' // 可選填新密碼
})
const updateSuccess = ref('')
const updateError = ref('')
const updateLoading = ref(false)

async function handleUpdate() {
  updateSuccess.value = ''
  updateError.value = ''
  updateLoading.value = true

  try {
    const payload = {
      fullName: editForm.fullName.trim()
    }
    // 若有填寫新密碼才帶入
    if (editForm.password.trim()) {
      payload.password = editForm.password.trim()
    }

    // 呼叫 api.js 的 updateMember (帶入自己的 ID)
    const updated = await updateMember(props.member.id, payload)
    updateSuccess.value = '會員資料修改成功！'
    editForm.password = ''
    // 通知 App.vue 更新畫面上顯示的會員姓名
    emit('member-updated', updated)
  } catch (err) {
    updateError.value = err.message || '修改失敗'
  } finally {
    updateLoading.value = false
  }
}

// =================== 功能三：會員登出 ===================
function handleLogout() {
  logout()
  emit('logged-out')
}

// =================== 功能四：刪除自己的帳號 (條件 5) ===================
const deleteLoading = ref(false)

async function handleDelete() {
  const confirmed = window.confirm(
    `確定要永久刪除帳號「${props.member.username}」嗎？此操作無法復原！`
  )
  if (!confirmed) return

  deleteLoading.value = true
  try {
    // 呼叫 api.js 的 deleteMember (帶入自己的 ID)
    await deleteMember(props.member.id)
    alert('您的會員帳號已成功刪除。')
    // 刪除成功，自動登出並退回登入畫面！
    emit('logged-out')
  } catch (err) {
    alert(err.message || '刪除失敗')
  } finally {
    deleteLoading.value = false
  }
}
</script>

<template>
  <div class="page">
    <!-- 頂部歡迎區塊 (條件 2) -->
    <header class="welcome">
      <div>
        <h1 style="margin: 0; font-size: 1.5rem;">👋 歡迎回來，{{ member.fullName }}！</h1>
        <small style="color: #64748b;">帳號：{{ member.username }} (ID: #{{ member.id }})</small>
      </div>
      <button type="button" class="secondary" @click="handleLogout">
        🚪 登出
      </button>
    </header>

    <!-- 區塊一：查詢會員 (條件 4) -->
    <section>
      <h2>🔍 查詢會員</h2>
      <p style="font-size: 0.85rem; color: #64748b; margin-top: -6px; margin-bottom: 12px;">
        藉由會員姓名或會員帳號搜尋平台上的其他會員資訊
      </p>

      <form class="search-form" @submit.prevent="handleSearch">
        <input
          v-model="searchKeyword"
          type="text"
          placeholder="請輸入會員姓名或帳號關鍵字 (留空查詢全部)..."
        />
        <button type="submit" :disabled="searchLoading">
          {{ searchLoading ? '查詢中...' : '查詢' }}
        </button>
      </form>

      <!-- 查詢結果表格展示 -->
      <div v-if="searchResults !== null" style="margin-top: 15px;">
        <p v-if="searchResults.length === 0" style="color: #64748b; font-size: 0.9rem;">
          查無符合條件的會員資料。
        </p>
        <table v-else>
          <thead>
            <tr>
              <th style="width: 60px;">ID</th>
              <th>會員帳號</th>
              <th>真實姓名</th>
              <th>註冊時間</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in searchResults" :key="item.id">
              <td><strong>#{{ item.id }}</strong></td>
              <td>{{ item.username }}</td>
              <td>{{ item.fullName }}</td>
              <td style="font-size: 0.85rem; color: #64748b;">
                {{ new Date(item.createdAt).toLocaleDateString('zh-TW') }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <!-- 區塊二：修改個人資料 (條件 3) -->
    <section>
      <h2>✏️ 修改個人資料</h2>
      
      <div v-if="updateSuccess" class="success">{{ updateSuccess }}</div>
      <div v-if="updateError" class="error">{{ updateError }}</div>

      <form @submit.prevent="handleUpdate">
        <label>
          會員帳號 (不可修改)
          <input
            :value="member.username"
            disabled
            style="background: #f1f5f9; color: #64748b; cursor: not-allowed;"
          />
        </label>

        <label>
          真實姓名
          <input
            v-model="editForm.fullName"
            type="text"
            required
            placeholder="請輸入新姓名"
          />
        </label>

        <label>
          重設密碼 (若不修改請留空)
          <input
            v-model="editForm.password"
            type="password"
            placeholder="若要更換密碼請輸入新密碼"
          />
        </label>

        <button type="submit" :disabled="updateLoading" style="justify-self: start;">
          {{ updateLoading ? '儲存中...' : '💾 儲存修改' }}
        </button>
      </form>
    </section>

    <!-- 區塊三：危險區域 (刪除帳號) (條件 5) -->
    <section class="danger-zone">
      <h2 style="color: #dc2626;">⚠️ 帳號管理</h2>
      <p style="font-size: 0.85rem; color: #64748b; margin-top: -6px; margin-bottom: 12px;">
        刪除您的帳號為不可逆操作，刪除後將無法再使用此帳號登入。
      </p>
      <button
        type="button"
        class="danger"
        :disabled="deleteLoading"
        @click="handleDelete"
      >
        {{ deleteLoading ? '刪除中...' : '🗑️ 永久刪除我的會員帳號' }}
      </button>
    </section>
  </div>
</template>
