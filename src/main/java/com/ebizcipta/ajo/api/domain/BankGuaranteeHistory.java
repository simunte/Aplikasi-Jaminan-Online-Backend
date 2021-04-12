package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_guarantee_history")
public class BankGuaranteeHistory extends Base{

    @ManyToOne
    @JoinColumn(name = "bank_guarantee")
    private Registration registration;

    @Column(name = "bank_guarantee_status")
    private String bgStatus;

    private String notes;
}
