# 簡易會員平台 (Member Management Platform)

本專案為面試實作題之「前後端分離會員登入與 CRUD 管理平台」，採用 **Java 21 + Spring Boot 3** 作為後端核心，搭配 **Vue 3** 打造簡約直覺的單頁面管理介面，並使用 **H2 Database** 內嵌資料庫，達成**開箱即用、免安裝資料庫伺服器**的高可攜性架構。

---

## 🌟 核心功能與面試條件對照表

| 題目條件與要求 | 實作方式與技術亮點 | 狀態 |
| :--- | :--- | :---: |
| **條件 1: 後端技術** | **Java 21**、**Spring Boot 3.3.4**、**Spring Data JPA**、**RESTful API** | ✅ 完成 |
| **條件 2: 前端技術** | **Vue 3 (Composition API `<script setup>`)**、**原生 Fetch 封裝**、**響應式單頁應用 (SPA)** | ✅ 完成 |
| **條件 3: 資料庫** | **H2 檔案型內嵌資料庫** (免安裝 MySQL，重啟資料不遺失，附 H2 Web Console) | ✅ 完成 |
| **條件 4: 版本控制** | Git 完整版本控制，包含 `.gitignore` 與詳細專案文件 | ✅ 完成 |
| **功能 1: 註冊功能** | 填寫帳號、密碼、姓名建立帳號；密碼經 **BCrypt** 強度雜湊加密後入庫 | ✅ 完成 |
| **功能 2: 登入功能** | 帳密比對通過後簽發 **JWT Token**，進入個人歡迎頁面 | ✅ 完成 |
| **功能 3: 修改功能** | 網頁修改個人姓名或更換新密碼；後端比對 Token 防範水平越權 (IDOR) | ✅ 完成 |
| **功能 4: 查詢功能** | 模糊搜尋其他會員；輸入關鍵字可依「會員姓名」或「會員帳號」進行檢索 | ✅ 完成 |
| **功能 5: 刪除功能** | 刪除自己的帳號；操作前彈窗二次確認，刪除後自動銷毀憑證並登出 | ✅ 完成 |
| **功能 6: 權限控制** | 修改、刪除必須在已登入情況下進行；後端實作 **`AuthInterceptor`** 嚴格驗證 JWT | ✅ 完成 |

---

## 🏗️ 系統架構目錄

```text
memberLogin/
├── backend/                               # Spring Boot 後端專案
│   ├── src/main/java/com/example/membersystem/
│   │   ├── config/                        # WebConfig (CORS/攔截器), GlobalExceptionHandler, DataInitializer
│   │   ├── controller/                    # AuthController (登入/註冊), MemberController (CRUD)
│   │   ├── dto/                           # 傳輸信封 (RegisterRequest, LoginRequest, MemberResponse, ApiResponse)
│   │   ├── entity/                        # Member.java (JPA 資料表模型)
│   │   ├── repository/                    # MemberRepository.java (Spring Data JPA)
│   │   ├── security/                      # JwtUtil (Token簽發解析), AuthInterceptor (登入攔截)
│   │   └── service/                       # MemberService.java (商業邏輯、密碼加密、權限比對)
│   ├── src/main/resources/
│   │   └── application.yml                # 資料庫路徑與 JWT 設定檔
│   └── pom.xml
│
├── frontend/                              # Vue 3 前端專案
│   ├── src/
│   │   ├── api.js                         # 原生 Fetch API 封裝 (自動掛載 Token、401處理)
│   │   ├── style.css                      # 簡約現代風格樣式表
│   │   ├── components/
│   │   │   ├── LoginPage.vue              # 未登入：登入 / 註冊 雙頁籤切換卡片
│   │   │   └── MemberPage.vue             # 已登入：歡迎區、查詢會員、修改資料、刪除帳號
│   │   └── App.vue                        # 總管元件 (依登入狀態切換畫面)
│   ├── vite.config.js                     # 反向代理設定 (/api -> http://localhost:8080)
│   └── package.json
└── README.md
```

---

## 🚀 快速啟動指南 (Quick Start)

### 1. 啟動後端 (Spring Boot)
開啟終端機進入 `backend` 目錄：
```bash
cd backend
mvn spring-boot:run
```
* 後端 API 服務將運行於：`http://localhost:8080`
* H2 Database 網頁控制台：`http://localhost:8080/h2-console`
  * JDBC URL: `jdbc:h2:file:./data/memberdb`
  * 使用者名稱: `sa`
  * 密碼: (留空)

### 2. 啟動前端 (Vue 3)
開啟另一個終端機進入 `frontend` 目錄：
```bash
cd frontend
npm install
npm run dev
```
* 前端網頁將運行於：`http://localhost:5173`

---

## 🔑 預設測試帳號

為方便審核者免手動註冊即可快速體驗，系統初次啟動時已由 `DataInitializer` 自動注入以下測試帳號：

| 帳號 (username) | 預設密碼 | 姓名 | 說明 |
| :--- | :--- | :--- | :--- |
| `test01` | `123456` | 測試會員小王 | 主要測試帳號，可用於登入、修改、刪除 |
| `alice` | `123456` | 愛麗絲 | 他人會員帳號，可於查詢功能搜尋 |
| `bob` | `123456` | 鮑伯陳 | 他人會員帳號，可於查詢功能搜尋 |

---

## 📡 RESTful API 端點清單

| 請求動詞 | 端點路徑 | 權限需求 | 說明 |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | 公開 | 會員註冊 |
| `POST` | `/api/auth/login` | 公開 | 會員登入 (取得 JWT Token) |
| `GET` | `/api/members/me` | **需登入 (Bearer Token)** | 取得當前登入者個人完整資訊 |
| `PUT` | `/api/members/{id}` | **需登入 (Bearer Token)** | 修改會員資料 (僅限本人) |
| `DELETE` | `/api/members/{id}` | **需登入 (Bearer Token)** | 刪除會員資料 (僅限本人) |
| `GET` | `/api/members/search` | 公開 | 依關鍵字模糊搜尋會員姓名或帳號 |
