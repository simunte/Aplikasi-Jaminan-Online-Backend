package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.domain.Role;
import com.ebizcipta.ajo.api.service.dto.DashboardDTO;
import com.ebizcipta.ajo.api.service.dto.DashboardITDTO;

import java.util.List;
import java.util.Optional;

public interface DashboardService {
    Optional<DashboardITDTO> getCountByStatus();
}
