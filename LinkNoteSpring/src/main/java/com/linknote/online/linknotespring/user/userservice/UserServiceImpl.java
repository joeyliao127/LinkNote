package com.linknote.online.linknotespring.user.userservice;

import com.linknote.online.linknotespring.user.userexception.DatabaseOperationException;
import com.linknote.online.linknotespring.user.userexception.EmailAlreadyRegisteredException;
import com.linknote.online.linknotespring.user.model.RegisterModel;
import com.linknote.online.linknotespring.user.model.UserEmailModel;
import com.linknote.online.linknotespring.user.userdao.UserDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
  @Autowired
  UserDAO userDAO;

  @Override
  public Boolean createUser(RegisterModel registerModel) {
    System.out.println("檢查註冊的Email:" + registerModel.getEmail());
    List<UserEmailModel> userEmailModel = userDAO.getByEmail(registerModel.getEmail());
    if(userEmailModel.isEmpty()){
      try{
        Integer rowsAffected = userDAO.createUser(registerModel);
        if(rowsAffected == 1) {
          return true;
        }
      }catch (DatabaseOperationException e){
        throw new DatabaseOperationException("database error", e);
      }
    }else{
      throw new EmailAlreadyRegisteredException("email already registed");
    }
    return false;
  }
}
