package com.kaushalpanjee.cbt

import androidx.compose.ui.unit.*

import androidx.compose.runtime.staticCompositionLocalOf

data class AppDimens(
    val paddingSmall: Dp,
    val paddingMedium: Dp,
    val paddingLarge: Dp,

    val radiusSmall: Dp,
    val radiusMedium: Dp,

    val textSmall: TextUnit,
    val textMedium: TextUnit,
    val textLarge: TextUnit,

    val topBarHeight: Dp,
    val progressHeight: Dp
)

// ✅ VALUES
val SmallDimens = AppDimens(
    4.dp, 8.dp, 16.dp,
    6.dp, 10.dp,
    10.sp, 14.sp, 18.sp,
    50.dp, 4.dp
)

val MediumDimens = AppDimens(
    6.dp, 12.dp, 20.dp,
    8.dp, 12.dp,
    12.sp, 16.sp, 20.sp,
    60.dp, 5.dp
)

val LargeDimens = AppDimens(
    8.dp, 16.dp, 24.dp,
    10.dp, 16.dp,
    14.sp, 18.sp, 22.sp,
    70.dp, 6.dp
)

// ✅ LOCAL
val LocalDimens = staticCompositionLocalOf { MediumDimens }




























//data class AppDimens(
//    val paddingSmall: Dp,
//    val paddingMedium: Dp,
//    val paddingLarge: Dp,
//
//    val radiusSmall: Dp,
//    val radiusMedium: Dp,
//
//    val textSmall: TextUnit,
//    val textMedium: TextUnit,
//    val textLarge: TextUnit,
//
//    val topBarHeight: Dp,
//    val progressHeight: Dp
//)
//
//// ✅ VALUES
//val SmallDimens = AppDimens(
//    4.dp, 8.dp, 16.dp,
//    6.dp, 10.dp,
//    10.sp, 14.sp, 18.sp,
//    50.dp, 4.dp
//)
//
//val MediumDimens = AppDimens(
//    6.dp, 12.dp, 20.dp,
//    8.dp, 12.dp,
//    12.sp, 16.sp, 20.sp,
//    60.dp, 5.dp
//)
//
//val LargeDimens = AppDimens(
//    8.dp, 16.dp, 24.dp,
//    10.dp, 16.dp,
//    14.sp, 18.sp, 22.sp,
//    70.dp, 6.dp
//)
//
//// ✅ LOCAL PROVIDER
//val LocalDimens = staticCompositionLocalOf { SmallDimens }

















//data class AppDimens(
//    val paddingSmall: Dp,
//    val paddingMedium: Dp,
//    val paddingLarge: Dp,
//    val buttonHeight: Dp
//)
//
//val CompactSmallDimens = AppDimens(6.dp, 12.dp, 16.dp, 42.dp)
//val CompactDimens = AppDimens(8.dp, 16.dp, 20.dp, 48.dp)
//val MediumDimens = AppDimens(12.dp, 20.dp, 26.dp, 56.dp)
//val ExpandedDimens = AppDimens(16.dp, 24.dp, 32.dp, 64.dp)
//
//val LocalAppDimens = staticCompositionLocalOf { CompactDimens }
//
//@Composable
//fun ProvideDimens(dimens: AppDimens, content: @Composable () -> Unit) {
//    CompositionLocalProvider(LocalAppDimens provides dimens) {
//        content()
//    }
//}

























//data class AppDimens(
//    val paddingSmall: Dp,
//    val paddingMedium: Dp,
//    val paddingLarge: Dp,
//    val buttonHeight: Dp
//)
//
//val CompactSmallDimens = AppDimens(6.dp, 12.dp, 16.dp, 42.dp)
//val CompactDimens = AppDimens(8.dp, 16.dp, 20.dp, 48.dp)
//val MediumDimens = AppDimens(10.dp, 20.dp, 24.dp, 54.dp)
//val ExpandedDimens = AppDimens(12.dp, 24.dp, 30.dp, 60.dp)
//
//val LocalAppDimens = staticCompositionLocalOf { CompactDimens }
//
//@Composable
//fun ProvideDimens(
//    dimens: AppDimens,
//    content: @Composable () -> Unit
//) {
//    CompositionLocalProvider(LocalAppDimens provides dimens) {
//        content()
//    }
//}






















//data class AppDimens(
//    val paddingSmall: Dp,
//    val paddingMedium: Dp,
//    val paddingLarge: Dp,
//    val buttonHeight: Dp
//)
//
//val CompactSmallDimens = AppDimens(
//    paddingSmall = 6.dp,
//    paddingMedium = 12.dp,
//    paddingLarge = 16.dp,
//    buttonHeight = 42.dp
//)
//
//val CompactDimens = AppDimens(
//    paddingSmall = 8.dp,
//    paddingMedium = 16.dp,
//    paddingLarge = 20.dp,
//    buttonHeight = 48.dp
//)
//
//val MediumDimens = AppDimens(
//    paddingSmall = 10.dp,
//    paddingMedium = 20.dp,
//    paddingLarge = 24.dp,
//    buttonHeight = 54.dp
//)
//
//val ExpandedDimens = AppDimens(
//    paddingSmall = 12.dp,
//    paddingMedium = 24.dp,
//    paddingLarge = 30.dp,
//    buttonHeight = 60.dp
//)
//
//val LocalAppDimens = staticCompositionLocalOf {
//    CompactDimens
//}
//
//@Composable
//fun AppUtils(
//    appDimens: AppDimens,
//    content: @Composable () -> Unit
//) {
//    CompositionLocalProvider(
//        LocalAppDimens provides appDimens,
//        content = content
//    )
//}
