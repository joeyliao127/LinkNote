# LinkNote

這是一款線上筆記軟體，可以分享筆記本給同伴閱讀＆撰寫筆記，並且支援 Markdown 語法。
特色：

- 權限管理
- 多條件篩選筆記
- 支援 Markdown 語法
  ![image](https://github.com/joeyliao127/LinkNote/blob/main/img/demo/linknote-edit-page.jpg?raw=true)

Website URL: https://linknote.online/

可用測試帳號登入：

帳號: test@test.com
密碼: abc123

## 技術總覽

可點選主題的下方連結導向該主題的技術細節。

系統架構

- [系統架構](#系統架構)

前端

- HTML
- CSS
- JavaScript
- JQuey
- Webpack
- 第三方 NPM 套件

[後端](#後端細節)

- Java Spring Boot

[Database](#Database技術細節)

- MySQL
- [RBAC 權限管理模型](RBAC權限管理模型)

[部署](#部署細節)

- AWS
- Docker-compose
- PVE
- Ubuntu
- CloudFlare

### 系統架構

![架構圖](https://github.com/joeyliao127/LinkNote/blob/main/img/LinkNote-%E7%B3%BB%E7%B5%B1%E6%9E%B6%E6%A7%8B.png?raw=true)
網站是建立於 AWS，並透過 Docker 部署應用程式，Docker 共有三個 Container，分別是 Nginx 和兩個 Spring boot 服務。

後端有兩個 Spring boot 服務，分別是 Authentication 與 Resource，Authentication 負責權限驗證，-而 Resource 負責操作筆記的 CRUD。

每當使用者發出請求後，則會 Nginx 會根據 path 來轉送請求給 Authentication 或 Resource，每當 Resource 收到使用者發出指定路徑的 Request，Resource 的 Interceptor 就會發送請求到 Authentication 驗證此使用者是否有權限存取資源，並返回相對應的結果。

靜態圖片的部分則是交流 AWS 的 CloudFront CDN 處理。

### 後端細節

- **RESTflu API**：後端採用 RESFful API 風格做發開，透過 path 清楚判斷 API 的用途為何。
- **三層式架構**：使用 Controller, Service, DAO 三層式架構 透過三層式架構更方便地維護程式碼，區分每個資料夾的功能。
- **Interceptor 與權限管理**：當後端 Resource 服務收到請求時，會被攔截器攔截，並且發送 Request 到 Authentication 驗證請求是否允許，通過驗證後才會進入 Resource 進行操作。
- **JWT Token**：透過 JWT token 限制存取 endpoint，當使用者發出帶有 bearer token 的 request，解析 token 後取得 payload 驗證身份。
- **Exception Handler** ：透過 Exception handler 統一處理異常，並自定義異常返回給前端有用的資訊，並依照錯誤類型賦予相對應的 HTTP status code。
- **CORS**在 AppConfig 中允許 GET, POST, PUT, DETELE, OPTIONS 五種方法的跨域請求，並限制 Origin，解決 Nginx 轉送 Request 時遇到的跨域問題。

## Database 技術細節

![ERD](https://github.com/joeyliao127/LinkNote/blob/main/img/LinkNote-ERD.png?raw=true)

- **正規化**：資料表設計符合第三正規化。
- **[RBAC 權限管理模型](#權限管理)**：引入 RBAC 權限管理模型管理使用者權限。
- **外鍵約束**：合理使用外鍵約束確保資料一制性，如刪除筆記本時，連同筆記本底下的 tags, notes 會根據 Foreign Key Constraint 的 CASECAD，一並刪除 tags 和 notes，確保資料一制性。
- **Indxe**：合理使用 Index 來優化搜尋效能。
- **複合式主鍵**：使用複合式主鍵來避免使用者建立相同的 Tag，在 tags 資料表中，根據 name 和 notebookId 欄位當作 PK，確保資料不重複。

### 權限管理

引入 RBAC 權限管理模型，透過`角色`、`存取目標`、`操作行為`等三個方面，打造出高擴展性的權限管理表，控管使用者對資源的存取。

在了解權限管理的細節之前，要先了解專案中 user 對於 resource 的關係。

1. user 可以有許多筆記本
2. 筆記本可以有許多筆記和 Tag
3. 筆記可以有許多 Tag
   當 user 想存取 resource 時，都是通過 user 對於筆記本之間的角色來決定。
   用一個例子做說明，比如`A001`建立一個筆記本，id 為`NB001`，且`NB001`底下有 noteId 為`N111`和`N112`。

**User A001 的筆記本**

- notebookId: `NB001`
  - noteId: `N101`
  - noteId: `N102`

notebookId 為 `NB001` 這個筆記本對是由 user `A001` 建立，因此 user `A001` 對於 notebook `NB001`的角色就是 Owner。

因此`A001`想存取`N101`這個筆記時，Authentication Server 就會查詢`notebook_users_role`表中，`A001`對於`NB001`筆記本的角色如果是 Owner 或 Collaborator，就可以訪問此資源。

`Role`：Owner
`Target`：notes
`Action`：read

結果就是`A001`可以存取`N101`的筆記。

上面提到 Owner 和 Collaborator 兩個角色，實際上在資料表中共有四種角色，分別是 Owner, Collaborator, Member, Guest，但目前只用到了 Owner 和 Collaborator 和 Guest，設定四種角色是因為未來將會開發新功能，讓使用這設定筆記可以給哪些對象閱讀，比如開放給 LinkNote 會員觀看，或者是非會員也能觀看筆記內容，但這都是未來待開發功能，只是先設計出方便擴展的 Table。

#### 角色權限

**Notebook 權限**

|     Role     | Create | Read | Update | Delete |
| :----------: | :----: | :--: | :----: | :----: |
|    Owner     |   ✅   |  ✅  |   ✅   |   ✅   |
| Collaborator |   ❌   |  ✅  |   ❌   |   ❌   |
|    Member    |   ❌   |  ❌  |   ❌   |   ❌   |
|    Guest     |   ❌   |  ❌  |   ❌   |   ❌   |

**Note 權限**

|     Role     | Create | Read | Update | Delete |
| :----------: | :----: | :--: | :----: | :----: |
|    Owner     |   ✅   |  ✅  |   ✅   |   ✅   |
| Collaborator |   ✅   |  ✅  |   ✅   |   ✅   |
|    Member    |   ❌   |  ❌  |   ❌   |   ❌   |
|    Guest     |   ❌   |  ❌  |   ❌   |   ❌   |

**Tag 權限**

|     Role     | Create | Read | Update | Delete |
| :----------: | :----: | :--: | :----: | :----: |
|    Owner     |   ✅   |  ✅  |   ✅   |   ✅   |
| Collaborator |   ✅   |  ✅  |   ✅   |   ✅   |
|    Member    |   ❌   |  ❌  |   ❌   |   ❌   |
|    Guest     |   ❌   |  ❌  |   ❌   |   ❌   |

**Collaborator 權限**

|     Role     | Create | Read | Update | Delete |
| :----------: | :----: | :--: | :----: | :----: |
|    Owner     |   ✅   |  ✅  |   ✅   |   ✅   |
| Collaborator |   ❌   |  ✅  |   ❌   |   ❌   |
|    Member    |   ❌   |  ❌  |   ❌   |   ❌   |
|    Guest     |   ❌   |  ❌  |   ❌   |   ❌   |

**Invitation 權限**

|     Role     | Create | Read | Update | Delete |
| :----------: | :----: | :--: | :----: | :----: |
|    Owner     |   ✅   |  ✅  |   ✅   |   ✅   |
| Collaborator |   ❌   |  ❌  |   ❌   |   ❌   |
|    Member    |   ❌   |  ❌  |   ✅   |   ❌   |
|    Guest     |   ❌   |  ❌  |   ❌   |   ❌   |

### 部署細節

- **Docker-compose**：透過 Docker-compose 打包專案部署上線，確保服務有獨立的作業環境。
  - **Docker container - Nginx**：根據請求路徑將流量分配給 Resource 和 Authentication。
  - **Docker container - Spring boot**：透過 Dockerfile 選擇包含 jre 的 image，並運行 Spring boot 服務。
- **SSL**：申請 Let's Encrypt SSL 憑證。
- **PVE**：透過 PVE 多開虛擬機並建立快照，並透過 Nginx 分配流量給不同的 VM。
- **AWS**：透過 AWS Cloud Front CDN 取得圖片。
