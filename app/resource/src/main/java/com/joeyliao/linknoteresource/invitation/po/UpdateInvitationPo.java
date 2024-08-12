package com.joeyliao.linknoteresource.invitation.po;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class UpdateInvitationPo {
 @NotNull
 private Boolean isAccept;
 private String inviteeEmail;
 private String inviteeId;
 @NotBlank
 private String notebookId;
 private String Authorization;
}
