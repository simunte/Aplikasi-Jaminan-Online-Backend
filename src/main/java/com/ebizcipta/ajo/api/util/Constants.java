package com.ebizcipta.ajo.api.util;

public class Constants {
    public static class User {
        public static final String SYSTEM_ACCOUNT = "system";
        public static final String DEFAULT_LANGUAGE = "en";
        public static final String USERNAME_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        public static final String APPROVAL_TYPE_ACTIVATED = "AKTIVATION";
        public static final String APPROVAL_TYPE_DELETED = "DELETION";
    }

    public static class Privilege {
        public static final String ALL = "ALL";
        public static final String CREATE = "CREATE";
        public static final String READ = "READ";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "DELETE";
        public static final String APPROVE = "APPROVE";
        public static final String REJECT = "REJECT";
        public static final String AMEND = "AMEND";
    }

    public static class UserStatus{
        public static final String CREATE_NEW = "WAITING FOR APPROVAL";
        public static final String APPROVE_NEW = "ACTIVE";
        public static final String REJECT_NEW = "REJECTED";
        public static final String REJECT_RESET_PASSWORD = "REJECTED RESET PASSWORD";
        public static final String DELETED = "INACTIVE";
        public static final String REQ_DELETED = "WAITING FOR DELETION APPROVAL";
        public static final String LOCKED = "LOCKED";
        public static final String REQ_RESET_PASSWORD= "WAITING FOR APPROVAL RESET PASSWORD";
    }

    public static class BankGuaranteeStatus{
        public static final String BGFROMSTAGING = "BG FROM STAGING";
        public static final String DRAFT = "PENDING BG";
        public static final String WAITINGBGAPPROVAL = "WAITING FOR APPROVAL";
        public static final String WAITINGBGVERIFICATION = "WAITING FOR VERIFICATION";
        public static final String WAITINGBGVALIDATION = "WAITING FOR VALIDATION";
        public static final String WAITINGBGCONFIRMATION = "WAITING FOR CONFIRMATION";
        public static final String APPROVEDBG = "APPROVED BG";
        public static final String REJECT = "REJECTED BG";
        public static final String FAILED_BG = "FAILED";
        public static final String WAITINGBGSETTLEMENT = "WAITING CHECKER SETTLEMENT";
        public static final String SETTLEDBG = "SETTLED BG";
        public static final String REJECTEDBGSETTLEMENT = "REJECTED BG SETTLEMENT";
        public static final String DELETEDBG = "DELETED BG";
        public static final String EXPIRED_BG = "EXPIRED BG";
    }

    public static class RoleStatus{
        public static final String WAITINGAPPROVED = "WAITING FOR APPROVAL";
        public static final String WAITINGAPPROVEDELETE= "WAITING FOR DELETION APPROVAL";
        public static final String WAITINGAPPROVEUPDATE= "WAITING FOR UPDATE APPROVAL";
        public static final String ACTIVE = "ACTIVE";
        public static final String NONAKTIF = "INACTIVE";
        public static final String REJECT = "REJECTED";
        public static final String REJECTDELETE= "REJECT DELETED";
        public static final String REJECTUPDATE = "REJECT UPDATED";

    }

    public static class action{
        public static final String APPROVE = "APPROVE";
        public static final String REJECT = "REJECT";

        //use in user history
        public static final String CREATED_USER_BY_MAKER = "CREATED USER BY MAKER";
        public static final String REJECTED_USER_BY_CHECKER = "REJECTED USER BY CHECKER";
        public static final String APPROVED_USER_BY_CHECKER="APPROVED USER BY CHECKER";
        public static final String DELETED_USER_BY_MAKER  = "DELETED USER BY MAKER";
        public static final String DELETED_USER_BY_CHECKER = "DELETED USER BY CHECKER";
        public static final String REJECTED_DELETION_BY_CHECKER= "REJECTED DELETION BY CHECKER";
        public static final String PASSWORD_RESET_BY_MAKER= "RESET PASSWORD BY MAKER";
        public static final String APPROVED_PASSWORD_RESET_BY_MAKER= "APPROVED RESET PASSWORD BY MAKER";
        public static final String REJECTED_PASSWORD_RESET_BY_MAKER= "REJECTED RESET PASSWORD BY MAKER";
        public static final String UNLOCK_USER= "UNLOCK USER";


    }
    public static class registrationAction{
        public static final String APPROVE = "APPROVAL";
        public static final String VERIFIKASI = "VERIFICATION";
        public static final String VALIDASI = "VALIDATION";
        public static final String CONFIRM = "CONFIRMATION";
        public static final String SETTLEMENT = "SETTLEMENT";
        public static final String REJECT = "REJECTION";
    }

