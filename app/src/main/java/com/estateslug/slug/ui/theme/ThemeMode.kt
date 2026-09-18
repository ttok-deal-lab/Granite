package com.estateslug.slug.ui.theme

/**
 * 사용자가 설정에서 고르는 화면 테마. 기본값은 [SYSTEM](기기 설정을 따름).
 * 저장은 [storedName]으로 하고, 알 수 없는 값은 [DEFAULT]로 읽는다.
 */
enum class ThemeMode(val storedName: String, val displayName: String) {
    LIGHT("light", "라이트"),
    DARK("dark", "다크"),
    SYSTEM("system", "시스템");

    companion object {
        val DEFAULT = SYSTEM

        fun fromStoredName(name: String?): ThemeMode =
            entries.firstOrNull { it.storedName == name } ?: DEFAULT
    }
}
