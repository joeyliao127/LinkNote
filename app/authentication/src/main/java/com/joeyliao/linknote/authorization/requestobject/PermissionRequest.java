package com.joeyliao.linknote.authorization.requestobject;

import com.joeyliao.linknote.authorization.enums.Behavior;
import com.joeyliao.linknote.authorization.enums.Role;
import com.joeyliao.linknote.authorization.enums.Target;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
public class PermissionRequest {

  @NotNull
  private String notebookId;
  @NotNull
  private Behavior behavior;
  private String userId;
  private Target target;
  private Role role;
}
