package com.consid.bpm.camunda.secrets.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@OutboundConnector(
        name = "SECRET_TEST",
        inputVariables = {"authentication", "name"},
        type = "com.consid.bpm:secret-test:1"
)
public class ConnectorFunction implements OutboundConnectorFunction {

    @Override
    public Object execute(OutboundConnectorContext context) throws Exception {
        ConnectorRequest request = context.bindVariables(ConnectorRequest.class);
        String apiKey = request.getAuthentication().getSecret();
        log.info("Authenticating with API key {}", apiKey);
        log.info("Hello {}", request.getName());
        return new ConnectorResponse(request.getName());
    }
}
