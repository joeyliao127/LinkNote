package com.joeyliao.linknoteresource.templates;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class templatesController {

  @GetMapping("/")
  public String home(){
    return "index";
  }

  @GetMapping("/notebooks")
  public String notebooks(){
    return "notebooks";
  }

  @GetMapping("/notebooks/{notebookId}/notes/{noteId}")
  public String notes(@PathVariable String noteId, @PathVariable String notebookId){
    return "notes";
  }
}
