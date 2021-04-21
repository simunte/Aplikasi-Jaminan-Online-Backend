package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.Confirmation;
import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.ConfirmationRepository;
import com.ebizcipta.ajo.api.repositories.RegistrationRepository;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import com.ebizcipta.ajo.api.service.FileManagementService;
import com.ebizcipta.ajo.api.service.dto.FileManagementDTO;
import com.ebizcipta.ajo.api.util.CurrencyUtil;
import com.ebizcipta.ajo.api.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@Transactional
@PropertySource("classpath:configuration/file-management.properties")
public class FileManagementServiceImpl implements FileManagementService{

    @Value("${file.directory.parent}")
    String parentDirectory;

    @Value("${file.directory.support-document}")
    String supportDocumentNasabah;

    @Value("${file.directory.pdf}")
    String pdfUploadDirectory;

    @Value("${file.directory.jasper.template}")
    String jasperTemplateDirectory;

    @Value("${file.directory.jasper.pdf}")
    String jasperPdfDirectory;

    @Value("${file.confirmation.jasper.template.name}")
    String confirmationJasperTemplate;

    @Value("${file.directory.img}")
    String imageDirectory;

    @Value("${bank.code}")
    String bankCode;

    public final static String DD_MM_YYYY = "dd-MM-yyyy";

    @Autowired
    private ConfirmationRepository confirmationRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private CurrencyUtil currency;


