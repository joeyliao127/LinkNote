package com.linknote.online.linknotespring.note.noterowmapper;

import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class TagRowMapper implements RowMapper<TagPO> {

  @Override
  public TagPO mapRow(ResultSet rs, int rowNum) throws SQLException {
    TagPO tagPO = new TagPO();
    tagPO.setTagId(rs.getInt("id"));
    tagPO.setName(rs.getString("name"));
    return tagPO;
  }
}
