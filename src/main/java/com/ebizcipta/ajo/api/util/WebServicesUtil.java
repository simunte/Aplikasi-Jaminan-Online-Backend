package com.ebizcipta.ajo.api.util;

import com.ebizcipta.ajo.api.domain.*;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.*;
import com.ebizcipta.ajo.api.service.dto.EmailRejectDTO;
import com.ebizcipta.ajo.api.service.impl.FileManagementServiceImpl;
import com.ebizcipta.ajo.api.service.validator.AbgValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@PropertySource("classpath:configuration/web-service.properties")
public class WebServicesUtil{
    @Autowired
    private FileManagementServiceImpl fileManagementService;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private ConfirmationRepository confirmationRepository;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;
    @Autowired
    private AbgValidator abgValidator;

    @Value("${uob.bank.code}")
    String codeUOB;
    @Value("${file.directory.parent}")
    String parentDirectory;
    @Value("${file.directory.pdf}")
    String pdfUploadDirectory;
    @Value("${file.directory.jasper.pdf}")
    String pdfJasperDirectory;

//    FOR SMTP
    @Value("${smtp.url}")
    String smtpApiUrl;
    @Value("${database.host}")
    String dbHost;
    @Value("${database.port}")
    String dbPort;
    @Value("${database.protocol}")
    String dbProtocol;

    private final static String POST_METHOD = "POST";
    private final static String GET_METHOD = "GET";
    private final static String PUT_METHOD = "PUT";
    private final static String DELETE_METHOD = "DELETE";
    public final static String DATE_FORMAT = "yyyy-MM-dd";

    public String HandleMethodPOST(MultipartEntity formRequest, String payloadBody, HttpURLConnection postConnection) throws IOException {
        String result="";
        OutputStream outputStream = postConnection.getOutputStream();
        if (formRequest.getContentLength() > 0){
            formRequest.writeTo(outputStream);
        }
        if (payloadBody != null){
            String PAYLOAD = payloadBody;
            outputStream.write(PAYLOAD.getBytes());
        }

        outputStream.close();
        int responseCode = postConnection.getResponseCode();
        log.info("HTTP CODE : "+String.valueOf(responseCode));
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED){
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    postConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();
            result = response.toString();
        }
        else {
            result = "{\n"+
                    "\t\"code:\""+String.valueOf(postConnection.getResponseCode())+"\",\n"+
                    "\t\"message:\""+postConnection.getResponseMessage()+"\",\n"+
                    "}";
        }
        log.info("RESULT AKHIR : "+result);

