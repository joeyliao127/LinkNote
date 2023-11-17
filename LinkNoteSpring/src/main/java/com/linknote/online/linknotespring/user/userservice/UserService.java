package com.linknote.online.linknotespring.user.userservice;

import com.linknote.online.linknotespring.user.userdto.RegisterRequest;

public interface UserService {
  String register(RegisterRequest registerRequest);
}
