package com.joeyliao.linknote.authorization.dao;

import com.joeyliao.linknote.authorization.enums.Action;
import com.joeyliao.linknote.authorization.enums.Role;
import com.joeyliao.linknote.authorization.requestobject.PermissionRequest;

public interface AuthorizationDAO {
  public Role getRole(String notebookId, String userId);

  public Action getPermission (PermissionRequest request);

}
