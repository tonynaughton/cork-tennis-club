package ie.wit.tennisapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchModel(
    var id: Long = 0,
    var playerOne: String = "",
    var playerTwo: String = "",
    var result: String = ""
) : Parcelable