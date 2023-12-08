package com.linknote.online.linknotespring.user.userservice;
import static org.springframework.util.DigestUtils.md5DigestAsHex;
import com.linknote.online.linknotespring.generic.exception.DatabaseOperationException;
import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userexception.EmailAlreadyDisabledException;
import com.linknote.online.linknotespring.user.userexception.EmailAlreadyRegisteredException;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.user.userexception.VerifyUserFailedException;
import com.linknote.online.linknotespring.user.userpo.UserEmailPo;
import com.linknote.online.linknotespring.user.userdao.UserDAO;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
  @Autowired
  UserDAO userDAO;


  @Autowired
  TokenService tokenService;
  private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  @Override
  public UserInfoPO getUserInfo(String email) {
    return userDAO.getUserInfo(email);
  }

  @Override
  public String register(RegisterRequestDto registerRequestDto) {
    System.out.println("開始驗證email");
    List<UserEmailPo> userEmailPo = userDAO.getByEmail(registerRequestDto.getEmail());
    if(userEmailPo.isEmpty()){
      System.out.println("通過email check");
      try{
        String hashedPassword = md5DigestAsHex(registerRequestDto.getPassword().getBytes());
        registerRequestDto.setPassword(hashedPassword);
        Integer userId = userDAO.createUser(registerRequestDto);
        log.info("Create eamil {}", registerRequestDto.getEmail());
        if(userId != null) {
          //gen JWT token
          return tokenService.genJWTToken(
              userId,
              registerRequestDto.getEmail(),
              registerRequestDto.getUsername()
          );
        }
      }catch (DatabaseOperationException e){
        log.error("database error");
        throw new RuntimeException("database error", e);
      }
    }else{
      System.out.println("沒通過email check");
      log.warn("email {} already registeed.", registerRequestDto.getEmail());
      throw new EmailAlreadyRegisteredException("email already registed");
    }
    return null;
  }

  @Override
  public UserInfoPO signInVerify(SignInRequestDto signInRequestDto) {
    System.out.println("email:"+signInRequestDto.getEmail());
    System.out.println("passowrd"+signInRequestDto.getPassword());
    signInRequestDto.setPassword(md5DigestAsHex(signInRequestDto.getPassword().getBytes()));
    List<UserInfoPO> userInfoPOS = userDAO.getByEmailAndPassword(signInRequestDto);
    if(userInfoPOS.isEmpty()){
      log.warn("使用者帳號或密碼錯誤，嘗試登入的email:" + signInRequestDto.getEmail());
      throw new VerifyUserFailedException("email or password incorrect");
    }else if(!userInfoPOS.get(0).getStatus()){
      log.warn("使用者帳號已停用");
      throw new EmailAlreadyDisabledException("Email already disabled");
    }
    return userInfoPOS.get(0);
  }

  @Override
  public Integer getUserIdByEmail(String email) {
    return userDAO.getUserIdByEmail(email);
  }

  @Override
  public String getUsernameByNotebookId(Integer notebookId) {
    return userDAO.getUsernameByNotebookId(notebookId);
  }
}
