package com.joeyliao.linknote.user.service;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

import com.joeyliao.linknote.generic.exception.DatabaseOperationException;
import com.joeyliao.linknote.token.service.TokenService;
import com.joeyliao.linknote.user.dao.UserDAO;
import com.joeyliao.linknote.user.dto.RegisterRequestDto;
import com.joeyliao.linknote.user.dto.SignInRequestDto;
import com.joeyliao.linknote.user.exception.EmailAlreadyDisabledException;
import com.joeyliao.linknote.user.exception.EmailAlreadyRegisteredException;
import com.joeyliao.linknote.user.exception.VerifyUserFailedException;
import com.joeyliao.linknote.user.po.UserEmailPo;
import com.joeyliao.linknote.user.po.UserInfoPO;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserDAO userDAO;


  @Autowired
  TokenService tokenService;
  private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  @Override
  public UserInfoPO getUserInfoByToken(String Authorization) {
    UserInfoPO po = new UserInfoPO();
    Claims claims = tokenService.parserJWTToken(Authorization);
    po.setUserId(claims.get("userId", String.class));
    po.setUsername(claims.getSubject());
    po.setEmail(claims.get("email", String.class));
    log.info("UserInfo - Id:" + po.getUserId() );
    log.info("UserInfo - name:" + po.getUsername() );
    log.info(claims.get("email", String.class));
    return po;
  }

  @Override
  public String register(RegisterRequestDto registerRequestDto) {
    List<UserEmailPo> userEmailPo = userDAO.getByEmail(registerRequestDto.getEmail());
    if (!userEmailPo.isEmpty()) {
      log.warn("email {} already registered.", registerRequestDto.getEmail());
      throw new EmailAlreadyRegisteredException("email already registered");
    }

    String hashedPassword = md5DigestAsHex(registerRequestDto.getPassword().getBytes());
    registerRequestDto.setPassword(hashedPassword);
    UUID userId = userIdGenerator();
    registerRequestDto.setUuid(userId);
    Boolean result = userDAO.createUser(registerRequestDto);

    if (!result) {
      throw new DatabaseOperationException("Create User Error");

    }
    log.info("Create user {}", registerRequestDto.getEmail());

    return tokenService.genJWTToken(
        userId.toString(),
        registerRequestDto.getEmail(),
        registerRequestDto.getUsername());
  }

  @Override
  public UserInfoPO signInVerify(SignInRequestDto signInRequestDto) {
    signInRequestDto.setPassword(md5DigestAsHex(signInRequestDto.getPassword().getBytes()));
    List<UserInfoPO> userInfoPOS = userDAO.getByEmailAndPassword(signInRequestDto);
    if (userInfoPOS.isEmpty()) {
      log.warn("使用者帳號或密碼錯誤，嘗試登入的email:" + signInRequestDto.getEmail());
      throw new VerifyUserFailedException("email or password incorrect");
    } else if (!userInfoPOS.get(0).getStatus()) {
      log.warn("使用者帳號已停用");
      throw new EmailAlreadyDisabledException("Email already disabled");
    }
    return userInfoPOS.get(0);
  }

  @Override
  public Boolean checkEmailIsExist(String email) {
    String result = userDAO.getUserIdByEmail(email);
    return result != null;
  }

  @Override
  public String updateUserInfo(RegisterRequestDto registerRequestDto, String token) {
    String hashedPassword = md5DigestAsHex(registerRequestDto.getPassword().getBytes());
    registerRequestDto.setPassword(hashedPassword);
    log.info("開始更改user info，set名稱為：" + registerRequestDto.getUsername());
    userDAO.updateUser(registerRequestDto);
    String userId = tokenService.parserJWTToken(token).get("userId", String.class);

    return tokenService.genJWTToken(userId,
        registerRequestDto.getEmail(),
        registerRequestDto.getUsername());
  }

  private UUID userIdGenerator() {
    while (true) {
      UUID uuid = UUID.randomUUID();
      String result = userDAO.checkUserIdNotExist(uuid);
      if (result == null) {
        return uuid;
      }
    }
  }
}
