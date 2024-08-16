package com.joeyliao.linknote.authorization.controller;

import com.joeyliao.linknote.authorization.enums.Target;
import com.joeyliao.linknote.authorization.requestobject.NotePermissionRequest;
import com.joeyliao.linknote.authorization.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class NoteController {

  @Autowired
  PermissionRequestObjectHandler requestObjectHandler;

  @Autowired
  AuthorizationService authorizationService;

  @PostMapping("/api/auth/note")
  public Boolean checkNotePermission(
      @RequestBody @Valid NotePermissionRequest request,
      @RequestHeader String Authorization
  ) {

    requestObjectHandler.setRequestAttribute(
        request,
        Authorization,
        Target.NOTE
    );
    return authorizationService.checkNotePermission(request);
  }
}
