package com.kaushalpanjee.compose.data.mapper

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.core.content.res.TypedArrayUtils.getResourceId
import com.kaushalpanjee.R
import com.kaushalpanjee.compose.domain.model.Scheme
import javax.inject.Inject

/**
 * Created by Rishi Porwal
 */
class AboutUnnatiUiMapper @Inject constructor(
    private val context: Context
) {

    fun buildSchemes(
        ddugky: String,
        rseti: String,
        pmkvy: String
    ): List<Scheme> {

        return listOf(
            Scheme(
                getString(context, R.string.ddugky_title),
                ddugky,
                listOf(
                    getString(context, R.string.ddugky_1),
                    getString(context, R.string.ddugky_2),
                    getString(context, R.string.ddugky_3)
                )
            ),
            Scheme(
                getString(context, R.string.rseti_title),
                rseti,
                listOf(
                    getString(context, R.string.rseti_1),
                    getString(context, R.string.rseti_2),
                    getString(context, R.string.rseti_3)
                )
            ),
            Scheme(
                getString(context, R.string.pmkvy_),
                pmkvy,
                (1..10).map {
                    context.getString(
                        context.resources.getIdentifier(
                            "pmkvy_$it",
                            "string",
                            context.packageName
                        )
                    )
                }
            )
        )
    }
}
