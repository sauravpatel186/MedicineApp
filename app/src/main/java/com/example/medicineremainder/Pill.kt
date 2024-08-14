package com.example.medicineremainder

import android.os.Parcel
import android.os.Parcelable

data class Pill(
    val pillId: Int,
    val pillName: String,
    val pillDosage: String,
    val pillTime: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString()?:"",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pillId)
        parcel.writeString(pillName)
        parcel.writeString(pillDosage)
        parcel.writeString(pillTime)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Pill> {
        override fun createFromParcel(parcel: Parcel): Pill {
            return Pill(parcel)
        }

        override fun newArray(size: Int): Array<Pill?> {
            return arrayOfNulls(size)
        }
    }
}
