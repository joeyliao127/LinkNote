package com.linknote.online.linknotespring.note.noteService;

public interface PermissionValidatorService {
  void verifyNotebookPermission(Integer notebookId, Integer userId);
}
