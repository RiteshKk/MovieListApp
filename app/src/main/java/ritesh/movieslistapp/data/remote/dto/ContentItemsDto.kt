package ritesh.movieslistapp.data.remote.dto
import kotlinx.serialization.Serializable

@Serializable
data class ContentItemsDto(
    val content: List<ContentDto>
)