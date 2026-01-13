package com.warehouseinhand.slug.domain.court

data class CourtSalesSummary(
    val id: Long,
    val buildingName: String,
    val address: String,

    /** 서버는 enum으로 줌(안정적). UI에서는 라벨로 변환 */
    val categories: List<SalesCategory>,

    /** 서버 원문 유지(파싱은 UseCase/Mapper에서) */
    val salesDateTime: String,

    val appraisalPrice: Long,

    /** 썸네일(없을 수도 있음) */
    val salesPicture: String?,

    /** 유찰 횟수 */
    val failBidCount: Int,

    /** 찜 수 */
    val zzimCount: Int,

    val soldOut: Boolean,

    /** "내가 찜했는지"는 보통 서버/로컬 조합이라 Domain에서 optional로 둠 */
    val isFavorite: Boolean? = null
)