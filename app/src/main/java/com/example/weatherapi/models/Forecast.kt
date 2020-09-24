package com.example.weatherapi.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Forecast(

	@field:SerializedName("main")
	val main: Main? = null,

	@field:SerializedName("dt")
	val dt: Long? = null,

	@field:SerializedName("name")
	val name: String? = null,
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readParcelable(Main::class.java.classLoader),
		parcel.readValue(Long::class.java.classLoader) as? Long,
		parcel.readString()
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(main, flags)
		parcel.writeValue(dt)
		parcel.writeString(name)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Forecast> {
		override fun createFromParcel(parcel: Parcel): Forecast {
			return Forecast(parcel)
		}

		override fun newArray(size: Int): Array<Forecast?> {
			return arrayOfNulls(size)
		}
	}
}


