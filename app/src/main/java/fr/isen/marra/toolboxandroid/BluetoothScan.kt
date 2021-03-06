package fr.isen.marra.toolboxandroid

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_bluetooth_cell.view.*

class BluetoothScan(private val devicesResults: ArrayList<ScanResult>, private val deviceClickListener: (BluetoothDevice) -> Unit) :
        RecyclerView.Adapter<BluetoothScan.ScanViewHolder>() {
            class ScanViewHolder(scanView: View) : RecyclerView.ViewHolder(scanView) {
                val layout = scanView.layoutCell
                val name_device: TextView = scanView.name
                val mac: TextView = scanView.address
                val rssi: TextView = scanView.rssi_tv
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
                val view =
                  LayoutInflater.from(parent.context).inflate(R.layout.activity_bluetooth_cell, parent, false)

        return ScanViewHolder(view)
    }

    override fun getItemCount(): Int = devicesResults.size

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {

        val distance = devicesResults[position].rssi
        holder.name_device.text = devicesResults[position].device.name ?: "Nom inconnu"
        holder.mac.text = devicesResults[position].device.address
        holder.rssi.text = devicesResults[position].rssi.toString()
        holder.layout.setOnClickListener {
            deviceClickListener.invoke(devicesResults[position].device)
        }

        if (distance < -80) {
            holder.rssi.rssi_tv.setBackgroundColor(Color.parseColor("#000C8F"))
        } else if (distance >= -80 && distance < -60) {
            holder.rssi.rssi_tv.setBackgroundColor(Color.parseColor("#000EFB"))
        } else if (distance > -40) {
            holder.rssi.rssi_tv.setBackgroundColor(Color.parseColor("#1885FB"))
        }

    }

    fun addDeviceToList(result: ScanResult) {
        val index = devicesResults.indexOfFirst { it.device.address == result.device.address }
        if (index != -1) {
            devicesResults[index] = result
        } else {
            devicesResults.add(result)
        }
    }

    fun clearResults() {
        devicesResults.clear()
    }
}