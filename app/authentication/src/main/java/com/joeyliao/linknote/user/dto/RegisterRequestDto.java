package com.joeyliao.linknote.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;

@Data
public class RegisterRequestDto {
  @NotBlank(message = "email不能為空")
  @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$", message = "Email格式不正確")
  private String email;
  @NotBlank(message = "username不能為空")
  private String username;
  @NotBlank(message = "password不能為空")
  private String password;
  private UUID uuid;
}