package com.linknote.online.linknotespring.note.noterowmapper;

import com.linknote.online.linknotespring.note.notepo.po.NotePO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class NoteRowMapper implements RowMapper<NotePO> {

  @Override
  public NotePO mapRow(ResultSet rs, int rowNum) throws SQLException {
    NotePO notePO = new NotePO();
    notePO.setNoteId(rs.getInt("id"));
    notePO.setName(rs.getString("name"));
    notePO.setQuestion(rs.getString("question"));
    notePO.setNoteContent(rs.getString("content"));
    notePO.setKeypoint(rs.getString("keypoint"));
    notePO.setShared(rs.getBoolean("sharedPermission"));
    notePO.setStar(rs.getBoolean("star"));
    notePO.setCreateDate(rs.getTimestamp("createDate"));
    return notePO;
  }
}
