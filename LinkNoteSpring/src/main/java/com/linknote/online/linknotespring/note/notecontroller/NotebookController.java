package com.linknote.online.linknotespring.note.notecontroller;

import com.linknote.online.linknotespring.note.noteService.NotebookService;
import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookTagsParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboratorsParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookTagParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotebooksParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNotebookParamDto;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class NotebookController {
  @Autowired
  NotebookService notebookService;

  @Autowired
  TokenService tokenService;

  //取得使用者資訊
  @GetMapping("/api/notebooks")
  public ResponseEntity<NotebooksResPO> getNotebooks(
      @RequestHeader String Authorization,
      @RequestParam(defaultValue = "0") @Min(0) Integer offset,
      @RequestParam(defaultValue = "0") @Max(20) @Min(0) Integer limit,
      @RequestParam(defaultValue = "null") String keyword,
      @RequestParam Boolean coNotebook
      ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    GetNotebooksParamsDto params = new GetNotebooksParamsDto();
    params.setLimit(limit);
    params.setOffset(offset);
    params.setUserId(userId);
    params.setKeyword(keyword);
    params.setCoNotebook(coNotebook);
    return ResponseEntity.status(HttpStatus.OK).body(notebookService.getNotebooks(params));
  }

  //新增筆記本
  @PostMapping("/api/notebooks")
  public ResponseEntity<Object> createNotebook(
      @RequestBody @Valid CreateNotebookParamsDto params,
      @RequestHeader String Authorization
  ){
    if(params.getEmails().size() > 4){
      return ResponseEntity.status(400).body(Map.of("result", false, "msg", "collaborators最多四人"));
    }
    params.setUserId(tokenService.parserJWTToken(Authorization).get("userId", Integer.class));
    notebookService.createNotebook(params);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("result", true));
  }

  //新增notebook tag
  @PostMapping("/api/notebooks/{notebookId}/tags")
  public ResponseEntity<Object> createNotebookTag(
      @RequestBody @Valid CreateNotebookTagsParamsDto params,
      @PathVariable Integer notebookId,
      @RequestHeader String Authorization
  ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    notebookService.createNotebookTag(params.getTag(), notebookId, userId);
    return ResponseEntity.status(201).body(Map.of("result", true));
  }

  //新增協作者
@PostMapping("/api/notebooks/{notebookId}/collaborators")
public ResponseEntity<Object> createCollaborator(
    @PathVariable Integer notebookId,
    @RequestHeader String Authorization,
    @RequestBody @Valid CreateCollaboratorParamsDto params
){
   Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
   params.setUserId(userId);
   params.setNotebookId(notebookId);
   notebookService.createCollaborator(params);
   return ResponseEntity.status(201).body(Map.of("result", true));
}

  //更新notebook name
  @PutMapping("/api/notebooks/{notebookId}")
  public ResponseEntity<Object> updateNotebook(
      @PathVariable Integer notebookId,
      @RequestBody @Valid UpdateNotebookParamDto params,
      @RequestHeader String Authorization
  ){
    //檢查使用者是否沒有傳遞任何參數，如果是就不更新內容
    if(params.getName().isEmpty() && params.getDescription().isEmpty()){
      return ResponseEntity.status(200).body(Map.of("result", true));
    }
    params.setUserId(tokenService.parserJWTToken(Authorization).get("userId", Integer.class));
    params.setNotebookId(notebookId);
    notebookService.updateNotebook(params);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }


  @DeleteMapping("/api/notebooks/{notebookId}")
  public ResponseEntity<Object> deleteNotebook(
      @PathVariable Integer notebookId,
      @RequestHeader String Authorization
  ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    DeleteNotebookParamsDto params = new DeleteNotebookParamsDto();
    params.setNotebookId(notebookId);
    params.setUserId(userId);
    notebookService.deleteNotebook(params);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }

  @DeleteMapping("/api/notebooks/{notebookId}/tags/{tag}")
  public ResponseEntity<Object> deleteNotebookTag(
      @PathVariable Integer notebookId,
      @PathVariable String tag,
      @RequestHeader String  Authorization
  ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    DeleteNotebookTagParamDto param = new DeleteNotebookTagParamDto();
    param.setTag(tag);
    param.setNotebookId(notebookId);
    param.setUserId(userId);
    notebookService.deleteNotebookTag(param);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }

  //刪除共編
  @DeleteMapping("/api/notebooks/{notebookId}/collaborators/{collaboratorId}")
  public ResponseEntity<Object> deleteCollaborators(
      @PathVariable Integer notebookId,
      @PathVariable Integer collaboratorId,
      @RequestHeader String Authorization
  ){

    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    DeleteCollaboratorsParamDto params = new DeleteCollaboratorsParamDto();
    params.setCollaboratorId(collaboratorId);
    params.setUserId(userId);
    params.setNotebookId(notebookId);
    notebookService.deleteCollaborators(params);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }
}


