package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.NotebookCreateParamsDTO;
import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParamsDTO;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.noterowmapper.NotebookIdRowMapper;
import com.linknote.online.linknotespring.note.noterowmapper.TagRowMapper;
import com.linknote.online.linknotespring.note.noterowmapper.notebooksPORowMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NoteDaoImpl implements NoteDao{
  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private static final Logger log = LoggerFactory.getLogger(NoteDaoImpl.class);
  @Override
  public List<NotebooksPO> getNotebooks(NotebooksQueryParamsDTO params, Boolean getCoNotebook) {
    String sql;
    if(getCoNotebook){
      sql = "SELECT n.id as notebookId, n.name as notebookName, n.selected FROM notebooks n "
          + "JOIN notebookCollaborators c ON c.notebookId = n.id "
          + "JOIN users u ON u.id = c.userId "
          + "WHERE u.id = :userId LIMIT :limit OFFSET :offset";

    }else{
      sql = "SELECT n.id as notebookId, n.name as notebookName, n.selected FROM notebooks n "
          + "JOIN users u ON u.id = n.userId "
          + "WHERE userId = :userId LIMIT :limit OFFSET :offset";

    }
    Map<String, Object> map = new HashMap<>();
    map.put("userId", params.getUserId());
    map.put("offset", params.getOffset());
    map.put("limit", params.getLimit() + 1); //+1為了驗證是否有nextPage
    return namedParameterJdbcTemplate.query(sql, map, new notebooksPORowMapper());
  }


  @Override
  public void createNotebook(NotebookCreateParamsDTO params, Integer userId) {
    String sql = "INSERT INTO notebooks (name, description, userId) VALUES(:name, :description, :userId)";
    Map<String, Object> map = new HashMap<>();
    map.put("name",params.getName());
    map.put("description", params.getDescription());
    map.put("userId", userId);
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public Integer createTag(String tag) {
      String sql = "INSERT INTO tags (name) VALUE(:tag)";
      Map<String, Object> map = new HashMap<>();
      map.put("tag", tag);
      namedParameterJdbcTemplate.update(sql, map);
      return null;
  }

  @Override
  public Integer getTagIdByTagName(String tag) {
    String sql = "SELECT id FROM tags WHERE name = :tag";
    Map<String ,Object> map = new HashMap<>();
    map.put("tag", tag);
    List<Integer> tagId = namedParameterJdbcTemplate.query(sql, map, new TagRowMapper());
    if(tagId.isEmpty()){
      return null;
    }else{
      return tagId.get(0);
    }
  }

  @Override
  public Integer getNotebookIdByNotebookName(String notebookName) {
    String sql = "SELECT id as notebookId FROM notebooks WHERE name = :notebookName";
    Map<String ,Object> map = new HashMap<>();
    map.put("notebookName", notebookName);
    List<Integer> notebookId = namedParameterJdbcTemplate.query(sql, map, new NotebookIdRowMapper());
    if(notebookId.isEmpty()){
      return null;
    }else{
      return notebookId.get(0);
    }
  }

  @Override
  public void updateCollaborators(Integer notebookId, Integer userId) {

  }

  @Override
  public void updateNotebookTags(Integer notebookId, Integer tagId) {
    String sql = "INSERT INTO notebooks_tags (notebookId, tagId)"
        + " VALUES (:notebookId, :tagId)";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    map.put("tagId", tagId);
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void updateNoteTags(Integer noteId, Integer tagId) {

  }

  @Override
  public void addTagByUserId(Integer id) {

  }

}