    public static class Role{
        public static final String BENEFICIARY_ADMIN_1 ="BENEFICIARY_ADMIN_1";
        public static final String BENEFICIARY_ADMIN_2 ="BENEFICIARY_ADMIN_2";
        public static final String BENEFICIARY_USER ="BENEFICIARY_USER";
        public static final String BANK_ADMIN_2_MAKER ="BANK_ADMIN_2_MAKER";
        public static final String BANK_ADMIN_2_CHECKER ="BANK_ADMIN_2_CHECKER";
        public static final String IT = "SYSTEM_OWNER";
        public static final String BANK_ADMIN_1_MAKER="BANK_ADMIN_MAKER";
        public static final String BANK_ADMIN_1_CHECKER="BANK_ADMIN_CHECKER";
        public static final String TRO_CHECKER="TRO_CHECKER";
        public static final String TRO_MAKER="TRO_MAKER";
        public static final String CHECKER = "CHECKER";
        public static final String MAKER = "MAKER";
        public static final String KOMITE = "KOMITE";
        public static final String NASABAH = "NASABAH";
    }

    public static class MasterDataStatus {
        public static final String WAITING_FOR_APPROVAL = "WAITING FOR APPROVAL";
        public static final String WAITING_FOR_DELETE_APPROVAL = "WAITING FOR DELETION APPROVAL";
        public static final String ACTIVAT = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String REJECT = "REJECTED";
    }

    public static class MenuReport {
        public static final String REKAPITULASI = "REKAPITULASI";
        public static final String VERIFIKASI = "VERIFIKASI";
        public static final String SETTLEMENT = "SETTLEMENT";
    }

    public static class ColumnDetail{
        public static final String COLUMN_LIST = "[{'name':'Nomor Jaminan','code':'nomor_jaminan','column_show':true},{'name':'Nomor Amendment','code':'nomor_amendment','column_show':false},{'name':'Applicant','code':'applicant','column_show':true},{'name':'Jenis Produk','code':'jenis_produk','column_show':true},{'name':'Jenis Jaminan','code':'jenis_jaminan','column_show':true},{'name':'Nama Beneficiary','code':'nama_beneficiary','column_show':true},{'name':'Unit Pengguna','code':'unit_pengguna','column_show':true},{'name':'Nama Bank Penerbit','code':'nama_bank_penerbit','column_show':true},{'name':'Alamat Bank Penerbit','code':'alamat_bank_penerbit','column_show':true},{'name':'Nomor Kontrak','code':'nomor_kontrak','column_show':true},{'name':'Uraian Pekerjaan','code':'uraian_pekerjaan','column_show':true},{'name':'Currency','code':'currency','column_show':true},{'name':'Nilai Jaminan','code':'nomor_jaminan','column_show':true},{'name':'Tanggal Terbit','code':'tanggal_terbit','column_show':true},{'name':'Tanggal Berlaku','code':'tanggal_berlaku','column_show':true},{'name':'Tanggal Berakhir','code':'tanggal_berakhir','column_show':true},{'name':'Tenggang Waktu Claim','code':'tenggang_waktu_claim','column_show':true},{'name':'Tanggal Batas Claim','code':'tanggal_batas_claim','column_show':true},{'name':'Bank Guarantee Status','code':'bank_guarantee_status','column_show':true}]";
    }

    public static class defaultPassword{
        public static final String PASSWORD ="welcome1";
    }

    public static class setUpConfiguration{
        public static final String CRON_SCHEDULER ="0 10 2 * * *";
        public static final String CRON_SCHEDULER_EOD ="* 20 1 * * *";
    }

