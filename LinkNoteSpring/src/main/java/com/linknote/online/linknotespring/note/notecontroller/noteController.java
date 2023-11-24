package com.linknote.online.linknotespring.note.notecontroller;

import com.linknote.online.linknotespring.note.noteService.NoteService;
import com.linknote.online.linknotespring.note.notedto.NotebookCreateParamsDTO;
import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParamsDTO;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
public class noteController {
  @Autowired
  NoteService noteService;

  @Autowired
  TokenService tokenService;
  @GetMapping("/api/notebooks")
  public ResponseEntity<NotebooksResPO> getNotebooks(
      @RequestHeader String Authorization,
      @RequestParam(defaultValue = "0") @Min(0) Integer offset,
      @RequestParam(defaultValue = "0") @Max(20) @Min(0) Integer limit
      ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    NotebooksQueryParamsDTO params = new NotebooksQueryParamsDTO();
    params.setLimit(limit);
    params.setOffset(offset);
    params.setUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(noteService.getNotebooks(params));
  }

  @PostMapping("/api/notebooks")
  public ResponseEntity<Object> createNotebook(
      @RequestBody @Valid NotebookCreateParamsDTO params,
      @RequestHeader String Authorization
  ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    noteService.createNotebook(params, userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("result", true));
  }

  //用來更新權限表，一次可以丟很多筆資料
  @PutMapping("/api/notebooks/{notebookId}/collaborators")
  public ResponseEntity<Object> updateCollaborators(@PathVariable Integer notebookId){
    return null;
  }
}
