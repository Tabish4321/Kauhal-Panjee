package com.kaushalpanjee.common.model.response


data class BlockResponse(
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String,
    val blockList: MutableList<BlockList>
)

data class BlockList(
    val blockName: String,
    val blockCode: String,
    val lgdBlockCode:String,
)