    public static class Module {
        public static final String LOGIN = "Login Module";
        public static final String LOGOUT = "Logout Module";
        public static final String ERROR = "Error";
        public static final String USER = "User Module";
        public static final String PASSWORD_HISTORY = "Password History Module";
        public static final String ABG_WEBSERVICES = "ABG Web Services Module";
        public static final String URAIAN_PEKERJAAN = "Uraian Pekerjaan Module";
        public static final String UNIT_PENGGUNA = "Unit Pengguna Module";
        public static final String BANK_PENERBIT = "Bank Penerbit Module";
        public static final String BANK_GUARANTEE_HISTORY = "BG HISTORY Module";
        public static final String BENEFICIARY = "Beneficiary Module";
        public static final String REGISTRATION = "BG REGISTRATION Module";
        public static final String CONFIRMATION = "BG Confirmation Module";
        public static final String CURRENCY = "Currency Module";
        public static final String JENIS_JAMINAN = "Jenis Jaminan Module";
        public static final String JENIS_PRODUK = "Jenis Produk Module";
        public static final String MASTER_CONFIGURATION = "Master Configuration Module";
        public static final String ROLE = "Role Module";
        public static final String MAIL = "Email Module";
        public static final String AS_400 = "AS 400 Module";
        public static final String ALAMAT_BANK_PENERBIT = "Alamat Bank Penerbit";
    }

    public static class ExecutionType {
        public static final String START_EXECUTION = "START EXECUTION";
        public static final String END_EXECUTION = "END EXECUTION";
    }

    public static class Event {
        public static final String LOGIN = "LOGIN";
        public static final String LOGOUT = "LOGOUT";
        public static final String CREATE = "CREATE";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "DELETE";
        public static final String ACTIVATED = "ACTIVATED";
        public static final String DEACTIVATED = "DEACTIVATED";
        public static final String ERROR = "ERROR";
        public static final String APPROVAL = "APPROVAL";
        public static final String VERIFIKASI = "VERIFIKASI";
        public static final String SETTLEMENT = "SETTLEMENT";
        public static final String MAIL = "MAIL";
    }

    public static class Remark {

        public static final String STAGING_GET_DATA_TXNM = "Get and insert Data TXNM From AS 400";
        public static final String STAGING_GET_DATA_TXNH = "Get and insert Data TXNH From AS 400";
        public static final String STAGING_GET_DATA_TXNM_BACKUP = "Get and insert Data TXNM From AS 400 for Backup";
        public static final String STAGING_GET_DATA_TXNH_BACKUP = "Get and insert Data TXNH From AS 400 for Backup";


        public static final String LOGIN = "You are login";
        public static final String LOGOUT = "You are logout";
        public static final String ERROR = "Error";
        public static final String USER_FIRST_LOGIN = "First Login User";
        public static final String UPDATE_USER_FORCECHANGEPASS_OR_ACCOUNTLOCKED = "This user has been markeed to force changed password or account locked";

        public static final String CREATE_USER_WAITING_APPROVAL = "Create new user but still waiting approval";
        public static final String UPDATE_USER_WAITING_APPROVAL = "Update existing user but still waiting approval";
        public static final String DELETE_USER_WAITING_APPROVAL = "Inactive existing user but still waiting approval";
        public static final String CREATE_USER_STATUS_APPROVED = "Approved a request for new created user or updating user";
        public static final String CREATE_USER_STATUS_REJECTED = "Rejected a request for new created user or updating user";
        public static final String DELETE_USER_STATUS_APPROVED = "Approved a request for delete/ inactive user";
        public static final String DELETE_USER_STATUS_REJECTED = "Rejected a request for delete/ inactive user";

        public static final String RESET_PASSWORD_WAITING_APPROVAL = "Inactive existing user but still waiting approval";
        public static final String DELETE_PASSWORD_HISTORY = "Delete password history first index because greater than 5";

        public static final String CREATE_NEW_PASSWORD_HISTORY = "Create new password history";
        public static final String USER_CHANGED_PASSWORD = "This user changed the password";

        public static final String RESET_PASSWORD_STATUS_APPROVED = "Approved a request for reset password";
        public static final String RESET_PASSWORD_STATUS_REJECTED = "Rejected a request for reset password";

