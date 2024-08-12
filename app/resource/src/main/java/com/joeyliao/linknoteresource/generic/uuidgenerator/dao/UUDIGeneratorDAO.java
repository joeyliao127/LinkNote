package com.joeyliao.linknoteresource.generic.uuidgenerator.dao;

import com.joeyliao.linknoteresource.generic.enums.Target;
import java.util.List;
import java.util.UUID;

public interface UUDIGeneratorDAO {

  List<String> checkUUIDDoesNotExist(Target target, String uuid);
}
