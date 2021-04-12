package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.Confirmation;
import com.ebizcipta.ajo.api.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long>{

    Optional<Confirmation> findByRegistration(Registration registration);

    Optional<Confirmation> findByRegistrationNomorJaminan(String nomotJaminan);

    Optional<Confirmation> findByNomorKonfirmasiBankAndRegistration(String nomorKonfirmasiBank, Registration registration);
}
