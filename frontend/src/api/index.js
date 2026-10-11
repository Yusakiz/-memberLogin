import axios from 'axios'

// 建立 Axios 實例
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 請求攔截器：每次發送 API 請求前，若本地有 Token 則自動帶入 Header
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 回應攔截器：統一處理未登入 (401) 或其他錯誤
api.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      // Token 過期或未登入，清除 Token 並導向登入頁
      localStorage.removeItem('token')
      localStorage.removeItem('member')
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
    }
    const message = error.response?.data?.message || error.message || '連線發生錯誤'
    return Promise.reject(new Error(message))
  }
)

export const authApi = {
  // 會員註冊
  register: (data) => api.post('/auth/register', data),
  // 會員登入
  login: (data) => api.post('/auth/login', data)
}

export const memberApi = {
  // 取得當前登入者資訊
  getCurrentMember: () => api.get('/members/me'),
  // 修改會員資料 (需登入且只能改自己)
  updateMember: (id, data) => api.put(`/members/${id}`, data),
  // 查詢會員列表 (可依關鍵字模糊搜尋)
  searchMembers: (keyword = '') => api.get('/members/search', { params: { keyword } }),
  // 刪除會員帳號 (需登入且只能刪自己)
  deleteMember: (id) => api.delete(`/members/${id}`)
}

export default api
