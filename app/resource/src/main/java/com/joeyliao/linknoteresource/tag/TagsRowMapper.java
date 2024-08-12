package com.joeyliao.linknoteresource.tag;

import com.joeyliao.linknoteresource.tag.dto.TagDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class TagsRowMapper implements RowMapper<TagDTO> {

  @Override
  public TagDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
    TagDTO dto = new TagDTO();
    dto.setName(rs.getString("name"));
    dto.setTagId(rs.getString("id"));
    return dto;
  }
}
