package fr.isen.marra.toolboxandroid

import android.bluetooth.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_ble_details.*


class BleDetails : AppCompatActivity() {

    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG: String = "services"
    private lateinit var adapter: BleDetailAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_details)

        val device: BluetoothDevice = intent.getParcelableExtra("ble_device")
        nameDevice.text = device.name
        bluetoothGatt = device.connectGatt(this, true, gattCallback)
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt:BluetoothGatt, status:Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    runOnUiThread {
                        waitingForConnection.text = STATE_CONNECTED
                    }
                    bluetoothGatt?.discoverServices()
                    Log.i(TAG, "Connected to GATT")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    runOnUiThread {
                        waitingForConnection.text = STATE_DISCONNECTED
                    }
                    Log.i(TAG, "Disconnected from GATT")
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            runOnUiThread {
                detailsView.adapter = BleDetailAdapter(
                        gatt?.services?.map {
                            BluetoothService(
                                    it.uuid.toString(),
                                    it.characteristics
                            )
                        }?.toMutableList() ?: arrayListOf()
                        , this@BleDetails, gatt)
                detailsView.layoutManager = LinearLayoutManager(this@BleDetails)
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic, status: Int) {
            val value = characteristic.getStringValue(0)
            Log.e("TAG", "onCharacteristicRead: " + value + " UUID " + characteristic.uuid.toString())
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic, status: Int) {
            val value = characteristic.value
            Log.e("TAG", "onCharacteristicWrite: " + value + " UUID " + characteristic.uuid.toString())
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic) {
            val value = byteArrayToHexString(characteristic.value)
            Log.e("TAG", "onCharacteristicChanged: " + value + " UUID " + characteristic.uuid.toString())
            adapter.notifyDataSetChanged()
        }
    }

    private fun byteArrayToHexString(array: ByteArray): String {
        val result = StringBuilder(array.size * 2)
        for ( byte in array ) {
            val toAppend = String.format("%X", byte) // hexadecimal
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1) // remove last '-'
        return result.toString()
    }

    override fun onStop() {
        super.onStop()
        bluetoothGatt?.close()
    }

    companion object {
        private const val STATE_DISCONNECTED = "déconnecté"
        private const val STATE_CONNECTING = 1
        private const val STATE_CONNECTED = "Connecté"
       /* const val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
        const val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"*/
    }
}