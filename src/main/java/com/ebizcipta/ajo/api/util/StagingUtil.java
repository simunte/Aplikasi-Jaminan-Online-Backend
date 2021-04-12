package com.ebizcipta.ajo.api.util;

import com.ebizcipta.ajo.api.config.AS400Config;
import com.ebizcipta.ajo.api.config.AS400FtpConfig;
import com.ebizcipta.ajo.api.domain.*;
import com.ebizcipta.ajo.api.domain.Currency;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.*;
import com.ebizcipta.ajo.api.service.mapper.RegistrationMapper;
import com.ebizcipta.ajo.api.service.validator.BgRegistrationValidator;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;
import java.util.*;

@Slf4j
@Transactional
@Component
@Configuration
@EnableScheduling
@PropertySource("classpath:configuration/web-service.properties")
public class StagingUtil{
    @Value("${txnm.source.path}")
    private String txnmSourcePath;
    @Value("${txnm.destination.path}")
    private String txnmDestinationPath;
    @Value("${txnh.source.path}")
    private String txnhSourcePath;
    @Value("${txnh.destination.path}")
    private String txnhDestinationPath;

    private final DateUtil dateUtil;
    private final BankGuaranteeTxnmTempRepository bankGuaranteeTxnmTempRepository;
    private final BankGuaranteeTxnhTempRepository bankGuaranteeTxnhTempRepository;
    private final BankGuaranteeBackupTempRepository bankGuaranteeBackupTempRepository;
    private final BgRegistrationValidator validator;
    private final CurrencyRepository currencyRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final UnitPenggunaRepository unitPenggunaRepository;
    private final RegistrationRepository registrationRepository;
    private final JenisJaminanRepository jenisJaminanRepository;
    private final AS400Config as400Config;
    private final AS400FtpConfig as400FtpConfig;
    private final AuditTrailUtil auditTrailUtil;

    public StagingUtil(DateUtil dateUtil, BankGuaranteeTxnmTempRepository bankGuaranteeTxnmTempRepository, BankGuaranteeTxnhTempRepository bankGuaranteeTxnhTempRepository, BankGuaranteeBackupTempRepository bankGuaranteeBackupTempRepository, BgRegistrationValidator validator, CurrencyRepository currencyRepository, BeneficiaryRepository beneficiaryRepository, UnitPenggunaRepository unitPenggunaRepository, RegistrationRepository registrationRepository, JenisJaminanRepository jenisJaminanRepository, AS400Config as400Config, AS400FtpConfig as400FtpConfig, AuditTrailUtil auditTrailUtil) {
        this.dateUtil = dateUtil;
        this.bankGuaranteeTxnmTempRepository = bankGuaranteeTxnmTempRepository;
        this.bankGuaranteeTxnhTempRepository = bankGuaranteeTxnhTempRepository;
        this.bankGuaranteeBackupTempRepository = bankGuaranteeBackupTempRepository;
        this.validator = validator;
        this.currencyRepository = currencyRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.unitPenggunaRepository = unitPenggunaRepository;
        this.registrationRepository = registrationRepository;
        this.jenisJaminanRepository = jenisJaminanRepository;
        this.as400Config = as400Config;
        this.as400FtpConfig = as400FtpConfig;
        this.auditTrailUtil = auditTrailUtil;
    }

    public List<String> getDataFromStg(){
        List<String> result = as400Config.connect();
        return result;
    }

