package com.linknote.online.linknotespring.note.notecontroller;

import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class noteController {
  @GetMapping("/api/notebooks")
  public NotebooksPO getNotebooks(){

  }
}
