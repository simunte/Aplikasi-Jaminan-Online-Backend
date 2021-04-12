package com.ebizcipta.ajo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_guarantee_txnh_temp")
public class BankGuaranteeTxnhTemp extends Base {
    @Column(name = "C001_TXNH_TXN_NBR", length =15)
    private String C001_TXNH_TXN_NBR;

    @Column(name = "C002_TXNH_VER_NBR", length =3)
    private String C002_TXNH_VER_NBR;

    @Column(name = "C003_TXNH_REF_NBR", length =14)
    private String C003_TXNH_REF_NBR;

    @Column(name = "C004_CUS_SRC_KEY", length =20)
    private String C004_CUS_SRC_KEY;

    @Column(name = "C005_CUS_CIF_NBR", length =20)
    private String C005_CUS_CIF_NBR;

    @Column(name = "C006_CUS_CIF_IND", length =1)
    private String C006_CUS_CIF_IND;

    @Column(name = "C007_ACC_SRC_KEY", length =20)
    private String C007_ACC_SRC_KEY;

    @Column(name = "C008_TXNH_PROD_CD", length =10)
    private String C008_TXNH_PROD_CD;

    @Column(name = "C009_TXNH_FAC_CD", length =10)
    private String C009_TXNH_FAC_CD;

    @Column(name = "C010_TXNH_TXN_TYP_CD", length =10)
    private String C010_TXNH_TXN_TYP_CD;

    @Column(name = "C011_TXNH_GL_COST_CTR", length =4)
    private String C011_TXNH_GL_COST_CTR;

    @Column(name = "C012_TXNH_GL_CORP_CD", length =4)
    private String C012_TXNH_GL_CORP_CD;

    @Column(name = "C013_TXNH_GL_CD", length =10)
    private String C013_TXNH_GL_CD;

    @Column(name = "C014_TXNH_PST_DT", length =8)
    private String C014_TXNH_PST_DT;

    @Column(name = "C015_TXNH_PST_TIM", length =8)
    private String C015_TXNH_PST_TIM;

    @Column(name = "C016_TXNH_BNK_CD", length =2)
    private String C016_TXNH_BNK_CD;

    @Column(name = "C017_TXNH_BR_CD", length =3)
    private String C017_TXNH_BR_CD;

    @Column(name = "C018_TXNH_CHNL_CD", length =15)
    private String C018_TXNH_CHNL_CD;

    @Column(name = "C019_TXNH_GL_CCY_CD", length =3)
    private String C019_TXNH_GL_CCY_CD;

    @Column(name = "C020_TXNH_TXN_CCY_CD", length =3)
    private String C020_TXNH_TXN_CCY_CD;

    @Column(name = "C021_TXNH_TXN_MNR_CD", length =3)
    private String C021_TXNH_TXN_MNR_CD;

    @Column(name = "C022_TXNH_TXN_STS", length =1)
    private String C022_TXNH_TXN_STS;

    @Column(name = "C023_TXNH_TXN_AMT", length =18)
    private String C023_TXNH_TXN_AMT;

    @Column(name = "C024_TXNH_GL_EXCH_RT", length =18)
    private String C024_TXNH_GL_EXCH_RT;

    @Column(name = "C025_TXNH_TXN_EXCH_RT", length =18)
    private String C025_TXNH_TXN_EXCH_RT;

    @Column(name = "C026_TXNH_DR_CR_IND", length =1)
    private String C026_TXNH_DR_CR_IND;

    @Column(name = "C027_TXNH_TXN_DT", length =8)
    private String C027_TXNH_TXN_DT;

    @Column(name = "C028_TXNH_TXN_TIM", length =8)
    private String C028_TXNH_TXN_TIM;

    @Column(name = "C029_TXNH_SYS_CD", length =5)
    private String C029_TXNH_SYS_CD;

    @Column(name = "C030_TXNH_EXT_DT", length =8)
    private String C030_TXNH_EXT_DT;

    @Column(name = "C031_USR_DEF_1", length =60)
    private String C031_USR_DEF_1;

    @Column(name = "C032_USR_DEF_2", length =60)
    private String C032_USR_DEF_2;

    @Column(name = "C033_USR_DEF_3", length =60)
    private String C033_USR_DEF_3;

    @Column(name = "C034_USR_DEF_4", length =11)
    private String C034_USR_DEF_4;

    @Column(name = "C035_CTRY_REFNO", length =16)
    private String C035_CTRY_REFNO;

    @Column(name = "C036_TXNH_ORG_PROD_CD", length =3)
    private String C036_TXNH_ORG_PROD_CD;

    @Column(name = "C037_TXNH_COL_CHG_CCY", length =3)
    private String C037_TXNH_COL_CHG_CCY;

    @Column(name = "C038_USR_DEF_4B", length =27)
    private String C038_USR_DEF_4B;

    @Column(name = "C039_USR_DEF_5", length =60)
    private String C039_USR_DEF_5;

    @Column(name = "C040_USR_DEF_DT_1", length =8)
    private String C040_USR_DEF_DT_1;

    @Column(name = "C041_USR_DEF_DT_2", length =8)
    private String C041_USR_DEF_DT_2;

