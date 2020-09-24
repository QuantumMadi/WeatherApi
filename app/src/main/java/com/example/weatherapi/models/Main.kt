package com.example.weatherapi.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Main(

	@field:SerializedName("temp")
	val temp: Double? = null,

	@field:SerializedName("temp_min")
	val tempMin: Double? = null,

	@field:SerializedName("grnd_level")
	val grndLevel: Double? = null,

	@field:SerializedName("humidity")
	val humidity: Double? = null,

	@field:SerializedName("pressure")
	val pressure: Double? = null,

	@field:SerializedName("sea_level")
	val seaLevel: Double? = null,

	@field:SerializedName("feels_like")
	val feelsLike: Double? = null,

	@field:SerializedName("temp_max")
	val tempMax: Double? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readValue(Double::class.java.classLoader) as? Double
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(temp)
		parcel.writeValue(tempMin)
		parcel.writeValue(grndLevel)
		parcel.writeValue(humidity)
		parcel.writeValue(pressure)
		parcel.writeValue(seaLevel)
		parcel.writeValue(feelsLike)
		parcel.writeValue(tempMax)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Main> {
		override fun createFromParcel(parcel: Parcel): Main {
			return Main(parcel)
		}

		override fun newArray(size: Int): Array<Main?> {
			return arrayOfNulls(size)
		}
	}
}