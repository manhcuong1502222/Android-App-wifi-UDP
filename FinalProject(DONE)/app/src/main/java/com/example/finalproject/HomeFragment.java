package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Room.Room;
import com.example.finalproject.Room.RoomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rcvRooms;
    private RoomAdapter mRoomAdapter;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rcvRooms = view.findViewById(R.id.rcv_room);
        mainActivity = (MainActivity) getActivity();

        // Lấy danh sách phòng từ SharedPreferences và khởi tạo Adapter cho RecyclerView.
        Room.dataRoom = mainActivity.getListRoom("listRoom");
        if (Room.dataRoom == null || Room.dataRoom.isEmpty()) {

            Room.dataRoom.add(new Room( R.drawable.livingroom,"Living Room",Room.deviceInRoom()));
            Room.dataRoom.add(new Room( R.drawable.kitchen,"Kitchen",Room.deviceInRoom()));
            Room.dataRoom.add(new Room( R.drawable.bed,"Bedroom",Room.deviceInRoom()));
            Room.dataRoom.add(new Room(R.drawable.bathtub, "Bathroom",Room.deviceInRoom()));
            Room.dataRoom.add(new Room(R.drawable.light, "Studio",Room.deviceInRoom()));
            Room.dataRoom.add(new Room(R.drawable.laundry, "Washing room",Room.deviceInRoom()));

            mainActivity.saveListRoom(Room.dataRoom,"listRoom");


        }
        mRoomAdapter = new RoomAdapter(Room.dataRoom);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcvRooms.setLayoutManager(gridLayoutManager);

        rcvRooms.setAdapter(mRoomAdapter);

        FloatingActionButton fabAdd = view.findViewById(R.id.floating);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddRoomDialog();
            }
        });

        return view;
    }

    private void showAddRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.add_edit_room, null);

        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtImageName = dialogView.findViewById(R.id.edtImage);
        Button btnAdd = dialogView.findViewById(R.id.buttonAction);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomName = edtName.getText().toString();
                String imageName = edtImageName.getText().toString();
                if(!roomName.isEmpty() && !imageName.isEmpty()){
                    int imageResourceId = getResourceIdByName(imageName);
                    if(imageResourceId != 0){
                        Room.dataRoom.add(new Room(imageResourceId, roomName,Room.deviceInRoom()));
                        mRoomAdapter.notifyDataSetChanged();
                        mainActivity.saveListRoom(Room.dataRoom,"listRoom");
                        alertDialog.dismiss();
                    }else{
                        // Handle case where the image resource is not found
                        Toast.makeText(requireContext(), "Image not found", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    // Handle case where room name or image name is empty
                    Toast.makeText(requireContext(), "Please enter both room name and image name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }

    // Function to get resource ID by name
    public int getResourceIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", requireContext().getPackageName());
    }

}
