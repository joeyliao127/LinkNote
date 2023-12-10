package com.linknote.online.linknotespring.note.noteService;

import java.util.Map;

public interface PermissionValidatorService {
 Boolean verifyNotebookPermission(Integer notebookId, Integer userId);
}
