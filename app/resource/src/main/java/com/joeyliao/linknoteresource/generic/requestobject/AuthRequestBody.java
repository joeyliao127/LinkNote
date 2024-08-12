package com.joeyliao.linknoteresource.generic.requestobject;

import com.joeyliao.linknoteresource.generic.enums.Behavior;
import com.joeyliao.linknoteresource.generic.enums.Target;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
public class AuthRequestBody {
    private String notebookId;
    private Behavior behavior;
}
