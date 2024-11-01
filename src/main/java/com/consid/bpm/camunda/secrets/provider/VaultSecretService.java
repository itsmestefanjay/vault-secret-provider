package com.consid.bpm.camunda.secrets.provider;

import java.util.Map;
import java.util.Optional;

public interface VaultSecretService {

    /**
     * gets a map of existing secrets for the configured Vault backend and path
     * @return the secrets or an empty map
     */
    Map<String, Object> getSecretsFromVault();

    /**
     * gets a secret by its key for the configured Vault backend and path
     * @return the optional secret
     */
    Optional<Object> getSecretByKey(String key);
}
