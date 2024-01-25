package com.example.finalproject.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class LightFragment extends Fragment {

    SeekBar sb_light;
    TextView textView_light, textView_status;

    Button btn_onoff;
    boolean isSeekBarEnabled = true;
    String deviceName;
    String value;
    private final int type = 2;

    private final int UDP_PORT = 6828; // Cổng UDP mà bạn sử dụng
    private final String SERVER_IP = "192.168.43.38"; // Địa chỉ IP của máy chủ UDP



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_light, container, false);

        sb_light = view.findViewById(R.id.seekBar_light);
        textView_light = view.findViewById(R.id.tv_light);
        textView_status = view.findViewById(R.id.tv_status_light);
        btn_onoff = view.findViewById(R.id.btnoff_light);

        // Lấy thông tin vị trí và tên phòng từ bundle nếu tồn tại.
        Bundle argument = getArguments();
        if (argument != null) {
            deviceName = argument.getString("deviceName", "");
        }

        sb_light.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Cập nhật TextView với giá trị phần trăm
                value = progress + "%";
                textView_light.setText(value);

                // Tạo JSON object và thêm các thông tin
                JSONObject dataToSend = new JSONObject();
                try {
                    dataToSend.put("type", type);
                    dataToSend.put("deviceName", deviceName);
                    dataToSend.put("status", isSeekBarEnabled ? "ON" : "OFF");
                    dataToSend.put("seekBarValue", value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Gửi dữ liệu qua UDP
                sendDataOverUDP(dataToSend.toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý ở đây
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý ở đây
            }
        });



        btn_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bật/tắt SeekBar
                isSeekBarEnabled = !isSeekBarEnabled;
                sb_light.setEnabled(isSeekBarEnabled);


                // Cập nhật TextView trạng thái
                String statusText = isSeekBarEnabled ? "Light Is ON" : "Light Is OFF";
                textView_status.setText(statusText);

//                // Nếu SeekBar bị tắt, đặt giá trị về 0%
//                if (!isSeekBarEnabled) {
//                    sb_light.setProgress(0);
//                    textView_light.setText("0%");
//                }

                // Tạo JSON object và thêm các thông tin
                JSONObject dataToSend = new JSONObject();
                try {
                    dataToSend.put("type", type);
                    dataToSend.put("deviceName", deviceName);
                    dataToSend.put("status", isSeekBarEnabled ? "ON" : "OFF");
                    dataToSend.put("seekBarValue", value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Gửi dữ liệu qua UDP
                sendDataOverUDP(dataToSend.toString());

            }
        });

        return view;

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