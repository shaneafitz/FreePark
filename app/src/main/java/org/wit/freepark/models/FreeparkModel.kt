package org.wit.freepark.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FreeparkModel(var id: Long = 0,
                         var location: String = "",
                         var description: String = "") : Parcelable
