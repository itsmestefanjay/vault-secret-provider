package com.consid.bpm.camunda.secrets.common;

import com.consid.bpm.camunda.secrets.provider.VaultSecretProvider;
import com.consid.bpm.camunda.secrets.provider.VaultSecretService;
import com.consid.bpm.camunda.secrets.provider.kv.GenericKeyValueVaultSecretService;
import com.consid.bpm.camunda.secrets.provider.kv.VersionedKeyValueVaultSecretService;
import com.consid.bpm.camunda.secrets.worker.CustomJobWorker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;

import java.net.URI;

@Configuration
public class ProviderAppConfig {

    @Bean
    public CustomJobWorker jobWorker(VaultSecretService vaultSecretService) {
        return new CustomJobWorker(vaultSecretService);
    }

    @Bean
    public VaultSecretProvider secretProvider(VaultSecretService vaultSecretService) {
        return new VaultSecretProvider(vaultSecretService);
    }

    @Bean
    public VaultTemplate vaultTemplate(VaultConfigurationProperties properties) {
        URI endpoint = URI.create(String.format("%s:%d", properties.getHost(), properties.getPort()));
        return new VaultTemplate(VaultEndpoint.from(endpoint), new TokenAuthentication(properties.getToken()));
    }

    @Bean
    @ConditionalOnProperty(name = "vault.backend", havingValue = "kv")
    VaultSecretService genericKeyValueVaultService(
            VaultTemplate vaultTemplate,
            VaultConfigurationProperties properties
    ) {
        return new GenericKeyValueVaultSecretService(vaultTemplate, properties.getPath(), properties.getBackend());
    }

    @Bean
    @ConditionalOnProperty(name = "vault.backend", havingValue = "secret")
    VaultSecretService versionedKeyValueVaultService(
            VaultTemplate vaultTemplate,
            VaultConfigurationProperties properties
    ) {
        return new VersionedKeyValueVaultSecretService(vaultTemplate, properties.getPath(), properties.getBackend(), properties.getSecretVersion());
    }

}
