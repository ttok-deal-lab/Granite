package com.warehouseinhand.slug.domain.search

data class AuctionSearchItem(
    val id: String,
    val caseNumber: String,

    val buildingName: String?, // search 응답엔 이름이 없어서 nullable  -> TODO 처리해야한다!
    val address: String,

    val salesCategories: List<String>, // raw string 유지 (enum 확장 대비)

    val salesDateTime: String,
    val registerDate: String,

    val appraisalPrice: Long,
    val salesPicture: String,//TODO : 논의 필요 salesPicture 라고 되어 있으나 하나가 아닌 여럿이 내려옴

    val failBidCount: Long,
    val zzimCount: Long,

    val verified: Boolean,
    val soldOut: Boolean,

    // UI/계정 상태 결합용
    val isFavorite: Boolean = false
)