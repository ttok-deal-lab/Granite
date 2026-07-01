package com.estateslug.slug.data.network.search

//TODO : 도메인 이동 필요!
enum class Region {
    ALL,
    SEOUL, BUSAN, DAEGU, INCHEON, GWANGJU, DAEJEON, ULSAN, SEJONG,
    GYEONGGI, GANGWON, CHUNGBUK, CHUNGNAM, JEONBUK, JEONNAM,
    GYEONGBUK, GYEONGNAM, JEJU
}

enum class BuildType {
    ALL,
    APARTMENT, VILLA, OFFICETEL, HOUSE, COMMERCIAL, OTHER
}

enum class AuctionFailCount {
    ALL,
    FIRST_AUCTION, SECOND_AUCTION, THIRD_AUCTION, MULTIPLE_AUCTION
}

enum class VerificationStatus {
    ALL,
    VERIFIED, NOT_VERIFIED
}

enum class SoldOutStatus {
    ALL, SOLD_OUT, NOT_SOLD_OUT
}

enum class Sort {
    LATEST_REGISTERED,
    MOST_INTERESTED,
    DEADLINE_SOON,
    LEAST_FAILED,
    PRICE_HIGH,
    PRICE_LOW
}