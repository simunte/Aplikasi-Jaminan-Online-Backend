package com.ebizcipta.ajo.api.util;

import com.ebizcipta.ajo.api.domain.*;
import com.ebizcipta.ajo.api.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@PropertySource("classpath:configuration/master-data.properties")
public class MasterData {

    @Value("#{'${alamat.bank.penerbit.alamat}'.split(';')}")
    private List<String> alamatBankPenerbitNama;
    @Value("#{'${alamat.bank.penerbit.cabang}'.split(';')}")
    private List<String> alamatBankPenerbitCabang;
    @Value("#{'${alamat.bank.penerbit.code}'.split(';')}")
    private List<String> alamatBankPenerbitCode;

    @Value("#{'${beneficiary.code.beneficiary}'.split(';')}")
    private List<String> beneficiaryCode;
    @Value("#{'${beneficiary.nama.beneficiary}'.split(';')}")
    private List<String> beneficiaryName;
    @Value("#{'${beneficiary.auth.token.url}'.split(';')}")
    private List<String> beneficiaryAuthTokenUrl;
    @Value("#{'${beneficiary.input.jaminan.url}'.split(';')}")
    private List<String> beneficiaryInputJaminanUrl;
    @Value("#{'${beneficiary.validity.url}'.split(';')}")
    private List<String> beneficiaryValidityUrl;
    @Value("#{'${beneficiary.api.uname}'.split(';')}")
    private List<String> beneficiaryApiUsername;
    @Value("#{'${beneficiary.password.api}'.split(';')}")
    private List<String> beneficiaryApiPassword;
    @Value("#{'${beneficiary.client.id}'.split(';')}")
    private List<String> beneficiaryClientId;
    @Value("#{'${beneficiary.client.secret}'.split(';')}")
    private List<String> beneficiaryClientSecret;
    @Value("#{'${beneficiary.grant.type}'.split(';')}")
    private List<String> beneficiaryGrantType;

    @Value("#{'${currency.code}'.split(';')}")
    private List<String> currencyCode;

    @Value("#{'${jenis.jaminan.code}'.split(';')}")
    private List<String> jenisJaminanCode;
    @Value("#{'${jenis.jaminan.nama}'.split(';')}")
    private List<String> jenisJaminanName;
    @Value("#{'${jenis.jaminan.code.staging}'.split(';')}")
    private List<String> jenisJaminanStagingCode;


    @Value("#{'${jenis.produk.code}'.split(';')}")
    private List<String> jenisProdukCode;
    @Value("#{'${jenis.produk.nama}'.split(';')}")
    private List<String> jenisProdukName;

    @Value("#{'${master.configuration.password}'.split(';')}")
    private List<String> masterConfigurationPassword;
    @Value("#{'${master.configuration.smtp.hostname}'.split(';')}")
    private List<String> masterConfigurationSmtpHostname;
    @Value("#{'${master.configuration.smtp.port}'.split(';')}")
    private List<String> masterConfigurationSmtpPort;
    @Value("#{'${master.configuration.smtp.password}'.split(';')}")
    private List<String> masterConfigurationSmtpPassword;
    @Value("#{'${master.configuration.smtp.username}'.split(';')}")
    private List<String> masterConfigurationSmtpUsername;
    @Value("#{'${master.configuration.mailing.list}'.split(';')}")
    private List<String> masterConfigurationMailingList;

    @Value("#{'${unit.pengguna.code}'.split(';')}")
    private List<String> unitPenggunaCode;
    @Value("#{'${unit.pengguna.nama}'.split(';')}")
    private List<String> unitPenggunaName;

    private final AlamatBankPenerbitRepository alamatBankPenerbitRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final CurrencyRepository currencyRepository;
    private final JenisJaminanRepository jenisJaminanRepository;
    private final JenisProdukRepository jenisProdukRepository;
    private final MasterConfigurationRepository masterConfigurationRepository;
    private final UnitPenggunaRepository unitPenggunaRepository;
    private final UserRepository userRepository;
    private final ColumnUserRepository columnUserRepository;

