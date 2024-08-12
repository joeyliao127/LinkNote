package com.joeyliao.linknoteresource.note.po;

import com.joeyliao.linknoteresource.note.dto.NoteDTO;
import com.joeyliao.linknoteresource.tag.dto.TagDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetNotesResponsePo {
  private List<NoteDTO> notes;
  private List<TagDTO> tags;
  private String notebookName;
  private Boolean nextPage;
}
