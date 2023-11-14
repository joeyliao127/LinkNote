package com.linknote.online.linknotespring.user.userservice;

import com.linknote.online.linknotespring.user.model.RegisterModel;
import com.linknote.online.linknotespring.user.model.UserEmailModel;

public interface UserService {
  Boolean createUser(RegisterModel registerModel);
}
