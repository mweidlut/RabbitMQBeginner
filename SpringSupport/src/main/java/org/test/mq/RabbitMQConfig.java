package org.test.mq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "rabbitmq")
@Component
public class RabbitMQConfig {

    private Config local;
    private Config dev;

    public Config getLocal() {
        return local;
    }

    public void setLocal(Config local) {
        this.local = local;
    }

    public Config getDev() {
        return dev;
    }

    public void setDev(Config dev) {
        this.dev = dev;
    }

    public static class Config {
        private String addresses;
        private String username;
        private String password;
        private String vhost;

        public String getAddresses() {
            return addresses;
        }

        public void setAddresses(String addresses) {
            this.addresses = addresses;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getVhost() {
            return vhost;
        }

        public void setVhost(String vhost) {
            this.vhost = vhost;
        }
    }
}
