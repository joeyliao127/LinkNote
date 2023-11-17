package com.linknote.online.linknotespring.user.userservice;

import com.linknote.online.linknotespring.generic.exception.DatabaseOperationException;
import com.linknote.online.linknotespring.user.userexception.EmailAlreadyRegisteredException;
import com.linknote.online.linknotespring.user.userdto.RegisterRequest;
import com.linknote.online.linknotespring.user.userpo.UserEmailPo;
import com.linknote.online.linknotespring.user.userdao.UserDAO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService{
  @Autowired
  UserDAO userDAO;

  @Autowired
  TokenService tokenService;
  private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  @Override
  public String register(RegisterRequest registerRequest) {
    System.out.println("開始驗證email");
    List<UserEmailPo> userEmailPo = userDAO.getByEmail(registerRequest.getEmail());
    if(userEmailPo.isEmpty()){
      System.out.println("通過email check");
      try{
        String hashedPassword = DigestUtils.md5DigestAsHex(registerRequest.getPassword().getBytes());
        registerRequest.setPassword(hashedPassword);
        Integer userId = userDAO.createUser(registerRequest);
        log.info("Create eamil {}", registerRequest.getEmail());
        if(userId != null) {
          //gen JWT token
          return tokenService.genJWTToken(
              userId,
              registerRequest.getEmail(),
              registerRequest.getUsername()
          );
        }
      }catch (DatabaseOperationException e){
        log.error("database error");
        throw new DatabaseOperationException("database error", e);
      }
    }else{
      System.out.println("沒通過email check");
      log.warn("email {} already registeed.", registerRequest.getEmail());
      throw new EmailAlreadyRegisteredException("email already registed");
    }
    return null;
  }
}
