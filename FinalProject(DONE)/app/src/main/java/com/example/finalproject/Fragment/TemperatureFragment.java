package com.example.finalproject.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class TemperatureFragment extends Fragment {

    private ProgressBar progressBar_temp;
    private TextView textView_temp;
    private Button btn_onoff, btn_settemp;
    private boolean isProgressBarEnabled = true;
    private Handler handler = new Handler();
    private int progressValue = 0;
    String deviceName = "";
    private final int type = 3;

    private final int UDP_PORT = 6828; // Cổng UDP mà bạn sử dụng
    private final String SERVER_IP = "192.168.43.38"; // Địa chỉ IP của máy chủ UDP



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temperature, container, false);
        

        progressBar_temp = view.findViewById(R.id.progress_temp);
        textView_temp = view.findViewById(R.id.tv_temp);
        btn_onoff = view.findViewById(R.id.btnoff_temp);
        btn_settemp = view.findViewById(R.id.btn_set_temp);

        // Lấy thông tin vị trí và tên phòng từ bundle nếu tồn tại.
        Bundle argument = getArguments();
        if (argument != null) {
            deviceName = argument.getString("deviceName", "");
        }

        // Thiết lập sự kiện lắng nghe cho nút bật/tắt
        btn_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProgressBarEnabled = !isProgressBarEnabled;

                // Tạo JSON object và thêm các thông tin
                JSONObject dataToSend = new JSONObject();
                try {
                    dataToSend.put("type", type);
                    dataToSend.put("deviceName", deviceName);
                    dataToSend.put("status", isProgressBarEnabled ? "ON" : "OFF");
                    dataToSend.put("progressValue", progressValue+"%");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Gửi dữ liệu qua UDP
                sendDataOverUDP(dataToSend.toString());

                if (isProgressBarEnabled) {
                    btn_settemp.setVisibility(View.VISIBLE);
                    startProgressBar();
                } else {
                    btn_settemp.setVisibility(View.INVISIBLE);
                    stopProgressBar();
                }
            }
        });

        btn_settemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPercentageInputDialog();
            }
        });

        return view;
    }

    private void startProgressBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isProgressBarEnabled) {
                    if (progressValue < 100) {
                        progressBar_temp.setProgress(progressValue);
                        textView_temp.setText(progressValue + "°C");
                        startProgressBar();
                    }
                }
            }
        }, 100);
    }

    private void stopProgressBar() {
        handler.removeCallbacksAndMessages(null);
        progressValue=0;
        progressBar_temp.setProgress(progressValue);
        textView_temp.setText("OFF");
    }

    private void showPercentageInputDialog() {
        // Sử dụng AlertDialog để hiển thị hộp thoại nhập %
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Set Percentage");

        // Sử dụng layout custom cho hộp thoại
        View view = getLayoutInflater().inflate(R.layout.dialog_set_temp, null);

        final EditText editText = view.findViewById(R.id.editText_settemp);

        builder.setView(view);

        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi nhấn nút Set
                String input = editText.getText().toString();
                if (!input.isEmpty()) {
                    int newPercentage = Integer.parseInt(input);
                    if (newPercentage >= 0 && newPercentage <= 100) {
                        progressValue = newPercentage;
                        progressBar_temp.setProgress(progressValue);
                        textView_temp.setText(progressValue + "°C");
                    }

                    // Tạo JSON object và thêm các thông tin
                    JSONObject dataToSend = new JSONObject();
                    try {
                        dataToSend.put("type", type);
                        dataToSend.put("deviceName", deviceName);
                        dataToSend.put("status", isProgressBarEnabled ? "ON" : "OFF");
                        dataToSend.put("progressValue", progressValue+"%");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Gửi dữ liệu qua UDP
                    sendDataOverUDP(dataToSend.toString());
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi nhấn nút Cancel
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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