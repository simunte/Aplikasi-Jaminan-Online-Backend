package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.ColumnUser;
import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.ColumnUserRepository;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import com.ebizcipta.ajo.api.service.ColumnUserService;
import com.ebizcipta.ajo.api.service.dto.ColumnUserDTO;
import com.ebizcipta.ajo.api.service.mapper.ColumnUserMapper;
import com.ebizcipta.ajo.api.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ColumnUserServiceImpl implements ColumnUserService{
    @Autowired
    private ColumnUserRepository columnUserRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Boolean saveColumnUser(ColumnUserDTO columnUserDTO) {
        ColumnUser columnUser = new ColumnUser();
        Optional<User> user = userRepository.findTop1ByUsernameAndApprovedDateIsNotNullAndStatusNotOrderByCreationDateDesc(columnUserDTO.getUsername(), Constants.UserStatus.DELETED);
        if (user.isPresent()){
            Optional<ColumnUser> columnUserFound = columnUserRepository.findByUser(user.get());
            if (columnUserFound.isPresent()){
                columnUser = ColumnUserMapper.INSTANCE.toEntity(columnUserDTO, columnUserFound.get());
            }else {
                columnUser = ColumnUserMapper.INSTANCE.toEntity(columnUserDTO, new ColumnUser());
            }
            columnUser.setUser(user.get());
            columnUserRepository.save(columnUser);
        }else {
            throw new BadRequestAlertException("User Not Found", "","");
        }

        return Boolean.TRUE;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColumnUserDTO> findAllColumnUser() {
        return columnUserRepository.findAll().stream()
                .map(columnUser -> ColumnUserMapper.INSTANCE.toDto(columnUser, new ColumnUserDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ColumnUserDTO> findByUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findTop1ByUsernameAndApprovedDateIsNotNullAndStatusNotOrderByCreationDateDesc(authentication.getName(), Constants.UserStatus.DELETED);
        return columnUserRepository.findByUser(user.get())
                .map(columnUser -> ColumnUserMapper.INSTANCE.toDto(columnUser, new ColumnUserDTO()));
    }
}
