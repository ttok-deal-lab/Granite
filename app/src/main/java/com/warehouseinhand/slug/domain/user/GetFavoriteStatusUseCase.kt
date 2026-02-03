package com.warehouseinhand.slug.domain.user

import com.warehouseinhand.slug.data.local.user.LocalUserDataRepository
import com.warehouseinhand.slug.data.network.user.RemoteUserDataRepository
import javax.inject.Inject

class GetFavoriteStatusUseCase @Inject constructor(
    private val localUserDataRepository: LocalUserDataRepository,
    private val remoteUserDataRepository: RemoteUserDataRepository
) {

    /**
     * 여러 아이템의 즐겨찾기 상태 조회
     * @param ids 조회할 아이템 ID 리스트
     * @return Result<Map<String, Boolean>> - 성공 시 (itemId to isFavorite) 맵, 실패 시 Error
     */
    suspend operator fun invoke(ids: List<String>): Result<Map<String, Boolean>> = runCatching {
        val userId = localUserDataRepository.getUserId().getOrNull()
            ?: throw IllegalStateException("User not logged in")

        remoteUserDataRepository
            .getFavoriteStatusMap(userId, ids)
            .getOrElse { emptyList() }
            .associate { it.productId to it.isFavorite }
    }

    /**
     * 단일 아이템의 즐겨찾기 상태 조회
     * @param id 조회할 아이템 ID
     * @return Result<Boolean> - 성공 시 즐겨찾기 여부, 실패 시 Error
     */
    suspend operator fun invoke(id: String): Result<Boolean> = runCatching {
        invoke(listOf(id))
            .getOrThrow()
            .getOrDefault(id, false)
    }
}