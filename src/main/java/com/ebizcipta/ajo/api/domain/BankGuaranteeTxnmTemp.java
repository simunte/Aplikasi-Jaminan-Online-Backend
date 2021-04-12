package com.ebizcipta.ajo.api.domain;

import com.ebizcipta.ajo.api.service.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_guarantee_txnm_temp")
public class BankGuaranteeTxnmTemp extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "C001_TXNM_VER_NBR", length = 3)
    private String C001_TXNM_VER_NBR;

    @Column(name = "C002_TXNM_REF_NBR", length = 14)
    private String C002_TXNM_REF_NBR;

    @Column(name = "C003_TXNM_CUS_SRC_KEY", length = 20)
    private String C003_TXNM_CUS_SRC_KEY;

    @Column(name = "C004_TXNM_PROD_CD", length = 10)
    private String C004_TXNM_PROD_CD;

    @Column(name = "C005_TXNM_PROD_GRP_CD", length = 3)
    private String C005_TXNM_PROD_GRP_CD;

    @Column(name = "C006_TXNM_FAC_CD", length = 10)
    private String C006_TXNM_FAC_CD;

    @Column(name = "C007_TXNM_FAC_LN_NBR", length = 1)
    private String C007_TXNM_FAC_LN_NBR;

    @Column(name = "C008_TXNM_GL_COST_CTR", length = 4)
    private String C008_TXNM_GL_COST_CTR;

    @Column(name = "C009_TXNM_GL_CORP_CD", length = 4)
    private String C009_TXNM_GL_CORP_CD;

    @Column(name = "C010_TXNM_LEGR_BAL_GL_CD", length = 10)
    private String C010_TXNM_LEGR_BAL_GL_CD;

    @Column(name = "C011_TXNM_CCY_CD", length = 3)
    private String C011_TXNM_CCY_CD;

    @Column(name = "C012_TXNM_TYP", length = 1)
    private String C012_TXNM_TYP;

    @Column(name = "C013_TXNM_SUB_TYP", length = 2)
    private String C013_TXNM_SUB_TYP;

    @Column(name = "C014_TXNM_LC_NBR", length = 11)
    private String C014_TXNM_LC_NBR;

    @Column(name = "C015_TXNM_REL_REF_NBR", length = 30)
    private String C015_TXNM_REL_REF_NBR;

    @Column(name = "C016_TXNM_STR_DT")
    private String C016_TXNM_STR_DT;

    @Column(name = "C017_TXNM_EXP_DT")
    private String C017_TXNM_EXP_DT;

    @Column(name = "C018_TXNM_PAY_DT", length = 8)
    private String C018_TXNM_PAY_DT;

    @Column(name = "C019_TXNM_SHP_FR_PLC", length = 65)
    private String C019_TXNM_SHP_FR_PLC;

    @Column(name = "C020_TXNM_SHP_TO_PLC", length = 65)
    private String C020_TXNM_SHP_TO_PLC;

    @Column(name = "C021_TXNM_SHP_VIA_PLC", length = 65)
    private String C021_TXNM_SHP_VIA_PLC;

    @Column(name = "C022_TXNM_YR_BAS_CD", length = 3)
    private String C022_TXNM_YR_BAS_CD;

    @Column(name = "C023_TXNM_INT_TYP_CD", length = 1)
    private String C023_TXNM_INT_TYP_CD;

    @Column(name = "C024_TXNM_INT_BAS_TYP", length = 2)
    private String C024_TXNM_INT_BAS_TYP;

    @Column(name = "C025_TXNM_INT_BAS_RT", length = 8)
    private String C025_TXNM_INT_BAS_RT;

    @Column(name = "C026_TXNM_CUR_NET_RT", length = 8)
    private String C026_TXNM_CUR_NET_RT;

    @Column(name = "C027_TXNM_IMP_EXP_CTRY_CD", length = 3)
    private String C027_TXNM_IMP_EXP_CTRY_CD;

    @Column(name = "C028_TXNM_MARG_IND", length = 1)
    private String C028_TXNM_MARG_IND;

    @Column(name = "C029_TXNM_INT_VAR_RT_SIGN", length = 1)
    private String C029_TXNM_INT_VAR_RT_SIGN;

    @Column(name = "C030_TXNM_INT_VAR_RT", length = 8)
    private String C030_TXNM_INT_VAR_RT;

    @Column(name = "C031_TXNM_PAY_TYP_IND", length = 2)
    private String C031_TXNM_PAY_TYP_IND;

    @Column(name = "C032_TXNM_NEG_BNK_CD", length = 11)
    private String C032_TXNM_NEG_BNK_CD;

    @Column(name = "C033_TXNM_CORR_BNK_CD", length = 11)
    private String C033_TXNM_CORR_BNK_CD;

    @Column(name = "C034_TXNM_ADVS_BNK_CD", length = 11)
    private String C034_TXNM_ADVS_BNK_CD;

    @Column(name = "C035_TXNM_ISS_BNK_CD", length = 11)
    private String C035_TXNM_ISS_BNK_CD;

    @Column(name = "C036_TXNM_NTRO_BNK_CD", length = 11)
    private String C036_TXNM_NTRO_BNK_CD;

    @Column(name = "C037_TXNM_COLL_BNK_CD", length = 11)
    private String C037_TXNM_COLL_BNK_CD;

    @Column(name = "C038_TXNM_GL_PST_CCY", length = 3)
    private String C038_TXNM_GL_PST_CCY;

    @Column(name = "C039_TXNM_LEGR_BAL_GL_TYP", length = 1)
    private String C039_TXNM_LEGR_BAL_GL_TYP;

    @Column(name = "C040_TXNM_LEGR_BAL", length = 18)
    private String C040_TXNM_LEGR_BAL;

    @Column(name = "C041_TXNM_ORG_AMT", length = 18)
    private String C041_TXNM_ORG_AMT;

    @Column(name = "C042_TXNM_TOT_AMT_DR", length = 18)
    private String C042_TXNM_TOT_AMT_DR;

    @Column(name = "C043_TXNM_ACCU_BAL", length = 18)
    private String C043_TXNM_ACCU_BAL;

    @Column(name = "C044_TXNM_NET_INT_GL_CD", length = 10)
    private String C044_TXNM_NET_INT_GL_CD;

    @Column(name = "C045_TXNM_NET_INT", length = 18)
    private String C045_TXNM_NET_INT;

    @Column(name = "C046_TXNM_DISC_INT", length = 18)
    private String C046_TXNM_DISC_INT;

    @Column(name = "C047_TXNM_ACRU_INT_GL_CD", length = 10)
    private String C047_TXNM_ACRU_INT_GL_CD;

    @Column(name = "C048_TXNM_ACRU_INT", length = 18)
    private String C048_TXNM_ACRU_INT;

    @Column(name = "C049_TXNM_AMRT_INT", length = 18)
    private String C049_TXNM_AMRT_INT;

    @Column(name = "C050_TXNM_INT_IN_SUS_GL", length = 10)
    private String C050_TXNM_INT_IN_SUS_GL;

    @Column(name = "C051_TXNM_INT_IN_SUS", length = 18)
    private String C051_TXNM_INT_IN_SUS;

    @Column(name = "C052_TXNM_UNERN_INT_GL_CD", length = 10)
    private String C052_TXNM_UNERN_INT_GL_CD;

    @Column(name = "C053_TXNM_UNERN_INT", length = 18)
    private String C053_TXNM_UNERN_INT;

    @Column(name = "C054_TXNM_TOT_CLM_AMT", length = 18)
    private String C054_TXNM_TOT_CLM_AMT;

    @Column(name = "C055_TXNM_REC_STS", length = 1)
    private String C055_TXNM_REC_STS;

    @Column(name = "C056_TXNM_IMP_INL_IND", length = 3)
    private String C056_TXNM_IMP_INL_IND;

    @Column(name = "C057_TXNM_RECOURSE_IND", length = 2)
    private String C057_TXNM_RECOURSE_IND;

    @Column(name = "C058_TXNM_LOAN_IO_IND", length = 1)
    private String C058_TXNM_LOAN_IO_IND;

    @Column(name = "C059_TXNM_LOAN_IND_CD", length = 10)
    private String C059_TXNM_LOAN_IND_CD;

    @Column(name = "C060_USR_DEF_1", length = 44)
    private String C060_USR_DEF_1;

    @Column(name = "C061_USR_DEF_2", length = 60)
    private String C061_USR_DEF_2;

    @Column(name = "C062_USR_DEF_3", length = 60)
    private String C062_USR_DEF_3;

    @Column(name = "C063_USR_DEF_4", length = 11)
    private String C063_USR_DEF_4;

    @Column(name = "C064_TXNM_IB_ACC_MODE", length = 1)
    private String C064_TXNM_IB_ACC_MODE;

    @Column(name = "C065_FILLER_01", length = 2)
    private String C065_FILLER_01;

    @Column(name = "C066_TXNM_CTRY_REF", length = 16)
    private String C066_TXNM_CTRY_REF;

    @Column(name = "C067_TXNM_CUST_REF", length = 16)
    private String C067_TXNM_CUST_REF;

    @Column(name = "C068_TXNM_PG_GROUP", length = 1)
    private String C068_TXNM_PG_GROUP;

    @Column(name = "C069_TXNM_PDREC_IND", length = 1)
    private String C069_TXNM_PDREC_IND;

    @Column(name = "C070_FILLER_02", length = 12)
    private String C070_FILLER_02;

    @Column(name = "C071_USR_DEF_5", length = 60)
    private String C071_USR_DEF_5;

    @Column(name = "C072_USR_DEF_DT_1", length = 8)
    private String C072_USR_DEF_DT_1;

    @Column(name = "C073_USR_DEF_DT_2", length = 8)
    private String C073_USR_DEF_DT_2;

    @Column(name = "C074_USR_DEF_TIM_3", length = 8)
    private String C074_USR_DEF_TIM_3;

    @Column(name = "C075_USR_DEF_11", length = 18)
    private String C075_USR_DEF_11;

    @Column(name = "C076_USR_DEF_12", length = 18)
    private String C076_USR_DEF_12;

    @Column(name = "C077_USR_DEF_13", length = 18)
    private String C077_USR_DEF_13;

    @Column(name = "C078_USR_DEF_14", length = 18)
    private String C078_USR_DEF_14;

    @Column(name = "C079_USR_DEF_15", length = 18)
    private String C079_USR_DEF_15;

    @Column(name = "C080_USR_DEF_16", length = 18)
    private String C080_USR_DEF_16;

    @Column(name = "C081_USR_DEF_17", length = 18)
    private String C081_USR_DEF_17;

    @Column(name = "C082_REC_TYPE", length = 1)
    private String C082_REC_TYPE;

    @Column(name = "C083_TXNM_STDAT", length = 8)
    private String C083_TXNM_STDAT;

    @Column(name = "C084_TXNM_LC_ISSDT", length = 8)
    private String C084_TXNM_LC_ISSDT;

    @Column(name = "C085_TXNM_TENOR", length = 3)
    private String C085_TXNM_TENOR;

    @Column(name = "C086_TXNM_PASS_DUEDT", length = 8)
    private String C086_TXNM_PASS_DUEDT;

    @Column(name = "C087_TXNM_CHQ_NO", length = 35)
    private String C087_TXNM_CHQ_NO;

    @Column(name = "C088_TXNM_DRW_BANK", length = 11)
    private String C088_TXNM_DRW_BANK;

    @Column(name = "C089_TXNM_CLR_MODE", length = 1)
    private String C089_TXNM_CLR_MODE;

    @Column(name = "C090_TXNM_LN_INSDA", length = 8)
    private String C090_TXNM_LN_INSDA;

    @Column(name = "C091_TXNM_BEN_NAME", length = 35)
    private String C091_TXNM_BEN_NAME;

    @Column(name = "C092_TXNM_BEN_ADD1", length = 35)
    private String C092_TXNM_BEN_ADD1;

    @Column(name = "C093_TXNM_BEN_ADD2", length = 35)
    private String C093_TXNM_BEN_ADD2;

    @Column(name = "C094_TXNM_BEN_ADD3", length = 35)
    private String C094_TXNM_BEN_ADD3;

    @Column(name = "C095_TXNM_CLM_DATE", length = 8)
    private String C095_TXNM_CLM_DATE;

    @Column(name = "C096_TXNM_CLM_TYP", length = 3)
    private String C096_TXNM_CLM_TYP;

    @Column(name = "C097_TXNM_PLC_TAKE", length = 65)
    private String C097_TXNM_PLC_TAKE;

    @Column(name = "C098_TXNM_PLC_DEST", length = 65)
    private String C098_TXNM_PLC_DEST;

    @Column(name = "C099_TXNM_PURS_CD", length = 4)
    private String C099_TXNM_PURS_CD;

    @Column(name = "C100_TXNM_PRIN_PAST_DAYS", length = 5)
    private String C100_TXNM_PRIN_PAST_DAYS;

    @Column(name = "C101_TXNM_INT_PAST_DAYS", length = 5)
    private String C101_TXNM_INT_PAST_DAYS;

    @Column(name = "C102_TXNM_APPLNM1", length = 35)
    private String C102_TXNM_APPLNM1;

    @Column(name = "C103_TXNM_APPLNM2", length = 35)
    private String C103_TXNM_APPLNM2;

    @Column(name = "C104_TXNM_APPL_ADR1", length = 35)
    private String C104_TXNM_APPL_ADR1;

    @Column(name = "C105_TXNM_APPL_ADR2", length = 35)
    private String C105_TXNM_APPL_ADR2;

    @Column(name = "C106_TXNM_APPL_ADR3", length = 35)
    private String C106_TXNM_APPL_ADR3;

    @Column(name = "C107_TXNM_CUST_NAM", length = 35)
    private String C107_TXNM_CUST_NAM;

    @Column(name = "C108_TXNM_ALPHA_ID", length = 11)
    private String C108_TXNM_ALPHA_ID;

    @Column(name = "C109_TXNM_DRAWERNM", length = 35)
    private String C109_TXNM_DRAWERNM;

    @Column(name = "C110_TXNM_SHPBY", length = 30)
    private String C110_TXNM_SHPBY;

    @Column(name = "C111_TXNM_DRAWEENM", length = 35)
    private String C111_TXNM_DRAWEENM;

    @Column(name = "C112_TXNM_DRAWEE_ADR1", length = 35)
    private String C112_TXNM_DRAWEE_ADR1;

    @Column(name = "C113_TXNM_DRAWEE_ADR2", length = 35)
    private String C113_TXNM_DRAWEE_ADR2;

    @Column(name = "C114_TXNM_DRAWEE_ADR3", length = 35)
    private String C114_TXNM_DRAWEE_ADR3;

    @Column(name = "C115_TXNM_SHP_CONAM", length = 35)
    private String C115_TXNM_SHP_CONAM;

    @Column(name = "C116_TXNM_VESSELNM", length = 30)
    private String C116_TXNM_VESSELNM;

    @Column(name = "C117_TXNM_EXP_PLACE", length = 29)
    private String C117_TXNM_EXP_PLACE;

    @Column(name = "C118_TXNM_AMEND_NO", length = 3)
    private String C118_TXNM_AMEND_NO;

    @Column(name = "C119_TXNM_BILL_AMT", length = 15)
    private String C119_TXNM_BILL_AMT;

    @Column(name = "C120_TXNM_ADV_MODE", length = 2)
    private String C120_TXNM_ADV_MODE;

    @Column(name = "C121_TXNM_LC_MODE", length = 2)
    private String C121_TXNM_LC_MODE;

    @Column(name = "C122_TXNM_CLS_DT", length = 8)
    private String C122_TXNM_CLS_DT;

    @Column(name = "C123_TXNM_DEAL_NBR", length = 30)
    private String C123_TXNM_DEAL_NBR;

    @Column(name = "C124_TXNM_ENTRY_DT")
    private String C124_TXNM_ENTRY_DT;

    @Column(name = "C125_MAIN_FX_CODE", length = 3)
    private String C125_MAIN_FX_CODE;

    @Column(name = "C126_SUB_FX_CODE", length = 3)
    private String C126_SUB_FX_CODE;

    @Column(name = "C127_FX_DESCRP_1", length = 40)
    private String C127_FX_DESCRP_1;

    @Column(name = "C128_FX_DESCRP_2", length = 40)
    private String C128_FX_DESCRP_2;

    @Column(name = "C129_FX_DESCRP_3", length = 40)
    private String C129_FX_DESCRP_3;

    @Column(name = "C130_CTR_PARTYNM", length = 35)
    private String C130_CTR_PARTYNM;

    @Column(name = "C131_CTR_PARTY_LOC", length = 6)
    private String C131_CTR_PARTY_LOC;

    @Column(name = "C132_CTR_PARTY_CTY", length = 2)
    private String C132_CTR_PARTY_CTY;

    @Column(name = "C133_CTR_PARTY_TYP", length = 1)
    private String C133_CTR_PARTY_TYP;

    @Column(name = "C134_TXN_PURC_CODE", length = 6)
    private String C134_TXN_PURC_CODE;

    @Column(name = "C135_TXN_PURC_IND", length = 1)
    private String C135_TXN_PURC_IND;

    @Column(name = "C136_TXN_PURC_DESCR1", length = 40)
    private String C136_TXN_PURC_DESCR1;

    @Column(name = "C137_TXN_PURC_DESCR2", length = 40)
    private String C137_TXN_PURC_DESCR2;

    @Column(name = "C138_SAFE_APPR_NBR", length = 30)
    private String C138_SAFE_APPR_NBR;

    @Column(name = "C139_TXNM_GOODS_DESC1", length = 65)
    private String C139_TXNM_GOODS_DESC1;

    @Column(name = "C140_TXNM_GOODS_DESC2", length = 65)
    private String C140_TXNM_GOODS_DESC2;

    @Column(name = "C141_TXNM_ISS_BNK_SWF_CD", length = 11)
    private String C141_TXNM_ISS_BNK_SWF_CD;

    @Column(name = "C142_TXNM_RESTRICTED_LC", length = 1)
    private String C142_TXNM_RESTRICTED_LC;

    @Column(name = "C143_TXNM_2ND_ADVBANK", length = 11)
    private String C143_TXNM_2ND_ADVBANK;

    @Column(name = "C144_TXNM_DISCREP_FLG", length = 1)
    private String C144_TXNM_DISCREP_FLG;

    @Column(name = "C145_TXNM_PUR_AMT", length = 18)
    private String C145_TXNM_PUR_AMT;

    @Column(name = "C146_TXNM_COLLECT_AMT", length = 18)
    private String C146_TXNM_COLLECT_AMT;

    @Column(name = "C147_TXNM_LEGR_BAL_LCY", length = 18)
    private String C147_TXNM_LEGR_BAL_LCY;

    @Column(name = "C148_TXNM_ACRU_INT_LCY", length = 18)
    private String C148_TXNM_ACRU_INT_LCY;

    @Column(name = "C149_TXNM_INT_IN_SUS_LCY", length = 18)
    private String C149_TXNM_INT_IN_SUS_LCY;

    @Column(name = "C150_TXNM_UNERN_INT_LCY", length = 18)
    private String C150_TXNM_UNERN_INT_LCY;

    @Column(name = "C151_TXNM_PUR_DATE", length = 8)
    private String C151_TXNM_PUR_DATE;

    @Column(name = "C152_TXNM_PURSETTLE_DT", length = 8)
    private String C152_TXNM_PURSETTLE_DT;

    @Column(name = "C153_TXNM_ACCEPT_DT", length = 8)
    private String C153_TXNM_ACCEPT_DT;

    @Column(name = "C154_TXNM_PG_TYPE", length = 3)
    private String C154_TXNM_PG_TYPE;

    @Column(name = "C155_TXNM_PG_PURPOSE", length = 15)
    private String C155_TXNM_PG_PURPOSE;

    @Column(name = "C156_TXNM_SHP_CONO", length = 11)
    private String C156_TXNM_SHP_CONO;

    @Column(name = "C157_TXNM_REL_TX_SUB_TYP", length = 2)
    private String C157_TXNM_REL_TX_SUB_TYP;

    @Column(name = "C158_TXNM_RSK_CTR_PTY_2ND", length = 11)
    private String C158_TXNM_RSK_CTR_PTY_2ND;

    @Column(name = "C159_TXNM_FAC_CD_2ND", length = 10)
    private String C159_TXNM_FAC_CD_2ND;

    @Column(name = "C160_TXNM_FAC_LN_NBR_2ND", length = 1)
    private String C160_TXNM_FAC_LN_NBR_2ND;

    @Column(name = "C161_TXNM_DUE_DT", length = 8)
    private String C161_TXNM_DUE_DT;

    @Column(name = "C162_TXNM_CONF_MATU_DT", length = 8)
    private String C162_TXNM_CONF_MATU_DT;

    @Column(name = "C163_TXNM_EXTD_MATU_DT", length = 8)
    private String C163_TXNM_EXTD_MATU_DT;

    @Column(name = "C164_TXNM_LC_NBR_EXP", length = 30)
    private String C164_TXNM_LC_NBR_EXP;

    @Column(name = "transaction_date")
    private Long transactionDate;
}
