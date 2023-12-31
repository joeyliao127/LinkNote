package com.linknote.online.linknotespring.user.usercontroller;
import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.generic.exception.DatabaseOperationException;
import com.linknote.online.linknotespring.user.userexception.EmailAlreadyRegisteredException;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import com.linknote.online.linknotespring.user.userservice.UserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://127.0.0.1:5501"
    , methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
    , maxAge = 60 * 60 * 24
)
@RestController
public class UserController {
  @Autowired
  UserServiceImpl userService;
  @Autowired
  TokenService tokenService;
  private final static Logger log = LoggerFactory.getLogger(UserController.class);


  @PostMapping("/api/user/register")
  public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequestDto registerRequestDto)
  throws DatabaseOperationException, EmailAlreadyRegisteredException {
    String JWTToken = userService.register(registerRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", JWTToken));
  }
  @PostMapping("/api/user/auth")
  public ResponseEntity<Object> signInAuthentication(
      @RequestBody(required = false) @Valid SignInRequestDto signInRequestDto,
      @RequestHeader(required = false) String Authorization){
    if(Authorization == null){
      log.info("接收到帳密登入請求：" + signInRequestDto.getEmail());
      UserInfoPO verifyResult = userService.signInVerify(signInRequestDto);
      String token = tokenService.genJWTToken(verifyResult.getUserId()
          ,verifyResult.getEmail()
          ,verifyResult.getUsername());
      log.info("允許使用者登入:"+ verifyResult.getUsername());
      return ResponseEntity.status(HttpStatus.OK)
          .body(Map.of(
              "result", true,
              "token", token
          ));
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
  public ResponseEntity<Object> parseToken(@RequestHeader String Authorization){
    Claims payload = tokenService.parserJWTToken(Authorization);
    return ResponseEntity.status(200).body(Map.of(
        "result", true,
        "email", payload.get("email", String.class),
        "username", payload.getSubject()));
  }

  @GetMapping("/api/user/notebook/{notebookId}")
  public ResponseEntity<Object> getUserInfoByNotebookId(@PathVariable Integer notebookId){
    return ResponseEntity.status(200).body(Map.of("result", true, "owner", userService.getUsernameByNotebookId(notebookId)));
  }

}