    public void ftpFunction(){
        try {
            if (as400FtpConfig.openConnectionFtp()){
                as400FtpConfig.downloadFile(txnmSourcePath, txnmDestinationPath);
                as400FtpConfig.downloadFile(txnhSourcePath, txnhDestinationPath);
                as400FtpConfig.closeConnectionFtp();
            }else {
                throw new BadRequestAlertException("Can't Connect to FTP Server", "","");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void insertDataToTxnmTempTableFromFile(){
        //System.out.println("START INSERT TXNM TO TEMPORARY");
        log.info("START INSERT TXNM TO TEMPORARY");
        FileInputStream inputStream = null;
        Scanner scanner = null;
        int total = 0;
        try {
            inputStream = new FileInputStream(txnmDestinationPath);
            scanner = new Scanner(inputStream, "UTF-8");
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (total >= 1 && scanner.hasNextLine()){
                    BankGuaranteeTxnmTemp bankGuaranteeTxnmTemp = new BankGuaranteeTxnmTemp();
                    bankGuaranteeTxnmTemp.setC001_TXNM_VER_NBR(
                            line.length() > 0 && !(line.length() < 4)
                                    ? line.substring(0,3) : (line.length() > 0
                                    ? line.substring(0,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC002_TXNM_REF_NBR(line.length() > 3 && !(line.length() < 18)
                            ? line.substring(3,17) : (line.length() > 3
                            ? line.substring(3,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC003_TXNM_CUS_SRC_KEY(line.length() > 17 && !(line.length() < 38)
                            ? line.substring(17,37) : (line.length() > 17
                            ? line.substring(17,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC004_TXNM_PROD_CD(line.length() > 37 && !(line.length() < 48)
                            ? line.substring(37,47) : (line.length() > 37
                            ? line.substring(37,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC005_TXNM_PROD_GRP_CD(line.length() > 47 && !(line.length() < 51)
                            ? line.substring(47,50) : (line.length() > 47
                            ? line.substring(47,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC006_TXNM_FAC_CD(line.length() > 50 && !(line.length() < 61)
                            ? line.substring(50,60) : (line.length() > 50
                            ? line.substring(50,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC007_TXNM_FAC_LN_NBR(line.length() > 60 && !(line.length() < 62)
                            ? line.substring(60,61) : (line.length() > 60
                            ? line.substring(60,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC008_TXNM_GL_COST_CTR(line.length() > 61 && !(line.length() < 66)
                            ? line.substring(61,65) : (line.length() > 61
                            ? line.substring(61,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC009_TXNM_GL_CORP_CD(line.length() > 65 && !(line.length() < 70)
                            ? line.substring(65,69) : (line.length() > 65
                            ? line.substring(65,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC010_TXNM_LEGR_BAL_GL_CD(line.length() > 69 && !(line.length() < 80)
                            ? line.substring(69,79) : (line.length() > 69
                            ? line.substring(69,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC011_TXNM_CCY_CD(line.length() > 79 && !(line.length() < 83)
                            ? line.substring(79,82) : (line.length() > 79
                            ? line.substring(79,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC012_TXNM_TYP(line.length() > 82 && !(line.length() < 84)
                            ? line.substring(82,83) : (line.length() > 82
                            ? line.substring(82,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC013_TXNM_SUB_TYP(line.length() > 83 && !(line.length() < 86)
                            ? line.substring(83,85) : (line.length() > 83
                            ? line.substring(83,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC014_TXNM_LC_NBR(line.length() > 85 && !(line.length() < 97)
                            ? line.substring(85,96) : (line.length() > 85
                            ? line.substring(85,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC015_TXNM_REL_REF_NBR(line.length() > 96 && !(line.length() < 127)
                            ? line.substring(96,126) : (line.length() > 96
                            ? line.substring(96,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC016_TXNM_STR_DT(line.length() > 126 && !(line.length() < 135)
                            ? line.substring(126,134) : (line.length() > 126
                            ? line.substring(126,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC017_TXNM_EXP_DT(line.length() > 134 && !(line.length() < 143)
                            ? line.substring(134,142) : (line.length() > 134
                            ? line.substring(134,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC018_TXNM_PAY_DT(line.length() > 142 && !(line.length() < 151)
                            ? line.substring(142,150) : (line.length() > 142
                            ? line.substring(142,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC019_TXNM_SHP_FR_PLC(line.length() > 150 && !(line.length() < 216)
                            ? line.substring(150,215) : (line.length() > 150
                            ? line.substring(150,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC020_TXNM_SHP_TO_PLC(line.length() > 215 && !(line.length() < 281)
                            ? line.substring(215,280) : (line.length() > 215
                            ? line.substring(215,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC021_TXNM_SHP_VIA_PLC(line.length() > 280 && !(line.length() < 346)
                            ? line.substring(280,345) : (line.length() > 280
                            ? line.substring(280,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC022_TXNM_YR_BAS_CD(line.length() > 345 && !(line.length() < 349)
                            ? line.substring(345,348) : (line.length() > 345
                            ? line.substring(345,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC023_TXNM_INT_TYP_CD(line.length() > 348 && !(line.length() < 350)
                            ? line.substring(348,349) : (line.length() > 348
                            ? line.substring(348,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC024_TXNM_INT_BAS_TYP(line.length() > 349 && !(line.length() < 352)
                            ? line.substring(349,351) : (line.length() > 349
                            ? line.substring(349,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC025_TXNM_INT_BAS_RT(line.length() > 351 && !(line.length() < 360)
                            ? line.substring(351,359) : (line.length() > 351
                            ? line.substring(351,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC026_TXNM_CUR_NET_RT(line.length() > 359 && !(line.length() < 368)
                            ? line.substring(359,367) : (line.length() > 359
                            ? line.substring(359,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC027_TXNM_IMP_EXP_CTRY_CD(line.length() > 367 && !(line.length() < 371)
                            ? line.substring(367,370) : (line.length() > 367
                            ? line.substring(367,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC028_TXNM_MARG_IND(line.length() > 370 && !(line.length() < 372)
                            ? line.substring(370,371) : (line.length() > 370
                            ? line.substring(370,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC029_TXNM_INT_VAR_RT_SIGN(line.length() > 371 && !(line.length() < 373)
                            ? line.substring(371,372) : (line.length() > 371
                            ? line.substring(371,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC030_TXNM_INT_VAR_RT(line.length() > 372 && !(line.length() < 381)
                            ? line.substring(372,380) : (line.length() > 372
                            ? line.substring(372,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC031_TXNM_PAY_TYP_IND(line.length() > 380 && !(line.length() < 383)
                            ? line.substring(380,382) : (line.length() > 380
                            ? line.substring(380,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC032_TXNM_NEG_BNK_CD(line.length() > 382 && !(line.length() < 394)
                            ? line.substring(382,393) : (line.length() > 382
                            ? line.substring(382,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC033_TXNM_CORR_BNK_CD(line.length() > 393 && !(line.length() < 405)
                            ? line.substring(393,404) : (line.length() > 393
                            ? line.substring(393,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC034_TXNM_ADVS_BNK_CD(line.length() > 404 && !(line.length() < 416)
                            ? line.substring(404,415) : (line.length() > 404
                            ? line.substring(404,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC035_TXNM_ISS_BNK_CD(line.length() > 415 && !(line.length() < 427)
                            ? line.substring(415,426) : (line.length() > 415
                            ? line.substring(415,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC036_TXNM_NTRO_BNK_CD(line.length() > 426 && !(line.length() < 438)
                            ? line.substring(426,437) : (line.length() > 426
                            ? line.substring(426,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC037_TXNM_COLL_BNK_CD(line.length() > 437 && !(line.length() < 449)
                            ? line.substring(437,448) : (line.length() > 437
                            ? line.substring(437,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC038_TXNM_GL_PST_CCY(line.length() > 448 && !(line.length() < 452)
                            ? line.substring(448,451) : (line.length() > 448
                            ? line.substring(448,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC039_TXNM_LEGR_BAL_GL_TYP(line.length() > 451 && !(line.length() < 453)
                            ? line.substring(451,452) : (line.length() > 451
                            ? line.substring(451,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC040_TXNM_LEGR_BAL(line.length() > 452 && !(line.length() < 471)
                            ? line.substring(452,470) : (line.length() > 452
                            ? line.substring(452,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC041_TXNM_ORG_AMT(line.length() > 470 && !(line.length() < 489)
                            ? line.substring(470,488) : (line.length() > 470
                            ? line.substring(470,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC042_TXNM_TOT_AMT_DR(line.length() > 488 && !(line.length() < 507)
                            ? line.substring(488,506) : (line.length() > 488
                            ? line.substring(488,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC043_TXNM_ACCU_BAL(line.length() > 506 && !(line.length() < 525)
                            ? line.substring(506,524) : (line.length() > 506
                            ? line.substring(506,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC043_TXNM_ACCU_BAL(line.length() > 524 && !(line.length() < 535)
                            ? line.substring(524,534) : (line.length() > 524
                            ? line.substring(524,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC045_TXNM_NET_INT(line.length() > 534 && !(line.length() < 553)
                            ? line.substring(534,552) : (line.length() > 534
                            ? line.substring(534,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC046_TXNM_DISC_INT(line.length() > 552 && !(line.length() < 571)
                            ? line.substring(552,570) : (line.length() > 552
                            ? line.substring(552,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC047_TXNM_ACRU_INT_GL_CD(line.length() > 570 && !(line.length() < 581)
                            ? line.substring(570,580) : (line.length() > 570
                            ? line.substring(570,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC048_TXNM_ACRU_INT(line.length() > 580 && !(line.length() < 599)
                            ? line.substring(580,598) : (line.length() > 580
                            ? line.substring(580,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC049_TXNM_AMRT_INT(line.length() > 598 && !(line.length() < 617)
                            ? line.substring(598,616) : (line.length() > 598
                            ? line.substring(598,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC050_TXNM_INT_IN_SUS_GL(line.length() > 616 && !(line.length() < 627)
                            ? line.substring(616,626) : (line.length() > 616
                            ? line.substring(616,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC051_TXNM_INT_IN_SUS(line.length() > 626 && !(line.length() < 645)
                            ? line.substring(626,644) : (line.length() > 626
                            ? line.substring(626,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC052_TXNM_UNERN_INT_GL_CD(line.length() > 644 && !(line.length() < 655)
                            ? line.substring(644,654) : (line.length() > 644
                            ? line.substring(644,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC053_TXNM_UNERN_INT(line.length() > 654 && !(line.length() < 673)
                            ? line.substring(654,672) : (line.length() > 654
                            ? line.substring(654,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC054_TXNM_TOT_CLM_AMT(line.length() > 672 && !(line.length() < 691)
                            ? line.substring(672,690) : (line.length() > 672
                            ? line.substring(672,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC055_TXNM_REC_STS(line.length() > 690 && !(line.length() < 692)
                            ? line.substring(690,691) : (line.length() > 690
                            ? line.substring(690,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC056_TXNM_IMP_INL_IND(line.length() > 691 && !(line.length() < 695)
                            ? line.substring(691,694) : (line.length() > 691
                            ? line.substring(691,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC057_TXNM_RECOURSE_IND(line.length() > 694 && !(line.length() < 697)
                            ? line.substring(694,696) : (line.length() > 694
                            ? line.substring(694,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC058_TXNM_LOAN_IO_IND(line.length() > 696 && !(line.length() < 698)
                            ? line.substring(696,697) : (line.length() > 696
                            ? line.substring(696,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC059_TXNM_LOAN_IND_CD(line.length() > 697 && !(line.length() < 708)
                            ? line.substring(697,707) : (line.length() > 697
                            ? line.substring(697,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC060_USR_DEF_1(line.length() > 707 && !(line.length() < 752)
                            ? line.substring(707,751) : (line.length() > 707
                            ? line.substring(707,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC061_USR_DEF_2(line.length() > 751 && !(line.length() < 812)
                            ? line.substring(751,811) : (line.length() > 751
                            ? line.substring(751,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC062_USR_DEF_3(line.length() > 811 && !(line.length() < 872)
                            ? line.substring(811,871) : (line.length() > 811
                            ? line.substring(811,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC063_USR_DEF_4(line.length() > 871 && !(line.length() < 883)
                            ? line.substring(871,882) : (line.length() > 871
                            ? line.substring(871,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC064_TXNM_IB_ACC_MODE(line.length() > 882 && !(line.length() < 884)
                            ? line.substring(882,883) : (line.length() > 882
                            ? line.substring(882,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC065_FILLER_01(line.length() > 883 && !(line.length() < 886)
                            ? line.substring(883,885) : (line.length() > 883
                            ? line.substring(883,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC066_TXNM_CTRY_REF(line.length() > 885 && !(line.length() < 902)
                            ? line.substring(885,901) : (line.length() > 885
                            ? line.substring(885,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC067_TXNM_CUST_REF(line.length() > 901 && !(line.length() < 918)
                            ? line.substring(901,917) : (line.length() > 901
                            ? line.substring(901,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC068_TXNM_PG_GROUP(line.length() > 917 && !(line.length() < 919)
                            ? line.substring(917,918) : (line.length() > 917
                            ? line.substring(917,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC069_TXNM_PDREC_IND(line.length() > 918 && !(line.length() < 920)
                            ? line.substring(918,919) : (line.length() > 918
                            ? line.substring(918,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC070_FILLER_02(line.length() > 919 && !(line.length() < 932)
                            ? line.substring(919,931) : (line.length() > 919
                            ? line.substring(919,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC071_USR_DEF_5(line.length() > 931 && !(line.length() < 992)
                            ? line.substring(931,991) : (line.length() > 931
                            ? line.substring(931,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC072_USR_DEF_DT_1(line.length() > 991 && !(line.length() < 1000)
                            ? line.substring(991,999) : (line.length() > 991
                            ? line.substring(991,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC073_USR_DEF_DT_2(line.length() > 999 && !(line.length() < 1008)
                            ? line.substring(999,1007) : (line.length() > 999
                            ? line.substring(999,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC074_USR_DEF_TIM_3(line.length() > 1007 && !(line.length() < 1016)
                            ? line.substring(1007,1015) : (line.length() > 1007
                            ? line.substring(1007,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC075_USR_DEF_11(line.length() > 1015 && !(line.length() < 1034)
                            ? line.substring(1015,1033) : (line.length() > 1015
                            ? line.substring(1015,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC076_USR_DEF_12(line.length() > 1033 && !(line.length() < 1052)
                            ? line.substring(1033,1051) : (line.length() > 1033
                            ? line.substring(1033,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC077_USR_DEF_13(line.length() > 1051 && !(line.length() < 1070)
                            ? line.substring(1051,1069) : (line.length() > 1051
                            ? line.substring(1051,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC078_USR_DEF_14(line.length() > 1069 && !(line.length() < 1088)
                            ? line.substring(1069,1087) : (line.length() > 1069
                            ? line.substring(1069,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC079_USR_DEF_15(line.length() > 1087 && !(line.length() < 1106)
                            ? line.substring(1087,1105) : (line.length() > 1087
                            ? line.substring(1087,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC080_USR_DEF_16(line.length() > 1105 && !(line.length() < 1124)
                            ? line.substring(1105,1123) : (line.length() > 1105
                            ? line.substring(1105,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC081_USR_DEF_17(line.length() > 1123 && !(line.length() < 1142)
                            ? line.substring(1123,1141) : (line.length() > 1123
                            ? line.substring(1123,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC082_REC_TYPE(line.length() > 1141 && !(line.length() < 1143)
                            ? line.substring(1141,1142) : (line.length() > 1141
                            ? line.substring(1141,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC083_TXNM_STDAT(line.length() > 1142 && !(line.length() < 1151)
                            ? line.substring(1142,1150) : (line.length() > 1142
                            ? line.substring(1142,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC084_TXNM_LC_ISSDT(line.length() > 1150 && !(line.length() < 1159)
                            ? line.substring(1150,1158) : (line.length() > 1150
                            ? line.substring(1150,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC085_TXNM_TENOR(line.length() > 1158 && !(line.length() < 1162)
                            ? line.substring(1158,1161) : (line.length() > 1158
                            ? line.substring(1158,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC086_TXNM_PASS_DUEDT(line.length() > 1161 && !(line.length() < 1170)
                            ? line.substring(1161,1169) : (line.length() > 1161
                            ? line.substring(1161,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC087_TXNM_CHQ_NO(line.length() > 1169 && !(line.length() < 1205)
                            ? line.substring(1169,1204) : (line.length() > 1169
                            ? line.substring(1169,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC088_TXNM_DRW_BANK(line.length() > 1204 && !(line.length() < 1216)
                            ? line.substring(1204,1215) : (line.length() > 1204
                            ? line.substring(1204,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC089_TXNM_CLR_MODE(line.length() > 1215 && !(line.length() < 1217)
                            ? line.substring(1215,1216) : (line.length() > 1215
                            ? line.substring(1215,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC090_TXNM_LN_INSDA(line.length() > 1216 && !(line.length() < 1225)
                            ? line.substring(1216,1224) : (line.length() > 1216
                            ? line.substring(1216,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC091_TXNM_BEN_NAME(line.length() > 1224 && !(line.length() < 1260)
                            ? line.substring(1224,1259) : (line.length() > 1224
                            ? line.substring(1224,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC092_TXNM_BEN_ADD1(line.length() > 1259 && !(line.length() < 1295)
                            ? line.substring(1259,1294) : (line.length() > 1259
                            ? line.substring(1259,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC093_TXNM_BEN_ADD2(line.length() > 1294 && !(line.length() < 1330)
                            ? line.substring(1294,1329) : (line.length() > 1294
                            ? line.substring(1294,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC094_TXNM_BEN_ADD3(line.length() > 1329 && !(line.length() < 1365)
                            ? line.substring(1329,1364) : (line.length() > 1329
                            ? line.substring(1329,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC095_TXNM_CLM_DATE(line.length() > 1364 && !(line.length() < 1373)
                            ? line.substring(1364,1372) : (line.length() > 1364
                            ? line.substring(1364,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC096_TXNM_CLM_TYP(line.length() > 1372 && !(line.length() < 1376)
                            ? line.substring(1372,1375) : (line.length() > 1372
                            ? line.substring(1372,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC097_TXNM_PLC_TAKE(line.length() > 1375 && !(line.length() < 1441)
                            ? line.substring(1375,1440) : (line.length() > 1375
                            ? line.substring(1375,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC098_TXNM_PLC_DEST(line.length() > 1440 && !(line.length() < 1506)
                            ? line.substring(1440,1505) : (line.length() > 1440
                            ? line.substring(1440,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC099_TXNM_PURS_CD(line.length() > 1505 && !(line.length() < 1510)
                            ? line.substring(1505,1509) : (line.length() > 1505
                            ? line.substring(1505,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC100_TXNM_PRIN_PAST_DAYS(line.length() > 1509 && !(line.length() < 1515)
                            ? line.substring(1509,1514) : (line.length() > 1509
                            ? line.substring(1509,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC101_TXNM_INT_PAST_DAYS(line.length() > 1514 && !(line.length() < 1520)
                            ? line.substring(1514,1519) : (line.length() > 1514
                            ? line.substring(1514,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC102_TXNM_APPLNM1(line.length() > 1519 && !(line.length() < 1555)
                            ? line.substring(1519,1554) : (line.length() > 1519
                            ? line.substring(1519,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC103_TXNM_APPLNM2(line.length() > 1554 && !(line.length() < 1590)
                            ? line.substring(1554,1589) : (line.length() > 1554
                            ? line.substring(1554,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC104_TXNM_APPL_ADR1(line.length() > 1589 && !(line.length() < 1625)
                            ? line.substring(1589,1624) : (line.length() > 1589
                            ? line.substring(1589,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC105_TXNM_APPL_ADR2(line.length() > 1624 && !(line.length() < 1660)
                            ? line.substring(1624,1659) : (line.length() > 1624
                            ? line.substring(1624,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC106_TXNM_APPL_ADR3(line.length() > 1659 && !(line.length() < 1695)
                            ? line.substring(1659,1694) : (line.length() > 1659
                            ? line.substring(1659,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC107_TXNM_CUST_NAM(line.length() > 1694 && !(line.length() < 1730)
                            ? line.substring(1694,1729) : (line.length() > 1694
                            ? line.substring(1694,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC108_TXNM_ALPHA_ID(line.length() > 1729 && !(line.length() < 1741)
                            ? line.substring(1729,1740) : (line.length() > 1729
                            ? line.substring(1729,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC109_TXNM_DRAWERNM(line.length() > 1740 && !(line.length() < 1776)
                            ? line.substring(1740,1775) : (line.length() > 1740
                            ? line.substring(1740,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC110_TXNM_SHPBY(line.length() > 1775 && !(line.length() < 1806)
                            ? line.substring(1775,1805) : (line.length() > 1775
                            ? line.substring(1775,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC111_TXNM_DRAWEENM(line.length() > 1805 && !(line.length() < 1841)
                            ? line.substring(1805,1840) : (line.length() > 1805
                            ? line.substring(1805,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC112_TXNM_DRAWEE_ADR1(line.length() > 1840 && !(line.length() < 1876)
                            ? line.substring(1840,1875) : (line.length() > 1840
                            ? line.substring(1840,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC113_TXNM_DRAWEE_ADR2(line.length() > 1875 && !(line.length() < 1911)
                            ? line.substring(1875,1910) : (line.length() > 1875
                            ? line.substring(1875,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC114_TXNM_DRAWEE_ADR3(line.length() > 1910 && !(line.length() < 1946)
                            ? line.substring(1910,1945) : (line.length() > 1910
                            ? line.substring(1910,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC115_TXNM_SHP_CONAM(line.length() > 1945 && !(line.length() < 1981)
                            ? line.substring(1945,1980) : (line.length() > 1945
                            ? line.substring(1945,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC116_TXNM_VESSELNM(line.length() > 1980 && !(line.length() < 2011)
                            ? line.substring(1980,2010) : (line.length() > 1980
                            ? line.substring(1980,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC117_TXNM_EXP_PLACE(line.length() > 2010 && !(line.length() < 2040)
                            ? line.substring(2010,2039) : (line.length() > 2010
                            ? line.substring(2010,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC118_TXNM_AMEND_NO(line.length() > 2039 && !(line.length() < 2043)
                            ? line.substring(2039,2042) : (line.length() > 2039
                            ? line.substring(2039,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC119_TXNM_BILL_AMT(line.length() > 2042 && !(line.length() < 2058)
                            ? line.substring(2042,2057) : (line.length() > 2042
                            ? line.substring(2042,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC120_TXNM_ADV_MODE(line.length() > 2057 && !(line.length() < 2060)
                            ? line.substring(2057,2059) : (line.length() > 2057
                            ? line.substring(2057,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC121_TXNM_LC_MODE(line.length() > 2059 && !(line.length() < 2062)
                            ? line.substring(2059,2061) : (line.length() > 2059
                            ? line.substring(2059,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC122_TXNM_CLS_DT(line.length() > 2061 && !(line.length() < 2070)
                            ? line.substring(2061,2069) : (line.length() > 2061
                            ? line.substring(2061,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC123_TXNM_DEAL_NBR(line.length() > 2069 && !(line.length() < 2100)
                            ? line.substring(2069,2099) : (line.length() > 2069
                            ? line.substring(2069,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC124_TXNM_ENTRY_DT(line.length() > 2099 && !(line.length() < 2108)
                            ? line.substring(2099,2107) : (line.length() > 2099
                            ? line.substring(2099,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC125_MAIN_FX_CODE(line.length() > 2107 && !(line.length() < 2111)
                            ? line.substring(2107,2110) : (line.length() > 2107
                            ? line.substring(2107,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC126_SUB_FX_CODE(line.length() > 2110 && !(line.length() < 2114)
                            ? line.substring(2110,2113) : (line.length() > 2110
                            ? line.substring(2110,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC127_FX_DESCRP_1(line.length() > 2113 && !(line.length() < 2154)
                            ? line.substring(2113,2153) : (line.length() > 2113
                            ? line.substring(2113,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC128_FX_DESCRP_2(line.length() > 2153 && !(line.length() < 2194)
                            ? line.substring(2153,2193) : (line.length() > 2153
                            ? line.substring(2153,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC129_FX_DESCRP_3(line.length() > 2193 && !(line.length() < 2234)
                            ? line.substring(2193,2233) : (line.length() > 2193
                            ? line.substring(2193,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC130_CTR_PARTYNM(line.length() > 2233 && !(line.length() < 2269)
                            ? line.substring(2233,2268) : (line.length() > 2233
                            ? line.substring(2233,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC131_CTR_PARTY_LOC(line.length() > 2268 && !(line.length() < 2275)
                            ? line.substring(2268,2274) : (line.length() > 2268
                            ? line.substring(2268,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC132_CTR_PARTY_CTY(line.length() > 2274 && !(line.length() < 2277)
                            ? line.substring(2274,2276) : (line.length() > 2274
                            ? line.substring(2274,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC133_CTR_PARTY_TYP(line.length() > 2276 && !(line.length() < 2278)
                            ? line.substring(2276,2277) : (line.length() > 2276
                            ? line.substring(2276,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC134_TXN_PURC_CODE(line.length() > 2277 && !(line.length() < 2284)
                            ? line.substring(2277,2283) : (line.length() > 2277
                            ? line.substring(2277,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC135_TXN_PURC_IND(line.length() > 2283 && !(line.length() < 2285)
                            ? line.substring(2283,2284) : (line.length() > 2283
                            ? line.substring(2283,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC136_TXN_PURC_DESCR1(line.length() > 2284 && !(line.length() < 2325)
                            ? line.substring(2284,2324) : (line.length() > 2284
                            ? line.substring(2284,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC137_TXN_PURC_DESCR2(line.length() > 2324 && !(line.length() < 2365)
                            ? line.substring(2324,2364) : (line.length() > 2324
                            ? line.substring(2324,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC138_SAFE_APPR_NBR(line.length() > 2364 && !(line.length() < 2395)
                            ? line.substring(2364,2394) : (line.length() > 2364
                            ? line.substring(2364,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC139_TXNM_GOODS_DESC1(line.length() > 2394 && !(line.length() < 2460)
                            ? line.substring(2394,2459) : (line.length() > 2394
                            ? line.substring(2394,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC140_TXNM_GOODS_DESC2(line.length() > 2459 && !(line.length() < 2525)
                            ? line.substring(2459,2524) : (line.length() > 2459
                            ? line.substring(2459,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC141_TXNM_ISS_BNK_SWF_CD(line.length() > 2524 && !(line.length() < 2536)
                            ? line.substring(2524,2535) : (line.length() > 2524
                            ? line.substring(2524,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC142_TXNM_RESTRICTED_LC(line.length() > 2535 && !(line.length() < 2537)
                            ? line.substring(2535,2536) : (line.length() > 2535
                            ? line.substring(2535,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC143_TXNM_2ND_ADVBANK(line.length() > 2536 && !(line.length() < 2548)
                            ? line.substring(2536,2547) : (line.length() > 2536
                            ? line.substring(2536,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC144_TXNM_DISCREP_FLG(line.length() > 2547 && !(line.length() < 2549)
                            ? line.substring(2547,2548) : (line.length() > 2547
                            ? line.substring(2547,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC145_TXNM_PUR_AMT(line.length() > 2548 && !(line.length() < 2567)
                            ? line.substring(2548,2566) : (line.length() > 2548
                            ? line.substring(2548,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC146_TXNM_COLLECT_AMT(line.length() > 2566 && !(line.length() < 2585)
                            ? line.substring(2566,2584) : (line.length() > 2566
                            ? line.substring(2566,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC147_TXNM_LEGR_BAL_LCY(line.length() > 2584 && !(line.length() < 2603)
                            ? line.substring(2584,2602) : (line.length() > 2584
                            ? line.substring(2584,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC148_TXNM_ACRU_INT_LCY(line.length() > 2602 && !(line.length() < 2621)
                            ? line.substring(2602,2620) : (line.length() > 2602
                            ? line.substring(2602,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC149_TXNM_INT_IN_SUS_LCY(line.length() > 2620 && !(line.length() < 2639)
                            ? line.substring(2620,2638) : (line.length() > 2620
                            ? line.substring(2620,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC150_TXNM_UNERN_INT_LCY(line.length() > 2638 && !(line.length() < 2657)
                            ? line.substring(2638,2656) : (line.length() > 2638
                            ? line.substring(2638,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC151_TXNM_PUR_DATE(line.length() > 2656 && !(line.length() < 2665)
                            ? line.substring(2656,2664) : (line.length() > 2656
                            ? line.substring(2656,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC152_TXNM_PURSETTLE_DT(line.length() > 2664 && !(line.length() < 2673)
                            ? line.substring(2664,2672) : (line.length() > 2664
                            ? line.substring(2664,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC153_TXNM_ACCEPT_DT(line.length() > 2672 && !(line.length() < 2681)
                            ? line.substring(2672,2680) : (line.length() > 2672
                            ? line.substring(2672,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC154_TXNM_PG_TYPE(line.length() > 2680 && !(line.length() < 2684)
                            ? line.substring(2680,2683) : (line.length() > 2680
                            ? line.substring(2680,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC155_TXNM_PG_PURPOSE(line.length() > 2683 && !(line.length() < 2699)
                            ? line.substring(2683,2698) : (line.length() > 2683
                            ? line.substring(2683,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC156_TXNM_SHP_CONO(line.length() > 2698 && !(line.length() < 2710)
                            ? line.substring(2698,2709) : (line.length() > 2698
                            ? line.substring(2698,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC157_TXNM_REL_TX_SUB_TYP(line.length() > 2709 && !(line.length() < 2712)
                            ? line.substring(2709,2711) : (line.length() > 2709
                            ? line.substring(2709,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC158_TXNM_RSK_CTR_PTY_2ND(line.length() > 2711 && !(line.length() < 2723)
                            ? line.substring(2711,2722) : (line.length() > 2711
                            ? line.substring(2711,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC159_TXNM_FAC_CD_2ND(line.length() > 2722 && !(line.length() < 2733)
                            ? line.substring(2722,2732) : (line.length() > 2722
                            ? line.substring(2722,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC160_TXNM_FAC_LN_NBR_2ND(line.length() > 2732 && !(line.length() < 2734)
                            ? line.substring(2732,2733) : (line.length() > 2732
                            ? line.substring(2732,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC161_TXNM_DUE_DT(line.length() > 2733 && !(line.length() < 2742)
                            ? line.substring(2733,2741) : (line.length() > 2733
                            ? line.substring(2733,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC162_TXNM_CONF_MATU_DT(line.length() > 2741 && !(line.length() < 2750)
                            ? line.substring(2741,2749) : (line.length() > 2741
                            ? line.substring(2741,line.length()) : "")
                    );
                    bankGuaranteeTxnmTemp.setC163_TXNM_EXTD_MATU_DT(line.length() > 2749 && !(line.length() < 2758)
                            ? line.substring(2749,2757) : (line.length() > 2749
                            ? line.substring(2749,line.length()) : "")
                    );
                    if (line.substring(2099,2107)!=null){
                        bankGuaranteeTxnmTemp.setTransactionDate(dateUtil.stringToDate(line.substring(2099,2107)));
                    }
                    bankGuaranteeTxnmTempRepository.save(bankGuaranteeTxnmTemp);
                }
                log.info(String.valueOf(total));
                total ++;
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        log.info(String.valueOf("Total Txnm Row Get : "+total));
        //System.out.println("END TXNM INSERT TO TEMPORARY");
        log.info("END TXNM INSERT TO TEMPORARY");
    }

    @Transactional
    public void insertDataToTxnhTempTableFromFile(){
        //System.out.println("START INSERT TXNH TO TEMPORARY");
        log.info("START INSERT TXNH TO TEMPORARY");
        FileInputStream inputStream = null;
        Scanner scanner = null;
        int total = 0;
        try {
            inputStream = new FileInputStream(txnhDestinationPath);
            scanner = new Scanner(inputStream, "UTF-8");
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (total >= 1 && scanner.hasNextLine()){
                    BankGuaranteeTxnhTemp bankGuaranteeTxnhTemp = new BankGuaranteeTxnhTemp();
                    bankGuaranteeTxnhTemp.setC001_TXNH_TXN_NBR(
                            line.length() > 0 && line.length() > 15
                            ? line.substring(0,15) : (line.length() > 0
                            ? line.substring(0,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC002_TXNH_VER_NBR(line.length() > 15 && line.length() > 18
                            ? line.substring(15,18) : (line.length() > 15
                            ? line.substring(15,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC003_TXNH_REF_NBR(line.length() > 18 && line.length() > 32
                            ? line.substring(18,32) : (line.length() > 18
                            ? line.substring(18,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC004_CUS_SRC_KEY(line.length() > 32 && line.length() > 52
                            ? line.substring(32,52) : (line.length() > 32
                            ? line.substring(32,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC005_CUS_CIF_NBR(line.length() > 52 && line.length() > 72
                            ? line.substring(52,72) : (line.length() > 52
                            ? line.substring(52,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC006_CUS_CIF_IND(line.length() > 72 && line.length() > 73
                            ? line.substring(72,73) : (line.length() > 72
                            ? line.substring(72,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC007_ACC_SRC_KEY(line.length() > 73 && line.length() > 93
                            ? line.substring(73,93) : (line.length() > 73
                            ? line.substring(73,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC008_TXNH_PROD_CD(line.length() > 93 && line.length() > 103
                            ? line.substring(93,103) : (line.length() > 93
                            ? line.substring(93,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC009_TXNH_FAC_CD(line.length() > 103 && line.length() > 113
                            ? line.substring(103,113) : (line.length() > 103
                            ? line.substring(103,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC010_TXNH_TXN_TYP_CD(line.length() > 113 && line.length() > 123
                            ? line.substring(113,123) : (line.length() > 113
                            ? line.substring(113,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC011_TXNH_GL_COST_CTR(line.length() > 123 && line.length() > 127
                            ? line.substring(123,127) : (line.length() > 123
                            ? line.substring(123,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC012_TXNH_GL_CORP_CD(line.length() > 127 && line.length() > 131
                            ? line.substring(127,131) : (line.length() > 127
                            ? line.substring(127,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC013_TXNH_GL_CD(line.length() > 131 && line.length() > 141
                            ? line.substring(131,141) : (line.length() > 131
                            ? line.substring(131,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC014_TXNH_PST_DT(line.length() > 141 && line.length() > 149
                            ? line.substring(141,149) : (line.length() > 141
                            ? line.substring(141,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC015_TXNH_PST_TIM(line.length() > 149 && line.length() > 157
                            ? line.substring(149,157) : (line.length() > 149
                            ? line.substring(149,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC016_TXNH_BNK_CD(line.length() > 157 && line.length() > 159
                            ? line.substring(157,159) : (line.length() > 157
                            ? line.substring(157,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC017_TXNH_BR_CD(line.length() > 159 && line.length() > 162
                            ? line.substring(159,162) : (line.length() > 159
                            ? line.substring(159,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC018_TXNH_CHNL_CD(line.length() > 162 && line.length() > 177
                            ? line.substring(162,177) : (line.length() > 162
                            ? line.substring(162,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC019_TXNH_GL_CCY_CD(line.length() > 177 && line.length() > 180
                            ? line.substring(177,180) : (line.length() > 177
                            ? line.substring(177,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC020_TXNH_TXN_CCY_CD(line.length() > 180 && line.length() > 183
                            ? line.substring(180,183) : (line.length() > 180
                            ? line.substring(180,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC021_TXNH_TXN_MNR_CD(line.length() > 183 && line.length() > 186
                            ? line.substring(183,186) : (line.length() > 183
                            ? line.substring(183,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC022_TXNH_TXN_STS(line.length() > 186 && line.length() > 187
                            ? line.substring(186,187) : (line.length() > 186
                            ? line.substring(186,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC023_TXNH_TXN_AMT(line.length() > 187 && line.length() > 205
                            ? line.substring(187,205) : (line.length() > 187
                            ? line.substring(187,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC024_TXNH_GL_EXCH_RT(line.length() > 205 && line.length() > 223
                            ? line.substring(205,223) : (line.length() > 205
                            ? line.substring(205,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC025_TXNH_TXN_EXCH_RT(line.length() > 223 && line.length() > 241
                            ? line.substring(223,241) : (line.length() > 223
                            ? line.substring(223,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC026_TXNH_DR_CR_IND(line.length() > 241 && line.length() > 242
                            ? line.substring(241,242) : (line.length() > 241
                            ? line.substring(241,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC027_TXNH_TXN_DT(line.length() > 242 && line.length() > 250
                            ? line.substring(242,250) : (line.length() > 242
                            ? line.substring(242,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC028_TXNH_TXN_TIM(line.length() > 250 && line.length() > 258
                            ? line.substring(250,258) : (line.length() > 250
                            ? line.substring(250,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC029_TXNH_SYS_CD(line.length() > 258 && line.length() > 263
                            ? line.substring(258,263) : (line.length() > 258
                            ? line.substring(258,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC030_TXNH_EXT_DT(line.length() > 263 && line.length() > 271
                            ? line.substring(263,271) : (line.length() > 263
                            ? line.substring(263,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC031_USR_DEF_1(line.length() > 271 && line.length() > 331
                            ? line.substring(271,331) : (line.length() > 271
                            ? line.substring(271,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC032_USR_DEF_2(line.length() > 331 && line.length() > 391
                            ? line.substring(331,391) : (line.length() > 331
                            ? line.substring(331,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC033_USR_DEF_3(line.length() > 391 && line.length() > 451
                            ? line.substring(391,451) : (line.length() > 391
                            ? line.substring(391,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC034_USR_DEF_4(line.length() > 451 && line.length() > 462
                            ? line.substring(451,462) : (line.length() > 451
                            ? line.substring(451,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC035_CTRY_REFNO(line.length() > 462 && line.length() > 478
                            ? line.substring(462,478) : (line.length() > 462
                            ? line.substring(462,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC036_TXNH_ORG_PROD_CD(line.length() > 478 && line.length() > 481
                            ? line.substring(478,481) : (line.length() > 478
                            ? line.substring(478,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC037_TXNH_COL_CHG_CCY(line.length() > 481 && line.length() > 484
                            ? line.substring(481,484) : (line.length() > 481
                            ? line.substring(481,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC038_USR_DEF_4B(line.length() > 484 && line.length() > 511
                            ? line.substring(484,511) : (line.length() > 484
                            ? line.substring(484,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC039_USR_DEF_5(line.length() > 511 && line.length() > 571
                            ? line.substring(511,571) : (line.length() > 511
                            ? line.substring(511,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC040_USR_DEF_DT_1(line.length() > 571 && line.length() > 579
                            ? line.substring(571,579) : (line.length() > 571
                            ? line.substring(571,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC041_USR_DEF_DT_2(line.length() > 579 && line.length() > 587
                            ? line.substring(579,587) : (line.length() > 579
                            ? line.substring(579,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC042_USR_DEF_TIM_3(line.length() > 587 && line.length() > 595
                            ? line.substring(587,595) : (line.length() > 587
                            ? line.substring(587,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC043_USR_DEF_11(line.length() > 595 && line.length() > 613
                            ? line.substring(595,613) : (line.length() > 595
                            ? line.substring(595,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC044_USR_DEF_12(line.length() > 613 && line.length() > 631
                            ? line.substring(613,631) : (line.length() > 613
                            ? line.substring(613,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC045_USR_DEF_13(line.length() > 631 && line.length() > 649
                            ? line.substring(631,649) : (line.length() > 631
                            ? line.substring(631,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC046_USR_DEF_14(line.length() > 649 && line.length() > 667
                            ? line.substring(649,667) : (line.length() > 649
                            ? line.substring(649,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC047_TXNH_COL_CHG_AMT(line.length() > 667 && line.length() > 685
                            ? line.substring(667,685) : (line.length() > 667
                            ? line.substring(667,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC048_USR_DEF_16(line.length() > 685 && line.length() > 703
                            ? line.substring(685,703) : (line.length() > 685
                            ? line.substring(685,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC049_USR_DEF_17(line.length() > 703 && line.length() > 721
                            ? line.substring(703,721) : (line.length() > 703
                            ? line.substring(703,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC050_REC_TYPE(line.length() > 721 && line.length() > 722
                            ? line.substring(721,722) : (line.length() > 721
                            ? line.substring(721,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC051_TXNH_TYP(line.length() > 722 && line.length() > 723
                            ? line.substring(722,723) : (line.length() > 722
                            ? line.substring(722,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC052_TXNH_SUB_TYP(line.length() > 723 && line.length() > 753
                            ? line.substring(723,753) : (line.length() > 723
                            ? line.substring(723,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC053_TXNH_MKR_ID(line.length() > 753 && line.length() > 783
                            ? line.substring(753,783) : (line.length() > 753
                            ? line.substring(753,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC054_TXNH_CKR_ID(line.length() > 783 && line.length() > 813
                            ? line.substring(783,813) : (line.length() > 783
                            ? line.substring(783,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC055_TXNH_CHNL_TYP(line.length() > 813 && line.length() > 843
                            ? line.substring(813,843) : (line.length() > 813
                            ? line.substring(813,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC056_TXNH_MAT_DT(line.length() > 843 && line.length() > 851
                            ? line.substring(843,851) : (line.length() > 843
                            ? line.substring(843,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC057_TXNH_ENT_DT(line.length() > 851 && line.length() > 859
                            ? line.substring(851,859) : (line.length() > 851
                            ? line.substring(851,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC058_TXNH_ENT_TIM(line.length() > 859 && line.length() > 865
                            ? line.substring(859,865) : (line.length() > 859
                            ? line.substring(859,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC059_TXNH_PRN_LCY(line.length() > 865 && line.length() > 883
                            ? line.substring(865,883) : (line.length() > 865
                            ? line.substring(865,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC060_TXNH_PRN_OCY(line.length() > 883 && line.length() > 901
                            ? line.substring(883,901) : (line.length() > 883
                            ? line.substring(883,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC061_TXNH_DR_MOD(line.length() > 901 && line.length() > 909
                            ? line.substring(901,909) : (line.length() > 901
                            ? line.substring(901,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC062_TXNH_CR_MOD(line.length() > 909 && line.length() > 917
                            ? line.substring(909,917) : (line.length() > 909
                            ? line.substring(909,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC063_TXNH_PEN(line.length() > 917 && line.length() > 935
                            ? line.substring(917,935) : (line.length() > 917
                            ? line.substring(917,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC064_TXNH_COMM(line.length() > 935 && line.length() > 953
                            ? line.substring(935,953) : (line.length() > 935
                            ? line.substring(935,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC065_TXNH_CHARGES(line.length() > 953 && line.length() > 971
                            ? line.substring(953,971) : (line.length() > 953
                            ? line.substring(953,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC066_TXNH_TXN_BNK_CD(line.length() > 971 && line.length() > 973
                            ? line.substring(971,973) : (line.length() > 971
                            ? line.substring(971,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC067_TXNH_TXN_BR_CD(line.length() > 973 && line.length() > 976
                            ? line.substring(973,976) : (line.length() > 973
                            ? line.substring(973,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC068_REL_REF_NBR(line.length() > 976 && line.length() > 987
                            ? line.substring(976,987) : (line.length() > 976
                            ? line.substring(976,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC069_MAIN_FX_CODE(line.length() > 987 && line.length() > 990
                            ? line.substring(987,990) : (line.length() > 987
                            ? line.substring(987,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC070_SUB_FX_CODE(line.length() > 990 && line.length() > 993
                            ? line.substring(990,993) : (line.length() > 990
                            ? line.substring(990,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC071_FX_DESCRP_1(line.length() > 993 && line.length() > 1033
                            ? line.substring(993,1033) : (line.length() > 993
                            ? line.substring(993,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC072_FX_DESCRP_2(line.length() > 1033 && line.length() > 1073
                            ? line.substring(1033,1073) : (line.length() > 1033
                            ? line.substring(1033,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC073_FX_DESCRP_3(line.length() > 1073 && line.length() > 1113
                            ? line.substring(1073,1113) : (line.length() > 1073
                            ? line.substring(1073,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC074_CTR_PARTYNM(line.length() > 1113 && line.length() > 1148
                            ? line.substring(1113,1148) : (line.length() > 1113
                            ? line.substring(1113,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC075_CTR_PARTY_LOC(line.length() > 1148 && line.length() > 1154
                            ? line.substring(1148,1154) : (line.length() > 1148
                            ? line.substring(1148,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC076_CTR_PARTY_CTY(line.length() > 1154 && line.length() > 1156
                            ? line.substring(1154,1156) : (line.length() > 1154
                            ? line.substring(1154,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC077_CTR_PARTY_TYP(line.length() > 1156 && line.length() > 1157
                            ? line.substring(1156,1157) : (line.length() > 1156
                            ? line.substring(1156,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC078_TXN_PURC_CODE(line.length() > 1157 && line.length() > 1163
                            ? line.substring(1157,1163) : (line.length() > 1157
                            ? line.substring(1157,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC079_TXN_PURC_IND(line.length() > 1163 && line.length() > 1164
                            ? line.substring(1163,1164) : (line.length() > 1163
                            ? line.substring(1163,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC080_TXN_PURC_DESCR1(line.length() > 1164 && line.length() > 1204
                            ? line.substring(1164,1204) : (line.length() > 1164
                            ? line.substring(1164,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC081_TXN_PURC_DESCR2(line.length() > 1204 && line.length() > 1244
                            ? line.substring(1204,1244) : (line.length() > 1204
                            ? line.substring(1204,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC082_SAFE_APPR_NBR(line.length() > 1244 && line.length() > 1274
                            ? line.substring(1244,1274) : (line.length() > 1244
                            ? line.substring(1244,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC083_TXNH_FX_IND(line.length() > 1274 && line.length() > 1275
                            ? line.substring(1274,1275) : (line.length() > 1274
                            ? line.substring(1274,line.length()) : "")
                    );
                    bankGuaranteeTxnhTemp.setC084_TXNH_MODE(line.length() > 1275 && line.length() > 1276
                            ? line.substring(1275,1276) : (line.length() > 1275
                            ? line.substring(1275,line.length()) : "")
                    );
                    if (line.substring(851,859)!=null){
                        bankGuaranteeTxnhTemp.setTransactionDate(dateUtil.stringToDate(line.substring(242,250)));
                    }
                    bankGuaranteeTxnhTempRepository.save(bankGuaranteeTxnhTemp);
                }
                log.info(String.valueOf(total));
                total ++;
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        log.info(String.valueOf("Total Txnh Row Get : "+total));
        //System.out.println("END TXNH INSERT TO TEMPORARY");
        log.info("END TXNH INSERT TO TEMPORARY");
    }

    @Transactional
    public Boolean mvTempToBGTable(String checkDate, Integer dateDiv){
        log.info("START INSERT TO REGISTRATION");
        Boolean result = Boolean.FALSE;

        int dateDiff = (int) dateDiv;
        String yesterday = dateUtil.instantToString(Instant.ofEpochMilli(dateUtil.stringToDate(checkDate)).minus(Period.ofDays(dateDiff)), "yyyyMMdd");
        String today= dateUtil.instantToString(Instant.ofEpochMilli(dateUtil.stringToDate(checkDate)), "yyyyMMdd");
        Boolean txnmProcess = mvTempTxnmToBGTable(yesterday, today);
        if (txnmProcess){
            Boolean txnhProcess = mvTempTxnhToBGTable(yesterday, today);
            if (txnhProcess){
                result = Boolean.TRUE;
            }
        }
        log.info("END INSERT TO REGISTRATION");
        return result;
    }

    @Transactional
    public Boolean mvTempToBGTable(){
        log.info("START INSERT TO REGISTRATION");
        Boolean result = Boolean.FALSE;
        String yesterday = dateUtil.instantToString(Instant.now().minus(Period.ofDays(1)), "yyyyMMdd");
        String today = dateUtil.instantToString(Instant.now(), "yyyyMMdd");
        Boolean txnmProcess = mvTempTxnmToBGTable(yesterday, today);
        if (txnmProcess){
            Boolean txnhProcess = mvTempTxnhToBGTable(yesterday, today);
            if (txnhProcess){
                result = Boolean.TRUE;
            }
        }
        log.info("END INSERT TO REGISTRATION");
        return result;
    }

    @Transactional
    private Boolean mvTempTxnmToBGTable(String startDate, String endDate){
        Boolean result = Boolean.TRUE;
        String bankGuarantiCode = "RCMPG";
        List<Beneficiary> beneficiaries = beneficiaryRepository.findByStatusIn(Statusutil.getStatusBeneficiary());
        List<BankGuaranteeTxnmTemp> bankGuaranteeTxnmTemps = bankGuaranteeTxnmTempRepository.getListByTransactionDate(startDate, endDate, bankGuarantiCode);
        bankGuaranteeTxnmTemps.forEach(bankGuaranteeTxnmTemp -> {
            String validate = validator.emptyValTempToRegistration(bankGuaranteeTxnmTemp, beneficiaries);
            if (validate != null){
                Registration registration = new Registration();
                BankGuaranteeBackupTemp bankGuaranteeBackupTemp = new BankGuaranteeBackupTemp();
                bankGuaranteeBackupTemp.setNomorJaminan(bankGuaranteeTxnmTemp.getC002_TXNM_REF_NBR().trim());
                registration.setNomorJaminan(bankGuaranteeTxnmTemp.getC002_TXNM_REF_NBR().trim());
                bankGuaranteeBackupTemp.setNomorAmentmend("0");
                registration.setNomorAmentmend("0");
                Optional<Currency> currency = currencyRepository.findByCurrency(bankGuaranteeTxnmTemp.getC011_TXNM_CCY_CD().trim());
                if (currency.isPresent()){
                    bankGuaranteeBackupTemp.setCurrency(currency.get());
                    registration.setCurrency(currency.get());
                }
                bankGuaranteeBackupTemp.setTanggalBerlaku(dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC016_TXNM_STR_DT().trim()));
                registration.setTanggalBerlaku(dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC016_TXNM_STR_DT().trim()));
                bankGuaranteeBackupTemp.setTanggalBerakhir(dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC017_TXNM_EXP_DT().trim()));
                registration.setTanggalBerakhir(dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC017_TXNM_EXP_DT().trim()));
                Optional<JenisJaminan> jenisJaminan = jenisJaminanRepository.findByCodeJaminanStaging(bankGuaranteeTxnmTemp.getC154_TXNM_PG_TYPE().trim());
                if (jenisJaminan.isPresent()){
                    bankGuaranteeBackupTemp.setJenisJaminan(jenisJaminan.get());
                    registration.setJenisJaminan(jenisJaminan.get());
                }
                BigDecimal nilaiJaminan = new BigDecimal(bankGuaranteeTxnmTemp.getC041_TXNM_ORG_AMT().trim());
                bankGuaranteeBackupTemp.setNilaiJaminan(nilaiJaminan.divide(new BigDecimal(100)));
                registration.setNilaiJaminan(nilaiJaminan.divide(new BigDecimal(100)));
                Optional<Beneficiary> beneficiary = beneficiaryRepository.findByCodeBeneficiary(validate);
                if (beneficiary.isPresent()){
                    bankGuaranteeBackupTemp.setBeneficiary(beneficiary.get());
                    registration.setBeneficiary(beneficiary.get());
                }
                bankGuaranteeBackupTemp.setBgStatus(Constants.BankGuaranteeStatus.BGFROMSTAGING);
                registration.setBgStatus(Constants.BankGuaranteeStatus.BGFROMSTAGING);
                bankGuaranteeBackupTemp.setApplicant(bankGuaranteeTxnmTemp.getC107_TXNM_CUST_NAM().trim());
                registration.setApplicant(bankGuaranteeTxnmTemp.getC107_TXNM_CUST_NAM().trim());
                bankGuaranteeBackupTemp.setTanggalTerbit(dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC124_TXNM_ENTRY_DT().trim()));
                registration.setTanggalTerbit(dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC124_TXNM_ENTRY_DT().trim()));
                bankGuaranteeBackupTemp.setTanggalBatasClaim(dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC095_TXNM_CLM_DATE().trim()));
                registration.setTanggalBatasClaim(dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC095_TXNM_CLM_DATE().trim()));
                bankGuaranteeBackupTemp.setTenggangWaktuClaim(dateUtil.countDaysDif(dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC095_TXNM_CLM_DATE()), dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC017_TXNM_EXP_DT().trim())));
                registration.setTenggangWaktuClaim(dateUtil.countDaysDif(dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC095_TXNM_CLM_DATE()), dateUtil.stringToDate(bankGuaranteeTxnmTemp.getC017_TXNM_EXP_DT().trim())));
                bankGuaranteeBackupTempRepository.save(bankGuaranteeBackupTemp);
                registrationRepository.save(registration);

                auditTrailUtil.saveAudit(
                        Constants.Event.CREATE,
                        Constants.Module.AS_400,
                        null,
                        new JSONObject(bankGuaranteeBackupTemp).toString(),
                        Constants.Remark.STAGING_GET_DATA_TXNM_BACKUP,
                        "Systems/Ops"
                );

                auditTrailUtil.saveAudit(
                        Constants.Event.CREATE,
                        Constants.Module.AS_400,
                        null,
                        new JSONObject(bankGuaranteeBackupTemp).toString(),
                        Constants.Remark.STAGING_GET_DATA_TXNM,
                        "Systems/Ops"
                );
            }
        });
        return result;
    }

    @Transactional
    private Boolean mvTempTxnhToBGTable(String startDate, String endDate){
        Boolean result = Boolean.TRUE;
        String typeTxnh = "HKBBGAMA";
        List<String> bankGuaranteeTxnhTemps = bankGuaranteeTxnhTempRepository.getListTxnhString(startDate, endDate, typeTxnh);
        bankGuaranteeTxnhTemps.forEach(bankGuaranteeTxnhTemp -> {
            Optional<BankGuaranteeBackupTemp> bankGuaranteeBackupTemp = bankGuaranteeBackupTempRepository.findTop1ByNomorJaminanOrderByNomorAmentmendDesc(bankGuaranteeTxnhTemp);
            if (bankGuaranteeBackupTemp.isPresent()){
                Integer amendmentTemp = Integer.valueOf(bankGuaranteeBackupTemp.get().getNomorAmentmend())+1;
                BankGuaranteeBackupTemp newBankGuaranteeBackupTemp = new BankGuaranteeBackupTemp();
                insertBgBackupTemp(newBankGuaranteeBackupTemp, bankGuaranteeBackupTemp.get(), amendmentTemp.toString());
                auditTrailUtil.saveAudit(
                        Constants.Event.CREATE,
                        Constants.Module.AS_400,
                        null,
                        new JSONObject(bankGuaranteeBackupTemp).toString(),
                        Constants.Remark.STAGING_GET_DATA_TXNH_BACKUP,
                        "Systems/Ops"
                );
            }

            Optional<Registration> registration = registrationRepository.findTop1ByNomorJaminanAndBgStatusNotOrderByNomorAmentmendDesc(bankGuaranteeTxnhTemp, Constants.BankGuaranteeStatus.DELETEDBG);
            if (registration.isPresent()){
                Integer amendmentReg = Integer.valueOf(registration.get().getNomorAmentmend())+1;
                Registration newRegistration = new Registration();
                insertRegistration(newRegistration, registration.get(), amendmentReg.toString());
                auditTrailUtil.saveAudit(
                        Constants.Event.CREATE,
                        Constants.Module.AS_400,
                        null,
                        new JSONObject(newRegistration).toString(),
                        Constants.Remark.STAGING_GET_DATA_TXNH,
                        "Systems/Ops"
                );
            }

        });
        return result;
    }

    @Transactional
    private void insertRegistration(Registration registration, Registration oldRegistration, String nomorAmendment){
        registration = RegistrationMapper.INSTANCE.oldToNewEntity(oldRegistration, registration);
        registration.setNomorAmentmend(nomorAmendment);
        registration.setBgStatus(Constants.BankGuaranteeStatus.BGFROMSTAGING);
        registrationRepository.save(registration);
    }

    @Transactional
    private void insertBgBackupTemp(BankGuaranteeBackupTemp bankGuaranteeBackupTemp, BankGuaranteeBackupTemp oldBankGuaranteeBackupTemp, String nomorAmendment){
        bankGuaranteeBackupTemp.setNomorJaminan(oldBankGuaranteeBackupTemp.getNomorJaminan());
        bankGuaranteeBackupTemp.setNomorAmentmend(nomorAmendment);
        bankGuaranteeBackupTemp.setCurrency(oldBankGuaranteeBackupTemp.getCurrency());
        bankGuaranteeBackupTemp.setTanggalBerlaku(oldBankGuaranteeBackupTemp.getTanggalBerlaku());
        bankGuaranteeBackupTemp.setTanggalBerakhir(oldBankGuaranteeBackupTemp.getTanggalBerakhir());
        bankGuaranteeBackupTemp.setJenisJaminan(oldBankGuaranteeBackupTemp.getJenisJaminan());
        bankGuaranteeBackupTemp.setNilaiJaminan(oldBankGuaranteeBackupTemp.getNilaiJaminan());
        bankGuaranteeBackupTemp.setBeneficiary(oldBankGuaranteeBackupTemp.getBeneficiary());
        bankGuaranteeBackupTemp.setBgStatus(Constants.BankGuaranteeStatus.BGFROMSTAGING);
        bankGuaranteeBackupTemp.setApplicant(oldBankGuaranteeBackupTemp.getApplicant());
        bankGuaranteeBackupTemp.setTanggalTerbit(oldBankGuaranteeBackupTemp.getTanggalTerbit());
        bankGuaranteeBackupTemp.setTanggalBatasClaim(oldBankGuaranteeBackupTemp.getTanggalBatasClaim());
        bankGuaranteeBackupTemp.setTenggangWaktuClaim(oldBankGuaranteeBackupTemp.getTenggangWaktuClaim());
        bankGuaranteeBackupTempRepository.save(bankGuaranteeBackupTemp);
    }

    @Transactional
    public Boolean deleteBankGuaranteeTemp(){
        log.info("START DELETE TXNM and TXNH TABLE");
        bankGuaranteeTxnmTempRepository.deleteAll();
        bankGuaranteeTxnhTempRepository.deleteAll();
        log.info("END DELETE TXNM and TXNH TABLE");
        return Boolean.TRUE;
    }

    public Boolean deleteTxnmFileContent(){
        try {
            new FileOutputStream(txnmDestinationPath).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }

    public Boolean deleteTxnhFileContent(){
        try {
            new FileOutputStream(txnhDestinationPath).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }
}
