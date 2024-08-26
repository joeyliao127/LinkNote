package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.po.generic.UserInfo;
import com.joeyliao.linknoteresource.dao.InvitationDAO;
import com.joeyliao.linknoteresource.dto.invitation.ReceivedInvitationDTO;
import com.joeyliao.linknoteresource.dto.invitation.SentInvitationDTO;
import com.joeyliao.linknoteresource.exception.InvitationAlreadyExistException;
import com.joeyliao.linknoteresource.po.invitation.CreateInvitationPo;
import com.joeyliao.linknoteresource.po.invitation.DeleteInvitationPo;
import com.joeyliao.linknoteresource.po.invitation.GetInvitationRequestPo;
import com.joeyliao.linknoteresource.po.invitation.GetReceivedInvitationResponsePo;
import com.joeyliao.linknoteresource.po.invitation.GetSentInvitationResponsePo;
import com.joeyliao.linknoteresource.po.invitation.InvitationResponsePo;
import com.joeyliao.linknoteresource.po.invitation.UpdateInvitationPo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService {

  @Autowired
  InvitationDAO invitationDAO;

  @Autowired
  CollaboratorService collaboratorService;

  @Value("${authenticationServer}")
  private String authenticationServerPath;

  private final RestTemplate restTemplate = new RestTemplate();


  @Override
  public void createInvitation(CreateInvitationPo po) throws BadRequestException {
    log.info(
        "Invitor: " + po.getInviterEmail() + " invitee: " + po.getInviteeEmail() + " Notebook: "
            + po.getNotebookId());
    if (checkInvitationIsExist(po)) {
      log.info("Invitation已經存在");
      throw new InvitationAlreadyExistException("重複的Invitation");
    }
    log.info("Invitation不存在");
    log.info("驗證Email是否存在：" + po.getInviteeEmail());
    verifyEmailIsExist(po.getInviteeEmail());
    log.info("Email存在，新增Invitation");
    po.setInviterEmail(getUserInfoByToken(po.getAuthorization()).getEmail());
    invitationDAO.createInvitation(po);
    log.info("新增成功.");
  }

  private Boolean checkInvitationIsExist(CreateInvitationPo po) {
    List<Integer> list = invitationDAO.checkInvitationNotExist(po);
    log.info("Invitation check回傳結果：");
    return !list.isEmpty();
  }

  private void verifyEmailIsExist(String email) throws BadRequestException {
    String url = "http://" + authenticationServerPath + "/api/auth/user/email?email=" + email;
    log.info("請求驗證email的路徑：" + url);
    Map<String, Object> map = new HashMap<>();
    log.info("驗證invitee email: " + email);
    map.put("email", email);
    Boolean checkResponse = restTemplate.getForObject(url, Boolean.class, map);
    log.info("Email驗證結果：" + checkResponse);
    if (Boolean.FALSE.equals(checkResponse)) {
      log.info("無效的Email");
      throw new BadRequestException("Invalid email address");
    }
  }


  private UserInfo getUserInfoByToken(String authorization) {
    String url = "http://" + authenticationServerPath + "/api/user/info";
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", authorization);
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    UserInfo userInfo = restTemplate.exchange(url, HttpMethod.GET, httpEntity, UserInfo.class)
        .getBody();
    return userInfo;
  }

  @Override
  public GetSentInvitationResponsePo getSentInvitation(GetInvitationRequestPo po) {
    po.setLimit(po.getLimit() + 1);
    po.setUserEmail(getUserInfoByToken(po.getAuthorization()).getEmail());
    List<SentInvitationDTO> list = invitationDAO.getSentInvitation(po);
    GetSentInvitationResponsePo responsePo = new GetSentInvitationResponsePo();
    responsePo.setInvitations(list);
    setResponsePo(list, po.getLimit(), responsePo);
    return responsePo;
  }

  @Override
  public GetReceivedInvitationResponsePo getReceivedInvitation(GetInvitationRequestPo po) {
    po.setLimit(po.getLimit() + 1);
    po.setUserEmail(getUserInfoByToken(po.getAuthorization()).getEmail());
    List<ReceivedInvitationDTO> list = invitationDAO.getReceivedInvitation(po);
    GetReceivedInvitationResponsePo responsePo = new GetReceivedInvitationResponsePo();
    responsePo.setInvitations(list);
    setResponsePo(list, po.getLimit(), responsePo);
    return responsePo;
  }

  private void setResponsePo(List list, Integer limit, InvitationResponsePo po) {
    if ((list.size()) == limit) {
      list.remove(list.size() - 1);
      po.setNextPage(true);
    } else {
      po.setNextPage(false);
    }
  }

  @Override
  @Transactional
  public void updateInvitation(UpdateInvitationPo po) {
    po.setInviteeEmail(getUserInfoByToken(po.getAuthorization()).getEmail());
    po.setInviteeId(getUserInfoByToken(po.getAuthorization()).getUserId());
    invitationDAO.updateInvitation(po);
    collaboratorService.createCollaborator(po.getInviteeId(), po.getNotebookId());
  }


  @Override
  public void deleteInvitation(DeleteInvitationPo po) {
    String email = getUserInfoByToken(po.getAuthorization()).getEmail();
    po.setUserEmail(email);
    invitationDAO.deleteInvitation(po);
  }


}