        public static final String WRONG_PASSWORD_COUNTER_ADD = "Add a counter for wrong password";
        public static final String WRONG_PASSWORD_ACCOUNT_LOCKED = "Account locked because of maximum invalid password has been reached";

        public static final String UNLOCKED_ACCOUNT = "This user was unlocked an account";

        public static final String CREATE_ABGWEBSERVICE_WAITING_APPROVAL = "Create ABG Web Service but waiting approval";
        public static final String UPDATE_ABGWEBSERVICE_WAITING_APPROVAL = "Update ABG Web Service but waiting approval";
        public static final String DELETE_ABGWEBSERVICE_WAITING_APPROVAL = "Delete ABG Web Service but waiting approval";
        public static final String CREATE_UPDATE_ABGWEBSERVICE_STATUS_APPROVED = "Approved a request for new data / updated data ABG Web Service";
        public static final String DELETE_ABGWEBSERVICE_STATUS_APPROVED = "Approved a request for delete data ABG Web Service";
        public static final String DELETE_ABGWEBSERVICE_STATUS_REJECTED = "Rejected a request for delete data ABG Web Service";
        public static final String CREATE_UPDATE_ABGWEBSERVICE_STATUS_REJECTED = "Rejected a request for new data / updated data updating ABG Web Service";

        public static final String UPDATE_URAIAN_PEKERJAAN = "Update existing uraian pekerjaan";
        public static final String CREATE_URAIAN_PEKERJAAN = "Create new data for uraian pekerjaan";

        public static final String CREATE_UNITPENGGUNA_WAITING_APPROVAL = "Create Unit Pengguna but waiting approval";
        public static final String UPDATE_UNITPENGGUNA_WAITING_APPROVAL = "Update Unit Pengguna but waiting approval";
        public static final String DELETE_UNITPENGGUNA_WAITING_APPROVAL = "Delete Unit Pengguna but waiting approval";
        public static final String CREATE_UPDATE_UNITPENGGUNA_STATUS_APPROVED = "Approved a request for new data / updated data Unit Pengguna";
        public static final String DELETE_UNITPENGGUNA_STATUS_APPROVED = "Approved a request for delete data Unit Pengguna";
        public static final String DELETE_UNITPENGGUNA_STATUS_REJECTED = "Rejected a request for delete data Unit Pengguna";
        public static final String CREATE_UPDATE_UNITPENGGUNA_STATUS_REJECTED = "Rejected a request for new data / updated data updating Unit Pengguna";

        public static final String CREATE_BANKPENERBIT_WAITING_APPROVAL = "Create Bank Penerbit but waiting approval";
        public static final String UPDATE_BANKPENERBIT_WAITING_APPROVAL = "Update Bank Penerbit but waiting approval";
        public static final String DELETE_BANKPENERBIT_WAITING_APPROVAL = "Delete Bank Penerbit but waiting approval";
        public static final String CREATE_UPDATE_BANKPENERBIT_STATUS_APPROVED = "Approved a request for new data / updated data Bank Penerbit";
        public static final String DELETE_BANKPENERBIT_STATUS_APPROVED = "Approved a request for delete data Bank Penerbit";
        public static final String DELETE_BANKPENERBIT_STATUS_REJECTED = "Rejected a request for delete data Bank Penerbit";
        public static final String CREATE_UPDATE_BANKPENERBIT_STATUS_REJECTED = "Rejected a request for new data / updated data updating Bank Penerbit";

        public static final String CREATE_BG_HISTORY = "Create BG History";
        public static final String UPDATE_BG_HISTORY = "Update BG History";
        public static final String CREATE_BG_HISTORY_MANUAL_DATA_TRANSFER_WAITING = "Create BG History from Manual Data Transfer status waiting for verification";
        public static final String CREATE_BG_HISTORY_MANUAL_DATA_TRANSFER_FAILED = "Create BG History from Manual Data Transfer status failed";

