package com.ebizcipta.ajo.api.service.dto;

import lombok.Data;

@Data
public class EmailRejectDTO {
    public String setFrom;
    public String recipient;
    public String subject;
    public String namaPemohon;
    public String noKontrak;
    public String noJaminan;
    public String tanggalTerbit;
    public String tanggalBerlaku;
    public String tanggalJatuhTempo;
    public String currency;
    public String nilaiJaminan;
    public String catatan;
}
