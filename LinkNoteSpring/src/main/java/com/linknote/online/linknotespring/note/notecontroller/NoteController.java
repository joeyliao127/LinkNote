package com.linknote.online.linknotespring.note.notecontroller;

import com.linknote.online.linknotespring.note.noteService.NoteService;
import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.GetTagsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotesParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteSharedParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@CrossOrigin(origins = "http://127.0.0.1:5501"
    , methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
    , maxAge = 60 * 60 * 24
)
public class NoteController {

  @Autowired
  NoteService noteService;

  @Autowired
  TokenService tokenService;

  @GetMapping("/api/notebooks/{notebookId}/notes/{noteId}")
  public ResponseEntity<Object> getNoteByNoteId(
      @RequestHeader String Authorization,
      @PathVariable Integer notebookId,
      @PathVariable Integer noteId
  ){

    GetNoteParamDto params = new GetNoteParamDto();
    params.setNotebookId(notebookId);
    params.setNoteId(noteId);
    params.setUserId(tokenService.parserJWTToken(Authorization).get("userId", Integer.class));
    return ResponseEntity.status(200).body(noteService.getNote(params));
  }

  @GetMapping("/api/notebooks/{notebookId}/notes/{noteId}/tags")
  public ResponseEntity<Object> getNoteTags(
      @RequestHeader String Authorization,
      @PathVariable Integer notebookId,
      @PathVariable Integer noteId
  ){
    GetTagsParamDto params = new GetTagsParamDto();
    params.setNotebookId(notebookId);
    params.setNoteId(noteId);
    params.setUserId(tokenService.parserJWTToken(Authorization).get("userId", Integer.class));
    return ResponseEntity.status(200).body(noteService.getNoteTags(params));
  }


  //新建note
  @PostMapping("/api/notebooks/{notebookId}/notes")
  public ResponseEntity<Object> createNote(
      @PathVariable Integer notebookId
      ){
    CreateNoteParamsDto params = new CreateNoteParamsDto();
    params.setNotebookId(notebookId);
   Integer noteId = noteService.createNote(params);
      return ResponseEntity.status(201).body(Map.of("result", true, "noteId", noteId));
  }

  //更新筆記內容
  @PutMapping("/api/notebooks/{notebookId}/notes/{noteId}")
  public ResponseEntity<Object> updateNote(
      @RequestBody @Valid UpdateNoteParamsDto params,
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

  //更新note的tag
  @PutMapping("/api/notebooks/{notebookId}/notes/{noteId}/tags")
  public ResponseEntity<Object> updateNoteTag(
      @PathVariable Integer notebookId,
      @PathVariable Integer noteId,
      @RequestBody UpdateNoteTagParamDto params,
      @RequestHeader String Authorization
  ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    params.setUserId(userId);
    params.setNotebookId(notebookId);
    params.setNoteId(noteId);
    noteService.createNoteTag(params);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }

  @PutMapping("/api/notebooks/{notebookId}/notes/{noteId}/star")
  public ResponseEntity<Object> updateNoteStar(
      @RequestBody UpdateNoteStarParamDto params,
      @RequestHeader String Authorization,
      @PathVariable Integer notebookId,
      @PathVariable Integer noteId
  ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    params.setUserId(userId);
    params.setNoteId(noteId);
    params.setNotebookId(notebookId);
    noteService.updateNoteStar(params);

    return ResponseEntity.ok().body(Map.of("result", true));
  }

  @PutMapping("/api/notebooks/{notebookId}/notes/{noteId}/shared")
  public ResponseEntity<Object> updateNoteShared(
      @RequestBody UpdateNoteSharedParamDto params,
      @RequestHeader String Authorization,
      @PathVariable Integer notebookId,
      @PathVariable Integer noteId
  ){
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    params.setUserId(userId);
    params.setNoteId(noteId);
    params.setNotebookId(notebookId);
    noteService.updateNoteShared(params);

    return ResponseEntity.ok().body(Map.of("result", true));
  }

  @DeleteMapping("/api/notebooks/{notebookId}/notes/{noteId}")
  public ResponseEntity<Object> deleteNote(
      @RequestHeader String Authorization,
      @PathVariable Integer notebookId,
      @PathVariable Integer noteId
  ){
    DeleteNoteParamDto params = new DeleteNoteParamDto();
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    params.setUserId(userId);
    params.setNoteId(noteId);
    params.setNotebookId(notebookId);
    noteService.deleteNote(params);

    return ResponseEntity.ok().body(Map.of("result", true));
  }
}
