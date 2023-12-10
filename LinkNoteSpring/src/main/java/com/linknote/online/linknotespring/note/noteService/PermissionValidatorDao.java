package com.linknote.online.linknotespring.note.noteService;

public interface PermissionValidatorDao {
  Integer verifyNotebookCollaborator(Integer notebookId, Integer userId);
  Integer verifyNotebookOwner(Integer notebookId, Integer userId);
}
