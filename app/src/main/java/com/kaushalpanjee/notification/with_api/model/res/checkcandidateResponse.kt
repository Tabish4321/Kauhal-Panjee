package com.kaushalpanjee.notification.with_api.model.res

data class checkcandidateResponse(
    val content: List<CheckCandidate>?,
    val status: String?,
    val message: String?,
    val showDialog: Boolean?
)
//{
//    "status": "SUCCESS",
//    "message": "Invitation Found but Batch Not Completed",
//    "showDialog": true,
//    "showHappyUnhappy": false,
//    "content": [
//    {
//        "batchcompletiondate": "2025-11-14",
//        "login_id": "2523464946",
//        "start_date": "2025-11-05"
//    }
//    ]
//}

