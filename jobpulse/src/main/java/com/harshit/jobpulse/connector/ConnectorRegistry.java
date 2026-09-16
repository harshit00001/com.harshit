package com.harshit.jobpulse.connector;

import com.harshit.jobpulse.config.ConnectorType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ConnectorRegistry {

    private final Map<ConnectorType, JobConnector> connectors = new EnumMap<>(ConnectorType.class);

    public ConnectorRegistry(List<JobConnector> discovered) {
        discovered.forEach(connector -> connectors.put(connector.type(), connector));
    }

    public JobConnector require(ConnectorType type) {
        JobConnector connector = connectors.get(type);
        if (connector == null) {
            throw new ConnectorException("No connector registered for type " + type);
        }
        return connector;
    }
}
