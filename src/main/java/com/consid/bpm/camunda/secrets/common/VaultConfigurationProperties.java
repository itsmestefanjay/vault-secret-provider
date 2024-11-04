package com.consid.bpm.camunda.secrets.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "vault")
public class VaultConfigurationProperties {

    private String host;
    private int port;
    private String token;
    private String path;
    private String backend;
    private int secretVersion;

}
