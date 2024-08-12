package com.joeyliao.linknoteresource.note.rowmapper;

import com.joeyliao.linknoteresource.note.dto.NoteDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class NotesRowMapper implements RowMapper<NoteDTO> {

  @Override
  public NoteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
    NoteDTO dto = new NoteDTO();
    dto.setNoteId(rs.getString("id"));
    dto.setName(rs.getString("name"));
    dto.setQuestion(rs.getString("question"));
    dto.setContent(rs.getString("content"));
    dto.setKeypoint(rs.getString("keypoint"));
    dto.setCreateDate(rs.getTimestamp("createDate"));
    dto.setStar(rs.getBoolean("star"));
    return dto;
  }
}
