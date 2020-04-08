package fr.isen.marra.toolboxandroid

import android.bluetooth.BluetoothGattCharacteristic
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class BluetoothService(title: String, items: List<BluetoothGattCharacteristic>) :
        ExpandableGroup<BluetoothGattCharacteristic>(title, items)