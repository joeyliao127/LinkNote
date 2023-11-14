package com.linknote.online.linknotespring.user.usercontroller;

import com.linknote.online.linknotespring.user.model.RegisterModel;
import com.linknote.online.linknotespring.user.userexception.DatabaseOperationException;
import com.linknote.online.linknotespring.user.userexception.EmailAlreadyRegisteredException;
import com.linknote.online.linknotespring.user.userservice.UserServiceImpl;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class UserController {
  @Autowired
  UserServiceImpl userService;
  @PostMapping("/api/user")
  public ResponseEntity<Object> register(@RequestBody RegisterModel registerModel)
  throws DatabaseOperationException, EmailAlreadyRegisteredException {
    Boolean createStatus = userService.createUser(registerModel);
    if(createStatus){
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("ok", true));
    }else{
      return null;
    }
  }

}
