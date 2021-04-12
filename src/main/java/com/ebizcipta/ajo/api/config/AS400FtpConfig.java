package com.ebizcipta.ajo.api.config;

import com.ebizcipta.ajo.api.config.properties.AS400FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
@EnableConfigurationProperties(AS400FtpProperties.class)
public class AS400FtpConfig {
    private FTPClient ftp;
    private final AS400FtpProperties as400FtpProperties;

    public AS400FtpConfig(AS400FtpProperties as400FtpProperties) {
        this.as400FtpProperties = as400FtpProperties;
    }

    public Boolean openConnectionFtp() throws IOException{
        //System.out.println("STARTING CONNECTION");
        log.info("STARTING CONNECTION");
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        int port = Integer.parseInt(as400FtpProperties.getPort());
        ftp.connect(as400FtpProperties.getServer(), port);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }
        Boolean result = ftp.login(as400FtpProperties.getUsername(), as400FtpProperties.getPassword());
        //System.out.println("END CONNECTION");
        log.info("END CONNECTION");
        return result;
    }

    public void closeConnectionFtp() throws IOException{
        ftp.disconnect();
    }

    public Boolean downloadFile(String sourcePath, String destinationPath) throws IOException{
        //System.out.println("START DOWNLOAD FILE");
        log.info("START DOWNLOAD FILE");
        FileOutputStream out = new FileOutputStream(destinationPath);
        Boolean result = ftp.retrieveFile(sourcePath, out);
        //System.out.println("END DOWNLOAD FILE");
        log.info("END DOWNLOAD FILE");
        return  result;
    }

}
