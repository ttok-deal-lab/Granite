package com.estateslug.slug.home.bottomsheet.location

import com.estateslug.slug.data.network.search.Region
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.ALL
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.BUSAN
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.CHUNGBUK
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.CHUNGNAM
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.DAEGU
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.DAEJEON
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.GANGWON
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.GWANGJU
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.GYEONGBUK
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.GYEONGGI
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.GYEONGNAM
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.INCHEON
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.JEJU
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.JEONBUK
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.JEONNAM
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.SEJONG
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.SEOUL
import com.estateslug.slug.home.bottomsheet.location.Location.LocationSub.ULSAN



sealed interface Location {
    fun getLocationString(): String
    enum class LocationMain(
        val displayName: String,
        val shortDisplayName: String
    ) : Location {
        ALL("전국", "전국"),

        // 특별시
        SEOUL("서울특별시", "서울"),

        // 광역시
        BUSAN("부산광역시", "부산"),
        DAEGU("대구광역시", "대구"),
        INCHEON("인천광역시", "인천"),
        GWANGJU("광주광역시", "광주"),
        DAEJEON("대전광역시", "대전"),
        ULSAN("울산광역시", "울산"),

        // 특별자치시
        SEJONG("세종특별자치시", "세종"),

        // 도
        GYEONGGI("경기도", "경기"),
        GANGWON("강원특별자치도", "강원"),
        CHUNGBUK("충청북도", "충북"),
        CHUNGNAM("충청남도", "충남"),
        JEONBUK("전북특별자치도", "전북"),
        JEONNAM("전라남도", "전남"),
        GYEONGBUK("경상북도", "경북"),
        GYEONGNAM("경상남도", "경남"),

        // 특별자치도
        JEJU("제주특별자치도", "제주"),
        ;

        override fun getLocationString() = displayName

        // 짧은 이름을 가져오는 추가 메서드
        fun getShortLocationString() = shortDisplayName
    }

    sealed interface LocationSub : Location {
        
