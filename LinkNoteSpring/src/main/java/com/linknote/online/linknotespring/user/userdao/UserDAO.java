package com.linknote.online.linknotespring.user.userdao;

import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.user.userpo.UserEmailPo;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import java.util.List;

public interface UserDAO {
  List<UserEmailPo> getByEmail(String email);
  Integer createUser(RegisterRequestDto registerRequestDto);



  List<UserInfoPO> getByEmailAndPassword(
      SignInRequestDto signInRequestDto);
}
