package com.ebizcipta.ajo.api.util;

import com.ebizcipta.ajo.api.domain.MasterConfiguration;
import com.ebizcipta.ajo.api.repositories.MasterConfigurationRepository;
import com.ebizcipta.ajo.api.service.dto.EmailRejectDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Component
public class EmailUtil {

    @Autowired
    private WebServicesUtil webServicesUtil;

    @Autowired
    private MasterConfigurationRepository masterConfigurationRepository;
    @Autowired
    private AuditTrailUtil auditTrailUtil;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Async
    public Boolean sendEmail( EmailRejectDTO emailRejectDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MasterConfiguration getProperties = getProperties();
        String text = textRejectBG(emailRejectDTO);

        /*
        Properties properties = new Properties();
        properties.put("mail.smtp.host", getProperties.getSmtpHostname());
        properties.put("mail.smtp.port", getProperties.getSmtpPort());
        properties.put("mail.smtp.username", getProperties.getUsername());
        properties.put("mail.smtp.password", getProperties.getSmtpPassword());
        properties.put("mail.smtp.starttls.enable" , true);
        properties.put("mail.smtp.starttls.require" , true);
        properties.put("mail.smtp.auth", true);
        Session mailSession = Session.getInstance(properties, new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getProperties.getUsername(), getProperties.getSmtpPassword());
            }

        });

        String mailResponse = null;
        try {
            MimeMessage msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(emailRejectDTO.getSetFrom()));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailRejectDTO.recipient) );
            msg.setSubject(emailRejectDTO.getSubject());
            msg.setSentDate(new Date());
            msg.setText(text, "utf-8", "html");
            Transport.send(msg);
        } catch (MessagingException mex) {
            log.info("gagal mengirim email, exception: " + mex);
            mailResponse = mex.getCause().toString();
            return Boolean.FALSE;
        }*/

        try {
            webServicesUtil.handleSendEmailRejection(getProperties, emailRejectDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO AUDIT TRAIL
        auditTrailUtil.saveAudit(
                Constants.Event.MAIL,
                Constants.Module.MAIL,
                null,
                new JSONObject(emailRejectDTO).toString(),
                Constants.Remark.SEND_EMAIL,
                authentication.getName()
        );
        return Boolean.TRUE;

    }

    private MasterConfiguration getProperties(){
        Optional<MasterConfiguration> masterConfiguration = masterConfigurationRepository.findTop1ByStatusOrderByCreationDateDesc("ACTIVE");
        return masterConfiguration.get();
    }

    private String textRejectBG(EmailRejectDTO emailRejectDTO){
        Context context = new Context();
        context.setVariable("namaPemohon" , emailRejectDTO.getNamaPemohon());
        context.setVariable("noKontrak" , emailRejectDTO.getNoKontrak());
        context.setVariable("noJaminan" , emailRejectDTO.getNoJaminan());
        context.setVariable("tanggalTerbit" , emailRejectDTO.getTanggalTerbit());
        context.setVariable("tanggalBerlaku" , emailRejectDTO.getTanggalBerlaku());
        context.setVariable("tanggalJatuhTempo" , emailRejectDTO.getTanggalJatuhTempo());
        context.setVariable("currency", emailRejectDTO.getCurrency());
        context.setVariable("nilaiJaminan" , emailRejectDTO.getNilaiJaminan());
        context.setVariable("catatan" , emailRejectDTO.getCatatan());
        String text = templateEngine.process("index" ,context);
        return text;
    }
}