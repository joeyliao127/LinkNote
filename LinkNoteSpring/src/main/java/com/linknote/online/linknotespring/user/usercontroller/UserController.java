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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
public class UserController {
  @Autowired
  UserServiceImpl userService;
  @Autowired
  TokenService tokenService;
  @PostMapping("/api/user")
  public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequestDto registerRequestDto)
  throws DatabaseOperationException, EmailAlreadyRegisteredException {
    System.out.println(registerRequestDto.getUsername());
    System.out.println(registerRequestDto.getEmail());
    System.out.println(registerRequestDto.getPassword());
    String JWTToken = userService.register(registerRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", JWTToken));
  }
  @PostMapping("/api/user/auth")
  public ResponseEntity<Object> signInAuthentication(@RequestBody SignInRequestDto signInRequestDto){
    System.out.println("開始處理signin requsest");
    UserInfoPO verifyResult = userService.signInVerify(signInRequestDto);
    System.out.println("將要打包載token中的Username：" + verifyResult.getUsername());
    String token = tokenService.genJWTToken(verifyResult.getUserId()
                                           ,verifyResult.getEmail()
                                           ,verifyResult.getUsername());
    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of(
            "token", token,
            "username", verifyResult.getUsername(),
            "email",verifyResult.getEmail()
            ));
  }


  @GetMapping("/api/tokenParse")
  public ResponseEntity<Claims> parseToken(@RequestHeader String Authorization){
    String token = Authorization.substring(7);
    Claims payload = tokenService.parserJWTToken(token);

    return ResponseEntity.status(HttpStatus.OK).body(payload);
  }

}
