package com.example.betterreadsdataloader.connection;

import org.springframework.boot.context.properties.ConfigurationProperties;


import java.io.File;

@ConfigurationProperties(prefix = "datastax.astra") // Since we are creating a class for this configuration, we need to configure it in the main class
public class DataStaxAstraProperties {
    private File secureConnectBundle;

    public File getSecureConnectBundle() {
        return secureConnectBundle;
    }

    public void setSecureConnectBundle(File secureConnectBundle) {
        this.secureConnectBundle = secureConnectBundle;
    }
}
