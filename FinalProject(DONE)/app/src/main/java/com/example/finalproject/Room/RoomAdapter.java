package com.example.finalproject.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.DeviceFragment;
import com.example.finalproject.MainActivity;
import com.example.finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.UserViewHolder>  {
    private List<Room> mListRoom;

    public RoomAdapter(List<Room> mListRoom) {
        this.mListRoom = mListRoom;
    }

    private final int UDP_PORT = 6828; // Cổng UDP mà bạn sử dụng
    private final String SERVER_IP = "192.168.43.38"; // Địa chỉ IP của máy chủ UDP

    int type = 4;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent,false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Room room = mListRoom.get(position);
        if(room == null){
            return;
        }
        holder.imgRoom.setImageResource(room.getRoomImg());
        holder.tvRoom.setText(room.getRoomName());

        //Xử lý sự kiện khi mục phòng được nhấp vào
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo một fragment mới để hiển thị danh sách thiết bị trong phòng và truyền thông tin vị trí và tên phòng.
                DeviceFragment deviceFragment = new DeviceFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("roomName", room.getRoomName());
                deviceFragment.setArguments(bundle);

                // Đổi fragment sử dụng FragmentManager.
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_main, deviceFragment).addToBackStack("deviceFragment").commit();

                // Tạo JSON object và thêm các thông tin
                JSONObject dataToSend = new JSONObject();
                try {
                    dataToSend.put("type", type);
                    dataToSend.put("roomName", room.getRoomName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Gửi dữ liệu qua UDP
                sendDataOverUDP(dataToSend.toString());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                if(currentPosition != RecyclerView.NO_POSITION){
                    Toast.makeText(view.getContext(),"Bạn đã xóa phòng: " + mListRoom.get(currentPosition).getRoomName().toString(),Toast.LENGTH_SHORT).show();
                    mListRoom.remove(currentPosition);
                    notifyDataSetChanged();
                    holder.mainActivity.saveListRoom(Room.dataRoom,"listRoom");

                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        if(mListRoom != null){
            return mListRoom.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgRoom;
        private TextView tvRoom;
        private MainActivity mainActivity;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imgRoom = itemView.findViewById(R.id.img_room);
            tvRoom = itemView.findViewById(R.id.txt_room);
            mainActivity = (MainActivity) itemView.getContext();
        }
    }

    private void sendDataOverUDP(String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket udpSocket = new DatagramSocket();
                    InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                    byte[] buf = data.getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, UDP_PORT);
                    udpSocket.send(packet);

                    udpSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
