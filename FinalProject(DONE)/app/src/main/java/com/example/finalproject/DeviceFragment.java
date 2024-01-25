package com.example.finalproject;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.finalproject.DeviceRoom.Device;
import com.example.finalproject.DeviceRoom.DeviceAdapter;
import com.example.finalproject.Room.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class DeviceFragment extends Fragment {
    private Toolbar tb_Device;
    private RecyclerView rcv_Device;
    private MainActivity mMainActivity;
    private DeviceAdapter deviceAdapter;
    private FloatingActionButton fl_addDev;

    private int position;
    private String roomName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);

        tb_Device = view.findViewById(R.id.tb_device);
        rcv_Device = view.findViewById(R.id.rcv_Device);
        fl_addDev = view.findViewById(R.id.fl_addDev);

        fl_addDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDeviceDialog();
            }
        });
        mMainActivity = (MainActivity) getActivity();

        setBackButtonOnToolbar();

        // Lấy thông tin vị trí và tên phòng từ bundle nếu tồn tại.
        Bundle argument = getArguments();
        if (argument != null) {
            roomName = argument.getString("roomName", "");
            position = argument.getInt("position", 0);
        }

        deviceAdapter = new DeviceAdapter(Room.dataRoom.get(position).getDeviceList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rcv_Device.setLayoutManager(linearLayoutManager);
        rcv_Device.setAdapter(deviceAdapter);

        return view;
    }

    public void setBackButtonOnToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(tb_Device);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Bundle agr = getArguments();
            String title = agr.getString("roomName");
            activity.getSupportActionBar().setTitle(title);
        }

        tb_Device.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack(); // Kết thúc fragment hiện tại và quay lại fragment trước đó
            }
        });
    }

    private void showAddDeviceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.add_edit_device, null);

        EditText edtName = dialogView.findViewById(R.id.edtNameDevice);
        EditText edtImageDevice = dialogView.findViewById(R.id.edtImageDevice);
        Button btnAdd = dialogView.findViewById(R.id.buttonActionDevice);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceName = edtName.getText().toString();
                String imageName = edtImageDevice.getText().toString();
                int type;
                if(imageName.equals("humidity")){
                    type = 1;
                } else if (imageName.equals("temperature")) {
                    type = 3;
                }else {
                    type = 2;
                }
                if(!deviceName.isEmpty() && !imageName.isEmpty()){
                    int imageResourceId = getResourceIdByNameDevice(imageName);
                    if(imageResourceId != 0){
                        Room.dataRoom.get(position).addDevice(new Device(imageResourceId, deviceName,type));
                        deviceAdapter.notifyDataSetChanged();
                        mMainActivity.saveListRoom(Room.dataRoom,"listRoom");
                        alertDialog.dismiss();
                    }else{
                        // Handle case where the image resource is not found
                        Toast.makeText(requireContext(), "Image not found", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    // Handle case where room name or image name is empty
                    Toast.makeText(requireContext(), "Please enter both device name and image name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }


    // Function to get resource ID by name
    public int getResourceIdByNameDevice(String name) {
        return getResources().getIdentifier(name, "drawable", requireContext().getPackageName());
    }
}