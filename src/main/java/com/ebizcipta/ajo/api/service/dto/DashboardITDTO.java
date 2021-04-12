package com.ebizcipta.ajo.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DashboardITDTO {
    @JsonProperty(value = "user")
    private List<DashboardDTO> dashboardUser;
    @JsonProperty(value = "user_group")
    private List<DashboardDTO> dashboardUserGroup;
    @JsonProperty(value = "alamat_bank_penerbit")
    private List<DashboardDTO> dashboardAlamatBankPenerbit;
    @JsonProperty(value = "beneficiary")
    private List<DashboardDTO> dashboardBeneficiary;
    @JsonProperty(value = "currency")
    private List<DashboardDTO> dashboardCurrency;
    @JsonProperty(value = "jenis_jaminan")
    private List<DashboardDTO> dashboardJenisJaminan;
    @JsonProperty(value = "jenis_produk")
    private List<DashboardDTO> dashboardJenisProduk;
    @JsonProperty(value = "unit_pengguna")
    private List<DashboardDTO> dashboardUnitPengguna;
    @JsonProperty(value = "master_configuration")
    private List<DashboardDTO> dashboardMasterConfiguration;
    @JsonProperty(value = "bank_garansi")
    private List<DashboardDTO> dashboardBankGaransi;
}
