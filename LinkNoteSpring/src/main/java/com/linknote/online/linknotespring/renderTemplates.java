package com.linknote.online.linknotespring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class renderTemplates {
  @GetMapping("/home")
  public String home(){
    return "index";
  }
  @GetMapping("/notebooks")
  public String notebooks(){
    return "userSpace";
  }

  @GetMapping("/notebooks/{notebookId}/notes/{noteId}")
  public String notes(){
    return "notePage";
  }

}
