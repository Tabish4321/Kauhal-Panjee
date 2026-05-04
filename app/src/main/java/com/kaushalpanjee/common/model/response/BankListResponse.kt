package com.kaushalpanjee.common.model.response

import com.kaushalpanjee.common.model.BankItem

/**
 * Created by Rishi Porwal
 */
data class BankListResponse(val data: List<BankItem>,
                            val responseCode: Int,
                            val responseDesc: String)
