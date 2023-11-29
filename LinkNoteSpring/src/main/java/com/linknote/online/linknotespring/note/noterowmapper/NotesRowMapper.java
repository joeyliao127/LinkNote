package com.linknote.online.linknotespring.note.noterowmapper;

import com.linknote.online.linknotespring.note.notepo.po.NotesPO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class NotesRowMapper implements RowMapper<NotesPO> {

  @Override
  public NotesPO mapRow(ResultSet rs, int rowNum) throws SQLException {
    NotesPO notesPO = new NotesPO();
    notesPO.setNoteId(rs.getInt("noteId"));
    notesPO.setName(rs.getString("name"));
    notesPO.setQuestion(rs.getString("question"));
    notesPO.setStar(rs.getBoolean("star"));
    notesPO.setCreateDate(rs.getTimestamp("createDate"));
    return notesPO;
  }
}