    @Column(name = "C042_USR_DEF_TIM_3", length =8)
    private String C042_USR_DEF_TIM_3;

    @Column(name = "C043_USR_DEF_11", length =18)
    private String C043_USR_DEF_11;

    @Column(name = "C044_USR_DEF_12", length =18)
    private String C044_USR_DEF_12;

    @Column(name = "C045_USR_DEF_13", length =18)
    private String C045_USR_DEF_13;

    @Column(name = "C046_USR_DEF_14", length =18)
    private String C046_USR_DEF_14;

    @Column(name = "C047_TXNH_COL_CHG_AMT", length =18)
    private String C047_TXNH_COL_CHG_AMT;

    @Column(name = "C048_USR_DEF_16", length =18)
    private String C048_USR_DEF_16;

    @Column(name = "C049_USR_DEF_17", length =18)
    private String C049_USR_DEF_17;

    @Column(name = "C050_REC_TYPE", length =1)
    private String C050_REC_TYPE;

    @Column(name = "C051_TXNH_TYP", length =1)
    private String C051_TXNH_TYP;

    @Column(name = "C052_TXNH_SUB_TYP", length =30)
    private String C052_TXNH_SUB_TYP;

    @Column(name = "C053_TXNH_MKR_ID", length =30)
    private String C053_TXNH_MKR_ID;

    @Column(name = "C054_TXNH_CKR_ID", length =30)
    private String C054_TXNH_CKR_ID;

    @Column(name = "C055_TXNH_CHNL_TYP", length =30)
    private String C055_TXNH_CHNL_TYP;

    @Column(name = "C056_TXNH_MAT_DT", length =8)
    private String C056_TXNH_MAT_DT;

    @Column(name = "C057_TXNH_ENT_DT", length =8)
    private String C057_TXNH_ENT_DT;

    @Column(name = "C058_TXNH_ENT_TIM", length =6)
    private String C058_TXNH_ENT_TIM;

    @Column(name = "C059_TXNH_PRN_LCY", length =18)
    private String C059_TXNH_PRN_LCY;

    @Column(name = "C060_TXNH_PRN_OCY", length =18)
    private String C060_TXNH_PRN_OCY;

    @Column(name = "C061_TXNH_DR_MOD", length =8)
    private String C061_TXNH_DR_MOD;

    @Column(name = "C062_TXNH_CR_MOD", length =8)
    private String C062_TXNH_CR_MOD;

    @Column(name = "C063_TXNH_PEN", length =18)
    private String C063_TXNH_PEN;

    @Column(name = "C064_TXNH_COMM", length =18)
    private String C064_TXNH_COMM;

    @Column(name = "C065_TXNH_CHARGES", length =18)
    private String C065_TXNH_CHARGES;

    @Column(name = "C066_TXNH_TXN_BNK_CD", length =2)
    private String C066_TXNH_TXN_BNK_CD;

    @Column(name = "C067_TXNH_TXN_BR_CD", length =3)
    private String C067_TXNH_TXN_BR_CD;

    @Column(name = "C068_REL_REF_NBR", length =11)
    private String C068_REL_REF_NBR;

    @Column(name = "C069_MAIN_FX_CODE", length =3)
    private String C069_MAIN_FX_CODE;

    @Column(name = "C070_SUB_FX_CODE", length =3)
    private String C070_SUB_FX_CODE;

    @Column(name = "C071_FX_DESCRP_1", length =40)
    private String C071_FX_DESCRP_1;

    @Column(name = "C072_FX_DESCRP_2", length =40)
    private String C072_FX_DESCRP_2;

    @Column(name = "C073_FX_DESCRP_3", length =40)
    private String C073_FX_DESCRP_3;

    @Column(name = "C074_CTR_PARTYNM", length =35)
    private String C074_CTR_PARTYNM;

    @Column(name = "C075_CTR_PARTY_LOC", length =6)
    private String C075_CTR_PARTY_LOC;

    @Column(name = "C076_CTR_PARTY_CTY", length =2)
    private String C076_CTR_PARTY_CTY;

    @Column(name = "C077_CTR_PARTY_TYP", length =1)
    private String C077_CTR_PARTY_TYP;

    @Column(name = "C078_TXN_PURC_CODE", length =6)
    private String C078_TXN_PURC_CODE;

    @Column(name = "C079_TXN_PURC_IND", length =1)
    private String C079_TXN_PURC_IND;

    @Column(name = "C080_TXN_PURC_DESCR1", length =40)
    private String C080_TXN_PURC_DESCR1;

    @Column(name = "C081_TXN_PURC_DESCR2", length =40)
    private String C081_TXN_PURC_DESCR2;

    @Column(name = "C082_SAFE_APPR_NBR", length =30)
    private String C082_SAFE_APPR_NBR;

    @Column(name = "C083_TXNH_FX_IND", length =1)
    private String C083_TXNH_FX_IND;

    @Column(name = "C084_TXNH_MODE", length =1)
    private String C084_TXNH_MODE;


    @Column(name = "transaction_date")
    private Long transactionDate;
}
