package fr.isen.marra.toolboxandroid

import android.Manifest
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bluetooth.*
import kotlin.collections.ArrayList


class BluetoothActivity : AppCompatActivity() {
    private var playScan:String = "Lancer le scan bluetooth"
    private var scanInProcess:String = "Scan bluetooth en cours"
    private lateinit var handler: Handler
    private var mScanning: Boolean = false
    private lateinit var adapter: BluetoothScan
    private val devices = ArrayList<ScanResult>()
    private val permManager = PermissionManager(this)
    private val blePermission = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        bleTextFailed.visibility = View.GONE
        play_iv.setImageResource(R.drawable.play_arrow)

        buttonRetourBle.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        play_iv.setOnClickListener {
            if (permManager.arePermissionsOk(blePermission)) {
                when {
                    isBLEEnabled -> {

                        initBLEScan()
                        initScan()
                    }
                    bluetoothAdapter != null -> {
                        val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        startActivityForResult(enableBTIntent, 44)
                    }
                    else -> {
                        bleTextFailed.visibility = View.VISIBLE
                    }
                }
                recyclerViewBle.adapter = BluetoothScan(devices, ::onDeviceClicked)
                recyclerViewBle.layoutManager = LinearLayoutManager(this)
            } else {
                permManager.requestMultiplePermissions(this, blePermission, 30)
            }
        }
    }


    private fun initScan() {
        progressBar.visibility = View.VISIBLE
        divider.visibility = View.GONE

        handler = Handler()
        scanLeDevice(true)
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            divider.visibility = View.VISIBLE
            when {
                enable -> {
                    handler.postDelayed({
                        mScanning = false
                        stopScan(leScanCallback)
                        play_iv.setImageResource(R.drawable.play_arrow)
                        progressBar.visibility = View.GONE
                        divider.visibility = View.VISIBLE
                        state_tv.text = playScan
                    }, 10000)

                    mScanning = true
                    startScan(leScanCallback)
                    play_iv.setImageResource(R.drawable.pause)
                    progressBar.visibility = View.GONE
                    divider.visibility = View.VISIBLE
                    state_tv.text = scanInProcess
                    adapter.clearResults()
                    adapter.notifyDataSetChanged()
                }

                else -> {
                    mScanning = false
                    stopScan(leScanCallback)
                    play_iv.setImageResource(R.drawable.play_arrow)
                    progressBar.visibility = View.GONE
                    divider.visibility = View.VISIBLE
                    state_tv.text = playScan
                }
            }
        }
    }

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLE", "${result.device}")
            runOnUiThread {
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initBLEScan() {
        adapter = BluetoothScan(arrayListOf(), ::onDeviceClicked)
        recyclerViewBle.adapter = adapter
        recyclerViewBle.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        handler = Handler()

        scanLeDevice(true)
        recyclerViewBle.setOnClickListener {
            scanLeDevice(!mScanning)
        }
    }

    private fun onDeviceClicked(device: BluetoothDevice) {
        val intent = Intent(this, BleDetails::class.java)
        intent.putExtra("ble_device", device)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 44) {
            if (resultCode == Activity.RESULT_OK) {
                if (isBLEEnabled) {
                    Toast.makeText(this, "Bluetooth enable", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Bluetooth disable", Toast.LENGTH_SHORT).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth enabling cancel", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        scanLeDevice(false)
    }


    open class PermissionManager(val context: Context){

        fun requestAPermission(activity: Activity, perm: String, code: Int) {
            ActivityCompat.requestPermissions(activity, arrayOf(perm), code)
        }

        fun isPermissionOk(perm: String): Boolean {
            val result = ContextCompat.checkSelfPermission(context, perm)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun requestMultiplePermissions(activity: Activity, perms: Array<String>, code: Int) {
            ActivityCompat.requestPermissions(activity, perms, code)
        }

        fun arePermissionsOk(perms: Array<String>): Boolean {
            for (p in perms) {
                if (isPermissionOk(p))
                    continue
                else
                    return false
            }
            return true
        }

    }
}