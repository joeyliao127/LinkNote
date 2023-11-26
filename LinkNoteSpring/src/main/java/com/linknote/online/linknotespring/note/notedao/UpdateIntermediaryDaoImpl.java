package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.noteService.UpdateIntermediaryService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UpdateIntermediaryDaoImpl implements UpdateIntermediaryDao {

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Override
  public Integer updateNotebookTags(Integer notebookId, Integer tagId) {
    String sql = "INSERT INTO notebooks_tags (notebookId, tagId)"
        + " VALUES (:notebookId, :tagId)";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    map.put("tagId", tagId);
    return namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public Integer updateNotTags(List<Integer> tags, Integer noteId) {
    String sql = "INSERT INTO notes_tags (noteId, tagId) VALUES(:noteId, :tagId)";
    MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[tags.size()];
    for(int i=0; i < tags.size(); i++){
      parameterSources[i] = new MapSqlParameterSource();
      parameterSources[i].addValue("tagId", tags.get(i));
      System.out.println("插入tagId" + tags.get(i));
      parameterSources[i].addValue("noteId", noteId);
      System.out.println("插入noteId:" + noteId);
    }
    namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
    return null;
  }
}
