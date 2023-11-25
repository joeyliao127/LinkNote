package com.linknote.online.linknotespring.note.notecontroller;

import com.linknote.online.linknotespring.note.noteService.NoteService;
import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteController {

  @Autowired
  NoteService noteService;

  @PostMapping("/api/notebooks/{notebookId}/notes")
  public ResponseEntity<Object> createNote(
      @RequestBody @Valid CreateNoteParamsDto params,
      @PathVariable Integer notebookId
      ){
    System.out.println("接收到create請求");
    noteService.createNote(params, notebookId);
   return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("result", true));
  }

}
