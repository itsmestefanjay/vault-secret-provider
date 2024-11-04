package com.consid.bpm.camunda.secrets.provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class VaultSecretProviderTest {

    @Mock
    private VaultSecretService secretService;
    private VaultSecretProvider provider;

    @BeforeEach
    public void setUp() {
        provider = new VaultSecretProvider(secretService);
    }

    @Test
    public void test_secrets_retrieved_as_expected() {
        // given
        Mockito.when(secretService.getSecretsFromVault()).thenReturn(Map.of("apiKey", "1234-5678-9012-3456"));

        // when
        String apiKey = provider.getSecret("apiKey");

        // then
        assertThat(apiKey).isEqualTo("1234-5678-9012-3456");
    }

    @Test
    public void test_secrets_cached_as_expected() {
        // given
        Mockito.when(secretService.getSecretsFromVault()).thenReturn(Map.of("apiKey", "1234-5678-9012-3456"));

        // when
        String apiKey = provider.getSecret("apiKey");
        String ignored = provider.getSecret("apiKey");

        // then
        assertThat(apiKey).isEqualTo("1234-5678-9012-3456");
        verify(secretService, Mockito.times(1)).getSecretsFromVault();
        verifyNoMoreInteractions(secretService);
    }

    @Test
    public void test_unknown_secrets_return_null_as_expected() {
        // given
        Mockito.when(secretService.getSecretsFromVault()).thenReturn(Collections.emptyMap());

        // when
        String apiKey = provider.getSecret("unknown");

        // then
        assertThat(apiKey).isNull();
    }

}