        public static final String CREATE_BENEFICIARY_WAITING_APPROVAL = "Create Beneficiary but waiting approval";
        public static final String UPDATE_BENEFICIARY_WAITING_APPROVAL = "Update Beneficiary but waiting approval";
        public static final String DELETE_BENEFICIARY_WAITING_APPROVAL = "Delete Beneficiary but waiting approval";
        public static final String CREATE_UPDATE_BENEFICIARY_STATUS_APPROVED = "Approved a request for new data / updated data Beneficiary";
        public static final String DELETE_BENEFICIARY_STATUS_APPROVED = "Approved a request for delete data Beneficiary";
        public static final String DELETE_BENEFICIARY_STATUS_REJECTED = "Rejected a request for delete data Beneficiary";
        public static final String CREATE_UPDATE_BENEFICIARY_STATUS_REJECTED = "Rejected a request for new data / updated data updating Beneficiary";

        public static final String CREATE_CURRENCY_WAITING_APPROVAL = "Create Currency but waiting approval";
        public static final String UPDATE_CURRENCY_WAITING_APPROVAL = "Update Currency but waiting approval";
        public static final String DELETE_CURRENCY_WAITING_APPROVAL = "Delete Currency but waiting approval";
        public static final String CREATE_UPDATE_CURRENCY_STATUS_APPROVED = "Approved a request for new data / updated data Currency";
        public static final String DELETE_CURRENCY_STATUS_APPROVED = "Approved a request for delete data Currency";
        public static final String DELETE_CURRENCY_STATUS_REJECTED = "Rejected a request for delete data Currency";
        public static final String CREATE_UPDATE_CURRENCY_STATUS_REJECTED = "Rejected a request for new data / updated data updating Currency";

        public static final String CREATE_JENIS_JAMINAN_WAITING_APPROVAL = "Create Jenis Jaminan but waiting approval";
        public static final String UPDATE_JENIS_JAMINAN_WAITING_APPROVAL = "Update Jenis Jaminan but waiting approval";
        public static final String DELETE_JENIS_JAMINAN_WAITING_APPROVAL = "Delete Jenis Jaminan but waiting approval";
        public static final String CREATE_UPDATE_JENIS_JAMINAN_STATUS_APPROVED = "Approved a request for new data / updated data Jenis Jaminan";
        public static final String DELETE_JENIS_JAMINAN_STATUS_APPROVED = "Approved a request for delete data Jenis Jaminan";
        public static final String DELETE_JENIS_JAMINAN_STATUS_REJECTED = "Rejected a request for delete data Jenis Jaminan";
        public static final String CREATE_UPDATE_JENIS_JAMINAN_STATUS_REJECTED = "Rejected a request for new data / updated data updating Jenis Jaminan";

        public static final String CREATE_JENIS_PRODUK_WAITING_APPROVAL = "Create Jenis Produk but waiting approval";
        public static final String UPDATE_JENIS_PRODUK_WAITING_APPROVAL = "Update Jenis Produk but waiting approval";
        public static final String DELETE_JENIS_PRODUK_WAITING_APPROVAL = "Delete Jenis Produk but waiting approval";
        public static final String CREATE_UPDATE_JENIS_PRODUK_STATUS_APPROVED = "Approved a request for new data / updated data Jenis Produk";
        public static final String DELETE_JENIS_PRODUK_STATUS_APPROVED = "Approved a request for delete data Jenis Produk";
        public static final String DELETE_JENIS_PRODUK_STATUS_REJECTED = "Rejected a request for delete data Jenis Produk";
        public static final String CREATE_UPDATE_JENIS_PRODUK_STATUS_REJECTED = "Rejected a request for new data / updated data updating Jenis Produk";

        public static final String CREATE_MASTER_CONFIGURATION_WAITING_APPROVAL = "Create Master Configuration but waiting approval";
        public static final String UPDATE_MASTER_CONFIGURATION_WAITING_APPROVAL = "Update Master Configuration Produk but waiting approval";
        public static final String DELETE_MASTER_CONFIGURATION_WAITING_APPROVAL = "Delete Master Configuration Produk but waiting approval";
        public static final String CREATE_UPDATE_MASTER_CONFIGURATION_STATUS_APPROVED = "Approved a request for new data / updated data Master Configuration";
        public static final String DELETE_MASTER_CONFIGURATION_STATUS_APPROVED = "Approved a request for delete data Master Configuration";
        public static final String DELETE_MASTER_CONFIGURATION_STATUS_REJECTED = "Rejected a request for delete data Master Configuration";
        public static final String CREATE_UPDATE_MASTER_CONFIGURATION_STATUS_REJECTED = "Rejected a request for new data / updated data updating Master Configuration";

