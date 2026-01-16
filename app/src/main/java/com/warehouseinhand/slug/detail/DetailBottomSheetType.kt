package com.warehouseinhand.slug.detail

sealed interface DetailBottomSheetType {

    sealed interface InfoSheetType : DetailBottomSheetType {
        val info: InfoSheetData
        val subInfo: List<InfoSheetData>
            get() = listOf()

        //경매 구분
        object TypeOfAuction : InfoSheetType {
            override val info: InfoSheetData =
                InfoSheetData(
                    name = "경매 구분",
                    description = "해당 부동산이 어떤 사유로 경매에 나왔는지를 나타내는 항목이에요."
                )
            override val subInfo: List<InfoSheetData> = listOf(
                InfoSheetData(
                    name = "강제 경매",
                    description = "채무자가 빚을 갚지 않아 채권자가 법원에 강제로 경매를 신청한 경우로, 권리관계가 복잡한 경우가 많아 주의가 필요해요.",
                    isCritical = true
                ),
                InfoSheetData(
                    name = "임의 경매",
                    description = "담보(예: 근저당) 계약에 따라, 채권자가 법원에 신청하는 경매에요. 계약 관계가 명확한 경우가 많아, 권리분석이 비교적 단순하고 안정적입니다.",
                ),
            )
        }

        //대지권
        object RightToUseSite : InfoSheetType {
            override val info: InfoSheetData =
                InfoSheetData(
                    name = "대지권",
                    description = "건물이 지어진 땅(토지)에 대한 권리를 말해요. 대지권이 없으면, 건물이 있어도 땅을 쓸 권리가 없어 경매 낙찰받아도 분쟁 소지가 생겨요."
                )
        }

        //지상권
        object Superficies : InfoSheetType {
            override val info: InfoSheetData =
                InfoSheetData(
                    name = "지상권",
                    description = "다른 사람 땅 위에 건물이나 구조물을 지을 수 있는 권리예요. 토지는 내 것이 아니지만, 사용할 수 있는 법적 권리가 생겨요. 지상권은 토지 사용권인데, 이 권리가 다른 사람에게 있으면 낙찰 후에도 땅을 사용하기 어려울 수 있어요."
                )
        }

        //임차인
        object Lessee : InfoSheetType {
            override val info: InfoSheetData =
                InfoSheetData(
                    name = "임차인",
                    description = "부동산을 계약하고 일정 기간 사용하거나 거주하는 사람을 말해요."
                )
            override val subInfo: List<InfoSheetData> = listOf(
                InfoSheetData(
                    name = "대항력 있는 임차인",
                    description = "해당 집에 계속 살 수 있는 권리예요. 전입신고 + 실제 거주 중이라면, 낙찰자가 바뀌어도 퇴거 요구를 막을 수 있어요.",

                    ),
                InfoSheetData(
                    name = "우선 변제권",
                    description = "집이 경매로 팔릴 때, 임차인이 보증금을 먼저 돌려받을 수 있는 권리예요. 전입신고 + 확정일자를 받았다면, 다른 채권자보다 먼저 보증금을 배당받을 수 있어요.",
                ),
            )
        }

        //위반 건축물
        object IllegalConstruction : InfoSheetType {
            override val info: InfoSheetData =
                InfoSheetData(
                    name = "위반 건축물",
                    description = "건축법 등을 위반해서 지어진 건물이에요. 허가 없이 증축했거나, 용도나 구조가 법 기준에 맞지 않는 경우예요."
                )
        }

        //채권자
        object Creditor : InfoSheetType {
            override val info: InfoSheetData =
                InfoSheetData(
                    name = "채권자",
                    description = "돈을 빌려준 사람 또는 기관이에요. 채무자가 돈을 갚지 않으면, 경매를 신청해서 돈을 돌려받으려 해요."
                )

        }
//        //채권자 TODO: 보류
//        data class SaleEffect(override val subInfo: List<InfoSheetData>): InfoSheetType{
//            override val info: InfoSheetData =
//                InfoSheetData(
//                    name = "매각효력",
//                    description = ""
//                )
//        }
    }

    object Share : DetailBottomSheetType

    object EMPTY : DetailBottomSheetType
}

data class InfoSheetData(val name: String, val description: String, val isCritical: Boolean = false)