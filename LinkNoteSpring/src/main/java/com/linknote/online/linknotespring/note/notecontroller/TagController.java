package com.linknote.online.linknotespring.note.notecontroller;

import com.linknote.online.linknotespring.note.noteService.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController {
  @Autowired
  TagService tagService;

  @PostMapping("/api/notebooks/{notebookId}")
  public ResponseEntity<Object> createNotebookTag(){
    return null;
  }


}
