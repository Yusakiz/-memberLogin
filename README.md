# 簡易會員管理平台 (Member Management System)

本專案為面試實作題之「前後端分離會員登入與 CRUD 管理平台」，採用 **Spring Boot (Java 21)** 作為後端核心、**Vue 3 (Vite)** 作為前端框架，並使用 **H2 Database** 內嵌資料庫，達成**開箱即用、無須安裝外部資料庫伺服器**的高可攜性架構。

---

## 🌟 核心功能與面試條件對照表

| 項目 | 題目要求條件 | 實作方式與技術說明 |
| :--- | :--- | :--- |
| **條件 1: 後端** | Java 8+ / Spring Boot / Spring MVC | 使用 **Java 21**、**Spring Boot 3.3.4**、**Spring Data JPA**、**RESTful API** |
| **條件 2: 前端** | Vue 框架 | 使用 **Vue 3** (Composition API `<script setup>`)、**Vue Router 4**、**Axios** |
| **條件 3: 資料庫** | 免繁瑣設定之輕量資料庫 | 使用 **H2 Database** (嵌入式檔案型資料庫，資料自動保存於 `backend/data`，自帶 Web Console) |
| **條件 4: 版本控制** | Git 版本控制並上傳 Public Repo | 完整 Git Commit 紀錄，根目錄附 `.gitignore` 與詳細文件 |
| **功能 1: 註冊功能** | 填寫基本資料註冊帳號 | 支援帳號、密碼、姓名、信箱、電話；包含後端 Bean Validation 與 BCrypt 密碼雜湊加密 |
| **功能 2: 登入功能** | 輸入帳號密碼登入並進入歡迎頁 | 登入核身通過後簽發 JWT Token，導向歡迎首頁並展示個人資訊 |
| **功能 3: 修改功能** | 網頁修改個人帳號資料 | 提供姓名、信箱、電話與密碼變更功能，後端驗證身分防範水平越權 |
| **功能 4: 查詢功能** | 依姓名或帳號搜尋他人資訊 | 支援依會員姓名或帳號模糊搜尋 (Like 條件) 搜尋其他註冊會員 |
| **功能 5: 刪除功能** | 刪除自己的會員帳號資訊 | 提供帳號註銷功能，操作前跳出二次確認彈窗，完成後清除憑證並登出 |
| **功能 6: 權限控制** | 修改、刪除必須在已登入情況下進行 | 後端實作 **`AuthInterceptor`** 與 JWT 驗證，未登入訪問直接回傳 `401 Unauthorized` |

---

## 🏗️ 系統架構目錄

```text
memberLogin/
├── backend/                               # Spring Boot 後端專案
│   ├── src/main/java/com/example/membersystem/
│   │   ├── config/                        # 跨域 CORS、攔截器註冊、全域例外處理 (GlobalExceptionHandler)、預設資料初始化
│   │   ├── controller/                    # AuthController (登入/註冊), MemberController (CRUD)
│   │   ├── dto/                           # 請求/回應 DTO (ApiResponse, MemberResponse, etc.)
│   │   ├── entity/                        # Member 資料表實體 (JPA ORM)
│   │   ├── repository/                    # MemberRepository (Spring Data JPA)
│   │   ├── security/                      # JwtUtil (Token簽發與解析), AuthInterceptor (登入攔截)
│   │   └── service/                       # MemberService (商業邏輯處理)
│   ├── src/main/resources/
│   │   └── application.yml                # 資料庫與 JWT 設定檔
│   └── pom.xml
│
├── frontend/                              # Vue 3 前端專案
│   ├── src/
│   │   ├── api/                           # Axios 封裝與請求/回應攔截器
│   │   ├── router/                        # Vue Router (含前置路由守衛 beforeEach)
│   │   ├── views/                         # 頁面元件 (Login, Register, Home, Profile, Search)
│   │   ├── style.css                      # 現代化響應式樣式系統
│   │   └── App.vue
│   ├── vite.config.js                     # 逆向代理伺服器設定 (/api -> :8080)
│   └── package.json
└── README.md
```

---

## 🚀 快速啟動指南 (Quick Start)

### 1. 前置需求
* **Java**: JDK 17 或 21
* **Node.js**: v18 或以上

### 2. 啟動後端 (Spring Boot)
開啟終端機進入 `backend` 目錄：
```bash
cd backend
mvn spring-boot:run
```
* 後端 API 服務將運行於：`http://localhost:8080`
* H2 Database Web 控制台：`http://localhost:8080/h2-console`
  * JDBC URL: `jdbc:h2:file:./data/memberdb`
  * 使用者名稱: `sa`
  * 密碼: (留空)

### 3. 啟動前端 (Vue 3)
開啟另一個終端機進入 `frontend` 目錄：
```bash
cd frontend
npm install
npm run dev
```
* 前端頁面將運行於：`http://localhost:5173`

---

## 🔑 預設測試帳號

為方便審核者免手動註冊即可快速體驗系統完整功能，系統啟動時已由 `DataInitializer` 自動預先注入以下測試帳號：

| 角色 / 帳號 (username) | 預設密碼 | 姓名 | 說明 |
| :--- | :--- | :--- | :--- |
| `test01` | `123456` | 測試會員小王 | 主要測試帳號，可用於登入、修改、刪除 |
| `alice` | `123456` | 愛麗絲 | 他人會員帳號，可於查詢頁搜尋 |
| `bob` | `123456` | 鮑伯陳 | 他人會員帳號，可於查詢頁搜尋 |

*(當然您也可以隨時點選「註冊新帳號」體驗完整註冊流程)*

---

## 📡 RESTful API 端點一覽

| 請求方式 | 端點路徑 | 權限需求 | 說明 |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | 公開 | 會員註冊 |
| `POST` | `/api/auth/login` | 公開 | 會員登入 (取得 JWT Token) |
| `GET` | `/api/members/me` | **需登入 (Bearer Token)** | 取得目前登入者個人完整資訊 |
| `PUT` | `/api/members/{id}` | **需登入 (Bearer Token)** | 修改指定會員資料 (僅限本人) |
| `DELETE` | `/api/members/{id}` | **需登入 (Bearer Token)** | 刪除指定會員資料 (僅限本人) |
| `GET` | `/api/members/search` | 公開 | 依關鍵字模糊搜尋會員姓名或帳號 |

---

## 💡 關鍵設計亮點與面試說明

1. **無狀態架構 (Stateless Authentication)**：
   * 採用 **JWT (JSON Web Token)**，後端不佔用 Session 記憶體，天然適合前後端分離架構與橫向擴展。
2. **資安考量 (Security)**：
   * **密碼防護**：使用業界標準 **BCrypt** 強度雜湊演算法加密儲存，資料庫無任何明文密碼。
   * **越權防護 (IDOR 防範)**：後端修改與刪除 API 嚴格比對 Token 內之當前登入者 ID 與目標 ID，防止有心人士修改 URL 中的 ID 惡意篡改他人資料。
   * **攔截器機制**：透過 Spring MVC `HandlerInterceptor` 進行切面權限驗證，未登入者無法呼叫受保護之端點。
3. **優雅的例外處理 (Global Exception Handling)**：
   * 使用 `@RestControllerAdvice` 統一攔截欄位檢核失敗、參數錯誤與權限異常，回傳標準化 JSON 回應 (`ApiResponse`)。
