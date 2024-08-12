# LinkNote2.0

# API 規格

Notebooks

- [[API文件 - Resource#新增notebook|新增notebook]]
- [[API文件 - Resource#取得所有notebooks|取得所有notebooks]]
- [[API文件 - Resource#取得所有coNotebooks|取得所有coNotebooks]]
- [[API文件 - Resource#更新notebook|更新notebook]]
- [[API文件 - Resource#更新notebook|更新notebook]]

Notes

- [[API文件 - Resource#新增note|新增note]]
- [[API文件 - Resource#取得notes|取得notes]]
- [[API文件 - Resource#取得note|取得note]]
- [[API文件 - Resource#更新note|更新note]]
- [[API文件 - Resource#刪除Note|刪除Note]]

Invitations

- [[API文件 - Resource#新增invitation|新增invitation|]]
- [[API文件 - Resource#取得sent-invitation|取得sent-invitation]]
- [[API文件 - Resource#取得received-invitation|取得received-invitation]]
- [[API文件 - Resource#更新invitation|更新invitation]]
- [[API文件 - Resource#刪除Invitation|刪除Invitation]]

collaborator

- [[API文件 - Resource#取得collaborators|取得collaborators]]
- [[API文件 - Resource#刪除collaborator|刪除collaborator]]

tag

- [[API文件 - Resource#新增notebook Tag|新增notebook Tag]]
- [[API文件 - Resource#新增note tag|新增note tag]]
- [[API文件 - Resource#取得Notebook Tag|取得Notebook Tag]]
- [[API文件 - Resource#取得note tag|取得note tag]]
- [[API文件 - Resource#刪除notebook Tag|刪除notebook Tag]]
- [[API文件 - Resource#刪除note tag|刪除note tag]]

## Notebooks

共有五隻 API：

- [[API文件 - Resource#新增notebook|新增notebook]]
- [[API文件 - Resource#取得所有notebooks|取得所有notebooks]]
- [[API文件 - Resource#取得所有coNotebooks|取得所有coNotebooks]]
- [[API文件 - Resource#更新notebook|更新notebook]]
- [[API文件 - Resource#更新notebook|更新notebook]]

### 新增 notebook

path

```
POST /api/notebooks
```

Request Param

```
None
```

Path Variable

```
None
```

RequestBody

```json
{
  "name": "Joey",
  "description": "Joey test"
}
```

token:

```
token：true
```

Response Body

```json
{
  "result": true
}
```

### 取得所有 notebooks

path

```
GET /api/notebooks
```

Request Param

```
limit: required = false, default=1  用途：用來請求一次幾筆資料
offset: required = false, default=0 用途：請求從地幾筆資料開始取得
keyword: required = false 用途：透過關鍵字查找筆記
```

Path Variable

```
None
```

RequestBody

```
None
```

token

```
token: true
```

Response Body

```json
{
  "notebooks": [
    {
      "id": "NBJAVA",
      "name": "Java",
      "description": "Test"
    },
    {
      "id": "NBPython",
      "name": "Python",
      "description": "Test"
    }
  ],
  "nextPage": false
}
```

### 取得所有 coNotebooks

path

```
GET /api/coNotebooks
```

Request Param

```
limit: required = false, default=1  用途：用來請求一次幾筆資料
offset: required = false, default=0 用途：請求從地幾筆資料開始取得
keyword: required = false 用途：透過關鍵字查找筆記
```

path Variable

```
none
```

RequestBody

```
None
```

token

```
token: true
```

Response Body

```json
{
  "notebooks": [
    {
      "id": "NBJAVA",
      "name": "Java",
      "description": "Test"
    },
    {
      "id": "NBPython",
      "name": "Python",
      "description": "Test"
    }
  ],
  "nextPage": false
}
```

### 更新 notebook

path

```json
PUT /api/notebooks/{notebookId}
```

Request Param

```
None
```

Path Variable

```
notebookId: 要更新的notebookId
```

RequestBody

```
{
	"name": "Joey C++",
	"description": "Joey Update test"
}
```

token

```
token: true
```

Response Body

```json
{
  "result": true
}
```

### 刪除 notebook

path

```
PUT /api/notebooks/{notebookId}
```

Request Param

```
none
```

Path Variable

```
notebookId: 要刪除的notebookId
```

RequestBody

```
None
```

token

```
token: true
```

Response Body

```
{
	result: true
}
```

### notebook 細節說明

1. Pagination 分頁：
   在[[API文件 - Resource#取得所有notebooks|取得所有notebooks和coNotebooks]] api 中，後端收到請求後會對 limit 值加 1，為了確認是否有 nextPage。
   若查詢出的筆數 = limit + 1，那麼就代表還有下一頁，最中 return 結果時指需要 remove 最後一筆資料即可。
2. 權限驗證：
   權限驗證的部分，攔截器取得`GET, POST /api/notebooks`, `GET /api/coNotebooks`時，會發送驗證請求到 Auth 的`/api/user/auth/token`這個路徑，而不是 Auth 的`/api/auth/notebook`。因為`/api/auth/notebook`需要具體指明哪一個`notebookId`，有`Target`才能對`Target`做 CRUD。

## Notes

共有五隻 API

- [[API文件 - Resource#新增note|新增note]]
- [[API文件 - Resource#取得notes|取得notes]]
- [[API文件 - Resource#取得note|取得note]]
- [[API文件 - Resource#更新note|更新note]]
- [[API文件 - Resource#刪除Note|刪除Note]]

### 新增 note

Path

```
POST /api/notebooks/{notebookId}/notes
```

Request Param

```
none
```

Path Variable

```
notebookId: 要建立note的notebookId
```

RequestBody

```
none
```

token

```
token: true
```

Response Body

```json
{
  "result": true,
  "noteId": "N1bd550a6861f8504941e9c5380e27e02"
}
```

剛新建好的 note 名稱都叫做 new note

### 取得 notes

這個還要改，notes 回傳資訊應該要包含 tag
path

```json
GET /api/notebooks/{notebookId}/notes?limit=3&offset=0&keyword=java
```

Request Param

```
none
```

Path Variable

```
notebookId: 要取得notes的notebookId
```

RequestBody

```
none
```

token

```
token: true
```

Response Body

```json
{
  "notes": [
    {
      "noteId": "N1bd550a6861f8504941e9c5380e27e02",
      "name": "new Note",
      "question": null,
      "content": null,
      "keypoint": null,
      "createDate": "2024-02-15 10:16:12",
      "star": false
    },
    {
      "noteId": "NJ1",
      "name": "java note1",
      "question": "test",
      "content": null,
      "keypoint": null,
      "createDate": "2024-02-14 16:28:40",
      "star": true
    }
  ],
  "nextPage": true
}
```

#### 開發思路

取得 Note 和 Notes，原本在思考要請求一次後，取得和該 note 相關的 tag，還是分次請求，想了想還是一次請求完畢比較合適。

因此在 Get Note 時，在 noteService 需要注入 tagService 取得指定 noteId 的 tag 取出，並 set 到 Response 中。

### 取得 note

這還要改，加上 tag 資訊。
path

```
GET /api/notebooks/{notebookId}/notes/{noteId}
```

Request Param

```
none
```

Path Variable

```
notebookId: 要取得note的notebookId
noteId: 要取得的noteId
```

RequestBody

```
none
```

token

```
token: true
```

Response Body

```json
{
  "note": {
    "noteId": "N1bd550a6861f8504941e9c5380e27e02",
    "name": "new Note",
    "question": null,
    "content": null,
    "keypoint": null,
    "createDate": "2024-02-15 10:16:12",
    "star": false
  }
}
```

### 更新 note

path

```
PUT /api/notebooks/{notebookId}/notes/{noteId}
```

Request Param

```
none
```

Path Variable

```
notebookId: 要更新note的notebookId
noteId: 要更新的noteId
```

RequestBody

```json
{
  "name": "Java note5",
  "question": "test",
  "content": "test",
  "keypoint": "test",
  "star": true
}
```

以上參數都是 required false，但至少要有一個參數，不然會報錯。

token

```
token: true
```

Response Body

```json
{
  "result": true
}
```

### 刪除 Note

path

```json
DELETE /api/notebooks/NBJAVA/notes/{noteId}
```

Request Param

```
none
```

Path Variable

```
notebookId: 要建立note的notebookId
```

RequestBody

```
none
```

token

```
token: true
```

Response Body

```json
{
  "result": true
}
```

## Invitation

共有五個 API

### 新增 invitation

Path

```
POST /api/notebooks/{notebookId}/invitations
```

Request Param

```
none
```

Path Variable

```
notebookId: 要建立invitation的notebookId
```

RequestBody

```json
{
  "message": "test發送給A2 Java Notebook的共編邀請",
  "inviteeEmail": "a2@test.com"
}
```

token

```
token: true
```

Response Body

```json
{
  "result": true
}
```

message：required false
inviteeEmail：required true

#### 開發思路

在 Create invitation 時，在`create`之前要先檢查`invitation`表中，是否有`notebookId`,`Inviee`,`isPending` = 1 的欄位，如果有代表之前已經發過邀請，除非`isPending`變成 0 代表已經關閉，並且**isAccept**的狀態為 0(代表沒接受)，才可以重新發出邀請。

所以`create`的流程為：

1. 接收前端`notebook`和`invitee`請求資訊。
2. Service 層檢查之前是否已經存在相同的邀請，若有存在且`isPending`欄位為 1，代表此邀請還沒關，並**返回 200** status code，因為前端只是想 create Invitation，如果存在，回傳 200 或 400 對於實際需求並無差別。
3. 檢查邀請的 inviteeEmail 是否存在於資料庫，確認有此 user。
4. 檢查結束後確認沒有重複且已開啟的 invitation，create invitation。

### 取得 sent-invitation

path

```json
GET /api/invitations/sent-invitations?offset=0&limit=2
```

Request Param

```
limit: required = false, default=1  用途：用來請求一次幾筆資料
offset: required = false, default=0 用途：請求從地幾筆資料開始取得
keyword: required = false 用途：透過關鍵字查找invitation
```

目前 sql 裡面還沒加上 keyword，所以 keyword 是無效的。

Path Variable

```
none
```

RequestBody

```json
none
```

token

```
token: true
```

Response Body

```json
{
  "nextPage": false,
  "invitations": [
    {
      "notebookName": "Java",
      "notebookId": "NBJAVA",
      "message": "test發邀請給userA3成為Java筆記本的collaborator",
      "createDate": "2024-02-14 16:28:40",
      "inviteeName": "a3",
      "inviteeEmail": "a3@test.com"
    },

    {
      "notebookName": "Python",
      "notebookId": "NBPython",
      "message": "test發邀請給userA3成為Python筆記本的collaborator",
      "createDate": "2024-02-14 16:28:41",
      "inviteeName": "a3",
      "inviteeEmail": "a3@test.com"
    }
  ]
}
```

### 取得 received-invitation

path

```json
GET /api/invitations/received-invitations
```

Request Param

```
limit: required = false, default=1  用途：用來請求一次幾筆資料
offset: required = false, default=0 用途：請求從地幾筆資料開始取得
keyword: required = false 用途：透過關鍵字查找invitation
```

Path Variable

```
notebookId: 要建立note的notebookId
```

RequestBody

```json
none
```

token

```
token: true
```

Response Body

```json
{
  "nextPage": false,
  "invitations": [
    {
      "notebookName": "Java",
      "notebookId": "NBJAVA",
      "message": "test發邀請給userA3成為Java筆記本的collaborator",
      "createDate": "2024-02-14 16:28:40",
      "inviterName": "test",
      "inviterEmail": "test@test.com"
    },
    {
      "notebookName": "Python",
      "notebookId": "NBPython",
      "message": "test發邀請給userA3成為Python筆記本的collaborator",
      "createDate": "2024-02-14 16:28:41",
      "inviterName": "test",
      "inviterEmail": "test@test.com"
    }
  ]
}
```

### 更新 invitation

path

```json
PUT /api/notebooks/{notebookId}/invitations
```

Request Param

```
none
```

Path Variable

```
notebookId: 要update invitation的notebookId
```

RequestBody

```json
{
  "isAccept": true
}
```

isAccept：required true。
token

```
token: true
```

Response Body

```json
{
  "result": true
}
```

#### 開發思路

思考一下如何完成邀請共編人員的流程。

1. 前端會發出一個邀請共編人員的邀請，此時`invitation`就會多一個資料，並且等待 invitee 接受。這時`invitation`表就會執行一次 insert。
2. 當前多 invitee 案了 Allow 或 Deny，就會發一個`put request`到後端，更新狀態。所以`invitation`就會執行一次 update，將此 inviteId 的`isOpen`和`isAccept`做更新。
3. 在 invitation 的 Service 層中，接收到 update 的 endpoint 除了會依照 isAccept 的更新`invitation`，還要根據更新的 notebookId 和 inviterId, inviteeId 更新 notebooks_users_role 資料表，將 invitee 變成`collaborator`角色。
4. 權限的部分，只要是 member 都可以 update invitation，因為沒有透過 inivitationId 給前端，因此前端只能用 userId, notebookId, isPending 這三個欄位來更新資料，就沒有安全性的問題。

##### Invitee 更新 invitation

**update 這邊可以開 multi-thread**，記得要加上@Transaction
`update`的 request body 需要以下參數：

1. invitationId
2. isAccept
3. inviteeId(從 token 解析完在 set)
4. notebookId()

Member 可以 update 是因為在成為 collaborator 之前，都只是 member，而提交 Accept 或 Deny 都會需要更新 invitation，因此需要開放 update 讓 member 更新。
只是在 Resource Server 的 DAO 層做 update 時，\*\*sql 就要加上 WHERE invitee = :inviteeId，確保是 invitee 發出的 update 請求。

UpdateRequestPo

```java
private String inviteeEmail;  //(token解析後set email)
private String notebookId;  //(取得path variable後set notebookId)
private Boolean isAccept; //required = true
```

### 刪除 Invitation

path

```json
DELETE /api/notebooks/{notebookId}/invitation
```

Request Param

```
none
```

Path Variable

```
notebookId: 要刪除invitation的notebookId
```

RequestBody

```json
none
```

token

```
token: true
```

Response Body

```json
{
  "result": true
}
```

只有 owner 可以刪除 invitation。

```java
Integer invitationId;
String notebookId;
String uesrId;
```

userId 一樣在 delete API 接收到 token 後，解析出 userId 在 set 到 po 裡面。

### invitation 細節說明

1. 權限說明：
   在 GET sent-Invitation 和 received-inivitation 時，是不指定 notebookId 的，因為是根據 user 來判斷有哪些 invitation。因此權限驗證也是和 GET notebooks 一樣，只驗證使用者 token，通過即可。
2. AppConfig：
   我發現如果 api 路徑的前綴都有`/api/notebooks/**`開頭的，都會丟兩次驗證請求到 Auth Server。後來發現是因為在 AppConfig 中，Interceptor 的設定是一直往下匹配的，比如說`/api/notebooks/{notebookId}/notes`，就會丟兩次驗證請求到 Auth，第一次是驗證`/api/notebooks/{notebookId}/notes`，第二次是驗證`/api/notebooks`，因此在`/api/notebooks`需要移除以下路徑，這樣就不會重複發 request。

```java
.excludePathPatterns(
    "/api/notebooks/*/invitations",
    "/api/notebooks/*/notes/*/",
    "/api/notebooks//notes/*/tags/**",
    "/api/notebooks/*/collaborators"
);
```

3. GET Invitation 的返回值
   返回值中並沒有包含 InvitationId，因為在 Update 或 delete invitation 時，條件可以透過 inviteeEmail + notebookId + isOpen(1)，這是一組唯一 key，可以用這個來做條件篩選，更新該筆 Invitation。

## collaborator

共有兩個 API

- [[API文件 - Resource#取得collaborators|取得collaborators]]
- [[API文件 - Resource#刪除collaborator|刪除collaborator]]

### 取得 collaborators

path

```json
GET /api/notebooks/{notebookId}/collaborators
```

Request Param

```
none
```

Path Variable

```
notebookId: 要取得collabarators的notebookId
```

RequestBody

```json
none
```

token

```
token: true
```

Response Body

```json
{
  "collaborators": [
    {
      "name": "a1",
      "userEmail": "a1@test.com"
    }
  ],
  "owner": {
    "ownerName": "test",
    "ownerEmail": "test@test.com"
  }
}
```

### 刪除 collaborator

path

```json
DELETE /api/notebooks/{notebookId}/collaborator
```

Request Param

```
none
```

Path Variable

```
notebookId: 要刪除collaborator的notebookId
```

RequestBody

```json
none
```

token

```
token: true
```

Response Body

```json
{
  "result": true
}
```

## Tag

共有六隻 API

- [[API文件 - Resource#新增notebook Tag|新增notebook Tag]]
- [[API文件 - Resource#新增note tag|新增note tag]]
- [[API文件 - Resource#取得Notebook Tag|取得Notebook Tag]]
- [[API文件 - Resource#取得note tag|取得note tag]]
- [[API文件 - Resource#刪除notebook Tag|刪除notebook Tag]]
- [[API文件 - Resource#刪除note tag|刪除note tag]]

### 新增 notebook Tag

path

```json
POST /api/notebooks/{notebookId}/tags
```

Request Param

```
none
```

Path Variable

```
notebookId: 要建立tag的notebookId
```

RequestBody

```json
{
  "name": "Java Tag Create By test"
}
```

name：required = true

token

```
token: true
```

Response Body

```json
{
  "tagId": "Tfb483ca8290897c1653b739570e18739"
}
```

### 新增 note tag

path

```json
POST /api/notebooks/{notebookId}/notes/{noteId}/tags
```

Request Param

```
none
```

Path Variable

```
notebookId: 要建立tag的notebookId
noteId: 要建立tag的noteId
```

RequestBody

```json
{
  "tagId": "T6abd32e83b0054730c888a05a28f539e"
}
```

token

```
token: true
```

Response Body

```json
{
  "result": true
}
```

### 取得 Notebook Tag

path

```json
GET /api/notebooks/{notebookId}/tags
```

Request Param

```
none
```

Path Variable

```
notebookId: 要取得tag的notebookId
```

RequestBody

```json
none
```

token

```
token: true
```

Response Body

```json
{
  "tags": [
    {
      "tagId": "T6abd32e83b0054730c888a05a28f539e",
      "name": "Java Tag Create By A1"
    },
    {
      "tagId": "T29e2b3cb5df68eeed82b3f8114b78bcf",
      "name": "Java tag test"
    },
    {
      "tagId": "TJ1",
      "name": "java1"
    },
    {
      "tagId": "TJ2",
      "name": "java2"
    },
    {
      "tagId": "TJ3",
      "name": "java3"
    },
    {
      "tagId": "TJ4",
      "name": "java4"
    }
  ]
}
```

### 取得 note tag

path

```json
GET /api/notebooks/{notebookId}/notes/{noteId}/tags
```

Request Param

```
none
```

Path Variable

```
notebookId: 要取得tag的notebookId
noteId: 要取得tag的noteId
```

RequestBody

```json
none
```

token

```
token: true
```

Response Body

```json
{
  "tags": [
    {
      "tagId": "T6abd32e83b0054730c888a05a28f539e",
      "name": "Java Tag Create By A1"
    },
    {
      "tagId": "T29e2b3cb5df68eeed82b3f8114b78bcf",
      "name": "Java tag test"
    },
    {
      "tagId": "TJ1",
      "name": "java1"
    }
  ]
}
```

### 刪除 notebook Tag

path

```json
DELETE /api/notebooks/{notebookId}/tags?tagId=T6abd32e83b0054730c888a05a28f539e
```

Request Param

```
tagId : 想刪除的tagId
```

Path Variable

```
notebookId: 要刪除tag的notebookId
```

RequestBody

```json
none
```

token

```
token: true
```

Response Body

```json
{
  "result": true
}
```

### 刪除 note tag

path

```json
DELETE /api/notebooks/{notebookId}/notes/{noteId}/tagstagId=T29e2b3cb5df68eeed82b3f8114
```

Request Param

```
tagId : 想刪除的tagId
```

Path Variable

```
notebookId: 要刪除tag的notebookId
noteId: 要刪除tag的noteId
```

RequestBody

```json
none
```

token

```
token: true
```

Response Body

```json
{
  "result": true
}
```

## 待辦清單

- [x] get collaborator service 要再加上一個 get owner，並且返回到 json 中。
      目前是這樣：

```json
{
  "collaborators": [
    {
      "name": "a1",
      "userEmail": "a1@test.com"
    }
  ],
  "owner": {
    "name": "Joey",
    "email": "joey.liao@gmail.com"
  }
}
```
