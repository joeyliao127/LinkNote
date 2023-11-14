package com.linknote.online.linknotespring.user.userdao;

import com.linknote.online.linknotespring.user.model.RegisterModel;
import com.linknote.online.linknotespring.user.model.UserEmailModel;
import java.util.List;

public interface UserDAO {
  List<UserEmailModel> getByEmail(String email);
  Integer createUser(RegisterModel registerModel);
}
