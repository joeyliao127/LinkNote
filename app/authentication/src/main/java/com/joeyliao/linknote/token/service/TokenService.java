package com.joeyliao.linknote.token.service;

import io.jsonwebtoken.Claims;

public interface TokenService {

  String genJWTToken(String userId, String email, String username);
  Claims parserJWTToken(String Authorization);

  Boolean verifyToken(String Authorization);

}
