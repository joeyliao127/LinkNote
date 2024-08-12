package com.joeyliao.linknoteresource.notebook.service;

import com.joeyliao.linknoteresource.generic.enums.Target;
import com.joeyliao.linknoteresource.generic.po.UserInfo;
import com.joeyliao.linknoteresource.generic.uuidgenerator.service.UUIDGeneratorService;
import com.joeyliao.linknoteresource.notebook.dao.NotebookDAO;
import com.joeyliao.linknoteresource.notebook.po.GetNotebooksRequestPo;
import com.joeyliao.linknoteresource.notebook.po.GetNotebooksResponsePo;
import com.joeyliao.linknoteresource.notebook.po.CreateNotebookRequestPo;
import com.joeyliao.linknoteresource.notebook.po.UpdateNotebookPo;
import com.joeyliao.linknoteresource.tag.dao.TagDAO;
import com.joeyliao.linknoteresource.tag.po.CreateNotebookTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.CreateNotebookTagsRequestPo;
import com.joeyliao.linknoteresource.tag.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class NotebookServiceImpl implements NotebookService {

  @Autowired
  NotebookDAO notebookDAO;

  @Autowired
  TagService tagService;

  @Autowired
  UUIDGeneratorService uuidGeneratorService;

  @Value("${authenticationServer}")
  String authenticationServer;

  @Transactional
  @Override
  public String createNotebook(CreateNotebookRequestPo po, String authorization) {
    UserInfo userInfo = getUserInfo(authorization);
    po.setUserId(userInfo.getUserId());
    String id = uuidGeneratorService.generateUUID(Target.NOTEBOOK);
    log.info("Notebook產生的UUID為：" + id);
    notebookDAO.createNotebook(po, id);
    CreateNotebookTagsRequestPo tagPo = new CreateNotebookTagsRequestPo();
    tagPo.setTags(po.getTags());
    tagPo.setNotebookId(id);
    tagService.createNotebookTags(tagPo);
    return id;
  }

  @Override
  public GetNotebooksResponsePo getNotebooks(GetNotebooksRequestPo po) {
    UserInfo userInfo = getUserInfo(po.getAuthorization());
    log.info("getNotebooks: username為" + userInfo.getUsername());
    po.setUserId(userInfo.getUserId());
    return getNotebooks(po, false);
  }

  @Override
  public GetNotebooksResponsePo getCoNotebooks(GetNotebooksRequestPo po) {
    UserInfo userInfo = getUserInfo(po.getAuthorization());
    po.setUserId(userInfo.getUserId());
    return getNotebooks(po, true);
  }

  @Override
  public void updateNotebook(UpdateNotebookPo po) {
    notebookDAO.updateNotebook(po);
  }

  @Override
  public void deleteNotebook(String notebookId) {
    notebookDAO.deleteNotebook(notebookId);
  }

  private GetNotebooksResponsePo getNotebooks(GetNotebooksRequestPo po, Boolean isCoNotebook) {
    GetNotebooksResponsePo responsePo = new GetNotebooksResponsePo();
    if (isCoNotebook) {
      log.info("coNotebook查詢");
      responsePo.setNotebooks(notebookDAO.getCoNotebooks(po));
    } else {
      log.info("notebook查詢");
      responsePo.setNotebooks(notebookDAO.getNotebooks(po));
    }

    if (responsePo.getNotebooks().isEmpty()) {
      responsePo.setNextPage(false);
      return responsePo;
    }
    if (responsePo.getNotebooks().size() == po.getLimit() + 1) {
      responsePo.setNextPage(true);
      responsePo.getNotebooks().remove(responsePo.getNotebooks().size() - 1);
    } else {
      responsePo.setNextPage(false);
    }
    return responsePo;
  }

  private UserInfo getUserInfo(String Authorization){
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", Authorization);
    HttpEntity<UserInfo> requestEntity = new HttpEntity<>(headers);
    String url = "http://"+ authenticationServer +"/api/user/info";
    ResponseEntity<UserInfo> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,UserInfo.class);
    UserInfo body = response.getBody();
    return response.getBody();
  }
}
