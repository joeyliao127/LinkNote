package com.joeyliao.linknoteresource.po.note;

import com.joeyliao.linknoteresource.dto.note.NoteDTO;
import com.joeyliao.linknoteresource.dto.tag.TagDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetNoteResponsePo {
  private NoteDTO note;
  private List<TagDTO> tags;
}
