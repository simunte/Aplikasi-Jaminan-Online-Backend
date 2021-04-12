package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.domain.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    List<UserHistory> findByUserOrderByCreationDateAsc(User user);
}
