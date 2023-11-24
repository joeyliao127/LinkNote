package com.linknote.online.linknotespring.note.notecontroller;

import com.linknote.online.linknotespring.note.noteService.NoteService;
import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParams;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
  public NotebooksResPO getNotebooks(
      @RequestHeader String Authorization,
      @RequestParam(defaultValue = "0") @Min(0) Integer offset,
      @RequestParam(defaultValue = "0") @Max(20) @Min(0) Integer limit
      ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    NotebooksQueryParams params = new NotebooksQueryParams();
    params.setLimit(limit);
    params.setOffset(offset);
    params.setUserId(userId);
    return noteService.getNotebooks(params);
  }
}
