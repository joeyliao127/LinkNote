package com.linknote.online.linknotespring.note.noteService;

public interface PermissionValidatorDao {
  Integer verifyNotebookByUserIdAndNotebookId(Integer notebookId, Integer userId);
}
