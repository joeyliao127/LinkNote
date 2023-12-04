package com.linknote.online.linknotespring.note.noterowmapper;

import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class TagsRowMapper implements RowMapper<TagPO> {


  @Override
  public TagPO mapRow(ResultSet rs, int rowNum) throws SQLException {
    TagPO tagPO = new TagPO();
    tagPO.setName(rs.getString("name"));
    tagPO.setTagId(rs.getInt("id"));
    return tagPO;
  }
}
