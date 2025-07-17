/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.entity;

/**
 *
 * @author quang
 */
public class SystemConfig {
    private String configKey;
    private String configValue;

<<<<<<< HEAD
=======
    public SystemConfig() {}

    public SystemConfig(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

>>>>>>> eed6712 (đây là code của phần Caidatchung.java HotroJDialog.java GioHangJDialog.java)
    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

<<<<<<< HEAD
    public SystemConfig(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    public SystemConfig() {
=======
    @Override
    public String toString() {
        return "SystemConfig{" +
                "configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                '}';
>>>>>>> eed6712 (đây là code của phần Caidatchung.java HotroJDialog.java GioHangJDialog.java)
    }
}
