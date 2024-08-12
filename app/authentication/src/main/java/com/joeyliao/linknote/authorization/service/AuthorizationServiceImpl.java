package com.joeyliao.linknote.authorization.service;

import com.joeyliao.linknote.authorization.dao.AuthorizationDAO;
import com.joeyliao.linknote.authorization.enums.Action;
import com.joeyliao.linknote.authorization.enums.Role;
import com.joeyliao.linknote.authorization.requestobject.CollaboratorPermissionRequest;
import com.joeyliao.linknote.authorization.requestobject.InvitationPermissionRequest;
import com.joeyliao.linknote.authorization.requestobject.NotePermissionRequest;
import com.joeyliao.linknote.authorization.requestobject.NotebookPermissionRequest;
import com.joeyliao.linknote.authorization.requestobject.PermissionRequest;
import com.joeyliao.linknote.authorization.requestobject.TagPermissionRequest;
import com.joeyliao.linknote.token.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

  @Autowired
  AuthorizationDAO authorizationDAO;

  @Autowired
  TokenService tokenService;

  @Override
  public Boolean checkNotebookPermission(NotebookPermissionRequest request) {
    return checkPermission(request);
  }

  @Override
  public Boolean checkNotePermission(NotePermissionRequest request) {
    return checkPermission(request);
  }

  @Override
  public Boolean checkTagPermission(TagPermissionRequest request) {
    return checkPermission(request);
  }

  @Override
  public Boolean checkCollaboratorPermission(CollaboratorPermissionRequest request) {
    return checkPermission(request);
  }

  @Override
  public Boolean checkInvitationPermission(InvitationPermissionRequest request) {
    return checkPermission(request);
  }


  private Boolean checkPermission(PermissionRequest request) {
    Role role = getRole(request);
    request.setRole(role);
    Action action = authorizationDAO.getPermission(request);
    return action == Action.ALLOW;
  }

  private Role getRole(PermissionRequest permissionRequest) {
    String userId = permissionRequest.getUserId();

    if (userId == null) {
      return Role.ROLE_GUEST;
    }

    Role role = authorizationDAO.getRole(permissionRequest.getNotebookId(), userId);
    if (role == null) {
      return Role.ROLE_MEMBER;
    }

    return role;

  }

}
