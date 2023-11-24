package com.linknote.online.linknotespring.user.userservice;

import io.jsonwebtoken.Claims;

public interface TokenService {

  String genJWTToken(Integer userId, String email, String username);
  Claims parserJWTToken(String Authorization);

  Boolean verifyToken(String Authorization);

}
