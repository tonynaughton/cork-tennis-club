package ie.wit.tennisapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemberModel(
    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var age: String = "",
    var image: Uri = Uri.EMPTY
) : Parcelable