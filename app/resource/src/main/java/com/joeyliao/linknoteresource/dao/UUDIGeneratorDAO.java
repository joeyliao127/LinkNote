package com.joeyliao.linknoteresource.dao;

import com.joeyliao.linknoteresource.enums.generic.Target;
import java.util.List;

public interface UUDIGeneratorDAO {

  List<String> checkUUIDDoesNotExist(Target target, String uuid);
}
