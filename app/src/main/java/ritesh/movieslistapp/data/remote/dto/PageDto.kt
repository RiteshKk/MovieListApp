package ritesh.movieslistapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import ritesh.movieslistapp.domain.model.PageData

@Serializable
data class PageDto(
    @SerializedName("content-items")
    val contentItems: ContentItemsDto?,
    @SerializedName("page-num")
    val pageNum: String?,
    @SerializedName("page-size")
    val pageSize: String?,
    val title: String?,
    @SerializedName("total-content-items")
    val totalContentItems: String?
)

fun PageDto.toPageData(): PageData{
    return PageData(
        title = this.title ?: "",
        totalContentItems = this.totalContentItems ?: "",
        content = this.contentItems?.content?.map { it.toContent() } ?: emptyList()
    )
}