    public MasterData(AlamatBankPenerbitRepository alamatBankPenerbitRepository, BeneficiaryRepository beneficiaryRepository, CurrencyRepository currencyRepository, JenisJaminanRepository jenisJaminanRepository, JenisProdukRepository jenisProdukRepository, MasterConfigurationRepository masterConfigurationRepository, UnitPenggunaRepository unitPenggunaRepository, UserRepository userRepository, ColumnUserRepository columnUserRepository) {
        this.alamatBankPenerbitRepository = alamatBankPenerbitRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.currencyRepository = currencyRepository;
        this.jenisJaminanRepository = jenisJaminanRepository;
        this.jenisProdukRepository = jenisProdukRepository;
        this.masterConfigurationRepository = masterConfigurationRepository;
        this.unitPenggunaRepository = unitPenggunaRepository;
        this.userRepository = userRepository;
        this.columnUserRepository = columnUserRepository;
    }

    public void insertMasterData(){
        insertAlamatBankPenerbit();
        insertBeneficiary();
        insertCurrency();
        insertJenisJaminan();
        insertJenisProduk();
        insertMasterConfiguration();
        insertUnitPengguna();
        insertColumUser();
    }

    private void insertAlamatBankPenerbit(){
        int index = 0;
        for (String s : alamatBankPenerbitCode){
            if (!alamatBankPenerbitRepository.findByCodeEqualsIgnoreCase(s).isPresent()){
                AlamatBankPenerbit alamatBankPenerbit = new AlamatBankPenerbit();
                alamatBankPenerbit.setCode(s);
                alamatBankPenerbit.setAlamatBankPenerbit(alamatBankPenerbitNama.get(index));
                alamatBankPenerbit.setCabang(alamatBankPenerbitCabang.get(index));
                alamatBankPenerbit.setActivated(Boolean.TRUE);
                alamatBankPenerbit.setStatus(Constants.MasterDataStatus.ACTIVAT);
                alamatBankPenerbit.setApprovedBy("system");
                alamatBankPenerbit.setApprovedDate(Instant.now());
                alamatBankPenerbitRepository.save(alamatBankPenerbit);
            }
            index++;
        }
    }


    private void insertBeneficiary(){
        int index = 0;
        for (String s : beneficiaryCode){
            if (!beneficiaryRepository.findByCodeBeneficiaryEqualsIgnoreCase(s).isPresent()){
                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setCodeBeneficiary(s);
                beneficiary.setNamaBeneficiary(beneficiaryName.get(index));
                beneficiary.setAuthTokenUrl(beneficiaryAuthTokenUrl.get(index));
                beneficiary.setInputJaminanUrl(beneficiaryInputJaminanUrl.get(index));
                beneficiary.setValidityJaminanUrl(beneficiaryValidityUrl.get(index));
                beneficiary.setUsername(beneficiaryApiUsername.get(index));
                beneficiary.setPassword(beneficiaryApiPassword.get(index));
                beneficiary.setClientId(beneficiaryClientId.get(index));
                beneficiary.setClientSecret(beneficiaryClientSecret.get(index));
                beneficiary.setGrantType(beneficiaryGrantType.get(index));
                beneficiary.setActivated(Boolean.TRUE);
                beneficiary.setStatus(Constants.MasterDataStatus.ACTIVAT);
                beneficiary.setApprovedBy("system");
                beneficiary.setApprovedDate(Instant.now());
                beneficiaryRepository.save(beneficiary);
            }
            index++;
        }
    }

    private void insertCurrency(){
        int index = 0;
        for (String s : currencyCode){
            if (!currencyRepository.findByCurrencyEqualsIgnoreCase(s).isPresent()){
                Currency currency = new Currency();
                currency.setCurrency(s);
                currency.setActivated(Boolean.TRUE);
                currency.setStatus(Constants.MasterDataStatus.ACTIVAT);
                currency.setApprovedBy("system");
                currency.setApprovedDate(Instant.now());
                currencyRepository.save(currency);
            }
            index++;
        }
    }

