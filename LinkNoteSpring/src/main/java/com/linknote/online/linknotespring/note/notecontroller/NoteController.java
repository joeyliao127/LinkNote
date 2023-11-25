package com.linknote.online.linknotespring.note.notecontroller;

import com.linknote.online.linknotespring.note.noteService.NoteService;
import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookTagsParamsDto;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    Integer noteId = noteService.createNote(params, notebookId);

      return ResponseEntity.status(201).body(Map.of("result", true, "noteId", noteId));


  }


  @PutMapping("/api/notebooks/{notebookId}/notes")
  public ResponseEntity<Object> updateNote(
      @RequestBody
      @PathVariable Integer notebookId
  ){
//    noteService.updateNote()
    return ResponseEntity.ok().body(Map.of("result", true));
  }


}
