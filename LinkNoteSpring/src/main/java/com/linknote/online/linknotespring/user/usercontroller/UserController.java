package com.linknote.online.linknotespring.user.usercontroller;
import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.generic.exception.DatabaseOperationException;
import com.linknote.online.linknotespring.user.userexception.EmailAlreadyRegisteredException;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import com.linknote.online.linknotespring.user.userservice.UserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://127.0.0.1:5501")
@RestController
public class UserController {
  @Autowired
  UserServiceImpl userService;
  @Autowired
  TokenService tokenService;
  private final static Logger log = LoggerFactory.getLogger(UserController.class);

  @PostMapping("/api/user/register")
  public ResponseEntity<Object> register(
      @RequestBody @Valid RegisterRequestDto registerRequestDto,
      HttpServletResponse response
      )
  throws DatabaseOperationException, EmailAlreadyRegisteredException {
    String token = userService.register(registerRequestDto);
    setTokenToCookie(response ,token);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("result", true));
  }
  @PostMapping("/api/user/auth")
  public ResponseEntity<Object> signInAuthentication(
      @RequestBody(required = false) @Valid SignInRequestDto signInRequestDto,
      @RequestHeader(required = false) String Authorization,
      HttpServletResponse response
      ){
    if(Authorization == null){
      log.info("接收到帳密登入請求：" + signInRequestDto.getEmail());
      UserInfoPO verifyResult = userService.signInVerify(signInRequestDto);
      String token = tokenService.genJWTToken(verifyResult.getUserId()
          ,verifyResult.getEmail()
          ,verifyResult.getUsername());
      setTokenToCookie(response, token);
      log.info("允許使用者登入:"+ verifyResult.getUsername());
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("result", true));
    }else{
      log.info("接收到token登入請求，Bearer token");
      Boolean verifyResult = tokenService.verifyToken(Authorization);
      if(verifyResult){
       log.info("允許登入");
        return ResponseEntity.status(200).body(Map.of("parseResult", true));
      }else{
        return ResponseEntity.status(401).body(Map.of("parseResult", false));
      }
    }

  }

  @GetMapping("/api/user/email")
  public ResponseEntity<Object> getUserNameByEmail(
      @RequestParam String email
      ){
    return ResponseEntity.status(200).body(userService.getUserInfo(email));
  }

  @GetMapping("/api/user")
  public ResponseEntity<Object> parseToken(@CookieValue(defaultValue = "atta", name = "token") String token){
    log.info("/api/user token = " + token);
    Claims payload = tokenService.parserJWTToken(token);
    return ResponseEntity.status(200).body(Map.of(
        "result", true,
        "email", payload.get("email", String.class),
        "username", payload.getSubject()));
  }
  private void setTokenToCookie(HttpServletResponse response, String token){
    Cookie cookie = new Cookie("token", token);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(7 * 24 * 60 * 60);
    response.addCookie(cookie);
  }
}