        public static final String CREATE_CONFIRMATION_BG = "Create a confirmation of Bank Guarantee Registration";

        public static final String UPDATE_REGISTRATION_BG_STATUS_WAITING_APPROVAL = "Update status on BG Registration to waiting approval";
        public static final String UPDATE_REGISTRATION_BG_STATUS_DRAFT = "Update status on BG Registration to draft";
        public static final String UPDATE_REGISTRATION_BG_WAITING_VERIFICATION_MANUAL_DATA_TRANSFER  = "Update status on BG Registration to waiting verification from manual data transfer procedure";
        public static final String UPDATE_REGISTRATION_BG_FAILED_MANUAL_DATA_TRANSFER  = "Update status on BG Registration to failed on executing PLN ABG API from data transfer procedure";
        public static final String UPDATE_REGISTRATION_BG_SET_BG_APPROVED = "Update status BG Registration to approved BG";
        public static final String UPDATE_REGISTRATION_BG_SET_BG_REJECTED = "Update status BG Registration to rejected BG";
        public static final String UPDATE_REGISTRATION_BG_SET_BG_WAITING_VERIFICATION = "Update status BG Registration to waiting For verification BG";
        public static final String UPDATE_REGISTRATION_BG_SET_BG_VERIFIED = "Update status BG Registration to verified BG";
        public static final String UPDATE_REGISTRATION_BG_SET_BG_WAITING_SETTLEMENT = "Update status BG Registration to waiting for settlement BG";
        public static final String UPDATE_REGISTRATION_BG_SET_BG_SETTLED = "Update status BG Registration to settled BG";
        public static final String UPDATE_REGISTRATION_BG_SET_BG_DELETED = "Update status BG Registration to deleted BG";
        public static final String UPDATE_REGISTRATION_BG_SET_BG_DRAFT = "Update status BG Registration to Draft BG";
        public static final String UPDATE_REGISTRATION_BG_SET_BG_WAITINGAPPROVAL = "Update status BG Registration to waiting verification";
        public static final String CREATE_REGISTRATION_BG_SET_BG_DRAFT = "Create new BG Registration and still on draft";
        public static final String CREATE_REGISTRATION_BG_SET_BG_WAITINGAPPROVAL = "Create new BG Registration and waiting verification";
        public static final String UPDATE_REGISTRATION_BG_WAITING_VERIFICATION_AUTO_SCHEDULER = "Update status on BG Registration to waiting verification for auto scheduler";
        public static final String UPDATE_REGISTRATION_BG_FAILED_AUTO_SCHEDULER = "Update status on BG Registration to FAILED for auto scheduler";
        public static final String UPDATE_REGISTRATION_BG_VERIFIED_AUTO_SCHEDULER  = "Update status on BG Registration to AUTO VERIFIED BG for auto scheduler";
        public static final String UPDATE_REGISTRATION_BG_SETTLED_AUTO_SCHEDULER  = "Update status on BG Registration to AUTO SETTLED BG for auto scheduler";
        public static final String UPDATE_REGISTRATION_BG_EXPIRED_AUTO_SCHEDULER  = "Update status on BG Registration to AUTO EXPIRED BG for auto scheduler";

        public static final String SEND_EMAIL  = "Send Mail";

        public static final String DELETE_ROLE_WAITING_APPROVAL = "Delete Role but waiting approval";
        public static final String CREATE_ROLE_WAITING_APPROVAL = "Create Role but waiting approval";
        public static final String UPDATE_ROLE_WAITING_APPROVAL = "Update Role but waiting approval";
        public static final String CREATE_UPDATE_ROLE_APPROVED = "Approval a request role";
        public static final String CREATE_UPDATE_ROLE_REJECTED = "Rejected a request role";
        public static final String DELETE_ROLE_APPROVED = "Approval a request for delete role";
        public static final String DELETE_ROLE_REJECTED = "Rejected a request for delete role";
        public static final String CREATE_ROLE_TEMPORARY = "Create Role temporary";
        public static final String UPDATE_ROLE_TEMPORARY = "Update Role temporary";
    }

}
