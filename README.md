# LinkNote
這是一款線上筆記軟體，可以分享筆記本給同伴閱讀＆撰寫筆記，並且支援Markdown語法。
特色：
- 權限管理
- 多條件篩選筆記
- 支援Markdown語法
![image](https://raw.githubusercontent.com/joeyliao127/LinkNote-backup/main/img/demo/linknote-edit-page.jpg?token=GHSAT0AAAAAACV7RO7WMADEDAEBOOEQH4FKZWVJYIA)

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
 - 第三方NPM套件

[後端](#後端細節)
- Java Spring Boot

[Database](#Database技術細節)
- MySQL
- [RBAC權限管理模型](RBAC權限管理模型)

[部署](#部署細節)
- AWS
- Docker-compose
- PVE
- Ubuntu
- CloudFlare

### 系統架構
![架構圖](https://github.com/joeyliao127/LinkNote/blob/main/img/LinkNote-%E7%B3%BB%E7%B5%B1%E6%9E%B6%E6%A7%8B.png?raw=true)
網站是建立於AWS，並透過Docker部署應用程式，Docker共有三個Container，分別是Nginx和兩個Spring boot服務。

後端有兩個Spring boot服務，分別是Authentication與Resource，Authentication負責權限驗證，-而Resource負責操作筆記的CRUD。

每當使用者發出請求後，則會Nginx會根據path來轉送請求給Authentication或Resource，每當Resource收到使用者發出指定路徑的Request，Resource的Interceptor就會發送請求到Authentication驗證此使用者是否有權限存取資源，並返回相對應的結果。

靜態圖片的部分則是交流AWS的CloudFront CDN處理。

### 後端細節
- **RESTflu API**：後端採用RESFful API風格做發開，透過path清楚判斷API的用途為何。
- **三層式架構**：使用Controller, Service, DAO 三層式架構 透過三層式架構更方便地維護程式碼，區分每個資料夾的功能。
- **Interceptor與權限管理**：當後端Resource服務收到請求時，會被攔截器攔截，並且發送Request到Authentication驗證請求是否允許，通過驗證後才會進入Resource進行操作。
- **JWT Token**：透過 JWT token 限制存取 endpoint，當使用者發出帶有 bearer token 的 request，解析 token 後取得 payload 驗證身份。
- **Exception Handler** ：透過 Exception handler 統一處理異常，並自定義異常返回給前端有用的資訊，並依照錯誤類型賦予相對應的 HTTP status code。
- **CORS**在 AppConfig 中允許 GET, POST, PUT, DETELE, OPTIONS五種方法的跨域請求，並限制Origin，解決Nginx轉送Request時遇到的跨域問題。

## Database技術細節
![ERD](https://github.com/joeyliao127/LinkNote/blob/main/img/LinkNote-ERD.png?raw=true)
- **正規化**：資料表設計符合第三正規化。
- **[RBAC權限管理模型](#權限管理)**：引入RBAC權限管理模型管理使用者權限。
- **外鍵約束**：合理使用外鍵約束確保資料一制性，如刪除筆記本時，連同筆記本底下的tags, notes會根據Foreign Key Constraint的CASECAD，一並刪除tags和notes，確保資料一制性。
- **Indxe**：合理使用Index來優化搜尋效能。
- **複合式主鍵**：使用複合式主鍵來避免使用者建立相同的Tag，在tags資料表中，根據name和notebookId欄位當作PK，確保資料不重複。
### 權限管理
引入RBAC權限管理模型，透過`角色`、`存取目標`、`操作行為`等三個方面，打造出高擴展性的權限管理表，控管使用者對資源的存取。

在了解權限管理的細節之前，要先了解專案中user對於resource的關係。
1. user可以有許多筆記本
2. 筆記本可以有許多筆記和Tag
3. 筆記可以有許多Tag
當user想存取resource時，都是通過user對於筆記本之間的角色來決定。
用一個例子做說明，比如`A001`建立一個筆記本，id為`NB001`，且`NB001`底下有noteId為`N111`和`N112`。

**User A001的筆記本**
- notebookId: `NB001`
	- noteId: `N101`
	- noteId: `N102`

notebookId為 `NB001` 這個筆記本對是由user `A001` 建立，因此 user `A001` 對於notebook `NB001`的角色就是Owner。

因此`A001`想存取`N101`這個筆記時，Authentication Server就會查詢`notebook_users_role`表中，`A001`對於`NB001`筆記本的角色如果是Owner或Collaborator，就可以訪問此資源。

`Role`：Owner
`Target`：notes
`Action`：read

結果就是`A001`可以存取`N101`的筆記。

上面提到Owner和Collaborator兩個角色，實際上在資料表中共有四種角色，分別是Owner, Collaborator, Member, Guest，但目前只用到了Owner和Collaborator和Guest，設定四種角色是因為未來將會開發新功能，讓使用這設定筆記可以給哪些對象閱讀，比如開放給LinkNote會員觀看，或者是非會員也能觀看筆記內容，但這都是未來待開發功能，只是先設計出方便擴展的Table。

#### 角色權限

**Notebook權限**

|     Role     | Create | Read | Update | Delete |
| :----------: | :----: | :--: | :----: | :----: |
|    Owner     |   ✅    |  ✅   |   ✅    |   ✅    |
| Collaborator |   ❌    |  ✅   |   ❌    |   ❌    |
|    Member    |   ❌    |  ❌   |   ❌    |   ❌    |
|    Guest     |   ❌    |  ❌   |   ❌    |   ❌    |

**Note權限**

|     Role     | Create | Read | Update | Delete |
| :----------: | :----: | :--: | :----: | :----: |
|    Owner     |   ✅    |  ✅   |   ✅    |   ✅    |
| Collaborator |   ✅    |  ✅   |   ✅    |   ✅    |
|    Member    |   ❌    |  ❌   |   ❌    |   ❌    |
|    Guest     |   ❌    |  ❌   |   ❌    |   ❌    |

**Tag權限**

| Role | Create | Read | Update | Delete |
| :--: | :--: | :--: | :--: | :--: |
| Owner | ✅ | ✅ | ✅ | ✅ |
| Collaborator | ✅ | ✅ | ✅ | ✅ |
| Member | ❌ | ❌ | ❌ | ❌ |
| Guest | ❌ | ❌ | ❌ | ❌ |
**Collaborator權限**

| Role | Create | Read | Update | Delete |
| :--: | :--: | :--: | :--: | :--: |
| Owner | ✅ | ✅ | ✅ | ✅ |
| Collaborator | ❌ | ✅ | ❌ | ❌ |
| Member | ❌ | ❌ | ❌ | ❌ |
| Guest | ❌ | ❌ | ❌ | ❌ |
**Invitation權限**

|     Role     | Create | Read | Update | Delete |
| :----------: | :----: | :--: | :----: | :----: |
|    Owner     |   ✅    |  ✅   |   ✅    |   ✅    |
| Collaborator |   ❌    |  ❌   |   ❌    |   ❌    |
|    Member    |   ❌    |  ❌   |   ✅    |   ❌    |
|    Guest     |   ❌    |  ❌   |   ❌    |   ❌    |

### 部署細節
- **Docker-compose**：透過Docker-compose打包專案部署上線，確保服務有獨立的作業環境。
	- **Docker container - Nginx**：根據請求路徑將流量分配給Resource和Authentication。
	- **Docker container - Spring boot**：透過Dockerfile選擇包含jre的image，並運行Spring boot服務。
- **SSL**：申請Let's Encrypt SSL憑證。
- **PVE**：透過PVE多開虛擬機並建立快照，並透過Nginx分配流量給不同的VM。
- **AWS**：透過AWS Cloud Front CDN取得圖片。
