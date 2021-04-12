package com.ebizcipta.ajo.api.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Base entity class
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class Base implements Serializable {

	private static final long serialVersionUID = -7369920601847524273L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

    @CreatedDate
	@Column(name = "date_created", updatable = false)
	private Instant creationDate;

    @CreatedBy
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "date_modified", updatable = false)
    private Instant modificationDate;

    @LastModifiedBy
    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    @Column(name = "date_approved")
    private Instant approvedDate;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

	@Version
	@Column(name = "version")
	private Integer version = 0;

    @PrePersist
    public void prePersist() {
        this.creationDate = Instant.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.modificationDate = Instant.now();
    }
}
