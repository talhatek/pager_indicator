package com.tek.pagerindicator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RangeChanged(val startIndex: Int,val endIndex:Int) : Parcelable
