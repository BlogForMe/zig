package com.casanube.medical;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import static android.text.TextUtils.isEmpty;

public class ZigbeeSdk2 {
    private static ZigbeeSdk2 mInstance;
    //    private ExecutorService exec;
//    private Future<?> future;
    private DataThread dataThread;

    public static ZigbeeSdk2 getInstance() {
        if (mInstance == null) {
            synchronized (ZigbeeSdk2.class) {
                if (mInstance == null) {
                    mInstance = new ZigbeeSdk2();
                }
            }
        }
        return mInstance;
    }


    private final int CHANNEL = 0;

    private ICallResult iCallBack;

    public void startSdk(ICallResult iCallResult) {
        iCallBack = iCallResult;
        DevicePort.Init();
        DevicePort.OpenChannel(CHANNEL, 115200, 0xf0);

        handler.sendEmptyMessageDelayed(1, 255 * 1000);
        dataThread = new DataThread();
        dataThread.start();

        DevicePort.WriteString(ALLOW_JOIN);
    }

    class DataThread extends Thread {
        @Override
        public void run() {
//            try {
            while (!dataThread.isInterrupted()) {
                Log.i("ZigbeeSdk", "DataThread" + "   run " + dataThread.isInterrupted());
                readDataZig();
//                    Thread.sleep(1000);
            }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                Log.i("ZigbeeSdk", "DataThread  interrupt " + isInterrupted());
//                return;
//            }

        }
    }


//    String devices ="{\"cmd\":\"DEVICE_JOIN\",\"model\":\"240\",\"sid\":\"00124B000E915264\",\"short_addr\":0,\"endpoint\":0,\"data\":{\"hardwareVer\":\"1.1.01\",\"softwareVer\":\"2.1.01\"}}";

    public void readDataZig() {
        String ssCha = DevicePort.ReadString(CHANNEL);
//        String ssCha = devices;
//        Log.i("ZigbeeSdk", "sscat " + ssCha);
        if (!isEmpty(ssCha) && !"NULL".equals(ssCha) && !("OUT".equals(ssCha))) {
            try {
//                Log.i("ZigbeeSdk", "ssCha " + ssCha);
                JSONObject jsobObject = new JSONObject(ssCha);
                if (jsobObject.has("cmd") && "DEVICE_JOIN".equals(jsobObject.getString("cmd"))
                        && (!"240".equals(jsobObject.getString("model")))) {
                    if (iCallBack != null) {
//                        Log.i("ZigbeeSdk", "getResult");
                        iCallBack.getResult(true);
                        handler.removeMessages(1);
                        DevicePort.WriteString(DISABLE_JOIN);
                        DevicePort.WriteString(RESET);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (iCallBack != null) {
                iCallBack.getResult(false);
            }
        }
    };


    public void destory() {
        if (dataThread != null) {
            dataThread.interrupt();
            Log.i("destory  ", "isCancel   " + dataThread.isInterrupted());
            DevicePort.Destroy();
            dataThread = null;
        }
    }


    private String ALLOW_JOIN = "{\n" +
            "    \"cmd\":\"ALLOW_JOIN\",\n" +
            "    \"model\":\"240\",\n" +
            "    \"sid\":\"\",\n" +
            "    \"short_addr\":0,\n" +
            "    \"endpoint\":0,\n" +
            "    \"data\":null\n" +
            "}";

    String DISABLE_JOIN = "{\n" +
            "    \"cmd\":\"DISABLE_JOIN\",\n" +
            "    \"model\":\"240\",\n" +
            "    \"sid\":\"\",\n" +
            "    \"short addr\":0,\n" +
            "    \"endpoint\":0,\n" +
            "    \"data\":null\n" +
            "}";

    String RESET = "{\n" +
            "    \"cmd\":\"RESET\",\n" +
            "    \"model\":\"240\",\n" +
            "    \"sid\":\"\",\n" +
            "    \"short addr\":0,\n" +
            "    \"endpoint\":0,\n" +
            "    \"data\":null\n" +
            "}";

}
