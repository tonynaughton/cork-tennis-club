package ie.wit.tennisapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class ResultModel(
    var id: Long = 0,
    var playerOne: String = "",
    var playerTwo: String = "",
    var p1Score: Int = 0,
    var p2Score: Int = 0,
    var date: String = "--/--/----"
) : Parcelable