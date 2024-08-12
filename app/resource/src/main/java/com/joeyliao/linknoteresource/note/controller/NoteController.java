package com.joeyliao.linknoteresource.note.controller;

import com.joeyliao.linknoteresource.note.po.CreateNotePo;
import com.joeyliao.linknoteresource.note.po.DeleteNotePo;
import com.joeyliao.linknoteresource.note.po.GetNoteRequestPo;
import com.joeyliao.linknoteresource.note.po.GetNotesRequestPo;
import com.joeyliao.linknoteresource.note.po.updateNotePo;
import com.joeyliao.linknoteresource.note.service.NoteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class NoteController {

  @Autowired
  NoteService noteService;

  @Validated
  @PostMapping("/api/notebooks/{notebookId}/notes")
  public ResponseEntity<Object> createNote(
      @PathVariable @NotBlank String notebookId,
      @RequestBody @Valid CreateNotePo po
  ) {
    po.setNotebookId(notebookId);
    String noteId = noteService.createNote(po);
    return ResponseEntity.status(200).body(Map.of("result", true, "noteId", noteId));
  }

  @GetMapping("/api/notebooks/{notebookId}/notes")
  public ResponseEntity<Object> getNotesByNotebookId(
      @PathVariable String notebookId,
      @RequestParam(defaultValue = "0") @Min(0) Integer offset,
      @RequestParam(defaultValue = "1") @Max(20) @Min(1) Integer limit,
      @RequestParam(defaultValue = "null") String tag,
      @RequestParam(defaultValue = "false") String star,
      @RequestParam(defaultValue = "true") String sortDesc,
      @RequestParam(defaultValue = "null") String keyword
  ) {
    GetNotesRequestPo po = new GetNotesRequestPo();
    po.setNotebookId(notebookId);
    po.setOffset(offset);
    po.setLimit(limit);
    po.setTag(tag);
    po.setStar(star);
    po.setKeyword(keyword);
    po.setSortDesc(sortDesc);
    return ResponseEntity.status(200).body(noteService.getNotes(po));
  }

  @GetMapping("/api/notebooks/{notebookId}/notes/{noteId}")
  public ResponseEntity<Object> getNoteByNoteId(
      @PathVariable String notebookId,
      @PathVariable String noteId
  ) {
    GetNoteRequestPo po = new GetNoteRequestPo();
    po.setNotebookId(notebookId);
    po.setNoteId(noteId);
    return ResponseEntity.status(200).body(noteService.getNote(po));
  }

  @PutMapping("/api/notebooks/{notebookId}/notes/{noteId}")
  public ResponseEntity<Object> updateNote(
      @RequestBody updateNotePo po,
      @PathVariable String noteId
  ) {
    po.setNoteId(noteId);
    noteService.updateNote(po);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }

  @DeleteMapping("/api/notebooks/{notebookId}/notes/{noteId}")
  public ResponseEntity<Object> deleteNote(
      @PathVariable String noteId
  ){
    DeleteNotePo po = new DeleteNotePo();
    po.setNoteId(noteId);
    noteService.deleteNote(po);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }
}
