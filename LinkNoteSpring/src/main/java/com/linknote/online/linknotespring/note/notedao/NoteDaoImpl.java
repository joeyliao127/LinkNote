package com.linknote.online.linknotespring.note.notedao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NoteDaoImpl implements NoteDao{
  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;
}
