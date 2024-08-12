package com.joeyliao.linknoteresource.collaborator.controller;

import com.joeyliao.linknoteresource.collaborator.po.CreateCollaboratorPo;
import com.joeyliao.linknoteresource.collaborator.po.DeleteCollaboratorPo;
import com.joeyliao.linknoteresource.collaborator.po.GetCollaboratorsRequestPo;
import com.joeyliao.linknoteresource.collaborator.service.CollaboratorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class CollaboratorController {

  @Autowired
  CollaboratorService collaboratorService;
  @GetMapping("/api/notebooks/{notebookId}/collaborators")
  public ResponseEntity<Object> getCollaborators(
      @PathVariable String notebookId
  ){
    GetCollaboratorsRequestPo po = new GetCollaboratorsRequestPo();
    po.setNotebookId(notebookId);
    return ResponseEntity.status(200).body(collaboratorService.getCollaborators(po));
  }


  @DeleteMapping("/api/notebooks/{notebookId}/collaborators")
  public ResponseEntity<Object> deleteCollaborator(
      @PathVariable String notebookId,
      @RequestParam @Valid String userEmail
  ){
    DeleteCollaboratorPo po = new DeleteCollaboratorPo();
    po.setNotebookId(notebookId);
    po.setUserEmail(userEmail);
    collaboratorService.deleteCollaborator(po);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }
}
