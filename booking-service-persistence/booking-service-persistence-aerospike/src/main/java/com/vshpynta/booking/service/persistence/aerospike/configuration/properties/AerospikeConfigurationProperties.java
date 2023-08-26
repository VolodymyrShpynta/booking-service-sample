package com.vshpynta.booking.service.persistence.aerospike.configuration.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AerospikeConfigurationProperties {

    @NotNull
    private String hosts;

    private int defaultPort = 3000;

    @NotNull
    private String namespace;

    private String user;

    private String password;

    private String clusterName;

    private int eventLoopsSize = getDefaultEventLoopsSize();
    private int maxConnectionsPerNode = 300;
    private int connectionPoolsPerNode = 1;
    private int maxSocketIdleSec = 55;
    private int tendIntervalMs = 1_000;

    @Min(0)
    private int connectTimeoutMs = 5_000;
    @Min(0)
    private int operationTimeoutMs = 200;

    private boolean sendKey = false;

    private boolean durableDelete = true;

    private TlsProperties tls = new TlsProperties();

    private static int getDefaultEventLoopsSize() {
        try {
            return Runtime.getRuntime().availableProcessors();
        } catch (Exception e) {
            return 1;
        }
    }

    @Data
    public static class TlsProperties {
        boolean enabled = false;
    }
}
