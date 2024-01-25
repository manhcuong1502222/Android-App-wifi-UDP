package com.example.finalproject.Room;

import com.example.finalproject.DeviceRoom.Device;
import com.example.finalproject.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private int roomImg;
    private String roomName;
    private List<Device> deviceList;

    // Danh sách các phòng, được chia sẻ cho tất cả các phòng trong ứng dụng.
    public static List<Room> dataRoom ;

    public Room(int roomImg, String roomName, List<Device> deviceList) {
        this.roomImg = roomImg;
        this.roomName = roomName;
        this.deviceList = deviceList;

    }

    public int getRoomImg() {
        return roomImg;
    }

    public void setRoomImg(int roomImg) {
        this.roomImg = roomImg;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String name) {
        this.roomName = name;
    }

    public int getSizeDevice() {
        return deviceList.size();
    }

    public void addDevice(Device device) {
        deviceList.add(device);
    }

    public void removeDevice(Device device) {
        deviceList.remove(device);
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    // Phương thức tạo danh sách các thiết bị mặc định cho phòng.
    public static List<Device> deviceInRoom() {
        List<Device> dataDevice = new ArrayList<>();
        dataDevice.add(0, new Device(R.drawable.lamp, "Light",2));
        dataDevice.add(1, new Device(R.drawable.humidity, "Humidity",1));
        dataDevice.add(2, new Device(R.drawable.temperature, "Temperature",3));
        return dataDevice;
    }
}
