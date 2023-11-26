package com.linknote.online.linknotespring.note.notedao;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.NotebookParamDto;
import com.linknote.online.linknotespring.note.notedto.QueryNotebooksParamsDto;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import com.linknote.online.linknotespring.note.noterowmapper.NotebookIdRowMapper;
import com.linknote.online.linknotespring.note.noterowmapper.TagRowMapper;
import com.linknote.online.linknotespring.note.noterowmapper.notebooksPORowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NotebookDaoImpl implements NotebookDao {
  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private static final Logger log = LoggerFactory.getLogger(NotebookDaoImpl.class);
  @Override
  public List<NotebooksPO> getNotebooks(QueryNotebooksParamsDto params, Boolean getCoNotebook) {
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
  public void createNotebook(CreateNotebookParamsDto params, Integer userId) {
    String sql = "INSERT INTO notebooks (name, description, userId) VALUES(:name, :description, :userId)";
    Map<String, Object> map = new HashMap<>();
    map.put("name",params.getName());
    map.put("description", params.getDescription());
    map.put("userId", userId);
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public Integer getNotebookIdByUserId(Integer userId, Integer notebookId) {
    log.info("notebook dao: 收到的userId: "+ userId + " notebookId: "+notebookId);
    String sql = "SELECT id FROM notebooks WHERE userId = :userId AND id = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("userId", userId);
    map.put("notebookId", notebookId);
    List<Integer> tagId = namedParameterJdbcTemplate.query(sql, map, new RowMapper<Integer>() {
      @Override
      public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.info("驗證owner的Dao: 取得的id為 = " + rs.getInt("id"));
        return rs.getInt("id");
      }
    });
    if(tagId.isEmpty()){
      return null;
    }else{
      return tagId.get(0);
    }
  }

  @Override
  public Integer getNotebookIdByNotebookName(String notebookName, Integer userId) {
    String sql = "SELECT id as notebookId FROM notebooks WHERE name = :notebookName AND userId = :userId";
    Map<String ,Object> map = new HashMap<>();
    map.put("notebookName", notebookName);
    map.put("userId", userId);
    List<Integer> notebookId = namedParameterJdbcTemplate.query(sql, map, new NotebookIdRowMapper());
    if(notebookId.isEmpty()){
      return null;
    }else{
      return notebookId.get(0);
    }
  }

  @Override
  public String getNotebookNameByUserId(Integer userId, String newNotebook) {
    String sql = "SELECT name FROM notebooks WHERE userId = :userId AND name = :name";
    Map<String,Object> map = new HashMap<>();
    map.put("userId", userId);
    map.put("name", newNotebook);
    List<String> result = namedParameterJdbcTemplate.query(sql, map, new RowMapper<String>() {
      @Override
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("name");
      }
    });
    if(result.isEmpty()){
      return null;
    }else {
      return result.get(0);
    }
  }

  @Override
  public List<TagPO> getNotebookTags(Integer notebookId) {
    String sql = "SELECT t.id as is, t.name as name FROM tag t "
        + "JOIN notebooks_tags nt ON t.id = nt.tagId "
        + "JOIN notebooks n ON n.id = nt.notebookId "
        + "WHERE n.id = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    return namedParameterJdbcTemplate.query(sql, map, new TagRowMapper());
  }

  @Override
  public Integer updateNotebookName(NotebookParamDto params) {
    String sql = "UPDATE notebooks SET name=:name WHERE id=:notebookId AND userId=:userId";
    Map<String, Object> map = new HashMap<>();
    map.put("name", params.getName());
    map.put("notebookId", params.getNotebookId());
    map.put("userId", params.getUserId());
    return namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void updateCollaborators(Integer notebookId, Integer userId) {

  }


  @Override
  public void updateNoteTags(Integer noteId, Integer tagId) {

  }



}
