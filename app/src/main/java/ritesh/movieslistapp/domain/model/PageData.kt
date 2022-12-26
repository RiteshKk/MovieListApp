package ritesh.movieslistapp.domain.model

data class PageData(
    val title: String,
    val totalContentItems: String,
    val content: List<Content>
)
