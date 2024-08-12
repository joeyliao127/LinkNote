package com.joeyliao.linknote.user.service;

import com.joeyliao.linknote.user.dto.RegisterRequestDto;
import com.joeyliao.linknote.user.dto.SignInRequestDto;
import com.joeyliao.linknote.user.po.UserInfoPO;

public interface UserService {
  UserInfoPO getUserInfoByToken (String Authorization);
  String register(RegisterRequestDto registerRequestDto);
  UserInfoPO signInVerify(SignInRequestDto signInRequestDto);
  Boolean checkEmailIsExist(String email);


  String updateUserInfo(RegisterRequestDto registerRequestDto, String token);
}
