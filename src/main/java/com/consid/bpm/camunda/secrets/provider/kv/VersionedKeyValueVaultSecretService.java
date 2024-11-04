package com.consid.bpm.camunda.secrets.provider.kv;

import com.consid.bpm.camunda.secrets.provider.VaultSecretService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultVersionedKeyValueTemplate;
import org.springframework.vault.support.Versioned;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class VersionedKeyValueVaultSecretService implements VaultSecretService {

    private final VaultVersionedKeyValueTemplate vaultTemplate;
    @Getter
    private final String path;
    private final int version;

    public VersionedKeyValueVaultSecretService(VaultTemplate vaultVTemplate, String path, String backend, int version) {
        this.path = path;
        this.version = version;
        this.vaultTemplate = new VaultVersionedKeyValueTemplate(vaultVTemplate, backend);
        log.info("Initializing versioned key value backend '{}' for vault", backend);
    }


    @Override
    public Map<String, Object> getSecretsFromVault() {
        return readSecretsFromVault();
    }

    @Override
    public Optional<Object> getSecretByKey(String key) {
        Map<String, Object> secrets = readSecretsFromVault();
        return Optional.ofNullable(secrets.get(key));
    }

    private Map<String, Object> readSecretsFromVault() {
        try {
            // secrets are read from v2 api from backend/data/path
            Versioned<Map<String, Object>> secrets = vaultTemplate.get(path, Versioned.Version.from(version));
            if (secrets != null) {
                return secrets.getRequiredData();
            }
        } catch (IllegalStateException e) {
            log.debug("Cannot find secrets in path {}", path);
        }
        return Map.of();
    }
}
