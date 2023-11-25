package com.linknote.online.linknotespring.user.userdao;

import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.user.userpo.UserEmailPo;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import java.util.List;

public interface UserDAO {
  List<UserEmailPo> getByEmail(String email);
  Integer createUser(RegisterRequestDto registerRequestDto);

  List<Integer> getUserIdByEmail(String email);
  Integer verifuUserIdAndEmail(String email, Integer userId);

  List<UserInfoPO> getByEmailAndPassword(
      SignInRequestDto signInRequestDto);
  List<UserInfoPO> getByTokenUserIdAndEmailForToken(String email, Integer userId);

  Integer getCollaboratorsId(Integer userId, Integer notebookId);

  void updatyCollaborator(List<Integer> collaboratorList, Integer notebookId);


}
