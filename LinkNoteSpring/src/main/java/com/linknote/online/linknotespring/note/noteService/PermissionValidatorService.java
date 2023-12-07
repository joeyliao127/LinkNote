package com.linknote.online.linknotespring.note.noteService;

public interface PermissionValidatorService {
  Boolean verifyNotebookPerission(Integer notebookId, Integer userId);
}
