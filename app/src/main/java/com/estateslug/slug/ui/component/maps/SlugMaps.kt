package com.estateslug.slug.ui.component.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.estateslug.slug.R

@Composable
fun SlugMap(
    modifier: Modifier = Modifier,
    latitude: Double,
    longitude: Double,
) {
    KakaoMap(modifier = modifier, latitude = latitude,longitude = longitude)
}
@Composable
private fun KakaoMap(
    modifier: Modifier = Modifier,
    latitude: Double,
    longitude: Double,
) {
    val context = LocalContext.current
    val mapView: MapView = remember { MapView(context) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            mapView.apply {
                mapView.start(
                    object : MapLifeCycleCallback() {

                        override fun onMapDestroy() {
                            //TODO : callback 적용
                        }

                        // 지도 생명 주기 콜백: 지도 로딩 중 에러가 발생했을 때 호출
                        override fun onMapError(exception: Exception?) {
                            // TODO : error callback 처리
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        // KakaoMap이 준비되었을 때 호출
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                                LatLng.from(
                                    latitude,
                                    longitude
                                )
                            )
                            // 지도에 표시할 라벨의 스타일 설정
//
                            val style = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(
                                LabelStyle.from(R.drawable.ic_map_maker)))

                            val options = LabelOptions.from(LatLng.from(latitude, longitude))
                                .setStyles(style)
//
//                            // KakaoMap의 labelManager에서 레이어를 가져옴
                            val layer = kakaoMap.labelManager?.layer
////
////                            // 카메라를 지정된 위치로 이동
                            kakaoMap.moveCamera(cameraUpdate)
////
////                            // 지도에 라벨을 추가
                            layer?.addLabel(options)
                        }

                        // 현재 위치 반환
                        override fun getPosition(): LatLng {
                            return LatLng.from(latitude, longitude)
                        }
                    },
                )
            }
        },
    )
}
