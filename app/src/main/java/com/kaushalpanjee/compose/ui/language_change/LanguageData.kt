package com.kaushalpanjee.compose.ui.language_change

import androidx.compose.ui.graphics.Color
import com.kaushalpanjee.compose.ui.model.LanguageUiModel

/**
 * Created by Rishi Porwal
 */
object LanguageData {
    val languages = listOf(
        LanguageUiModel("en", "English", "A", Color(0xFFC9ECFF)),
        LanguageUiModel("hi", "हिंदी", "अ", Color(0xFFFAD6C1)),
        LanguageUiModel("ta", "தமிழ்", "ஆ", Color(0xFFA8EFAB)),
        LanguageUiModel("bn", "বাংলা", "আ", Color(0xFFFAF4D6)),
        LanguageUiModel("gu", "ગુજરાતી", "અ", Color(0xFFFADADD)),
        LanguageUiModel("kn", "ಕನ್ನಡ", "ಅ", Color(0xFFD6FADC)),
        LanguageUiModel("ml", "മലയാളം", "അ", Color(0xFFE9D6FA)),
        LanguageUiModel("or", "ଓଡ଼ିଆ", "ଅ", Color(0xFFFAE6C1)),
        LanguageUiModel("mr", "मराठी", "अ", Color(0xFFD1F5FA)),
        LanguageUiModel("pa", "ਪੰਜਾਬੀ", "ਅ", Color(0xFFFCE4EC)),
        LanguageUiModel("te", "తెలుగు", "అ", Color(0xFFE1FAE3)),
        LanguageUiModel("ur", "اردو", "ا", Color(0xFFD6F0FA))
    )
}
