package com.consid.bpm.camunda.secrets.provider.kv;

import com.consid.bpm.camunda.secrets.provider.VaultSecretService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.vault.core.VaultTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GenericKeyValueVaultSecretService implements VaultSecretService {

    private final VaultTemplate vaultTemplate;
    @Getter
    private final String path;
    private final String backend;

    public GenericKeyValueVaultSecretService(VaultTemplate vaultTemplate, String path, String backend) {
        this.vaultTemplate = vaultTemplate;
        this.path = path;
        this.backend = backend;
    }

    @Override
    public Map<String, Object> getSecretsFromVault() {
        return readSecretsFromPath();
    }

    @Override
    public Optional<Object> getSecretByKey(String key) {
        Map<String, Object> secrets = readSecretsFromPath();
        return Optional.ofNullable(secrets.get(key));
    }

    private Map<String, Object> readSecretsFromPath() {
        try {
            // secrets are read from v1 api from backend/path (no data segment)
            var response = vaultTemplate.read(backend + "/" + path);
            if (response != null) {
                return response.getRequiredData();
            }
        } catch (IllegalStateException e) {
            log.debug("Cannot find secrets in path {}", path);
        }
        return Collections.emptyMap();
    }
}
