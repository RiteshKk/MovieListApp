package ritesh.movieslistapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import ritesh.movieslistapp.domain.model.Content

@Serializable
data class ContentDto(
    val name: String?,
    @SerializedName("poster-image")
    val posterImage: String?
)

fun ContentDto.toContent(): Content {
    return Content(
        name = this.name ?: "",
        posterImage = this.posterImage ?: ""
    )
}