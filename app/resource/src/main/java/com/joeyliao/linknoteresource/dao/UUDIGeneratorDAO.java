package com.joeyliao.linknoteresource.dao;

import com.joeyliao.linknoteresource.enums.Target;
import java.util.List;

public interface UUDIGeneratorDAO {

  List<String> checkUUIDDoesNotExist(Target target, String uuid);
}
