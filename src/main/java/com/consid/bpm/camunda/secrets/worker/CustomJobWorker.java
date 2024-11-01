package com.consid.bpm.camunda.secrets.worker;

import com.consid.bpm.camunda.secrets.provider.VaultSecretService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomJobWorker {

    private final VaultSecretService vaultSecretService;
    private final Map<String, Object> secrets = new HashMap<>();

    public CustomJobWorker(VaultSecretService vaultSecretService) {
        this.vaultSecretService = vaultSecretService;
    }

    @JobWorker(name = "do-stuff")
    public void doStuff(ActivatedJob job, @Variable(name = "name") String name) {
        loadSecrets();
        var apikey = secrets.get("apiKey").toString();
        authenticate(apikey);
        log.info("Hello {}", name);
    }

    private void authenticate(String apikey) {
        log.info("Authenticating with API key {}", apikey);
    }

    private void loadSecrets() {
        if (secrets.isEmpty()) {
            secrets.putAll(vaultSecretService.getSecretsFromVault());
        }
    }

}
