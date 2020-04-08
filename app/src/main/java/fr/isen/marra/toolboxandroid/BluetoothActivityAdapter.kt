package fr.isen.marra.toolboxandroid

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_bluetooth_cell.view.*

class BluetoothActivityAdapter(private val scanResults: ArrayList<ScanResult>, val deviceClickListener: (BluetoothDevice) -> Unit) :
        RecyclerView.Adapter<BluetoothActivityAdapter.BleViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.activity_bluetooth_cell, parent, false)
        return BluetoothActivityAdapter.BleViewHolder(view, scanResults, deviceClickListener)
    }

    override fun getItemCount(): Int = scanResults.size

    override fun onBindViewHolder(holder: BleViewHolder, position: Int) {
        holder.pushInfo(position)
    }

    fun addDeviceToList(result: ScanResult) {
        val index = scanResults.indexOfFirst { it.device.address == result.device.address }
        if (index != -1) {
            scanResults[index] = result
        } else {
            scanResults.add(result)
        }
    }

    class BleViewHolder(
            scanView: View,
            private val scanResults: ArrayList<ScanResult>,
            val deviceClickListener: (BluetoothDevice) -> Unit) : RecyclerView.ViewHolder(scanView) {

        val rssi: TextView = scanView.distanceTextView
        private val device_name: TextView = scanView.nameBle
        private val address: TextView = scanView.AddressBle
        private val layout = scanView.cellBleLayout

        fun pushInfo(position: Int) {
            rssi.text = scanResults[position].rssi.toString()
            device_name.text = scanResults[position].device.name
            address.text = scanResults[position].device.address

            layout.setOnClickListener {
                deviceClickListener.invoke(scanResults[position].device)
            }
        }
    }
}



