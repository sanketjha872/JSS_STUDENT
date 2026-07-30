package com.jhainusa.jss_student.UserPref

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feedback(
    @SerialName("user_id")
    val userId: String? = null,
    val type: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
