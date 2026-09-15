package com.estateslug.slug.data.network.sales

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class SalesBuildingDTOTest {

    private val gson = Gson()

    @Test
    fun `v2 응답의 latitude longitude를 파싱해 도메인으로 넘긴다`() {
        val json = """
            {
              "siDoAddressName": "서울특별시",
              "guAddressName": "강남구",
              "dongAddressName": "대치동",
              "riAddressName": "string",
              "fullAddressName": "서울특별시 강남구 대치동 933-20",
              "detailAddressName": "4층403호",
              "category": "전용주거",
              "latitude": 37.4979,
              "longitude": 127.0276
            }
        """.trimIndent()

        val domain = gson.fromJson(json, SalesBuildingDTO::class.java).toDomain()

        assertEquals(37.4979, domain.latitude, 0.0)
        assertEquals(127.0276, domain.longitude, 0.0)
        assertEquals("서울특별시 강남구 대치동 933-20", domain.fullAddressName)
    }

    @Test
    fun `좌표 필드가 없는 과거 응답은 0으로 파싱된다`() {
        val json = """
            {
              "siDoAddressName": "서울특별시",
              "guAddressName": "강남구",
              "dongAddressName": "대치동",
              "riAddressName": "",
              "fullAddressName": "서울특별시 강남구 대치동 933-20",
              "detailAddressName": "4층403호",
              "category": "전용주거"
            }
        """.trimIndent()

        val domain = gson.fromJson(json, SalesBuildingDTO::class.java).toDomain()

        assertEquals(0.0, domain.latitude, 0.0)
        assertEquals(0.0, domain.longitude, 0.0)
    }
}
