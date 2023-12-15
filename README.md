# LinkNote

網址：https://linknote.online
![LinkNote](https://github.com/joeyliao127/LinkNote/blob/dev/LinkNoteHomePage.jpg?raw=true)

## 網站簡介

這是一款線上筆記網站，可以分享筆記本給同伴共同協作。
功能：

- 建立筆記本＆筆記
- 關鍵字查詢筆記
- 設定 Filter 篩選筆記
- 賦予、刪除其他使用者存取筆記本的權限

## 技術細節

### 前端

- HTML
- CSS
- Javascript

### 後端

- Java Spring Boot

### Database

- MySQL

### 部署

- Nginx
- SSL(Let's encrypt)
- Docker
- Ubuntu

## Spring Boot

- RESTful API
  開發後端 endpoint 時依照 RESTful API 風格進行開發，透過明確的 URL 與 HTTP method 更方便的調用 endpoint，並回傳 JSON 格式的資料。

- Controller, Service, DAO 三層式架構
  透過三層式架構更方便地維護程式碼，區分每個資料夾的功能。

- 筆記權限管理
  當 Spring boot 收到 request 後，會先檢查此 user 要存取的筆記本，若要存取的筆記本不屬於此 userId，就會透過 collaborators table 中的紀錄做查詢，若在此表中有相對應的資料，則允許使用者存取、修改數據。

- JWT token
  透過 JWT token 限制存取 endpoint，當使用者發出帶有 bearer token 的 request，解析 token 後取得 payload 驗證身份。

- Interceptor
  透過 Interceptor 驗證每一個 http request，檢查 Request header 中是否帶有 Authorization 並取得 token。驗證使用者通過後 return true 允許 request 訪問 endpoint。

若遇到 CORS 的預檢請求(Option method)則直接 return true，讓 Spring boot 回傳 CORS 允許 GET, POST, PUT, DELETE 方法，並再次接收真正的請求。

- Exception Handler
  透過 Exception handler 統一處理異常，並自定義異常返回給前端有用的資訊，並依照錯誤類型賦予相對應的 HTTP status code。

- Unit Test (JUnit5)
  透過 JUnit5 撰寫單元測試，確保解析 token 的 Function 解析內容的正確性。

- CORS
  在 local 開發時為了解決 CORS 的問題，在 AppConfig 中允許 GET, POST, PUT, DETELE 四種方法的跨域請求。

- Log
  撰寫 log，紀錄使用者登入情

- Git 版本控制
  由於單人開發系統，因此 git flow 較為簡單，建立 main 和 dev 分支，並 push 到 Github 上，在 local 端使用 dev 分支做開發，完成小部分功能後 commit 並 push 上 Github，發出 PR 並 merge 到 main 分支。

- Maven
  透過 maven 專案管理工具打包 Spring boot 應用程式，取得 jar 檔後部署上線。

## Database

![ERD](https://github.com/joeyliao127/LinkNote/blob/19ed11e7981dcaf786f5cf9c6d7cc12fe1e6047c/LinkNoteERD.jpg?raw=true)

- 資料庫設計：符合第二正規劃。

- 權限管理：透過 collaborator 紀錄 notebooks 和 users 之間的關係，哪個使用者允許訪問哪些筆記都紀錄在 collaborator table。此表的角色也算是中介表，避免 notebooks table 出現重複的資料。

- Foreign Key Constraint: 除了紅色線(notebooks.userId)的外鍵約束是 Restrict，其餘的 Foreign Key 都是 CASCADE，當 notebook 被刪除時，應該要連底下的 notes, tags 與其他相關的資料一並刪除，確保資料之間的關聯性。

- Index：當 column 內容會被當作 WHERE 條件查詢時，透過 INDEX 的 B+ tree 來提昇搜尋效能，如 notes.name、tags.name 欄位。

- 複合式主鍵：複合式主鍵用於避免使用者建立相同的筆記名稱或 Tag，若使用者建立了重複的筆記或 Tag，SQL 就會發出 exception，後端 Spring boot 的 Exception handler 就會處理此異常，並返回 400 告知前端資料已存在。

## 部署細節

- 透過 Nginx 反向代理 application
- 透過 Let's encrpyt 申請的 SSL 憑證將網址變成 https
- 透過 CloudFlare 保護網站避免 DDoS。
- 透過 CloudFlare 提供的 CDN 提升靜態文件的讀取速度
