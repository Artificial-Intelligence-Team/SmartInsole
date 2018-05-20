package com.example.alex.myapplication.model.data_presenter;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.TextView;

import com.example.alex.myapplication.R;
import com.example.alex.myapplication.model.bluno.BlunoLibrary;
import com.example.alex.myapplication.model.chat.ChatActivity;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class DataActivity extends BlunoLibrary {
    private CustomGauge gauge1;
    private CustomGauge gauge2;
    private CustomGauge gauge3;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    int temperature;
    int humidity;
    int nowTemperature;
    int nowHumidity;
    // Detect Button
    private  Button detectButton;
    private  Button bluetoothButton;
    private String receivedText = "";
    private Thread data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        // 連接藍芽
        onCreateProcess();														//onCreate Process by BlunoLibrary
        serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200
        bluetoothButton = findViewById(R.id.button1);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
            }
        });
        // 頁面切換
        ImageButton androidImageButton;
        androidImageButton = findViewById(R.id.imageButton1);
        androidImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(DataActivity.this  , ChatActivity.class);
                startActivity(intent);
            }
        });
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);*/

        // 數據取得與顯現
        detectButton = findViewById(R.id.button);
        gauge1 = findViewById(R.id.gauge1);
        gauge2 = findViewById(R.id.gauge2);
        gauge3 = findViewById(R.id.gauge3);
        gauge1.setEndValue(100);
        gauge2.setEndValue(100);
        gauge3.setEndValue(100);
        text1  = findViewById(R.id.textView1);
        text2  = findViewById(R.id.textView2);
        text3  = findViewById(R.id.textView3);
        text1.setText(Integer.toString(gauge1.getValue()));
        text2.setText(Integer.toString(gauge2.getValue()));

        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data != null && data.isAlive()) {
                    try {
                        // Thread B 加入 Thread A
                        data.join();
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                receivedText = "";
                // 告知藍芽傳送給我溫溼度數據
                serialSend("@");
                try{
                    Thread.sleep(3000);
                } catch (Exception e){e.getCause();}
                data = new Thread() {
                    public void run() {
                        if (receivedText.equals("")) {
                            System.out.print("空空的~");
                            receivedText = "10";
                            nowTemperature = Integer.parseInt(receivedText);
                            nowHumidity = Integer.parseInt(receivedText);
                        } else {
                            System.out.print("有喔~");
                            System.out.print(receivedText.split("=")[0] + ":");
                            System.out.print(receivedText.split("=")[1] + "\n\n");
                            try {
                                nowTemperature = Integer.parseInt(receivedText.split("=")[0]);
                                nowHumidity = Integer.parseInt(receivedText.split("=")[1]);
                            } catch (Exception e) {
                                nowTemperature = 50;
                                nowHumidity = 50;
                                System.out.println(e.toString());
                            }
                        }
                        for (temperature = 0; temperature <= nowTemperature; temperature++) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        gauge1.setValue(temperature);
                                        text1.setText("溫度:" + Integer.toString(temperature) + "°C");
                                    }
                                });
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        for (humidity = 0; humidity <= nowHumidity; humidity++) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        gauge2.setValue(humidity);
                                        text2.setText("濕度:" + Integer.toString(humidity) + "%");
                                    }
                                });
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String level;
                                    level = getFungusDanger(getTemperatureScore(temperature) + getHumidityScore(humidity));
                                    text3.setText(level);
                                    gauge3.setValue((getTemperatureScore(temperature) + getHumidityScore(humidity))*20 + 10);
                                }
                            });
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                data.start();
            }
        });
    }
    private String getFungusDanger (Integer score) {
        switch(score) {
            case 0:
                return("良好");
            case 1:
                return("輕微");
            case 2:
                return("中度");
            case 3:
                return("嚴重");
            case 4:
                return("危險");
            default: return null;
        }
    }
    private Integer getTemperatureScore (Integer value) {
        int x;
        if (value < 21) {
            x = 0;
        } else if (value < 26) {
            x = 1;
        } else if (value < 36) {
            x = 2;
        } else {
            x = 0;
        }
        return x;
    }

    private Integer getHumidityScore (Integer value) {
        int y;
        if (value < 63) {
            y = 0;
        } else if (value < 75) {
            y = 1;
        } else if (value < 96) {
            y = 2;
        } else {
            y = 0;
        }
        return y;
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void onResume(){
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();														//onResume Process by BlunoLibrary
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();														//onPause Process by BlunoLibrary
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();														//onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }

    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {											//Four connection state
            case isConnected:
                bluetoothButton.setText("Connected");
                bluetoothButton.setTextColor(Color.parseColor("#000000"));
                detectButton.setTextColor(Color.parseColor("#FF0000"));
                break;
            case isConnecting:
                bluetoothButton.setText("Connecting");
                break;
            case isToScan:
                bluetoothButton.setText("Scan");
                break;
            case isScanning:
                bluetoothButton.setText("Scanning");
                break;
            case isDisconnecting:
                bluetoothButton.setText("isDisconnecting");
                break;
            default:
                break;
        }
    }
    @Override
    public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
        // TODO Auto-generated method stub
        receivedText = theString;
        System.out.println("*****\n" + receivedText);
//        try {
//            byte[] data = receivedText.getBytes("UTF-8");
//            /*receivedText = "";
//            for (int i = 0; i < data.length; i++) {
//                if(i%2 == 0) {
//                    receivedText += Character.toString ((char) data[i]);
//                }
//            }*/
////            System.out.print("\n" + Arrays.toString(receivedText.getBytes("UTF-8")));
////            System.out.print("\n" + Arrays.toString("100".getBytes("UTF-8")));
//        } catch (Exception e) {
//            System.out.print("error");
//        }
        //((ScrollView)serialReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
    }
}
