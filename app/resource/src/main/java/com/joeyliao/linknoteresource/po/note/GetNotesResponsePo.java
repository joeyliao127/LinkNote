package com.joeyliao.linknoteresource.po.note;

import com.joeyliao.linknoteresource.dto.note.NoteDTO;
import com.joeyliao.linknoteresource.dto.tag.TagDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetNotesResponsePo {
  private List<NoteDTO> notes;
  private List<TagDTO> tags;
  private String name;
  private String description;
  private String id;
  private Boolean nextPage;
}
