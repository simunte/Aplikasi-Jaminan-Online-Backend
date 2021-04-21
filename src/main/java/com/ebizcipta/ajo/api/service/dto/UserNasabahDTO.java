package com.ebizcipta.ajo.api.service.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserNasabahDTO extends BaseDTO{
    private Long id;
    private String username;
    @JsonProperty(value = "complete_name")
    private String completeName;
    @JsonProperty(value = "company_name")
    private String companyName;
    private String position;
    @JsonProperty(value = "npwp_number")
    private String npwpNumber;
    @JsonProperty(value = "id_number")
    private String idNumber;
    @JsonProperty(value = "email")
    private String employeeEmail;
    @JsonProperty(value = "support_document")
    private List<SupportDocumentNasabahDTO> supportDocumentNasabahList;
}
