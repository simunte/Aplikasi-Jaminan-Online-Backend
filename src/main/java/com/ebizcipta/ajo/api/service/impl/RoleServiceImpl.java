package com.ebizcipta.ajo.api.service.impl;


import com.ebizcipta.ajo.api.domain.*;
import com.ebizcipta.ajo.api.exception.AjoException;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.*;
import com.ebizcipta.ajo.api.service.RoleService;
import com.ebizcipta.ajo.api.service.dto.*;
import com.ebizcipta.ajo.api.service.mapper.MenuBaseMapper;
import com.ebizcipta.ajo.api.service.mapper.MenuListMapper;
import com.ebizcipta.ajo.api.service.mapper.PrivilegeMapper;
import com.ebizcipta.ajo.api.service.mapper.RoleListMapper;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService{
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    BeneficiaryRepository beneficiaryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleTempRepository roleTempRepository;
    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional
    public Boolean saveRoleAndAndPrivelegeMenu(CreateRoleDTO createRoleDTO) {

        RoleTemp roleTemp = roleTempRepository.findByRoleId(createRoleDTO.getId().intValue());
        String createdByTmp  =roleTemp.getCreatedBy();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Privilege> privileges = createRoleDTO.getPrivilegeList() == null ? null : createRoleDTO.getPrivilegeList().stream()
                .map(privilegeDTO -> {
                    Menu menu = menuRepository.findById(privilegeDTO.getMenu())
                    .orElseThrow(()-> new EntityNotFoundException("Menu Not Found"));
                    Privilege privilege = PrivilegeMapper.INSTANCE.toEntity(privilegeDTO);
                    privilege.setMenu(menu);
                    return privilegeRepository.save(privilege);
                }).collect(Collectors.toCollection(LinkedList::new));

        Role role = new Role();
        if(null != createRoleDTO.getId()){
            role = roleRepository.findById(createRoleDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Role Not Found"));
            //delete
            List<Privilege> privilegeList = role.getPrivileges();
            role.setPrivileges(null);
            roleRepository.saveAndFlush(role);
            privilegeList.stream()
                    .forEach(privilege -> privilegeRepository.delete(privilege));
        }
        role.setName(createRoleDTO.getName());
        role.setDescription(createRoleDTO.getDescription());
        role.setActivated(createRoleDTO.getActivated());
        role.setStatus(Constants.RoleStatus.ACTIVE);
        role.setPrivileges(privileges);
        roleRepository.save(role);

        //TODO AUDIT TRAIL
        auditTrailUtil.saveAudit(
                createRoleDTO.getId() != null || createRoleDTO.getId().toString().isEmpty() ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.ROLE,
                new JSONObject(roleTemp).toString(),
                new JSONObject(role).toString(),
                createRoleDTO.getId() != null || createRoleDTO.getId().toString().isEmpty() ? Constants.Remark.CREATE_ROLE_WAITING_APPROVAL : Constants.Remark.UPDATE_ROLE_WAITING_APPROVAL,
                authentication.getName()
        );

        roleTempRepository.delete(roleTemp);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean saveRoleTemp(RoleTempDTO roleTempDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLoggedIn= authentication.getName();

        Optional<Role> role = roleRepository.findById(roleTempDTO.getRoleId().longValue());
        Role getRole =  role.get();
        Role oldRole = (Role) SerializationUtils.clone(getRole);
        if(roleTempDTO.getIsReject()){
            getRole.setNote(roleTempDTO.getNote());
            getRole.setStatus(Constants.RoleStatus.REJECT);
        }else{
            getRole.setStatus(Constants.RoleStatus.WAITINGAPPROVED);
        }
        roleRepository.save(getRole);

        //TODO AUDIT TRAIL
        auditTrailUtil.saveAudit(
                Constants.Event.APPROVAL,
                Constants.Module.ROLE,
                new JSONObject(oldRole).toString(),
                new JSONObject(getRole).toString(),
                roleTempDTO.getIsReject() ? Constants.Remark.CREATE_UPDATE_ROLE_REJECTED : Constants.Remark.CREATE_UPDATE_ROLE_APPROVED,
                authentication.getName()
        );

        //save temp
        RoleTemp isExist = roleTempRepository.findByRoleId(roleTempDTO.getRoleId());
        if(isExist != null){
            isExist.setName(roleTempDTO.getName());
            isExist.setNote(roleTempDTO.getNote());
            isExist.setRoleId(roleTempDTO.getRoleId());
            isExist.setData(roleTempDTO.getData());
            roleTempRepository.save(isExist);
        }else{
            //first re-submit
            RoleTemp roleTemp = new RoleTemp();
            roleTemp.setRoleId(roleTempDTO.getRoleId());
            roleTemp.setData(roleTempDTO.getData());
            roleTemp.setNote(roleTempDTO.getNote());
            roleTemp.setName(roleTempDTO.getName());
            roleTempRepository.save(roleTemp);
        }
        return Boolean.TRUE;

    }

    @Override
    @Transactional
    public RoleTempDTO roleTemp(Integer id){
        RoleTemp roleTemp = roleTempRepository.findByRoleId(id);
        RoleTempDTO roleTempDTO = new RoleTempDTO();
        roleTempDTO.setRoleId(roleTemp.getRoleId());
        roleTempDTO.setData(roleTemp.getData());
        return  roleTempDTO;
    }

    @Transactional
    public Boolean approveOrRejectRole(ApprovalRoleDTO approvalRoleDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String approvedBy= authentication.getName();
        Optional<Role> role = roleRepository.findById(approvalRoleDTO.id.longValue());
        Role role1=role.get();
        Role oldRole = (Role) SerializationUtils.clone(role1);
        String remark = null;
        if(role1.getStatus().equals(Constants.RoleStatus.WAITINGAPPROVED)){
            if(approvalRoleDTO.getAction().equals(Constants.action.APPROVE)){
                role1.setStatus(Constants.RoleStatus.ACTIVE);
                role1.setApprovedDate(Instant.now());
                role1.setApprovedBy(approvedBy);
                remark = Constants.Remark.CREATE_UPDATE_ROLE_APPROVED;
            }else if(approvalRoleDTO.getAction().equals(Constants.action.REJECT)){
                roleRepository.delete(role1);
                remark = Constants.Remark.DELETE_ROLE_APPROVED;
            }
        }else if(role1.getStatus().equals(Constants.RoleStatus.WAITINGAPPROVEDELETE)){
            if(approvalRoleDTO.getAction().equals(Constants.action.APPROVE)){
                role1.setStatus(Constants.RoleStatus.NONAKTIF);
                role1.setActivated(Boolean.FALSE);
                role1.setApprovedDate(Instant.now());
                role1.setApprovedBy(approvedBy);
                remark = Constants.Remark.DELETE_ROLE_APPROVED;
            }else if(approvalRoleDTO.getAction().equals(Constants.action.REJECT)){
                if(approvalRoleDTO.getAction().equals(Constants.action.REJECT)){
                    role1.setStatus(Constants.RoleStatus.ACTIVE);
                    remark = Constants.Remark.DELETE_ROLE_REJECTED;
                }
            }
        }

        //TODO AUDIT TRAIL
        auditTrailUtil.saveAudit(
                Constants.Event.APPROVAL,
                Constants.Module.ROLE,
                new JSONObject(oldRole).toString(),
                new JSONObject(role1).toString(),
                remark,
                authentication.getName()
        );
        return Boolean.TRUE;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleListDTO> findAllRoleAndPrivelegeMenu(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<RoleListDTO> roleList = null;
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            roleList = roleRepository.findAll().stream()
                    .map(role -> {
                        RoleListDTO roleListDTO = RoleListMapper.INSTANCE.toDto(role, new RoleListDTO());
                        List<MenuListDTO> menuListDTOS = role.getPrivileges().stream()
                                .map(privilege -> {
                                    MenuListDTO menuListDTO = MenuListMapper.INSTANCE.toDto(privilege.getMenu());
                                    menuListDTO.setCreate(privilege.getCreate());
                                    menuListDTO.setDelete(privilege.getDelete());
                                    menuListDTO.setRead(privilege.getRead());
                                    menuListDTO.setUpdate(privilege.getUpdate());
                                    menuListDTO.setApproval(privilege.getApproval());
                                    return menuListDTO;
                                }).collect(Collectors.toCollection(LinkedList::new));
                        roleListDTO.setMenuList(menuListDTOS);
                        return roleListDTO;
                    }).collect(Collectors.toCollection(LinkedList::new));
        }else {
            roleList = roleRepository.findByStatusAndModifiedByNot(status, authentication.getName()).stream()
                    .map(role -> {
                        RoleListDTO roleListDTO = RoleListMapper.INSTANCE.toDto(role, new RoleListDTO());
                        List<MenuListDTO> menuListDTOS = role.getPrivileges().stream()
                                .map(privilege -> {
                                    MenuListDTO menuListDTO = MenuListMapper.INSTANCE.toDto(privilege.getMenu());
                                    menuListDTO.setCreate(privilege.getCreate());
                                    menuListDTO.setDelete(privilege.getDelete());
                                    menuListDTO.setRead(privilege.getRead());
                                    menuListDTO.setUpdate(privilege.getUpdate());
                                    menuListDTO.setApproval(privilege.getApproval());
                                    return menuListDTO;
                                }).collect(Collectors.toCollection(LinkedList::new));
                        roleListDTO.setMenuList(menuListDTOS);
                        return roleListDTO;
                    }).collect(Collectors.toCollection(LinkedList::new));
        }
        return roleList;
    }

    @Override
    @Transactional
    public List<RoleListDTO> findAllRoleAndPrivelegeMenuTempAndReal(String status){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Role> listReal = null;
        List<RoleListDTO> dtos= new ArrayList<>();
        if (status == null || status == "" || status.equalsIgnoreCase("null")){
            String username =  authentication.getName();
            Optional<User> user = userRepository.findOneByUsernameAndIsEnabled(username, true);
            String roleNameLoggedIn  = user.get().getRoles().get(0).getCode();
            String[] listStatus = null;
            listReal = roleRepository.findAll();
            for(int i=0; i<listReal.size(); i++){
                RoleListDTO dto = new RoleListDTO();
                RoleTemp roleTemp = roleTempRepository.findByRoleId(listReal.get(i).getId().intValue());
                if(roleTemp != null){
                    dto.setName(roleTemp.getName());
                }else{
                    dto.setName(listReal.get(i).getName());
                }
                dto.setModifiedBy(listReal.get(i).getModifiedBy());
                dto.setModifiedDate(listReal.get(i).getModificationDate().toEpochMilli());
                dto.setNote(listReal.get(i).getNote());
                dto.setId(listReal.get(i).getId());
                dto.setActivated(listReal.get(i).getActivated());
                dto.setStatus(listReal.get(i).getStatus());
                dtos.add(dto);
            }
        }else {
            listReal = roleRepository.findByStatusAndModifiedByNot(status, authentication.getName());
            for(int i=0; i<listReal.size(); i++){
                RoleListDTO dto = new RoleListDTO();
                RoleTemp roleTemp = roleTempRepository.findByRoleId(listReal.get(i).getId().intValue());
                if(roleTemp != null){
                    dto.setName(roleTemp.getName());
                }else{
                    dto.setName(listReal.get(i).getName());
                }
                dto.setModifiedBy(listReal.get(i).getModifiedBy());
                dto.setModifiedDate(listReal.get(i).getModificationDate().toEpochMilli());
                dto.setNote(listReal.get(i).getNote());
                dto.setId(listReal.get(i).getId());
                dto.setActivated(listReal.get(i).getActivated());
                dto.setStatus(listReal.get(i).getStatus());
                dtos.add(dto);
            }
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public List<RoleListDTO> findAllRoleByUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();
        Optional<User> users = userRepository.findOneByUsernameAndIsEnabled(userLogin ,true);
        User userCreator = users.get();
        String validateRole = userCreator.getRoles().get(0).getName();
        List<RoleListDTO> roleList = roleRepository.findByRoleCreateLikeIgnoreCase(validateRole).stream()
                .map(role -> {
                    RoleListDTO roleListDTO = RoleListMapper.INSTANCE.toDto(role, new RoleListDTO());
                    List<MenuListDTO> menuListDTOS = role.getPrivileges().stream()
                            .map(privilege -> {
                                MenuListDTO menuListDTO = MenuListMapper.INSTANCE.toDto(privilege.getMenu());
                                menuListDTO.setCreate(privilege.getCreate());
                                menuListDTO.setDelete(privilege.getDelete());
                                menuListDTO.setRead(privilege.getRead());
                                menuListDTO.setUpdate(privilege.getUpdate());
                                menuListDTO.setApproval(privilege.getApproval());
                                return menuListDTO;
                            }).collect(Collectors.toCollection(LinkedList::new));
                    roleListDTO.setMenuList(menuListDTOS);
                    return roleListDTO;
                }).collect(Collectors.toCollection(LinkedList::new));
        return roleList;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleListDTO> findDetailRoleAndPrivelegeMenu(Integer roleId) {
        return roleRepository.findById(roleId.longValue())
                .map(role -> {
                    RoleListDTO roleListDTO = RoleListMapper.INSTANCE.toDto(role, new RoleListDTO());
                    List<MenuListDTO> menuListDTOS = role.getPrivileges().stream()
                            .map(privilege -> {
                                MenuListDTO menuListDTO = MenuListMapper.INSTANCE.toDto(privilege.getMenu());
                                menuListDTO.setCreate(privilege.getCreate());
                                menuListDTO.setDelete(privilege.getDelete());
                                menuListDTO.setRead(privilege.getRead());
                                menuListDTO.setUpdate(privilege.getUpdate());
                                menuListDTO.setApproval(privilege.getApproval());
                                return menuListDTO;
                            }).collect(Collectors.toCollection(LinkedList::new));
                    roleListDTO.setMenuList(menuListDTOS);
                    return roleListDTO;
                });
    }

    @Override
    @Transactional (readOnly = true)
    public List<MenuListDTO> findAllMenu(){
        return menuRepository.findAll().stream()
                .map(menu -> MenuBaseMapper.INSTANCE.toDto(menu))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleListDTO> findAllRole(){
        List<RoleListDTO> roleList = roleRepository.findAll().stream()
                .map(role -> RoleListMapper.INSTANCE.toDto(role , new RoleListDTO()))
                .collect(Collectors.toCollection(LinkedList::new));

        return roleList;
    }

    @Transactional
    public Boolean deleteRole(Integer[] listId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for(int i=0; i<listId.length; i++){
            Optional<Role> role = roleRepository.findById(listId[i].longValue());
            if(role.isPresent()){
                Role role1 = role.get();
                Role oldRole = (Role) SerializationUtils.clone(role1);
                role1.setStatus(Constants.RoleStatus.WAITINGAPPROVEDELETE);

                //TODO AUDIT TRAIL
                auditTrailUtil.saveAudit(
                        Constants.Event.DELETE,
                        Constants.Module.ROLE,
                        new JSONObject(oldRole).toString(),
                        new JSONObject(role1).toString(),
                        Constants.Remark.DELETE_ROLE_WAITING_APPROVAL,
                        authentication.getName()
                );
            }

        }
        return Boolean.TRUE;
    }
}
