package com.joeyliao.linknoteresource.generic.uuidgenerator.dao;

import com.joeyliao.linknoteresource.generic.enums.Target;
import com.joeyliao.linknoteresource.generic.uuidgenerator.rowmapper.UUIDRowMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UUIDGeneratorDAOImpl implements UUDIGeneratorDAO {

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public List<String> checkUUIDDoesNotExist(Target target, String id) {
    String targetToString = targetMap(target);
    String sql = "SELECT id FROM " + targetToString + " WHERE id = :id";
    log.info("UUID生成器 - Target:" + targetToString + "id:" + id);
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    return namedParameterJdbcTemplate.query(sql, map, new UUIDRowMapper());
  }

  private String targetMap(Target target){
    Map<Target, String > map = new HashMap<>();
    map.put(Target.NOTEBOOK, "notebooks");
    map.put(Target.NOTE, "notes");
    map.put(Target.TAG, "tags");
    return map.get(target);
  }
}
