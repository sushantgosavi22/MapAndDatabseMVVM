package com.sushant.mapanddatabasemvvm.dependancy

import com.google.android.gms.maps.model.LatLng
import com.sushant.mapanddatabasemvvm.database.model.LocationEntry
import com.sushant.mapanddatabasemvvm.database.model.UiLocationEntry

fun LocationEntry.convertToUiLocationEntry() : UiLocationEntry{
    val list = this.coordinates.split(",").map { it.trim().toDouble() }
    return UiLocationEntry(this.propertyName, LatLng(list.first(),list.last()))
}