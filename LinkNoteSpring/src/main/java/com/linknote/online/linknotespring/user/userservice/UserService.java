package com.linknote.online.linknotespring.user.userservice;

import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;

public interface UserService {
  UserInfoPO getUserInfo (String email);
  String register(RegisterRequestDto registerRequestDto);
  UserInfoPO signInVerify(SignInRequestDto signInRequestDto);
  Integer getUserIdByEmail(String email);

  String getUsernameByNotebookId(Integer notebookId);
}
