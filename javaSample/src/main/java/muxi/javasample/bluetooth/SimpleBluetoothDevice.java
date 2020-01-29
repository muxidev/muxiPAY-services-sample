package muxi.javasample.bluetooth;

import androidx.annotation.NonNull;

public class SimpleBluetoothDevice {

    private String name;
    private String address;

    SimpleBluetoothDevice() {
        this.name = " ";
        this.address = " ";
    }

    SimpleBluetoothDevice(String name,String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    void updateDevice(String name,String address) {
        this.name = name;
        this.address = address;
    }

    void updateDevice(SimpleBluetoothDevice sbd) {
        this.name = sbd.getName();
        this.address = sbd.getAddress();
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
