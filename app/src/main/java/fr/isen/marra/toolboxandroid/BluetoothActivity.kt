package fr.isen.marra.toolboxandroid

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_bluetooth.*

class BluetoothActivity : AppCompatActivity() {

    private var mScanning: Boolean = false
    private var bluetoothGatt: BluetoothGatt? = null
    private lateinit var handler: Handler
    private lateinit var adapter: BluetoothActivityAdapter


    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }


    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        stateTextView.text = "Lancer le scan bluetooth"

        DividerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        play_iv.setOnClickListener {
            when {
                isBLEEnabled -> {
                    initScan()
                }
                bluetoothAdapter != null -> {
                    val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableIntent, 44)
                }
                else -> {
                    blefailedText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initScan() {
        handler = Handler()

        adapter = BluetoothActivityAdapter(arrayListOf(), ::onDeviceClicked)
        bleRecyclerView.adapter = adapter
        bleRecyclerView.layoutManager = LinearLayoutManager(this)

        play_iv.setOnClickListener{scanLeDevice(true)}
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            DividerView.visibility = View.VISIBLE
            when {
                enable -> {
                    handler.postDelayed({
                        mScanning = false
                        stopScan(leScanCallBack)
                        play_iv.setImageResource(R.drawable.play_arrow)
                        progressBar.visibility = View.GONE
                        DividerView.visibility = View.VISIBLE
                        stateTextView.text = "Lancer le scan bluetooth"
                    }, 10000)
                    mScanning = true
                    startScan(leScanCallBack)
                    play_iv.setImageResource(R.drawable.pause)
                    DividerView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    stateTextView.text = "Scan bluetooth en cours ..."
                }
                else -> {
                    mScanning = false
                    stopScan(leScanCallBack)
                    play_iv.setImageResource(R.drawable.play_arrow)
                    progressBar.visibility = View.GONE
                    DividerView.visibility = View.VISIBLE
                    stateTextView.text = "Lancer le scan bluetooth"
                }
            }
        }
    }

    private val leScanCallBack = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLEActivity", "${result.device}")
            runOnUiThread {
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isBLEEnabled) {
            scanLeDevice(false)
            play_iv.setImageResource(R.drawable.play_arrow)
            progressBar.visibility = View.GONE
            DividerView.visibility = View.VISIBLE
            stateTextView.text = "Lancer le scan bluetooth"
        }
    }

    private fun onDeviceClicked(device: BluetoothDevice) {
        val intent = Intent(this, BleDetails::class.java)
        intent.putExtra("ble_device", device)
        startActivity(intent)
    }
}


