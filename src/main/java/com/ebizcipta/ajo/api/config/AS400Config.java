package com.ebizcipta.ajo.api.config;

import com.ebizcipta.ajo.api.config.properties.AS400ConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@EnableConfigurationProperties(AS400ConfigProperties.class)
public class AS400Config {
    private final AS400ConfigProperties as400ConfigProperties;

    public AS400Config(AS400ConfigProperties as400ConfigProperties) {
        this.as400ConfigProperties = as400ConfigProperties;
    }

    public List<String> connect() {
        List<String> result = new ArrayList<>();

        try {
            log.info("Trying to connect...");
            Class.forName(as400ConfigProperties.getDriver());
            Connection conn = DriverManager.getConnection(as400ConfigProperties.getUrl(),
                    as400ConfigProperties.getUsername(), as400ConfigProperties.getPassword());
            log.info("Connected");

            log.info("Get data");
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + as400ConfigProperties.getFile());
            log.info(resultSet.toString());
            ResultSetMetaData md = resultSet.getMetaData();
            Integer column = md.getColumnCount();
            log.info("COLUMN : "+column);
            while (resultSet.next()) {
                List<String> rowString = new ArrayList<>(column);
                for (int i = 0; i<=column;i++){
                    rowString.add(resultSet.getString(i));
                }
                result.add(rowString.toString());
                log.info(result.toString());
            }
            conn.close();
            log.info("Close connection");
        } catch (ClassNotFoundException | SQLException e) {
            log.error("AS400 Config Error: ", e);
        }

        return result;
    }
}
