package com.linknote.online.linknotespring.note.notecontroller;

import com.linknote.online.linknotespring.note.noteService.NoteService;
import com.linknote.online.linknotespring.note.noteService.TagService;
import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookTagsParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateTagParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteController {

  @Autowired
  NoteService noteService;

  @Autowired
  TokenService tokenService;

  @Autowired
  TagService tagService;

  //新建note
  @PostMapping("/api/notebooks/{notebookId}/notes")
  public ResponseEntity<Object> createNote(
      @RequestBody @Valid CreateNoteParamsDto params,
      @PathVariable Integer notebookId
      ){
    Integer noteId = noteService.createNote(params, notebookId);
      return ResponseEntity.status(201).body(Map.of("result", true, "noteId", noteId));
  }

  //新建note的tag
  @PostMapping("/api/notebooks/{notebookId}/notes/{noteId}/tags")
  public ResponseEntity<Object> createNoteTag(
      @PathVariable Integer notebookId,
      @PathVariable Integer noteId,
      @RequestBody CreateTagParamDto params,
      @RequestHeader String Authorization
  ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    params.setUserId(userId);
    params.setNotebookId(notebookId);
    params.setNoteId(noteId);
    tagService.createNoteTag(params);
    return ResponseEntity.status(200).body(Map.of("result", true));

  }
  //更新筆記內容
  @PutMapping("/api/notebooks/{notebookId}/notes/{noteId}")
  public ResponseEntity<Object> updateNote(
      @RequestBody UpdateNoteParamsDto params,
      @RequestHeader String Authorization,
      @PathVariable Integer notebookId,
      @PathVariable Integer noteId
  ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    params.setUserId(userId);
    params.setNoteId(noteId);
    params.setNotebookId(notebookId);
    noteService.updateNote(params);

    return ResponseEntity.ok().body(Map.of("result", true));
  }
}
