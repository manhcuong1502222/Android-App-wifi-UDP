package com.example.finalproject.DeviceRoom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.DeviceFragment;
import com.example.finalproject.Fragment.HumidityFragment;
import com.example.finalproject.Fragment.LightFragment;
import com.example.finalproject.Fragment.TemperatureFragment;
import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.Room.Room;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<Device> deviceList; // Danh sách các thiết bị cần hiển thị.

    // Constructor cho lớp DeviceAdapter.
    public DeviceAdapter(List<Device> deviceList) {
        this.deviceList = deviceList; // Khởi tạo danh sách thiết bị.
    }

    @NonNull
    @Override
    public DeviceAdapter.DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_device,parent,false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.DeviceViewHolder holder, int position) {
        Device device = deviceList.get(position);
        if (device == null) {
            return;
        }
        holder.img_device.setImageResource(device.getDeviceImg());
        holder.deviceName.setText(device.getDeviceName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                if(currentPosition != RecyclerView.NO_POSITION){
                    Toast.makeText(view.getContext(),"Bạn đã xóa phòng: " + deviceList.get(currentPosition).getDeviceName().toString(),Toast.LENGTH_SHORT).show();
                    deviceList.remove(currentPosition);
                    notifyDataSetChanged();
                    holder.mainActivity.saveListRoom(Room.dataRoom,"listRoom");

                }
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type = device.getDeviceType();
                if(type == 1){
                    HumidityFragment humidityFragment = new HumidityFragment();

                    // Gửi dữ liệu qua Fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("deviceName", device.getDeviceName());
                    humidityFragment.setArguments(bundle);

                    // Đổi fragment sử dụng FragmentManager.
                    FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, humidityFragment).commit();
                }
                else if(type == 2){
                    LightFragment lightFragment = new LightFragment();

                    // Gửi dữ liệu qua Fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("deviceName", device.getDeviceName());
                    lightFragment.setArguments(bundle);

                    // Đổi fragment sử dụng FragmentManager.
                    FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, lightFragment).commit();
                }
                else if (type == 3) {
                    TemperatureFragment temperatureFragment = new TemperatureFragment();

                    // Gửi dữ liệu qua Fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("deviceName", device.getDeviceName());
                    temperatureFragment.setArguments(bundle);

                    // Đổi fragment sử dụng FragmentManager.
                    FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, temperatureFragment).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (deviceList != null) {
            return deviceList.size();
        }
        return 0;
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_device;
        private TextView deviceName;
        private MainActivity mainActivity;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            img_device = itemView.findViewById(R.id.img_device);
            deviceName = itemView.findViewById(R.id.deviceName);
            mainActivity = (MainActivity) itemView.getContext();
        }
    }
}
