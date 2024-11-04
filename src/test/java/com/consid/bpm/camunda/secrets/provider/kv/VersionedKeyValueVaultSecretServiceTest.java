package com.consid.bpm.camunda.secrets.provider.kv;

import com.consid.bpm.camunda.secrets.provider.VaultSecretService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.vault.core.VaultVersionedKeyValueTemplate;
import org.springframework.vault.support.Versioned;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VersionedKeyValueVaultSecretServiceTest {

    @Mock
    private VaultVersionedKeyValueTemplate vaultTemplate;
    private VaultSecretService secretService;

    @BeforeEach
    public void setUp() {
        secretService = new VersionedKeyValueVaultSecretService(vaultTemplate, "path", 1);
    }

    @Test
    public void test_secrets_are_loaded_as_expected() {
        // given
        Map<String, Object> expectedSecrets = Map.of("apiKey", "0000-0000-0000-0000");
        Versioned.Version expectedVersion = Versioned.Version.from(1);
        Versioned<Map<String, Object>> versionedSecrets = Versioned.create(expectedSecrets, expectedVersion);
        Mockito.when(vaultTemplate.get(anyString(), any(Versioned.Version.class))).thenReturn(versionedSecrets);

        // when
        Map<String, Object> secrets = secretService.getSecretsFromVault();

        // then
        assertThat(secrets).isEqualTo(expectedSecrets);
        verify(vaultTemplate).get(eq("path"), eq(expectedVersion));
    }

    @Test
    public void test_existing_secret_by_key_is_returned_as_expected() {
        // given
        Map<String, Object> expectedSecrets = Map.of("apiKey", "0000-0000-0000-0000");
        Versioned.Version expectedVersion = Versioned.Version.from(1);
        Versioned<Map<String, Object>> versionedSecrets = Versioned.create(expectedSecrets, expectedVersion);
        Mockito.when(vaultTemplate.get(anyString(), any(Versioned.Version.class))).thenReturn(versionedSecrets);

        // when
        Optional<Object> secret = secretService.getSecretByKey("apiKey");

        // then
        assertThat(secret.isPresent()).isTrue();
        assertThat(secret.get()).isEqualTo("0000-0000-0000-0000");
    }

    @Test
    public void test_non_existing_secret_by_key_is_returned_empty_as_expected() {
        // given
        Versioned<Map<String, Object>> versionedSecrets = Versioned.create(Map.of());
        Mockito.when(vaultTemplate.get(anyString(), any(Versioned.Version.class))).thenReturn(versionedSecrets);

        // when
        Optional<Object> secret = secretService.getSecretByKey("unknown");

        // then
        assertThat(secret.isPresent()).isFalse();
    }

    @Test
    public void test_empty_secrets_return_empty_map_as_expected() {
        // given
        Versioned.Version expectedVersion = Versioned.Version.from(1);
        Versioned<Map<String, Object>> versionedSecrets = Versioned.create(null, expectedVersion);
        Mockito.when(vaultTemplate.get(anyString(), any(Versioned.Version.class))).thenReturn(versionedSecrets);

        // when
        Map<String, Object> secrets = secretService.getSecretsFromVault();

        // then
        assertThat(secrets).isEmpty();
    }

}