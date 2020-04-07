package fr.isen.marra.toolboxandroid

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bluetooth.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager


class BluetoothActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var mScanning: Boolean = false
    private lateinit var adapter: BluetoothActivityAdapter
    private val devices = ArrayList<ScanResult>()

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        buttonRetour5.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        bleTextFail.visibility = View.GONE

        bluetoothRecycler.adapter = BluetoothActivityAdapter(devices, ::onDeviceClicked)
        bluetoothRecycler.layoutManager = LinearLayoutManager(this)

        blePlaytxt.setOnClickListener {
            when {
                isBLEEnabled -> {
                    //init scan
                    if (blePlaytxt.text == "@string/scanGo") {
                        imageScan.setImageResource(android.R.drawable.ic_media_pause)
                        blePlaytxt.text = "@string/scanEnCours"
                        initBLEScan()
                        initScan()
                    } else if (blePlaytxt.text == "@string/scanEnCours") {
                        imageScan.setImageResource(android.R.drawable.ic_media_play)
                        blePlaytxt.text = "@string/scanGo"
                        progressBar.visibility = View.INVISIBLE
                    }
                }
                bluetoothAdapter != null -> {
                    //ask for permission
                    val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT)
                }
                else -> {
                    //device is not compatible with your device
                    bleTextFail.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initScan() {
        progressBar.visibility = View.VISIBLE
        bleDivider.visibility = View.GONE

        handler = Handler()

        scanLeDevice(true)
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if (enable) {
                Log.w("BLEScanActivity", "Scanning for devices")
                handler.postDelayed({
                    mScanning = false
                    stopScan(leScanCallback)
                }, SCAN_PERIOD)
                mScanning = true
                startScan(leScanCallback)
                adapter.clearResults()
                adapter.notifyDataSetChanged()
            } else {
                mScanning = false
                stopScan(leScanCallback)
            }
        }
    }

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLEScanActivity", "onResult()")
            Log.w("BLEScanActivity", "CaAMarche ${result?.device}")
            runOnUiThread {
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initBLEScan() {
        adapter = BluetoothActivityAdapter(
                arrayListOf(),
                ::onDeviceClicked
        )
        bluetoothRecycler.adapter = adapter
        bluetoothRecycler.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        handler = Handler()

        scanLeDevice(true)
        blePlaytxt.setOnClickListener{
            scanLeDevice(!mScanning)
        }

    }

    private fun onDeviceClicked(device: BluetoothDevice) {
        val intent = Intent(this, BluetoothActivityAdapter::class.java)
        intent.putExtra("ble_device", device)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                if (isBLEEnabled) {
                    Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth enabling has been canceled", Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        scanLeDevice(false)
    }


    companion object {
        private const val SCAN_PERIOD: Long = 10000
        private const val REQUEST_ENABLE_BT = 44
    }
}