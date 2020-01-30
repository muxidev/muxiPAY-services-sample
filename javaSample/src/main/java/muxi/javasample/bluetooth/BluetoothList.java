package muxi.javasample.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import muxi.javasample.R;
import muxi.payservices.sdk.service.IMPSManager;


public class BluetoothList {

    private String TAG = BluetoothList.class.getSimpleName();

    private Context context;
    private Spinner spnBluetoothDevice;
    private List<SimpleBluetoothDevice> listPairedDevices;
    private IMPSManager mpsManager;
    private BtComunication btComunication;
    private static SimpleBluetoothDevice mCurresntSelectedDevice = new SimpleBluetoothDevice();
    private ArrayAdapter<SimpleBluetoothDevice> adapter;

    public BluetoothList(Context context,Spinner spnBluetoothDevice){
        this.context = context;
        this.spnBluetoothDevice = spnBluetoothDevice;
        listPairedDevices = new ArrayList<>();
        adapter = createAdapter(listPairedDevices);
    }

    public void setMpsManager(IMPSManager mpsManager) {
        this.mpsManager = mpsManager;
    }

    public void setBtLinester(BtComunication btComunication){
        this.btComunication = btComunication;
    }

    public void setupDeviceList() {
        listPairedDevices.clear();

        Set<BluetoothDevice> deviceList = getPairedDevices();

        if (mCurresntSelectedDevice.isEmpty()) {
            listPairedDevices.add(new SimpleBluetoothDevice(context.getResources()
                    .getString(R.string.no_pinpad_selected)," "));
        } else {
            listPairedDevices.add(mCurresntSelectedDevice);
        }

        if (!deviceList.isEmpty()) {
            for (BluetoothDevice bd: deviceList) {
                listPairedDevices.add(new SimpleBluetoothDevice(bd.getName(),bd.getAddress()));
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void updateItemsOnSpinner() {
        if(isBluetoothOn()){
            setupDeviceList();

            adapter.setDropDownViewResource(R.layout.simple_dropdown_item);
            spnBluetoothDevice.setAdapter(adapter);
            spnBluetoothDevice.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(isBluetoothOn()){
                        //Check if exists one or more listPairedNames bonded
                        if (getPairedDevices().isEmpty()) {
                            openBluetoothOptions(context.getApplicationContext());
                        } else {
                            updateItemsOnSpinner();
                        }
                    }
                    else{
                        enableBluetooth();
                    }
                }
                return false;
            });

            spnBluetoothDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent,View view,int position,long id) {
                    mCurresntSelectedDevice
                            .updateDevice((SimpleBluetoothDevice)spnBluetoothDevice.getSelectedItem());
                    if(mpsManager != null){
                        Log.d(TAG,"Saving current bluetooth mac address " +
                                mCurresntSelectedDevice.getAddress());
                        mpsManager.setCurrentBluetoothDevice(mCurresntSelectedDevice.getAddress());
                    }
                    // Update the first position to show the actual selected device
                    listPairedDevices.remove(0);
                    listPairedDevices.add(0, mCurresntSelectedDevice);
                    adapter.notifyDataSetChanged();
                    btComunication.updateStatusBt();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });
        } else {
            enableBluetooth();
            updateItemsOnSpinner();
        }
    }

    public ArrayAdapter<SimpleBluetoothDevice> createAdapter(List<SimpleBluetoothDevice> listPairedDevices){
        return new ArrayAdapter<SimpleBluetoothDevice>(context,R.layout.simple_list_item,
                R.id.tv_spinner,listPairedDevices){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, null, parent);
                // If this is the selected item position
                if (position == 0) {
                    TextView textview = view.findViewById(R.id.tv_spinner);
                    textview.setTextColor(context.getResources().getColor(R.color.turquoise_blue_dark));
                    ImageView imageView = view.findViewById(R.id.iv_bluetooth_icon);
                    imageView.setVisibility(View.INVISIBLE);
                }
                else {
                    // for other views
                    view.setBackgroundColor(Color.WHITE);
                }
                return view;
            }
        };
    }

    public void setWhenDeconfigure(){
        mCurresntSelectedDevice.updateDevice(" "," ");
    }

    private boolean isBluetoothOn(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.isEnabled();
        } else {
            return false;
        }
    }

    private void openBluetoothOptions(Context context){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void enableBluetooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }
    }

    private Set<BluetoothDevice> getPairedDevices(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.getBondedDevices();
    }

    public interface BtComunication{
        void updateStatusBt();
    }
}