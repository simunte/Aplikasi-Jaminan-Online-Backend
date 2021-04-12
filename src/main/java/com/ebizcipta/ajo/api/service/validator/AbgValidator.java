package com.ebizcipta.ajo.api.service.validator;

import com.ebizcipta.ajo.api.domain.Beneficiary;
import com.ebizcipta.ajo.api.domain.Confirmation;
import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.util.DateUtil;
import org.springframework.stereotype.Component;

@Component
public class AbgValidator {
    public String getTokenAbgValidator(Beneficiary beneficiary){
        if (beneficiary.getGrantType().trim().isEmpty() || beneficiary.getGrantType() == null){
            return "Grantype tidak boleh kosong";
        }else if (beneficiary.getClientId().trim().isEmpty() || beneficiary.getClientId() == null){
            return "Client Id tidak boleh kosong";
        }else if (beneficiary.getClientSecret().trim().isEmpty() || beneficiary.getClientSecret() == null){
            return "Client Secret tidak boleh kosong";
        }else if (beneficiary.getUsername().trim().isEmpty() || beneficiary.getUsername() == null){
            return "Username tidak boleh kosong";
        }else if (beneficiary.getPassword().trim().isEmpty() || beneficiary.getPassword() == null){
            return "Password tidak boleh kosong";
        }else {
            return "SUCCESS";
        }
    }

    public String inputJaminanAbgValidator(Registration registration, User user, String fileName){
        if (registration.getAlamatBankPenerbit().getAlamatBankPenerbit().trim().isEmpty() || registration.getAlamatBankPenerbit().getAlamatBankPenerbit() == null){
            return "Alamat bank penerbit tidak boleh kosong";
        }else if (registration.getNomorJaminan().trim().isEmpty() || registration.getNomorJaminan()==null){
            return "Nomor jaminan tidak boleh kosong";
        }else if (registration.getJenisJaminan().getCodeJaminan().isEmpty() || registration.getJenisJaminan().getCodeJaminan() == null){
            return "Jenis Jaminan tidak boleh kosong";
        }else if (registration.getJenisProduk().getCodeProduk().isEmpty() || registration.getJenisProduk().getCodeProduk() == null){
            return "Jenis Produktidak boleh kosong";
        }else if (registration.getBeneficiary().getNamaBeneficiary().isEmpty() || registration.getBeneficiary().getNamaBeneficiary() == null){
            return "Beneficiary tidak boleh kosong";
        }else if (registration.getUnitPengguna().getUnitPengguna().isEmpty() || registration.getUnitPengguna().getUnitPengguna() == null){
            return "Unit Pengguna tidak boleh kosong";
        }else if (registration.getApplicant().isEmpty() || registration.getApplicant() == null){
            return "Applicant tidak boleh kosong";
        }else if (registration.getNomorKontrak().isEmpty() || registration.getNomorKontrak() == null){
            return "Nomor Kontrak tidak boleh kosong";
        }else if (registration.getUraianPekerjaan().isEmpty() || registration.getUraianPekerjaan() == null){
            return "Uraian Pekerjaan tidak boleh kosong";
        }else if (registration.getCurrency().getCurrency().isEmpty() || registration.getCurrency().getCurrency() == null){
            return "Currency tidak boleh kosong";
        }else if (registration.getNilaiJaminan().toString().isEmpty() || registration.getNilaiJaminan() == null){
            return "Nilai Jaminan tidak boleh kosong";
        }else if (fileName.isEmpty() || fileName == null){
            return "File tidak boleh kosong";
        }else if (user.getFirstName().isEmpty() || user.getFirstName() == null){
            return "Nama User tidak boleh kosong";
        }else if (user.getEmail().isEmpty() || user.getEmail() == null){
            return "Email tidak boleh kosong";
        }else {
            return "SUCCESS";
        }
    }

    public String validityJaminanAbgValidator(Confirmation confirmation, String fileName){
        if (confirmation.getNomorKonfirmasiBank().isEmpty() || confirmation.getNomorKonfirmasiBank() == null){
            return "Nomor Konfirmasi Bank tidak boleh kosong";
        }else if (confirmation.getRegistration().getNomorJaminan().isEmpty() || confirmation.getRegistration().getNomorJaminan() == null){
            return "Nomor Jaminan tidak boleh kosong";
        }else if (confirmation.getUser().getFirstName().isEmpty() || confirmation.getUser().getFirstName() == null){
            return "Nama Konfirmasi tidak boleh kosong";
        }else if (confirmation.getUser().getPosition().isEmpty() || confirmation.getUser().getPosition() == null){
            return "Jabatan tidak boleh kosong";
        }else if (fileName.isEmpty() || fileName == null){
            return "File tidak boleh kosong";
        }else {
            return "SUCCESS";
        }
    }
}
