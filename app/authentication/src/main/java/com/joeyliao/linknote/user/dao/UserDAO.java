package com.joeyliao.linknote.user.dao;

import com.joeyliao.linknote.user.dto.RegisterRequestDto;
import com.joeyliao.linknote.user.dto.SignInRequestDto;
import com.joeyliao.linknote.user.po.UserEmailPo;
import com.joeyliao.linknote.user.po.UserInfoPO;
import java.util.List;
import java.util.UUID;

public interface UserDAO {

  List<UserEmailPo> getByEmail(String email);

  String getUserIdByEmail(String email);

  List<UserInfoPO> getByEmailAndPassword(
      SignInRequestDto signInRequestDto);
  List<UserInfoPO> getByTokenUserIdAndEmailForToken(String email, String userId);

  String getUsernameByNotebookId(Integer notebookId);

  Boolean createUser(RegisterRequestDto registerRequestDto);

  String checkUserIdNotExist (UUID uuid);

  void updateUser(RegisterRequestDto registerRequestDto);
}
