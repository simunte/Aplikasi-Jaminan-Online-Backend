package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.PasswordHistory;
import com.ebizcipta.ajo.api.domain.RoleTemp;
import com.ebizcipta.ajo.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    List<PasswordHistory> findByUser(User user);
}
