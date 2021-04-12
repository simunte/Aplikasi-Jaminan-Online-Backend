package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Lob;

@Data
public class UserHistoryDTO {
    @JsonProperty(value = "created_date")
    public Long createdDate;

    @JsonProperty(value = "user_in_action")
    public String usersInAction;

    public String action;

    @JsonProperty(value = "user_saved")
    public String userSaved;

    @Lob
    public String note;
}
