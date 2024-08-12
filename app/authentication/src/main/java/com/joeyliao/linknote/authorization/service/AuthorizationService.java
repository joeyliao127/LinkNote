package com.joeyliao.linknote.authorization.service;

import com.joeyliao.linknote.authorization.requestobject.CollaboratorPermissionRequest;
import com.joeyliao.linknote.authorization.requestobject.InvitationPermissionRequest;
import com.joeyliao.linknote.authorization.requestobject.NotePermissionRequest;
import com.joeyliao.linknote.authorization.requestobject.NotebookPermissionRequest;
import com.joeyliao.linknote.authorization.requestobject.TagPermissionRequest;

public interface AuthorizationService {
  public Boolean checkNotebookPermission(NotebookPermissionRequest request);
  public Boolean checkNotePermission(NotePermissionRequest request);
  public Boolean checkTagPermission(TagPermissionRequest request);
  public Boolean checkCollaboratorPermission(CollaboratorPermissionRequest request);
  public Boolean checkInvitationPermission(InvitationPermissionRequest request);
}