        return result;
    }

    public String HandleMethodGET(HttpURLConnection getConnection){
        return "";
    }

    @Async
    public Boolean handleSendEmailRejection(MasterConfiguration masterConfiguration, EmailRejectDTO emailRejectDTO) throws IOException{
        log.info("Start Send Email");
        MultipartEntity formRequest = new MultipartEntity();
        StringBody setFrom = new StringBody(emailRejectDTO.getSetFrom());
        StringBody recipient = new StringBody(emailRejectDTO.getRecipient());
        StringBody subject = new StringBody(emailRejectDTO.getSubject());
        StringBody namaPemohon = new StringBody(emailRejectDTO.getNamaPemohon());
        StringBody noKontrak = new StringBody(emailRejectDTO.getNoKontrak());
        StringBody noJaminan = new StringBody(emailRejectDTO.getNoJaminan());
        StringBody tanggalTerbit = new StringBody(emailRejectDTO.getTanggalTerbit());
        StringBody tanggalBerlaku = new StringBody(emailRejectDTO.getTanggalBerlaku());
        StringBody tanggalJatuhTempo = new StringBody(emailRejectDTO.getTanggalJatuhTempo());
        StringBody currency = new StringBody(emailRejectDTO.getCurrency());
        StringBody nilaiJaminan = new StringBody(emailRejectDTO.getNilaiJaminan());
        StringBody catatan = new StringBody(emailRejectDTO.getCatatan());
        StringBody username = new StringBody(masterConfiguration.getUsername());
        StringBody password = new StringBody(masterConfiguration.getPassword());
        StringBody smtpHostname = new StringBody(masterConfiguration.getSmtpHostname());
        StringBody smtpPassword = new StringBody(masterConfiguration.getSmtpPassword());
        StringBody smtpPort = new StringBody(masterConfiguration.getSmtpPort());
        StringBody retentionData = new StringBody(masterConfiguration.getRetentionData());
        StringBody mailingList = new StringBody(masterConfiguration.getMailingList());

        formRequest.addPart("setFrom", setFrom);
        formRequest.addPart("recipient", recipient);
        formRequest.addPart("subject", subject);
        formRequest.addPart("namaPemohon", namaPemohon);
        formRequest.addPart("noKontrak", noKontrak);
        formRequest.addPart("noJaminan", noJaminan);
        formRequest.addPart("tanggalTerbit", tanggalTerbit);
        formRequest.addPart("tanggalBerlaku", tanggalBerlaku);
        formRequest.addPart("tanggalJatuhTempo", tanggalJatuhTempo);
        formRequest.addPart("currency", currency);
        formRequest.addPart("nilaiJaminan", nilaiJaminan);
        formRequest.addPart("catatan", catatan);
        formRequest.addPart("username", username);
        formRequest.addPart("password", password);
        formRequest.addPart("smtpHostname", smtpHostname);
        formRequest.addPart("smtpPassword", smtpPassword);
        formRequest.addPart("smtpPort", smtpPort);
        formRequest.addPart("retentionData", retentionData);
        formRequest.addPart("mailingList", mailingList);

        String completeUrl = dbProtocol+"://"+dbHost+":"+dbPort+smtpApiUrl;
        log.info(completeUrl);
        URL smtpUrl = new URL(completeUrl);
        HttpURLConnection postConnection = (HttpURLConnection) smtpUrl.openConnection();
        postConnection.setDoOutput(true);
        postConnection.setRequestMethod(POST_METHOD);
        postConnection.setRequestProperty("Content-type",formRequest.getContentType().getValue());
        String result = HandleMethodPOST(formRequest, null, postConnection);
        log.info(result);
        log.info("End Send Email");
        return  Boolean.TRUE;
    }

    public JSONObject handleGetToken(Beneficiary webServicesAbg)throws IOException{
        if (!abgValidator.getTokenAbgValidator(webServicesAbg).equalsIgnoreCase("SUCCESS")){
            throw new BadRequestAlertException(abgValidator.getTokenAbgValidator(webServicesAbg), "", "");
        }
        log.info("Start GET TOKEN");
        String result = "";
        String completeUrl = webServicesAbg.getAuthTokenUrl();
        URL tokenUrl = new URL(completeUrl);
        HttpURLConnection postConnection = (HttpURLConnection) tokenUrl.openConnection();
        postConnection.setDoOutput(true);
        postConnection.setRequestMethod(POST_METHOD);

        MultipartEntity formRequest = new MultipartEntity();
        StringBody grantType = new StringBody(webServicesAbg.getGrantType());
        StringBody clientId = new StringBody(webServicesAbg.getClientId());
        StringBody clientSecret = new StringBody(webServicesAbg.getClientSecret());
        StringBody username = new StringBody(webServicesAbg.getUsername());
        StringBody password = new StringBody(webServicesAbg.getPassword());
        formRequest.addPart("grant_type", grantType);
        formRequest.addPart("client_id", clientId);
        formRequest.addPart("client_secret", clientSecret);
        formRequest.addPart("username", username);
        formRequest.addPart("password", password);

        postConnection.setRequestProperty("Content-type",formRequest.getContentType().getValue());

        result = HandleMethodPOST(formRequest, null, postConnection);
        JSONObject resultJson = null;
        try {
            resultJson = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        log.info("END GET TOKEN");
        return  resultJson;
    }

    public JSONObject inputJaminanPln(Registration registration, JSONObject plnOauthResult, Beneficiary webServicesAbg) throws IOException{
        log.info("Start INPUT JAMINAN");
        Optional<User> user = userRepository.findById(registration.getUserApprove().getId());
        String result = "";

        String completeUrl = webServicesAbg.getInputJaminanUrl();
        URL inputJaminanUrl = new URL(completeUrl);
        HttpURLConnection postConnection = (HttpURLConnection) inputJaminanUrl.openConnection();
        postConnection.setDoOutput(true);
        postConnection.setRequestMethod(POST_METHOD);

        String accessToken = "";
        String tokenType = "";
        try {
            accessToken = (String) plnOauthResult.get("access_token");
            tokenType = (String) plnOauthResult.get("token_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postConnection.setRequestProperty("Authorization", tokenType+" "+accessToken);

        //NOMOR REFERENSI
        String nomorReferensiEx = "0";
        String nomorJaminanEx = registration.getNomorJaminan().replaceAll("\\s+", "");
        if (!registration.getNomorAmentmend().replaceAll("\\s+", "").equalsIgnoreCase("0")){
            nomorReferensiEx = registration.getNomorJaminan().replaceAll("\\s+", "");
            nomorJaminanEx = registration.getNomorJaminan().replaceAll("\\s+", "")+".AMA"+registration.getNomorAmentmend();
        }
        String fileName = registration.getSoftCopyJaminanUrl();

        if (!abgValidator.inputJaminanAbgValidator(registration, user.get(), fileName).trim().equalsIgnoreCase("SUCCESS")){
            throw new BadRequestAlertException(abgValidator.inputJaminanAbgValidator(registration, user.get(), fileName), "", "");
        }
        MultipartEntity formRequest = new MultipartEntity();
        StringBody tglKirim = new StringBody(dateUtil.instantToString(Instant.now(), DATE_FORMAT));
        StringBody bankPenerbit = new StringBody(codeUOB);
        StringBody alamatBankPenerbit = new StringBody(registration.getAlamatBankPenerbit().getAlamatBankPenerbit());
        StringBody nomorJaminan = new StringBody(nomorJaminanEx);
        StringBody nomorRef= new StringBody(nomorReferensiEx);
        StringBody jenisJaminan = new StringBody(registration.getJenisJaminan().getCodeJaminan());
        StringBody jenisProduk = new StringBody(registration.getJenisProduk().getCodeProduk());
        StringBody beneficiary = new StringBody(registration.getBeneficiary().getNamaBeneficiary());
        StringBody unitPengguna = new StringBody(registration.getUnitPengguna().getUnitPengguna());
        StringBody applicant = new StringBody(registration.getApplicant());
        StringBody nomorKontrak = new StringBody(registration.getNomorKontrak());
        StringBody uraianPekerjaan = new StringBody(registration.getUraianPekerjaan());
        StringBody currency = new StringBody(registration.getCurrency().getCurrency());
        StringBody nilaiJaminan = new StringBody(registration.getNilaiJaminan().toString());
        StringBody tglTerbit = new StringBody(dateUtil.instantToString(dateUtil.longToInstant(registration.getTanggalTerbit()), DATE_FORMAT));
        StringBody tglBerlaku = new StringBody(dateUtil.instantToString(dateUtil.longToInstant(registration.getTanggalBerlaku()), DATE_FORMAT));
        StringBody tglBerakhir = new StringBody(dateUtil.instantToString(dateUtil.longToInstant(registration.getTanggalBerakhir()), DATE_FORMAT));
        StringBody tglClaim = new StringBody(dateUtil.instantToString(dateUtil.longToInstant(registration.getTanggalBatasClaim()), DATE_FORMAT));
        FileBody namaFileJaminan = new FileBody(new File(fileName));
        StringBody namaUser = new StringBody(user.get().getFirstName());
        StringBody email = new StringBody(user.get().getEmail());

        formRequest.addPart("tgl_kirim", tglKirim);
        formRequest.addPart("bank_penerbit", bankPenerbit);
        formRequest.addPart("alamat_bank_penerbit", alamatBankPenerbit);
        formRequest.addPart("nomor_jaminan", nomorJaminan);
        formRequest.addPart("nomor_ref", nomorRef);
        formRequest.addPart("jenis_jaminan", jenisJaminan);
        formRequest.addPart("jenis_produk", jenisProduk);
        formRequest.addPart("beneficary", beneficiary);
        formRequest.addPart("unit_pengguna", unitPengguna);
        formRequest.addPart("applicant", applicant);
        formRequest.addPart("no_kontrak", nomorKontrak);
        formRequest.addPart("uraian_pekerjaan", uraianPekerjaan);
        formRequest.addPart("currency", currency);
        formRequest.addPart("nilai_jaminan", nilaiJaminan);
        formRequest.addPart("tgl_terbit", tglTerbit);
        formRequest.addPart("tgl_berlaku", tglBerlaku);
        formRequest.addPart("tgl_berakhir", tglBerakhir);
        formRequest.addPart("tgl_claim", tglClaim);
        formRequest.addPart("nama_file_jaminan", namaFileJaminan);
        formRequest.addPart("nama_user", namaUser);
        formRequest.addPart("email", email);

        postConnection.setRequestProperty("Content-Type", formRequest.getContentType().getValue());
        result = HandleMethodPOST(formRequest, null, postConnection);
        JSONObject resultJson = null;
        try {
            resultJson = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        log.info("End INPUT JAMINAN");
        return  resultJson;
    }

    public JSONObject validityJaminanPln(Registration registration, JSONObject plnOauthResult, Beneficiary webServicesAbg) throws IOException{
        //System.out.println("Start VALIDITY JAMINAN");
        log.info("Start VALIDITY JAMINAN");
        String result = "";

        String completeUrl = webServicesAbg.getValidityJaminanUrl();
        URL inputJaminanUrl = new URL(completeUrl);
        HttpURLConnection postConnection = (HttpURLConnection) inputJaminanUrl.openConnection();
        postConnection.setDoOutput(true);
        postConnection.setRequestMethod(POST_METHOD);

        String accessToken = "";
        String tokenType = "";
        try {
            accessToken = (String) plnOauthResult.get("access_token");
            tokenType = (String) plnOauthResult.get("token_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postConnection.setRequestProperty("Accept", "application/json");
        postConnection.setRequestProperty("Authorization", tokenType+" "+accessToken);

        Optional<Confirmation> confirmation = confirmationRepository.findByRegistration(registration);
        String fileName = parentDirectory+pdfJasperDirectory+confirmation.get().getSoftCopySuratKonfirmasi();
        if (!confirmation.isPresent()){
            throw new BadRequestAlertException("Nomor Jaminan Not Found","","");
        }

        if (!abgValidator.validityJaminanAbgValidator(confirmation.get(), fileName).trim().equalsIgnoreCase("SUCCESS")){
            throw new BadRequestAlertException(abgValidator.validityJaminanAbgValidator(confirmation.get(), fileName), "", "");
        }
        StringBody nomorValidityBank = new StringBody(confirmation.get().getNomorKonfirmasiBank());
        StringBody nomorJaminan = new StringBody(confirmation.get().getRegistration().getNomorJaminan());
        StringBody penandaTangan = new StringBody(confirmation.get().getUser().getFirstName());
        StringBody jabatanPenandaTangan = new StringBody(confirmation.get().getUser().getPosition());
        StringBody tanggalKonfirmasi= new StringBody(dateUtil.instantToString(confirmation.get().getTanggalSuratKonfirmasi(), DATE_FORMAT));
        FileBody namaFileValidity = new FileBody(new File(fileName));

        MultipartEntity formRequest = new MultipartEntity();
        formRequest.addPart("no_validity", nomorValidityBank);
        formRequest.addPart("nomor_jaminan", nomorJaminan);
        formRequest.addPart("penanda_tangan", penandaTangan);
        formRequest.addPart("jabatan_penanda_tangan", jabatanPenandaTangan);
        formRequest.addPart("tgl_konfirmasi", tanggalKonfirmasi);
        formRequest.addPart("nama_file_validity", namaFileValidity);

        postConnection.setRequestProperty("Content-Type", formRequest.getContentType().getValue());

        result = HandleMethodPOST(formRequest, null, postConnection);
        JSONObject resultJson = null;
        try {
            resultJson = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println("End VALIDITY JAMINAN");
        log.info("End VALIDITY JAMINAN");
        return  resultJson;
    }

    public Boolean PlnAbgApi(Registration registration) throws IOException {
        Boolean result = Boolean.TRUE;
        Beneficiary webServicesAbg = beneficiaryRepository.findById(registration.getBeneficiary().getId())
                .orElseThrow(()-> new EntityNotFoundException("Beneficiary Web Services Not Found"));

        //CALL API
        JSONObject plnOauthResult = handleGetToken(webServicesAbg);
        JSONObject inputJaminan = inputJaminanPln(registration, plnOauthResult, webServicesAbg);
        JSONObject validityJaminan = validityJaminanPln(registration, plnOauthResult, webServicesAbg);
        return result;
    }
}
