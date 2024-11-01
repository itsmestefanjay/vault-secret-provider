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

    private String path;
    private String host;
    private int port;
    private int secretVersion;
    private String token;
    private String backend;

}
