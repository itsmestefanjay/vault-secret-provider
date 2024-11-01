package com.consid.bpm.camunda.secrets.provider;

import io.camunda.connector.api.secret.SecretProvider;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class VaultSecretProvider implements SecretProvider {

    private final VaultSecretService vaultSecretService;
    private final Map<String, Object> secrets = new HashMap<>();

    public VaultSecretProvider(VaultSecretService vaultSecretService) {
        this.vaultSecretService = vaultSecretService;
    }

    @Override
    @Nullable
    public String getSecret(String key) {
        if (secrets.isEmpty()) {
            secrets.putAll(vaultSecretService.getSecretsFromVault());
        }
        if (secrets.containsKey(key)) {
            return secrets.get(key).toString();
        } else {
            log.debug("Cannot find secret for key {}", key);
            return null;
        }
    }

}
