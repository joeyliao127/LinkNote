package com.joeyliao.linknoteresource.tag.controller;

import com.joeyliao.linknoteresource.tag.po.CreateNoteTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.CreateNotebookTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.DeleteNoteTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.DeleteNotebookTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.GetNoteTagsRequestPo;
import com.joeyliao.linknoteresource.tag.po.updateNoteTagRequestPo;
import com.joeyliao.linknoteresource.tag.service.TagService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController {

  @Autowired
  TagService tagService;

  @PostMapping("/api/notebooks/{notebookId}/tags")
   public ResponseEntity<Object> createNotebookTag(
    @PathVariable String notebookId,
    @RequestBody @Valid CreateNotebookTagRequestPo po
  ) {
    po.setNotebookId(notebookId);
    return ResponseEntity.status(200).body(Map.of("tagId", tagService.createNotebookTag(po)));
  }

  @PostMapping("/api/notebooks/{notebookId}/notes/{noteId}/tags")
  public ResponseEntity<Object> createNoteTag(
      @RequestBody @Valid CreateNoteTagRequestPo po,
      @PathVariable String noteId) {
    po.setNoteId(noteId);
    tagService.createNoteTag(po);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }

  @GetMapping("/api/notebooks/{notebookId}/tags")
  public ResponseEntity<Object> getNotebookTags(
      @PathVariable String notebookId
  ) {
    return ResponseEntity.status(200).body(tagService.getNotebookTags(notebookId));
  }

  @GetMapping("/api/notebooks/{notebookId}/notes/{noteId}/tags")
  public ResponseEntity<Object> getNoteTags(
      @PathVariable String noteId
      ) {
    return ResponseEntity.status(200).body(tagService.getNoteTags(noteId));
  }


  @Validated
  @DeleteMapping("/api/notebooks/{notebookId}/tags")
  public ResponseEntity<Object> deleteNotebookTag(
      @RequestParam @NotBlank String tagId) {
    DeleteNotebookTagRequestPo po = new DeleteNotebookTagRequestPo();
    po.setTagId(tagId);
    tagService.deleteNotebookTag(po);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }

  @Validated
  @DeleteMapping("/api/notebooks/{notebookId}/notes/{noteId}/tags")
  public ResponseEntity<Object> deleteNoteTag(
      @RequestParam @NotBlank String tagId,
      @PathVariable String noteId) {
    DeleteNoteTagRequestPo po = new DeleteNoteTagRequestPo();
    po.setTagId(tagId);
    po.setNoteId(noteId);
    tagService.deleteNoteTag(po);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }

}
