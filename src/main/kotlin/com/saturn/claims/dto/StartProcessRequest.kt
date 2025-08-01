package com.saturn.claims.dto

data class StartProcessRequest(
    val claimId: String,
    val claimAmount: Double,
    val claimType: String,
    val customerName: String,
    val additionalVariables: Map<String, Any> = emptyMap()
)