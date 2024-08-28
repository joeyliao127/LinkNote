package com.joeyliao.linknoteresource.po.generic;

import com.joeyliao.linknoteresource.enums.Behavior;
import lombok.Data;

@Data
public class AuthRequestBody {
    private String notebookId;
    private Behavior behavior;
}
