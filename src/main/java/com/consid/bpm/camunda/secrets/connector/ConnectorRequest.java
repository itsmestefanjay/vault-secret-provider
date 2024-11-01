package com.consid.bpm.camunda.secrets.connector;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectorRequest {
    private Authentication authentication;
    private String name;
}