        fun toDistrict():String
        enum class SEOUL(val displayName: String) : LocationSub {
            ALL("전체"),
            JONGNO("종로구"),
            JUNG("중구"),
            YONGSAN("용산구"),
            SEONGDONG("성동구"),
            GWANGJIN("광진구"),
            DONGDAEMUN("동대문구"),
            JUNGNANG("중랑구"),
            SEONGBUK("성북구"),
            GANGBUK("강북구"),
            DOBONG("도봉구"),
            NOWON("노원구"),
            EUNPYEONG("은평구"),
            SEODAEMUN("서대문구"),
            MAPO("마포구"),
            YANGCHEON("양천구"),
            GANGSEO("강서구"),
            GURO("구로구"),
            GEUMCHEON("금천구"),
            YEONGDEUNGPO("영등포구"),
            DONGJAK("동작구"),
            GWANAK("관악구"),
            SEOCHO("서초구"),
            GANGNAM("강남구"),
            SONGPA("송파구"),
            GANGDONG("강동구"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class BUSAN(val displayName: String) : LocationSub {
            ALL("전체"),
            JUNG("중구"),
            SEO("서구"),
            DONG("동구"),
            YEONGDO("영도구"),
            BUSANJIN("부산진구"),
            DONGNAE("동래구"),
            NAM("남구"),
            BUK("북구"),
            HAEUNDAE("해운대구"),
            SAHA("사하구"),
            GEUMJEONG("금정구"),
            GANGSEO("강서구"),
            YEONJE("연제구"),
            SUYEONG("수영구"),
            SASANG("사상구"),
            GIJANG("기장군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class DAEGU(val displayName: String) : LocationSub {
            ALL("전체"),
            JUNG("중구"),
            DONG("동구"),
            SEO("서구"),
            NAM("남구"),
            BUK("북구"),
            SUSEONG("수성구"),
            DALSEO("달서구"),
            DALSEONG("달성군"),
            GUNWI("군위군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class INCHEON(val displayName: String) : LocationSub {
            ALL("전체"),
            JUNG("중구"),
            DONG("동구"),
            MICHUHOL("미추홀구"),
            YEONSU("연수구"),
            NAMDONG("남동구"),
            BUPYEONG("부평구"),
            GYEYANG("계양구"),
            SEO("서구"),
            GANGHWA("강화군"),
            ONGJIN("옹진군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class GWANGJU(val displayName: String) : LocationSub {
            ALL("전체"),
            DONG("동구"),
            SEO("서구"),
            NAM("남구"),
            BUK("북구"),
            GWANGSAN("광산구"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class DAEJEON(val displayName: String) : LocationSub {
            ALL("전체"),
            DONG("동구"),
            JUNG("중구"),
            SEO("서구"),
            YUSEONG("유성구"),
            DAEDEOK("대덕구"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class ULSAN(val displayName: String) : LocationSub {
            ALL("전체"),
            JUNG("중구"),
            NAM("남구"),
            DONG("동구"),
            BUK("북구"),
            ULJU("울주군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class SEJONG(val displayName: String) : LocationSub {
            ALL("전체"),
            SEJONG_CITY("세종특별자치시"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class GYEONGGI(val displayName: String) : LocationSub {
            ALL("전체"),
            SUWON("수원시"),
            SEONGNAM("성남시"),
            GOYANG("고양시"),
            YONGIN("용인시"),
            BUCHEON("부천시"),
            ANSAN("안산시"),
            ANYANG("안양시"),
            NAMYANGJU("남양주시"),
            HWASEONG("화성시"),
            PYEONGTAEK("평택시"),
            UIJEONGBU("의정부시"),
            SIHEUNG("시흥시"),
            PAJU("파주시"),
            GWANGMYEONG("광명시"),
            GIMPO("김포시"),
            GUNPO("군포시"),
            GWANGJU("광주시"),
            ICHEON("이천시"),
            YANGJU("양주시"),
            OSAN("오산시"),
            GURI("구리시"),
            ANSEONG("안성시"),
            POCHEON("포천시"),
            UIWANG("의왕시"),
            HANAM("하남시"),
            YEOJU("여주시"),
            YANGPYEONG("양평군"),
            DONGDUCHEON("동두천시"),
            GWACHEON("과천시"),
            GAPYEONG("가평군"),
            YEONCHEON("연천군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class GANGWON(val displayName: String) : LocationSub {
            ALL("전체"),
            CHUNCHEON("춘천시"),
            WONJU("원주시"),
            GANGNEUNG("강릉시"),
            DONGHAE("동해시"),
            TAEBAEK("태백시"),
            SOKCHO("속초시"),
            SAMCHEOK("삼척시"),
            HONGCHEON("홍천군"),
            HOENGSEONG("횡성군"),
            YEONGWOL("영월군"),
            PYEONGCHANG("평창군"),
            JEONGSEON("정선군"),
            CHEORWON("철원군"),
            HWACHEON("화천군"),
            YANGGU("양구군"),
            INJE("인제군"),
            GOSEONG("고성군"),
            YANGYANG("양양군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class CHUNGBUK(val displayName: String) : LocationSub {
            ALL("전체"),
            CHEONGJU("청주시"),
            CHUNGJU("충주시"),
            JECHEON("제천시"),
            BOEUN("보은군"),
            OKCHEON("옥천군"),
            YEONGDONG("영동군"),
            JEUNGPYEONG("증평군"),
            JINCHEON("진천군"),
            GOESAN("괴산군"),
            EUMSEONG("음성군"),
            DANYANG("단양군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class CHUNGNAM(val displayName: String) : LocationSub {
            ALL("전체"),
            CHEONAN("천안시"),
            GONGJU("공주시"),
            BORYEONG("보령시"),
            ASAN("아산시"),
            SEOSAN("서산시"),
            NONSAN("논산시"),
            GYERYONG("계룡시"),
            DANGJIN("당진시"),
            GEUMSAN("금산군"),
            BUYEO("부여군"),
            SEOCHEON("서천군"),
            CHEONGYANG("청양군"),
            HONGSEONG("홍성군"),
            YESAN("예산군"),
            TAEAN("태안군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class JEONBUK(val displayName: String) : LocationSub {
            ALL("전체"),
            JEONJU("전주시"),
            GUNSAN("군산시"),
            IKSAN("익산시"),
            JEONGEUP("정읍시"),
            NAMWON("남원시"),
            GIMJE("김제시"),
            WANJU("완주군"),
            JINAN("진안군"),
            MUJU("무주군"),
            JANGSU("장수군"),
            IMSIL("임실군"),
            SUNCHANG("순창군"),
            GOCHANG("고창군"),
            BUAN("부안군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class JEONNAM(val displayName: String) : LocationSub {
            ALL("전체"),
            MOKPO("목포시"),
            YEOSU("여수시"),
            SUNCHEON("순천시"),
            NAJU("나주시"),
            GWANGYANG("광양시"),
            DAMYANG("담양군"),
            GOKSEONG("곡성군"),
            GURYE("구례군"),
            GOHEUNG("고흥군"),
            BOSEONG("보성군"),
            HWASUN("화순군"),
            JANGHEUNG("장흥군"),
            GANGJIN("강진군"),
            HAENAM("해남군"),
            YEONGAM("영암군"),
            MUAN("무안군"),
            HAMPYEONG("함평군"),
            YEONGGWANG("영광군"),
            JANGSEONG("장성군"),
            WANDO("완도군"),
            JINDO("진도군"),
            SINAN("신안군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class GYEONGBUK(val displayName: String) : LocationSub {
            ALL("전체"),
            POHANG("포항시"),
            GYEONGJU("경주시"),
            GIMCHEON("김천시"),
            ANDONG("안동시"),
            GUMI("구미시"),
            YEONGJU("영주시"),
            YEONGCHEON("영천시"),
            SANGJU("상주시"),
            MUNGYEONG("문경시"),
            GYEONGSAN("경산시"),
            UISEONG("의성군"),
            CHEONGSONG("청송군"),
            YEONGYANG("영양군"),
            YEONGDEOK("영덕군"),
            CHEONGDO("청도군"),
            GORYEONG("고령군"),
            SEONGJU("성주군"),
            CHILGOK("칠곡군"),
            YECHEON("예천군"),
            BONGHWA("봉화군"),
            ULJIN("울진군"),
            ULLEUNG("울릉군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class GYEONGNAM(val displayName: String) : LocationSub {
            ALL("전체"),
            CHANGWON("창원시"),
            JINJU("진주시"),
            TONGYEONG("통영시"),
            SACHEON("사천시"),
            GIMHAE("김해시"),
            MIRYANG("밀양시"),
            GEOJE("거제시"),
            YANGSAN("양산시"),
            UIRYEONG("의령군"),
            HAMAN("함안군"),
            CHANGNYEONG("창녕군"),
            GOSEONG("고성군"),
            NAMHAE("남해군"),
            HADONG("하동군"),
            SANCHEONG("산청군"),
            HAMYANG("함양군"),
            GEOCHANG("거창군"),
            HAPCHEON("합천군"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class JEJU(val displayName: String) : LocationSub {
            ALL("전체"),
            JEJU_CITY("제주시"),
            SEOGWIPO("서귀포시"),
            ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }

        enum class ALL(val displayName: String) : LocationSub {
            ALL("전체"), ;

            override fun getLocationString() = displayName
            override fun toDistrict() = name
        }
    }

    companion object {
        fun getSubLocations(main: LocationMain): List<LocationSub> {
            return when (main) {
                LocationMain.SEOUL -> SEOUL.entries
                LocationMain.BUSAN -> BUSAN.entries
                LocationMain.DAEGU -> DAEGU.entries
                LocationMain.INCHEON -> INCHEON.entries
                LocationMain.GWANGJU -> GWANGJU.entries
                LocationMain.DAEJEON -> DAEJEON.entries
                LocationMain.ULSAN -> ULSAN.entries
                LocationMain.SEJONG -> SEJONG.entries
                LocationMain.GYEONGGI -> GYEONGGI.entries
                LocationMain.GANGWON -> GANGWON.entries
                LocationMain.CHUNGBUK -> CHUNGBUK.entries
                LocationMain.CHUNGNAM -> CHUNGNAM.entries
                LocationMain.JEONBUK -> JEONBUK.entries
                LocationMain.JEONNAM -> JEONNAM.entries
                LocationMain.GYEONGBUK -> GYEONGBUK.entries
                LocationMain.GYEONGNAM -> GYEONGNAM.entries
                LocationMain.JEJU -> JEJU.entries
                LocationMain.ALL -> ALL.entries
            }
        }
        fun LocationMain.toRegion(): Region = when (this) {
            LocationMain.SEOUL -> Region.SEOUL
            LocationMain.BUSAN -> Region.BUSAN
            LocationMain.DAEGU -> Region.DAEGU
            LocationMain.INCHEON -> Region.INCHEON
            LocationMain.GWANGJU -> Region.GWANGJU
            LocationMain.DAEJEON -> Region.DAEJEON
            LocationMain.ULSAN -> Region.ULSAN
            LocationMain.SEJONG -> Region.SEJONG
            LocationMain.GYEONGGI -> Region.GYEONGGI
            LocationMain.GANGWON -> Region.GANGWON
            LocationMain.CHUNGBUK -> Region.CHUNGBUK
            LocationMain.CHUNGNAM -> Region.CHUNGNAM
            LocationMain.JEONBUK -> Region.JEONBUK
            LocationMain.JEONNAM -> Region.JEONNAM
            LocationMain.GYEONGBUK -> Region.GYEONGBUK
            LocationMain.GYEONGNAM -> Region.GYEONGNAM
            LocationMain.JEJU -> Region.JEJU
            LocationMain.ALL -> Region.ALL
        }

        fun getSubLocationDefault(main: LocationMain): LocationSub {
            return when (main) {
                LocationMain.SEOUL -> SEOUL.ALL
                LocationMain.BUSAN -> BUSAN.ALL
                LocationMain.DAEGU -> DAEGU.ALL
                LocationMain.INCHEON -> INCHEON.ALL
                LocationMain.GWANGJU -> GWANGJU.ALL
                LocationMain.DAEJEON -> DAEJEON.ALL
                LocationMain.ULSAN -> ULSAN.ALL
                LocationMain.SEJONG -> SEJONG.ALL
                LocationMain.GYEONGGI -> GYEONGGI.ALL
                LocationMain.GANGWON -> GANGWON.ALL
                LocationMain.CHUNGBUK -> CHUNGBUK.ALL
                LocationMain.CHUNGNAM -> CHUNGNAM.ALL
                LocationMain.JEONBUK -> JEONBUK.ALL
                LocationMain.JEONNAM -> JEONNAM.ALL
                LocationMain.GYEONGBUK -> GYEONGBUK.ALL
                LocationMain.GYEONGNAM -> GYEONGNAM.ALL
                LocationMain.JEJU -> JEJU.ALL
                LocationMain.ALL -> ALL.ALL
            }
        }

        // 추가 유틸리티 함수들
        fun getSubLocationNames(main: LocationMain): List<String> {
            return getSubLocations(main).map { it.getLocationString() }
        }

        fun findSubLocation(main: LocationMain, subName: String): LocationSub? {
            return getSubLocations(main).find {
                it.getLocationString() == subName
            }
        }

        fun getMainFromSub(sub: LocationSub): LocationMain? {
            return when (sub) {
                is SEOUL -> LocationMain.SEOUL
                is BUSAN -> LocationMain.BUSAN
                is DAEGU -> LocationMain.DAEGU
                is INCHEON -> LocationMain.INCHEON
                is GWANGJU -> LocationMain.GWANGJU
                is DAEJEON -> LocationMain.DAEJEON
                is ULSAN -> LocationMain.ULSAN
                is SEJONG -> LocationMain.SEJONG
                is GYEONGGI -> LocationMain.GYEONGGI
                is GANGWON -> LocationMain.GANGWON
                is CHUNGBUK -> LocationMain.CHUNGBUK
                is CHUNGNAM -> LocationMain.CHUNGNAM
                is JEONBUK -> LocationMain.JEONBUK
                is JEONNAM -> LocationMain.JEONNAM
                is GYEONGBUK -> LocationMain.GYEONGBUK
                is GYEONGNAM -> LocationMain.GYEONGNAM
                is JEJU -> LocationMain.JEJU
                is ALL -> LocationMain.ALL
            }
        }
    }
}