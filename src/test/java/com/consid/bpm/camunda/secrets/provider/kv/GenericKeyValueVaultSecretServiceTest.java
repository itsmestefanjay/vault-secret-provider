package com.consid.bpm.camunda.secrets.provider.kv;

import com.consid.bpm.camunda.secrets.provider.VaultSecretService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GenericKeyValueVaultSecretServiceTest {

    @Mock
    private VaultTemplate vaultTemplate;
    private VaultSecretService secretService;

    @BeforeEach
    public void setUp() {
        secretService = new GenericKeyValueVaultSecretService(vaultTemplate, "path", "backend");
    }

    @Test
    public void test_secrets_are_loaded_as_expected() {
        // given
        Map<String, Object> expectedSecrets = Map.of("apiKey", "0000-0000-0000-0000");
        VaultResponse response = new VaultResponse();
        response.setData(expectedSecrets);
        Mockito.when(vaultTemplate.read(anyString())).thenReturn(response);

        // when
        Map<String, Object> secrets = secretService.getSecretsFromVault();

        // then
        assertThat(secrets).isEqualTo(expectedSecrets);
        verify(vaultTemplate).read(eq("backend/path"));
    }

    @Test
    public void test_existing_secret_by_key_is_returned_as_expected() {
        // given
        Map<String, Object> expectedSecrets = Map.of("apiKey", "0000-0000-0000-0000");
        VaultResponse response = new VaultResponse();
        response.setData(expectedSecrets);
        Mockito.when(vaultTemplate.read(anyString())).thenReturn(response);

        // when
        Optional<Object> secret = secretService.getSecretByKey("apiKey");

        // then
        assertThat(secret.isPresent()).isTrue();
        assertThat(secret.get()).isEqualTo("0000-0000-0000-0000");
    }

    @Test
    public void test_non_existing_secret_by_key_is_returned_empty_as_expected() {
        // given
        Map<String, Object> expectedSecrets = Map.of("apiKey", "0000-0000-0000-0000");
        VaultResponse response = new VaultResponse();
        response.setData(expectedSecrets);
        Mockito.when(vaultTemplate.read(anyString())).thenReturn(response);

        // when
        Optional<Object> secret = secretService.getSecretByKey("unknown");

        // then
        assertThat(secret.isPresent()).isFalse();
    }

    @Test
    public void test_empty_secrets_return_empty_map_as_expected() {
        // given
        VaultResponse response = new VaultResponse();
        Mockito.when(vaultTemplate.read(anyString())).thenReturn(response);

        // when
        Map<String, Object> secrets = secretService.getSecretsFromVault();

        // then
        assertThat(secrets).isEmpty();
    }

}