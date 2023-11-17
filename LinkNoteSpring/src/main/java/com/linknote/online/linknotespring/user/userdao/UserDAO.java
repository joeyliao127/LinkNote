package com.linknote.online.linknotespring.user.userdao;

import com.linknote.online.linknotespring.user.userdto.AuthenticationRequest;
import com.linknote.online.linknotespring.user.userdto.RegisterRequest;
import com.linknote.online.linknotespring.user.userpo.UserEmailPo;
import java.util.List;

public interface UserDAO {
  List<UserEmailPo> getByEmail(String email);
  Integer createUser(RegisterRequest registerRequest);



  List<AuthenticationRequest> getByEmailAndPassword(AuthenticationRequest authenticationRequest);
}
