package com.joeyliao.linknote.user.controller;

import com.joeyliao.linknote.generic.exception.DatabaseOperationException;
import com.joeyliao.linknote.token.service.TokenService;
import com.joeyliao.linknote.user.dto.RegisterRequestDto;
import com.joeyliao.linknote.user.dto.SignInRequestDto;
import com.joeyliao.linknote.user.exception.EmailAlreadyRegisteredException;
import com.joeyliao.linknote.user.po.UserInfoPO;
import com.joeyliao.linknote.user.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class UserController {

  @Autowired
  UserServiceImpl userService;


  @Autowired
  TokenService tokenService;

  @PostMapping("/api/user/register")
  public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequestDto registerRequestDto)
      throws DatabaseOperationException, EmailAlreadyRegisteredException {
    String JWTToken = userService.register(registerRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", JWTToken));
  }

  @PostMapping("/api/auth/user/token")
  public ResponseEntity<Object> signInAuthentication(
      @RequestHeader String Authorization) {
    log.info("接收到token登入請求，Bearer token");
    Boolean verifyResult = tokenService.verifyToken(Authorization);
    if (verifyResult) {
      log.info("允許登入");
      return ResponseEntity.status(200).body(true);
    } else {
      return ResponseEntity.status(401).body(false);
    }
  }


  @GetMapping("/api/auth/user/email")
  public ResponseEntity<Object> checkEmailIsExist(
      @RequestParam String email) {
    log.info("接收到email:" + email);
    return ResponseEntity.status(200).body(userService.checkEmailIsExist(email));
  }

  @PostMapping("/api/auth/user/signin")
  public ResponseEntity<Object> signInByEmailAndPassword(
      @RequestBody(required = true) SignInRequestDto signInRequestDto) {
    log.info("接收到帳密登入請求：" + signInRequestDto.getEmail());
    UserInfoPO verifyResult = userService.signInVerify(signInRequestDto);
    String token = tokenService.genJWTToken(verifyResult.getUserId()
        , verifyResult.getEmail()
        , verifyResult.getUsername());
    log.info("允許使用者登入:" + verifyResult.getUsername());
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of(
            "result", true,
            "token", token
        ));
  }

  @PutMapping("/api/user")
  public ResponseEntity<Object> updateUserInfo(
      @RequestBody RegisterRequestDto registerRequestDto,
      @RequestHeader String Authorization
  ) {
    log.info("接收到更新userinfo請求");
    String newToken = userService.updateUserInfo(registerRequestDto, Authorization);
    return ResponseEntity.status(200).body(Map.of("result", true, "token", newToken));
  }

  @GetMapping("/api/user/info")
  public ResponseEntity<Object> getUserNameByToken(
      @RequestHeader String Authorization
  ) {
    return ResponseEntity.status(200).body(userService.getUserInfoByToken(Authorization));
  }

}
