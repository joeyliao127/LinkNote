package com.linknote.online.linknotespring.user.userdao;

import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.user.userpo.UserEmailPo;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import java.util.List;

public interface UserDAO {

  String getUsernameByUserId(Integer userId);
  Integer verifyUserIdAndEmail(String email, Integer userId);
  List<UserEmailPo> getByEmail(String email);

  //給建立notebook時，使用者會點選add來檢查是否有此email。
  UserInfoPO getUserInfo (String email);
  Integer getUserIdByEmail(String email);

  List<UserInfoPO> getByEmailAndPassword(
      SignInRequestDto signInRequestDto);
  List<UserInfoPO> getByTokenUserIdAndEmailForToken(String email, Integer userId);
  Integer createUser(RegisterRequestDto registerRequestDto);





}