    @Transactional
    @Override
    public FileManagementDTO uploadSupportDocument(MultipartFile file){
        UUID nameFile = UUID.randomUUID();
        String newFileName = nameFile.toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String finalUrl = parentDirectory + supportDocumentNasabah + newFileName;
        try {
            byte[] fileByte = file.getBytes();
            Path path = Paths.get(finalUrl);
            Files.write(path, fileByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileManagementDTO fileManagementDTO = new FileManagementDTO();
        fileManagementDTO.setInputFileName(file.getName());
        fileManagementDTO.setOriginalFileName(file.getOriginalFilename());
        fileManagementDTO.setNewFileName(newFileName);
        fileManagementDTO.setFileSaveUrl(finalUrl);
        return fileManagementDTO;
    }
    @Override
    @Transactional
    public FileManagementDTO uploadFile(MultipartFile file , String type, Long idJaminan, String codeJaminan, Long tanggalTerbit){
        UUID uuid = UUID.randomUUID();
        String newFileName = null;
        String finalUrl = null;
        if(type.toLowerCase().equals("png")
            || type.toLowerCase().equals("jpg")
            || type.toLowerCase().equals("jpeg")
            || type.toLowerCase().equals("gif")){
            newFileName = uuid.toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            finalUrl = parentDirectory + imageDirectory + newFileName;
        }else{
            String sequenceNumber = null;
            String registrationId;
            Optional<Registration> registrationNone = registrationRepository.findTop1ByOrderByIdDesc();
            if (idJaminan == null){
              if(registrationNone.isPresent()){
                  registrationId = registrationNone.get().getId().toString();
              }else{
                  registrationId = "1";
              }
            } else {
                Registration registration = registrationRepository.findById(idJaminan)
                        .orElseThrow(() -> new BadRequestAlertException("Nomor Jaminan Tidak Ditemukan","",""));
                registrationId = registration.getId().toString();
            }
            if (registrationId.length() < 9){
                String noll = "0";
                String newString="";
                Integer length = 1;
                while (length <= (9 - registrationId.length())){
                    newString = newString.concat(noll);
                    length++;
                }
                sequenceNumber = newString+registrationId;
            }else {
                sequenceNumber = registrationId;
            }
            String tanggalTerbitNew = dateUtil.instantToString(dateUtil.longToInstant(tanggalTerbit), "yyyyMMdd");
            newFileName = bankCode+"-JM"+codeJaminan+sequenceNumber+"-"+tanggalTerbitNew+ "."+ FilenameUtils.getExtension(file.getOriginalFilename());
            finalUrl = parentDirectory + pdfUploadDirectory + newFileName;
        }

        try {
            byte[] fileByte = file.getBytes();
            Path path = Paths.get(finalUrl);
            Files.write(path, fileByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileManagementDTO fileManagementDTO = new FileManagementDTO();
        fileManagementDTO.setInputFileName(file.getName());
        fileManagementDTO.setOriginalFileName(file.getOriginalFilename());
        fileManagementDTO.setNewFileName(newFileName);
        fileManagementDTO.setFileSaveUrl(finalUrl);
        return fileManagementDTO;
    }

    @Override
    @Transactional
    public void downloadFile(HttpServletResponse response, String fileName) throws IOException {
        File myfile = new File(parentDirectory + pdfUploadDirectory +fileName);
        response.setHeader("Content-Disposition", "filename=" + myfile.getName());
        OutputStream outputStream = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(myfile);
        byte[] fileByte = new byte[(int) myfile.length()];
        int length;
        while((length = fileInputStream.read(fileByte)) > 0){
            outputStream.write(fileByte, 0, length);
        }
        fileInputStream.close();
        outputStream.flush();
//        return fileByte;
    }

    @Override
    @Transactional
    public void downloadFileNasabah(HttpServletResponse response, String fileUrl) throws IOException {
        File myfile = new File(fileUrl);
        response.setHeader("Content-Disposition", "filename=" + myfile.getName());
        OutputStream outputStream = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(myfile);
        byte[] fileByte = new byte[(int) myfile.length()];
        int length;
        while((length = fileInputStream.read(fileByte)) > 0){
            outputStream.write(fileByte, 0, length);
        }
        fileInputStream.close();
        outputStream.flush();
//        return fileByte;
    }

    @Override
    @Transactional(readOnly = true)
    public void downloadSuratConfirmasi(HttpServletResponse response, Long idJaminan, Boolean ttdFile) throws IOException {
        Registration registration = registrationRepository.findById(idJaminan)
                .orElseThrow(()-> new BadRequestAlertException("Nomor Jaminan Tidak Ditemukan","",""));
        Confirmation confirmation = confirmationRepository.findByRegistration(registration)
                .orElseThrow(()->new BadRequestAlertException("File Tersedia Jika Bank Guarantee Sudah Approve","",""));

        byte [] fileByte = null;
        if (!ttdFile && !confirmation.getSoftCopySuratKonfirmasi().isEmpty()){
            String filePath = parentDirectory+jasperPdfDirectory+confirmation.getSoftCopySuratKonfirmasi();
            downloadFileOnly(response, filePath);
        }else if (ttdFile && !confirmation.getSoftCopySuratKonfirmasiWithTtd().isEmpty()){
            String filePath = parentDirectory+jasperPdfDirectory+confirmation.getSoftCopySuratKonfirmasiWithTtd();
            downloadFileOnly(response, filePath);
        }else {
            Map<String, Object> mapFile = exportSuratKonfirmasi(confirmation, ttdFile);
            File file = (File) mapFile.get("file");

            //System.out.println();
            InputStream inputStream = new FileInputStream(file);
            response.setHeader("Content-Disposition", "filename=" + file.getName());
            IOUtils.copy(inputStream, response.getOutputStream());

            response.flushBuffer();
            inputStream.close();

            byte[] bytesArray = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray);
            fileByte = bytesArray;
        }
//        return fileByte;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> exportSuratKonfirmasi(Confirmation confirmation, Boolean ttd){
        String contentType = "application/octet-stream";

        Map<String, Object> resultMap = new HashedMap();
        HashMap title = variableSuratKonfirmasi(confirmation, ttd);
        List<Object> detail = null;

        File file;
        //FILENAME
        String sequenceNumber = null;
        String nomorKonfirmasi = confirmation.getNomorKonfirmasiBank();
        String idSequence = nomorKonfirmasi.substring(nomorKonfirmasi.lastIndexOf("/") + 1).trim();
        if (idSequence.length() < 9){
            String noll = "0";
            String newString="";
            Integer length = 1;
            while (length <= (9 - idSequence.length())){
                newString = newString.concat(noll);
                length++;
            }
            if (ttd==Boolean.TRUE){
                sequenceNumber = newString+idSequence+"v2";
            }else {
                sequenceNumber = newString+idSequence;
            }
        }else {
            if (ttd==Boolean.TRUE){
                sequenceNumber = idSequence+"v2";
            }else {
                sequenceNumber = idSequence;
            }
        }

        String tanggalTerbit = dateUtil.instantToString(dateUtil.longToInstant(confirmation.getRegistration().getTanggalTerbit()), "yyyyMMdd");
        String fileName = bankCode+"-VC"+confirmation.getRegistration().getJenisJaminan().getCodeJaminan()+sequenceNumber+"-"+tanggalTerbit+ ".pdf";

        file = new File(generatePdfFileJasperFromListObject(title, detail,
                fileName, confirmationJasperTemplate));
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new BadRequestAlertException("fail", "" ,"not Supported");
            }
        }
        resultMap.put("file", file);
        resultMap.put("contentType", contentType);

        return resultMap;
    }

    public HashMap variableSuratKonfirmasi(Confirmation confirmation, Boolean ttd){
        HashMap title = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findOneByUsernameAndIsEnabled(authentication.getName(), true);
        title.put("nomor_konfirmasi",confirmation.getNomorKonfirmasiBank());
        String tanggalApprove = null;
        if (confirmation.getRegistration().getApprovedDate() != null){
            tanggalApprove = dateUtil.instantToString(confirmation.getRegistration().getApprovedDate(), DD_MM_YYYY);
        }else {
            tanggalApprove = dateUtil.instantToString(Instant.now(), DD_MM_YYYY);
        }
        title.put("tgl_checker_approve",tanggalApprove);
        title.put("nama_beneficiary",confirmation.getRegistration().getBeneficiary().getNamaBeneficiary());
        title.put("jenis_produk",confirmation.getRegistration().getJenisProduk().getJenisProduk());
        title.put("jenis_jaminan",confirmation.getRegistration().getJenisJaminan().getJenisJaminan());
        title.put("nomor_jaminan",confirmation.getRegistration().getNomorJaminan());
        String tanggalTerbit = dateUtil.instantToString(dateUtil.longToInstant(confirmation.getRegistration().getTanggalTerbit()), DD_MM_YYYY);
        title.put("tanggal_terbit",tanggalTerbit);
        title.put("currency",confirmation.getRegistration().getCurrency().getCurrency());
        String nilaiJaminan = currency.currencyFormatting(confirmation.getRegistration().getNilaiJaminan());
        title.put("nilai_jaminan",nilaiJaminan);
        String tanggalBerlaku = dateUtil.instantToString(dateUtil.longToInstant(confirmation.getRegistration().getTanggalBerlaku()), DD_MM_YYYY);
        String tanggalBerakhir = dateUtil.instantToString(dateUtil.longToInstant(confirmation.getRegistration().getTanggalBerakhir()), DD_MM_YYYY);
        title.put("tanggal_berlaku",tanggalBerlaku + " s/d " + tanggalBerakhir);
        title.put("nomor_amendment",confirmation.getRegistration().getNomorAmentmend());
        title.put("nomor_kontrak",confirmation.getRegistration().getNomorKontrak());
        title.put("uraian_pekerjaan",confirmation.getRegistration().getUraianPekerjaan());
        title.put("applicant",confirmation.getRegistration().getApplicant());
        title.put("bank_penerbit",confirmation.getRegistration().getNamaBankPenerbit());
        title.put("checker", confirmation.getUser().getFirstName());
        if (ttd == Boolean.TRUE){
            title.put("tanda_tangan", confirmation.getUser().getSignature());
        }
        return title;
    }

    public String generatePdfFileJasperFromListObject(HashMap params, List<Object> detail, String fileName, String templateName) {
        String dest = parentDirectory+jasperPdfDirectory + fileName;
        String template = parentDirectory+jasperTemplateDirectory + templateName;
        try {
            JRDataSource ds = new JRBeanCollectionDataSource(detail);
            if (params == null) {
                params = new HashMap();
            }
            params.put("ObjectDataSource", ds);
            JasperPrint jp = JasperFillManager.fillReport(template, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(jp, dest);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return dest;
    }

    @Override
    public void downloadFileOnly(HttpServletResponse response, String filePathName) throws IOException {
        String result = filePathName.substring(0, filePathName.indexOf("/"))+"/";
        log.info(result);
        if (!result.trim().equalsIgnoreCase(parentDirectory)){
            throw new BadRequestAlertException("(No such file or directory)", "", "");
        }
        File myfile = new File(filePathName);
        response.setHeader("Content-Disposition", "filename=" + myfile.getName());
        OutputStream outputStream = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(myfile);
        byte[] fileByte = new byte[(int) myfile.length()];
        int length;
        while((length = fileInputStream.read(fileByte)) > 0){
            outputStream.write(fileByte, 0, length);
        }
        fileInputStream.close();
        outputStream.flush();
//        return fileByte;
    }
}
