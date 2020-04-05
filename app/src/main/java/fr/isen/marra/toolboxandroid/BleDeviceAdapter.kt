package fr.isen.marra.toolboxandroid

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_bluetooth_cell.view.*

class BleDeviceAdapter(private val scanResults: ArrayList<ScanResult>, private val deviceClickListener: (BluetoothDevice) -> Unit) :
        RecyclerView.Adapter<BleDeviceAdapter.DevicesViewHolder>() {


    class DevicesViewHolder(devicesView: View) : RecyclerView.ViewHolder(devicesView){
        val layout = devicesView.BLEDeviceLayout
        val deviceName: TextView = devicesView.nameDevice
        val deviceMac: TextView = devicesView.MacAddress
        val deviceRSSI: TextView = devicesView.RSSIDevice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleDeviceAdapter.DevicesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_bluetooth_cell, parent, false)


        return DevicesViewHolder(view)
    }

    override fun getItemCount(): Int = scanResults.size

    override fun onBindViewHolder(holder: BleDeviceAdapter.DevicesViewHolder, position: Int) {
        holder.deviceName.text = scanResults[position].device.name ?: "Device Unknown"
        holder.deviceMac.text = scanResults[position].device.address
        holder.deviceRSSI.text = scanResults[position].rssi.toString()
        holder.layout.setOnClickListener{
            deviceClickListener.invoke(scanResults[position].device)
        }
    }



    fun addDeviceToList(result: ScanResult) {
        val index = scanResults.indexOfFirst { it.device.address == result.device.address }
        if (index != -1) {
            scanResults[index] = result
        }else {
            scanResults.add(result)
        }
    }

    fun clearResults() {
        scanResults.clear()
    }
}