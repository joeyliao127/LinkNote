package com.joeyliao.linknote.authorization.controller;

import com.joeyliao.linknote.authorization.enums.Target;
import com.joeyliao.linknote.authorization.requestobject.InvitationPermissionRequest;
import com.joeyliao.linknote.authorization.service.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvitationController {

  @Autowired
  PermissionRequestObjectHandler requestObjectHandler;

  @Autowired
  AuthorizationService authorizationService;

  @PostMapping("/api/auth/invitation")
  public Boolean checkInvitationPermission(
      @RequestBody @Valid InvitationPermissionRequest request,
      @RequestHeader String Authorization
  ) {
    requestObjectHandler.setRequestAttribute(
        request,
        Authorization,
        Target.INVITATION
    );
    return authorizationService.checkInvitationPermission(request);
  }

}
