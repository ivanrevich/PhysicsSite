package com.aivhich.Physics.dto.request;

import com.aivhich.Physics.models.FormType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdateFormRequest {
    @JsonProperty("form_id")
    UUID formId;

    @JsonProperty("page_id")
    UUID pageId;

    @JsonProperty("htmlText")
    String htmlText;

    @JsonProperty("type")
    FormType formType;

    @JsonProperty("answers")
    List<String> answers;

    @JsonProperty("rightAnswers")
    List<String> right;

}
