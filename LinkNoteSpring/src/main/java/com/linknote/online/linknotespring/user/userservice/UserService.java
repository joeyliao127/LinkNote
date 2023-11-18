package com.linknote.online.linknotespring.user.userservice;

import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;

public interface UserService {
  String register(RegisterRequestDto registerRequestDto);
  UserInfoPO signInVerify(SignInRequestDto signInRequestDto);
}
