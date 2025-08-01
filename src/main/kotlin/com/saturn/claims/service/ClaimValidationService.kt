package com.saturn.claims.service

import org.flowable.engine.delegate.DelegateExecution
import org.flowable.engine.delegate.JavaDelegate
import org.springframework.stereotype.Service

@Service
class ClaimValidationService : JavaDelegate {
    
    override fun execute(execution: DelegateExecution) {
        val claimId = execution.getVariable("claimId") as? String
        val claimAmount = execution.getVariable("claimAmount") as? Double
        val claimType = execution.getVariable("claimType") as? String
        
        println("Validating claim: $claimId")
        
        val isValid = validateClaim(claimId, claimAmount, claimType)
        
        execution.setVariable("claimValid", isValid)
        
        if (isValid) {
            println("Claim $claimId is valid")
        } else {
            println("Claim $claimId is invalid")
        }
    }
    
    private fun validateClaim(claimId: String?, claimAmount: Double?, claimType: String?): Boolean {
        if (claimId.isNullOrBlank()) return false
        if (claimAmount == null || claimAmount <= 0) return false
        if (claimType.isNullOrBlank()) return false
        
        // Additional validation logic
        return when (claimType.lowercase()) {
            "health", "auto", "property", "life" -> claimAmount <= 100000.0
            else -> false
        }
    }
}