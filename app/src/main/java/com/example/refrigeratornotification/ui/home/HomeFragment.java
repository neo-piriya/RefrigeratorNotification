package com.example.refrigeratornotification.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.refrigeratornotification.MainActivity;
import com.example.refrigeratornotification.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    Button testBtn;
    TextView statusTextView;
    private MqttConnectOptions mOptions;
    private MqttAndroidClient client;
    private final String TOPIC = "test/string";

    private IMqttActionListener mConnectCallback = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d("test", "mConnectCallback onSuccess");
            subscribe();
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d("test", "mConnectCallback onFailure " + exception);
        }
    };
    private IMqttActionListener mSubscribeCallback = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d("test", "mSubscribeCallback onSuccess");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d("test", "mSubscribeCallback onSuccess");
        }
    };
    private IMqttActionListener mPublishCallback = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d("test", "mPublishCallback onSuccess");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d("test", "mPublishCallback onSuccess");
            disconnect();
            connect();
        }
    };
    private MqttCallback mMessageListener = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.d("test", "mMessageListener onSuccess topic=" + topic + ", message=" + message);
            setStatusTextView(message.toString());
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };

    private void setStatusTextView(String msg) {
        statusTextView.setText(msg.toUpperCase(Locale.ROOT));


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        testBtn = getView().findViewById(R.id.home_test);
        statusTextView = getView().findViewById(R.id.homs_status);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(client != null && client.isConnected())
                    publish();
            }
        });

        /*
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String name = sharedPreferences.getString("duration_notification","");
        Toast.makeText(getContext(), name,Toast.LENGTH_LONG).show();
        */

        mOptions = new MqttConnectOptions();
        mOptions.setUserName("myhome");
        mOptions.setPassword("testtest".toCharArray());
        client = new MqttAndroidClient(getContext(), "tcp://192.168.8.2:1883", MqttClient.generateClientId());
        Log.d("test",String.valueOf(client.isConnected()));

    }

    @Override
    public void onResume() {
        super.onResume();
        connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        disconnect();
    }

    private void connect() {
        try {
            client.connect(mOptions, getContext(), mConnectCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void subscribe() {
        try {
            IMqttToken subToken = client.subscribe(TOPIC, 1, getContext(), mSubscribeCallback);
            client.setCallback(mMessageListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void publish() {

        MqttMessage message = new MqttMessage("OFF".getBytes(StandardCharsets.UTF_8));
        try {
            client.publish(TOPIC, message, getContext(), mPublishCallback);
        } catch (Exception e) {
            e.getMessage();

        }
    }


}