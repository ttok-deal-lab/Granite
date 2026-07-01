package com.estateslug.slug.detail.subpage.auction

data class AuctionInfoUiModel(
    val checkCardList: List<AuctionCardUiModel>,
    val auctionHistoryUiModel: AuctionHistoryUiModel,
    val courtInfoUiModel: CourtInfoUiModel,
    val registryInfoUiModel: RegistryInfoUiModel,
    val courtDetailInfo: String,
) {
    companion object {

        val preview: AuctionInfoUiModel =
            AuctionInfoUiModel(
                checkCardList = AuctionCardUiModel.preview,
                auctionHistoryUiModel = AuctionHistoryUiModel.preview,
                courtInfoUiModel = CourtInfoUiModel.preview,
                registryInfoUiModel = RegistryInfoUiModel.preview,
                courtDetailInfo =
                    "1동의 건물의 표시\n" +
                    "서울특별시 서초구 서초동 1557-8\n" +
                    "주건축물\n" +
                    "[도로명주소]\n" +
                    "서울특별시 서초구 반포대로 26길 35\n" +
                    "철근콘크리트구조 철근콘크리트지붕\n" +
                    "6층 공동주택\n" +
                    "지하 2층 199.52㎡\n" +
                    "지하 1층 209.17㎡\n" +
                    "1층 44.34㎡\n" +
                    "1층 38.03㎡\n" +
                    "1층 16.66㎡\n" +
                    "1층 9.06㎡\n" +
                    "2층 202.04㎡\n" +
                    "3층 164.06㎡\n" +
                    "4층 162.48㎡\n" +
                    "5층 139.15㎡\n" +
                    "6층 129.96㎡\n" +
                    "\n" +
                    "전유부분의 건물의 표시\n"
            )
    }
}