package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class BankGuaranteeDTO {

    @JsonProperty(value = "registration_data")
    private ViewRegistrationDTO registrationDTO;

    @JsonProperty(value = "confirmation_data")
    private ViewConfirmationDTO confirmationDTO;
}
