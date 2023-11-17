package com.linknote.online.linknotespring.user.usercontroller;
import com.linknote.online.linknotespring.user.userdto.AuthenticationRequest;
import com.linknote.online.linknotespring.user.userdto.RegisterRequest;
import com.linknote.online.linknotespring.generic.exception.DatabaseOperationException;
import com.linknote.online.linknotespring.user.userexception.EmailAlreadyRegisteredException;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import com.linknote.online.linknotespring.user.userservice.TokenServiceImpl;
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
  public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequest registerRequest)
  throws DatabaseOperationException, EmailAlreadyRegisteredException {
    System.out.println(registerRequest.getUsername());
    System.out.println(registerRequest.getEmail());
    System.out.println(registerRequest.getPassword());
    String JWTToken = userService.register(registerRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(JWTToken);
  }
  @PostMapping("/api/user/auth")
  public ResponseEntity<Object> authentication(@RequestHeader String Authorization){
    return null;
  }


  @GetMapping("/api/tokenParse")
  public ResponseEntity<Claims> parseToken(@RequestHeader String Authorization){
    String token = Authorization.substring(7);
    Claims payload = tokenService.parserJWTToken(token);

    return ResponseEntity.status(HttpStatus.OK).body(payload);
  }

}
