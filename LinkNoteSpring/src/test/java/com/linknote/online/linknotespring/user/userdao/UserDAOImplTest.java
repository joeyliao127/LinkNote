package com.linknote.online.linknotespring.user.userdao;

import static org.junit.jupiter.api.Assertions.*;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserDAOImplTest {

  @Autowired
  UserDAO userDAO;

  @Test
  public void testGetUserInfo(){
    UserInfoPO po1 = userDAO.getUserInfo("joey.liao@gmail.com");
    assertEquals("Joey Liao", po1.getUsername());
  }

}