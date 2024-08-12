package com.joeyliao.linknoteresource.notebook.controller;

import com.joeyliao.linknoteresource.notebook.po.GetNotebooksRequestPo;
import com.joeyliao.linknoteresource.notebook.po.CreateNotebookRequestPo;
import com.joeyliao.linknoteresource.notebook.po.UpdateNotebookPo;
import com.joeyliao.linknoteresource.notebook.service.NotebookService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class NotebookController {

  @Autowired
  NotebookService notebookService;

  @PostMapping("/api/notebooks")
  public ResponseEntity<Object> createNotebook(
      @RequestHeader String Authorization,
      @RequestBody CreateNotebookRequestPo po
  ) {
    String notebookId = notebookService.createNotebook(po, Authorization);
    return ResponseEntity.status(200).body(Map.of("result", true, "notebookId", notebookId));
  }

  @GetMapping("/api/notebooks")
  public ResponseEntity<Object> getNotebooks(
      @RequestHeader String Authorization,
      @RequestParam(defaultValue = "0") @Min(0) Integer offset,
      @RequestParam(defaultValue = "1") @Max(20) @Min(1) Integer limit,
      @RequestParam(defaultValue = "null") String keyword
  ) {
    GetNotebooksRequestPo po = setAllNotebookPoParams(Authorization, offset, limit, keyword);
    return ResponseEntity.status(HttpStatus.OK)
        .body(notebookService.getNotebooks(po));
  }

  @GetMapping("/api/coNotebooks")
  public ResponseEntity<Object> getCoNotebooks(
      @RequestHeader String Authorization,
      @RequestParam(defaultValue = "0") @Min(0) Integer offset,
      @RequestParam(defaultValue = "1") @Max(20) @Min(0) Integer limit,
      @RequestParam(defaultValue = "null") String keyword
  ) {

    GetNotebooksRequestPo po = setAllNotebookPoParams(Authorization, offset, limit, keyword);
    return ResponseEntity.status(200).body(notebookService.getCoNotebooks(po));
  }

  @PutMapping("/api/notebooks/{notebookId}")
  public ResponseEntity<Object> updateNotebook(
      @PathVariable @NotNull String notebookId,
      @RequestBody UpdateNotebookPo po
  ) {
    po.setNotebookId(notebookId);
    notebookService.updateNotebook(po);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }

  @DeleteMapping("/api/notebooks/{notebookId}")
  public ResponseEntity<Object> deleteNotebook(
      @PathVariable String notebookId
  ) {
    notebookService.deleteNotebook(notebookId);
    return ResponseEntity.status(200).body(Map.of("result", true));
  }

  private GetNotebooksRequestPo setAllNotebookPoParams(
      String Authorization,
      Integer offset,
      Integer limit,
      String keyword) {
    GetNotebooksRequestPo params = new GetNotebooksRequestPo();
    params.setAuthorization(Authorization);
    params.setLimit(limit);
    params.setOffset(offset);
    params.setKeyword(keyword);
    return params;
  }
}
