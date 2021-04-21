package com.ebizcipta.ajo.api.repositories;

import com.ebizcipta.ajo.api.domain.Beneficiary;
import com.ebizcipta.ajo.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUsername(String username);
    Optional<User> findOneByUsernameAndIsEnabled(String username , Boolean enabled);
    Optional<User> findOneByIdAndIsEnabled(Long id , Boolean enabled);

    Optional<User> findTop1ByUsernameAndApprovedDateIsNotNullOrderByCreationDateDesc(String username);
    List<User> findByStatusAndApprovedByIsNotNull(String status);
    List<User> findByIsEnabled(Boolean enabled);
    List<User> findByStatus(String status);
    List<User> findByLastChangePasswordIsNotNullAndLastLoggedInIsNotNullAndStatusNot(String status);

    Optional<User> findTop1ByUsernameAndApprovedDateIsNotNullAndStatusNotOrderByCreationDateDesc(String username, String status);

    @Query(value = "SELECT * from users u \n" +
            "join user_role ur on u.id=ur.user_id \n" +
            "join role r on r.id=ur.role_id where r.role_create=:role " +
            "and u.is_enabled=1" , nativeQuery = true)
    List<User> getUserList(@Param("role") String role);

    @Query(value = "SELECT * from users u \n" +
            "join user_role ur on u.id=ur.user_id \n" +
            "join role r on r.id=ur.role_id where r.role_create=:role " +
            "and u.is_enabled=1" , nativeQuery = true)
    List<User> getUserListMaker(@Param("role") String role);

    @Query(value = "SELECT * from users u \n" +
            "join user_role ur on u.id=ur.user_id \n" +
            "join role r on r.id=ur.role_id where r.role_create=:role " +
            "and u.is_enabled=1" , nativeQuery = true)
    List<User> getUserListChecker(@Param("role") String role);

    @Query(value = "SELECT u.* from users u \n" +
            "join user_role ur on u.id=ur.user_id \n" +
            "join role r on r.id=ur.role_id where (r.role_create=:role) " +
            "and u.is_enabled=1" , nativeQuery = true)
    List<User> getUserListBenfMaker(@Param("role") String role);

    @Query(value = "SELECT u.* from users u \n" +
            "join user_role ur on u.id=ur.user_id \n" +
            "join role r on r.id=ur.role_id where (r.role_create=:role) " +
            "and u.is_enabled=1" , nativeQuery = true)
    List<User> getUserListBenfChecker(@Param("role") String role);

    @Query(value = "SELECT * from users u \n" +
            "join user_role ur on u.id=ur.user_id \n" +
            "join role r on r.id=ur.role_id where r.role_create=:role " +
            "and u.beneficiary=:benf and u.is_enabled=1" , nativeQuery = true)
    List<User> getUserListExternalMaker(@Param("role") String role , @Param("benf") Beneficiary benf);

    @Query(value = "SELECT * from users u \n" +
            "join user_role ur on u.id=ur.user_id \n" +
            "join role r on r.id=ur.role_id where r.role_create=:role " +
            "and u.beneficiary=:benf and u.is_enabled=1" , nativeQuery = true)
    List<User> getUserListExternalChecker(@Param("role") String role , @Param("benf") Beneficiary benf);

    //DASHBOARD
    @Query(value = "SELECT * from users u \n" +
            "join user_role ur on u.id=ur.user_id \n" +
            "join role r on r.id=ur.role_id where r.role_create=:role " +
            "and u.is_enabled=1 and u.status =:status and u.modified_by !=:username"
            , nativeQuery = true)
    List<User> getUserListITDashboard(@Param("role") String role, @Param("status") String status, @Param("username") String username);

    @Query(value = "SELECT * from users u \n" +
            "join user_role ur on u.id=ur.user_id \n" +
            "join role r on r.id=ur.role_id where r.role_create=:role " +
            "and u.is_enabled=1 and u.status =:status"
            , nativeQuery = true)
    List<User> getUserListGlobalDashboard(@Param("role") String role, @Param("status") String status);

    @Query(value = "SELECT u.* from users u \n" +
            "join user_role ur on u.id=ur.user_id \n" +
            "join role r on r.id=ur.role_id where (r.role_create=:role " +
            "or r.role_create='BENEFICIARY_ADMIN_1') " +
            "and u.is_enabled=1  and u.beneficiary=:benf and u.status =:status" , nativeQuery = true)
    List<User> getUserListBeneficiaryDashboard(@Param("role") String role , @Param("benf") Beneficiary benf, @Param("status") String status);

    //Privilege
    @Query(value = "SELECT COUNT(*) FROM USERS U, USER_ROLE UR, ROLE R, PRIVILEGE_ROLE PR, PRIVILEGE P, MENU M " +
            "WHERE U.ID = UR.USER_ID AND R.ID = UR.ROLE_ID AND PR.ROLE_ID = R.ID AND P.ID = PR.PRIVILEGE_ID AND M.ID = P.MENU_ID " +
            "AND U.ID=:user_id AND M.NAME=:menu AND P.CREATE_ACCESS=1" , nativeQuery = true)
    Long getUserMenuCreatePrivilege(@Param("user_id") Long userId, @Param("menu") String menu);

    @Query(value = "SELECT COUNT(*) FROM USERS U, USER_ROLE UR, ROLE R, PRIVILEGE_ROLE PR, PRIVILEGE P, MENU M " +
            "WHERE U.ID = UR.USER_ID AND R.ID = UR.ROLE_ID AND PR.ROLE_ID = R.ID AND P.ID = PR.PRIVILEGE_ID AND M.ID = P.MENU_ID " +
            "AND U.ID=:user_id AND M.NAME=:menu AND P.READ_ACCESS=1" , nativeQuery = true)
    Long getUserMenuReadPrivilege(@Param("user_id") Long userId, @Param("menu") String menu);

    @Query(value = "SELECT COUNT(*) FROM USERS U, USER_ROLE UR, ROLE R, PRIVILEGE_ROLE PR, PRIVILEGE P, MENU M " +
            "WHERE U.ID = UR.USER_ID AND R.ID = UR.ROLE_ID AND PR.ROLE_ID = R.ID AND P.ID = PR.PRIVILEGE_ID AND M.ID = P.MENU_ID " +
            "AND U.ID=:user_id AND M.NAME=:menu AND P.UPDATE_ACCESS=1" , nativeQuery = true)
    Long getUserMenuUpdatePrivilege(@Param("user_id") Long userId, @Param("menu") String menu);

    @Query(value = "SELECT COUNT(*) FROM USERS U, USER_ROLE UR, ROLE R, PRIVILEGE_ROLE PR, PRIVILEGE P, MENU M " +
            "WHERE U.ID = UR.USER_ID AND R.ID = UR.ROLE_ID AND PR.ROLE_ID = R.ID AND P.ID = PR.PRIVILEGE_ID AND M.ID = P.MENU_ID " +
            "AND U.ID=:user_id AND M.NAME=:menu AND P.DELETE_ACCESS=1" , nativeQuery = true)
    Long getUserMenuDeletePrivilege(@Param("user_id") Long userId, @Param("menu") String menu);

    @Query(value = "SELECT COUNT(*) FROM USERS U, USER_ROLE UR, ROLE R, PRIVILEGE_ROLE PR, PRIVILEGE P, MENU M " +
            "WHERE U.ID = UR.USER_ID AND R.ID = UR.ROLE_ID AND PR.ROLE_ID = R.ID AND P.ID = PR.PRIVILEGE_ID AND M.ID = P.MENU_ID " +
            "AND U.ID=:user_id AND M.NAME=:menu AND P.APPROVAL_ACCESS=1" , nativeQuery = true)
    Long getUserMenuApprovalPrivilege(@Param("user_id") Long userId, @Param("menu") String menu);

}
