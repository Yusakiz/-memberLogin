/**
 * 前端 API 連線小助手 (api.js)
 * 負責將前端的各項操作透過 HTTP 請求發送給後端 Spring Boot
 */

// 自訂 API 錯誤類別，記錄錯誤訊息與 HTTP 狀態碼 (如 400, 401)
export class ApiError extends Error {
  constructor(message, status) {
    super(message)
    this.status = status
  }
}

// 底層通用請求函式 (Request Helper)
async function request(path, options = {}) {
  const token = localStorage.getItem('token')
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers
  }

  // 1. 若瀏覽器保險箱存有 Token，每次發送自動帶在 Header 上！
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  // 2. 使用瀏覽器原生 fetch 發出網路請求
  const response = await fetch(path, { ...options, headers })
  const result = await response.json().catch(() => null)

  // 3. 錯誤處理：如果 HTTP 狀態碼異常、或後端 success 為 false
  if (!response.ok || (result && result.success === false)) {
    // 若收到 401 (未登入或 Token 過期)，自動清除本地 Token
    if (response.status === 401) {
      localStorage.removeItem('token')
    }
    const errorMsg = result?.message || `請求失敗 (HTTP ${response.status})`
    throw new ApiError(errorMsg, response.status)
  }

  // 4. 回傳 ApiResponse 包裝盒裡的 data 實體資料
  return result?.data
}

// =================== 對應後端 API 的 7 大功能 ===================

// 1. 會員登入 (條件 2)
export async function login(username, password) {
  const data = await request('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify({ username, password })
  })
  // 登入成功後，將後端發下來的 JWT Token 存進 localStorage！
  localStorage.setItem('token', data.token)
  return data.member
}

// 2. 會員註冊 (條件 1)
export async function register(form) {
  return request('/api/auth/register', {
    method: 'POST',
    body: JSON.stringify(form)
  })
}

// 3. 取得當前登入者資訊 (條件 2 歡迎頁使用)
export async function getMe() {
  return request('/api/members/me')
}

// 4. 修改會員資料 (條件 3 & 條件 6)
export async function updateMember(id, form) {
  return request(`/api/members/${id}`, {
    method: 'PUT',
    body: JSON.stringify(form)
  })
}

// 5. 查詢會員列表 (條件 4: 依姓名或帳號搜尋)
export async function searchMembers(keyword = '') {
  const query = keyword ? `?keyword=${encodeURIComponent(keyword)}` : ''
  return request(`/api/members/search${query}`)
}

// 6. 刪除會員帳號 (條件 5 & 條件 6)
export async function deleteMember(id) {
  await request(`/api/members/${id}`, {
    method: 'DELETE'
  })
  // 帳號已永久刪除，自動把本地的 Token 銷毀清空！
  localStorage.removeItem('token')
}

// 7. 會員登出
export function logout() {
  localStorage.removeItem('token')
}
