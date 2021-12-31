package ie.wit.tennisapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MemberModel(
    var id: Long = 0,
    var uuid: String = "",
    var email: String = "",
    var password: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var dateOfBirth: String = "",
    var experience: String = "",
    var image: Uri = Uri.EMPTY
) : Parcelable