    private void insertJenisJaminan(){
        int index = 0;
        for (String s : jenisJaminanCode){
            if (!jenisJaminanRepository.findByCodeJaminanEqualsIgnoreCase(s).isPresent()){
                JenisJaminan jenisJaminan = new JenisJaminan();
                jenisJaminan.setCodeJaminan(s);
                jenisJaminan.setJenisJaminan(jenisJaminanName.get(index));
                jenisJaminan.setCodeJaminanStaging(jenisJaminanStagingCode.get(index));
                jenisJaminan.setActivated(Boolean.TRUE);
                jenisJaminan.setStatus(Constants.MasterDataStatus.ACTIVAT);
                jenisJaminan.setApprovedBy("system");
                jenisJaminan.setApprovedDate(Instant.now());
                jenisJaminanRepository.save(jenisJaminan);
            }
            index++;
        }
    }

    private void insertJenisProduk(){
        int index = 0;
        for (String s : jenisProdukCode){
            if (!jenisProdukRepository.findByCodeProdukEqualsIgnoreCase(s).isPresent()){
                JenisProduk jenisProduk = new JenisProduk();
                jenisProduk.setCodeProduk(s);
                jenisProduk.setJenisProduk(jenisProdukName.get(index));
                jenisProduk.setActivated(Boolean.TRUE);
                jenisProduk.setStatus(Constants.MasterDataStatus.ACTIVAT);
                jenisProduk.setApprovedBy("system");
                jenisProduk.setApprovedDate(Instant.now());
                jenisProdukRepository.save(jenisProduk);
            }
            index++;
        }
    }

    private void insertUnitPengguna(){
        int index = 0;
        for (String s : unitPenggunaCode){
            if (!unitPenggunaRepository.findByCodeUnitPenggunaEqualsIgnoreCase(s).isPresent()){
                UnitPengguna unitPengguna = new UnitPengguna();
                unitPengguna.setCodeUnitPengguna(s);
                unitPengguna.setUnitPengguna(unitPenggunaName.get(index));
                unitPengguna.setActivated(Boolean.TRUE);
                unitPengguna.setStatus(Constants.MasterDataStatus.ACTIVAT);
                unitPengguna.setApprovedBy("system");
                unitPengguna.setApprovedDate(Instant.now());
                unitPenggunaRepository.save(unitPengguna);
            }
            index++;
        }
    }

    private void insertMasterConfiguration(){
        int index = 0;
        List<MasterConfiguration> masterConfigurations = masterConfigurationRepository.findAll();
        if (masterConfigurations.size() < 1){
            MasterConfiguration masterConfiguration = new MasterConfiguration();
            masterConfiguration.setPassword(masterConfigurationPassword.get(index));
            masterConfiguration.setSmtpHostname(masterConfigurationSmtpHostname.get(index));
            masterConfiguration.setSmtpPort(masterConfigurationSmtpPort.get(index));
            masterConfiguration.setSmtpPassword(masterConfigurationSmtpPassword.get(index));
            masterConfiguration.setUsername(masterConfigurationSmtpUsername.get(index));
            masterConfiguration.setMailingList(masterConfigurationMailingList.get(index));
            masterConfiguration.setActivated(Boolean.TRUE);
            masterConfiguration.setStatus(Constants.MasterDataStatus.ACTIVAT);
            masterConfiguration.setApprovedBy("system");
            masterConfiguration.setApprovedDate(Instant.now());
            masterConfigurationRepository.save(masterConfiguration);
        }
    }

    private void insertColumUser(){
        List<User> users = userRepository.findByStatus(Constants.UserStatus.APPROVE_NEW);
        for (User user : users){
            if (!columnUserRepository.findByUser(user).isPresent()){
                ColumnUser columnUser = new ColumnUser();
                columnUser.setListColumn(Constants.ColumnDetail.COLUMN_LIST);
                columnUser.setUser(user);
                columnUserRepository.save(columnUser);
            }
        }
    }
}
