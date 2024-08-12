package com.joeyliao.linknoteresource.generic.uuidgenerator.service;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

import com.joeyliao.linknoteresource.generic.enums.Target;
import com.joeyliao.linknoteresource.generic.uuidgenerator.dao.UUDIGeneratorDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UUIDGeneratorServiceImpl implements UUIDGeneratorService {

  @Autowired
  UUDIGeneratorDAO uudiGeneratorDAO;

  @Override
  public String generateUUID(Target target) {
    while (true) {
      Random random = new Random();
      double timeStamp = (double) System.currentTimeMillis() / random.nextInt(101);
      String id = md5DigestAsHex(String.valueOf(timeStamp).getBytes());
      List<String> uuidList = uudiGeneratorDAO.checkUUIDDoesNotExist(target, id);
      if (uuidList.isEmpty()) {
        return TargetPrefixMap(target) + id;
      }
    }
  }
  private String TargetPrefixMap(Target target){
    Map<Target, String> map = new HashMap<>();
    map.put(Target.NOTEBOOK, "NB");
    map.put(Target.TAG, "T");
    map.put(Target.NOTE, "N");
    return map.get(target);
  }
}
