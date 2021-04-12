package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.MasterConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterConfigurationRepository extends JpaRepository<MasterConfiguration, Long> {
    List<MasterConfiguration> findByStatusAndModifiedByNot(String status, String userLogin);
    List<MasterConfiguration> findByStatusNot(String status);
    Optional <MasterConfiguration> findTop1ByStatusOrderByCreationDateDesc(String status);
    Optional <MasterConfiguration> findTop1ByStatusNotNullOrderByCreationDateDesc();

}
