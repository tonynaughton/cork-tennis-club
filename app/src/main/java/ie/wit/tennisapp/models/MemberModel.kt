package ie.wit.tennisapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemberModel(
    var id: Long = 0,
    var email: String = "",
    var password: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var dob: String = "",
    var experience: String = "",
    var image: Uri = Uri.EMPTY
) : Parcelable