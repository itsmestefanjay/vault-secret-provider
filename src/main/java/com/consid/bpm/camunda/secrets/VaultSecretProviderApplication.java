package com.consid.bpm.camunda.secrets;

import io.camunda.connector.runtime.InboundConnectorsAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        InboundConnectorsAutoConfiguration.class
})
public class VaultSecretProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaultSecretProviderApplication.class, args);
    }

}
