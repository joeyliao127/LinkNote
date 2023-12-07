package com.linknote.online.linknotespring.note.notedao;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookTagParamDto;
import com.linknote.online.linknotespring.note.notedto.GetCollaboratorParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotesParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNotebookParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotebooksParamsDto;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.po.NotesPO;
import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import com.linknote.online.linknotespring.note.noterowmapper.NotebookIdRowMapper;
import com.linknote.online.linknotespring.note.noterowmapper.NotesRowMapper;
import com.linknote.online.linknotespring.note.noterowmapper.TagRowMapper;
import com.linknote.online.linknotespring.note.noterowmapper.NotebooksPORowMapper;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
  public List<NotebooksPO> getNotebooks(GetNotebooksParamsDto params, Boolean getCoNotebook) {
    String sql;
    if(getCoNotebook){
      sql = "SELECT n.id as notebookId, n.name as notebookName, n.selected as selected, n.description as description FROM notebooks n "
          + "JOIN collaborators c ON c.notebookId = n.id "
          + "JOIN users u ON u.id = c.userId "
          + "WHERE u.id = :userId ";

    }else{
      sql = "SELECT n.id as notebookId, n.name as notebookName, n.selected as selected, n.description as description FROM notebooks n "
          + "JOIN users u ON u.id = n.userId "
          + "WHERE userId = :userId ";
    }
    if(!Objects.equals(params.getKeyword(), "null")){
      sql += "name like %:keyword% ";
    }
    sql += "LIMIT :limit OFFSET :offset";
    Map<String, Object> map = new HashMap<>();
    map.put("userId", params.getUserId());
    map.put("offset", params.getOffset());
    map.put("limit", params.getLimit() + 1); //+1為了驗證是否有nextPage，ex: 前端查詢20筆，真正查詢時+1變成21筆。
    return namedParameterJdbcTemplate.query(sql, map, new NotebooksPORowMapper());
  }

  @Override
  public List<NotesPO> getNotes(GetNotesParamDto params) {
    Map<String, Object> map = new HashMap<>();
    String sql = "SELECT nt.id as noteId, nt.name, nt.question, nt.star, nt.createDate "
        + "FROM notes nt JOIN notebooks n ON nt.notebookId = n.id "
        + "JOIN users u ON u.id = n.userId "
        + "JOIN collaborators c ON c.notebookId = n.id ";
    if(!Objects.equals(params.getTag(), "null")){
      sql += "JOIN tags t ON n.id = t.notebookId "
          + "JOIN notes_tags nts ON t.id = nts.tagId AND nts.noteId = nt.id "
          + "WHERE (nt.notebookId = :notebookId "
          + "AND n.userId = :userId) "
          + "OR (c.userId =:userId AND c.notebookId = :notebookId) "
          + "AND t.name = :tag ";
      map.put("tag", params.getTag());
    }else{
      sql += "WHERE nt.notebookId = :notebookId AND n.userId = :userId "
          + "OR (c.userId =:userId AND c.notebookId = :notebookId) ";
    }
    map.put("notebookId", params.getNotebookId());
    map.put("userId", params.getUserId());

    if(!Objects.equals(params.getKeyword(), "null")){
      sql += "AND nt.name like :keyword ";
      map.put("keyword", "%" + params.getKeyword() + "%");
      System.out.println("keyword = " + params.getKeyword());
    }

    if(params.getStar()){
      sql += "AND nt.star = :star ";
      map.put("star", params.getStar());
    }

    if(params.getTimeAsc()){
      sql += "ORDER BY createDate ASC ";
    }else {
      sql += "ORDER BY createDate DESC ";
    }

    sql += "LIMIT :limit OFFSET :offset ";
    map.put("limit", params.getLimit());
    map.put("offset", params.getOffset());

    System.out.println("最終拼完的sql:" + sql);
    return namedParameterJdbcTemplate.query(sql, map, new NotesRowMapper());
  }

  @Override
  public List<UserInfoPO> getCollaborators(GetCollaboratorParamDto param) {
    String sql = "SELECT u.id as id, u.email as email, u.username as username, u.status as status FROM collaborators c JOIN users u ON u.id = c.userId JOIN notebooks n ON n.id = c.notebookId WHERE n.id = :notebookId AND c.owner = :userId";
    Map<String, Object> map = new HashMap<>();
    map.put("userId", param.getUserId());
    map.put("notebookId", param.getNotebookId());
    return namedParameterJdbcTemplate.query(sql, map,
        new RowMapper<UserInfoPO>() {
          @Override
          public UserInfoPO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserInfoPO userInfoPO = new UserInfoPO();
            userInfoPO.setEmail(rs.getString("email"));
            userInfoPO.setStatus(rs.getBoolean("status"));
            userInfoPO.setUserId(rs.getInt("id"));
            userInfoPO.setUsername(rs.getString("username"));
            return userInfoPO;
          }
        });
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
  public Integer verifyNotebookExist(Integer notebookId) {
    String sql = "SELECT id FROM notebooks WHERE id = :notebookId";
    Map<String, Object> map = new HashMap<>();
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
  public void updateNotebook(UpdateNotebookParamDto params) {
    String sql = "UPDATE notebooks SET";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", params.getNotebookId());
    map.put("userId", params.getUserId());
    if(!params.getName().isEmpty() && !params.getDescription().isEmpty()){
      sql += "  name=:name, description = :description";
      map.put("description", params.getDescription());
      map.put("name", params.getName());
    }else if(!params.getName().isEmpty()){
      sql += " name = :name";
      map.put("name", params.getName());
    } else if(!params.getDescription().isEmpty()){
      sql += " description = :description";
      map.put("description", params.getDescription());
    }
    sql += " WHERE id=:notebookId AND userId=:userId";
    namedParameterJdbcTemplate.update(sql, map);
  }


  @Override
  public void updateNoteTags(Integer noteId, Integer tagId) {

  }

  @Override
  public void deleteNotebookByNotebookId(DeleteNotebookParamsDto params) {
    String sql ="DELETE notebooks FROM notebooks "
        + "WHERE id = :notebookId AND userId = :userId";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", params.getNotebookId());
    map.put("userId", params.getUserId());
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void deleteNotebookTag(DeleteNotebookTagParamDto param) {
    String sql = "DELETE FROM tags WHERE notebookId = :notebookId AND name = :tag";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", param.getNotebookId());
    map.put("tag", param.getTag());
    namedParameterJdbcTemplate.update(sql, map);
  }
}
