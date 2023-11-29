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
public class NoteController {

  @Autowired
  NoteService noteService;

  @Autowired
  TokenService tokenService;

  @GetMapping("/api/notebooks/{notebookId}/notes")
  public ResponseEntity<Object> getNotes(
      @RequestHeader String Authorization,
      @PathVariable Integer notebookId,
      @RequestParam(defaultValue = "0") @Min(0) Integer offset,
      @RequestParam(defaultValue = "1") @Max(20) @Min(0) Integer limit,
      @RequestParam(defaultValue = "null") String tag,
      @RequestParam(defaultValue = "false") Boolean star,
      @RequestParam(defaultValue = "false") Boolean timeAsc,
      @RequestParam(defaultValue = "null") String keyword
  ){
    GetNotesParamDto param = new GetNotesParamDto();
    param.setUserId(tokenService.parserJWTToken(Authorization).get("userId", Integer.class));
    param.setNotebookId(notebookId);
    param.setOffset(offset);
    param.setLimit(limit);
    param.setTag(tag);
    param.setStar(star);
    param.setTimeAsc(timeAsc);
    param.setKeyword(keyword);
    return ResponseEntity.status(200).body(noteService.getNotes(param));
  }

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
      @RequestBody @Valid CreateNoteParamsDto params,
      @PathVariable Integer notebookId
      ){
    params.setNotebookId(notebookId);
    noteService.createNote(params);
      return ResponseEntity.status(201).body(Map.of("result", true));
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

  @DeleteMapping("/api/notebooks/{notebookId}/notes/{noteId}/tags")
  public ResponseEntity<Object> deleteNoteTag(
      @PathVariable Integer notebookId,
      @PathVariable Integer noteId,
      @RequestParam String tagName,
      @RequestHeader String Authorization
  ){
    DeleteNoteParamDto params = new DeleteNoteParamDto();
    Integer userId = tokenService.parserJWTToken(Authorization).get("userId", Integer.class);
    params.setUserId(userId);
    params.setNotebookId(notebookId);
    params.setNoteId(noteId);
    params.setTagName(tagName);
    noteService.deleteNoteTag(params);
    return ResponseEntity.status(200).body(Map.of("result", true));

  }
}
