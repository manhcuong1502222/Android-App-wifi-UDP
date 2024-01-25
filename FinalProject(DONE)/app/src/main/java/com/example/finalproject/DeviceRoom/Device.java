package com.example.finalproject.DeviceRoom;

public class Device {
    private int deviceImg;
    private String deviceName;
    private int deviceType;

    public Device(int deviceImg, String deviceName, int deviceType) {
        this.deviceImg = deviceImg;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

    public int getDeviceImg() {
        return deviceImg;
    }

    public void setDeviceImg(int deviceImg) {
        this.deviceImg = deviceImg;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
