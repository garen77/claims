package com.saturn.claims.service

import org.flowable.engine.delegate.DelegateExecution
import org.flowable.engine.delegate.JavaDelegate
import org.springframework.stereotype.Service

@Service
class ClaimRejectionService : JavaDelegate {
    
    override fun execute(execution: DelegateExecution) {
        val claimId = execution.getVariable("claimId") as? String
        val assessorNotes = execution.getVariable("assessorNotes") as? String
        val rejectionReason = execution.getVariable("rejectionReason") as? String
        
        println("Processing rejected claim: $claimId")
        
        // Process the rejected claim
        val rejectionId = processRejection(claimId, rejectionReason)
        
        // Set variables for audit trail
        execution.setVariable("rejectionId", rejectionId)
        execution.setVariable("claimStatus", "REJECTED")
        execution.setVariable("rejectionDate", System.currentTimeMillis())
        
        println("Claim $claimId rejected with rejection ID: $rejectionId")
        
        // Send notification to claimant
        sendRejectionNotification(claimId, rejectionReason, assessorNotes)
        
        // Log for audit purposes
        logRejection(claimId, rejectionReason, assessorNotes)
    }
    
    private fun processRejection(claimId: String?, rejectionReason: String?): String {
        val rejectionId = "REJ-${System.currentTimeMillis()}"
        
        println("Processing rejection for claim $claimId")
        println("Reason: $rejectionReason")
        println("Rejection ID: $rejectionId")
        
        return rejectionId
    }
    
    private fun sendRejectionNotification(claimId: String?, rejectionReason: String?, assessorNotes: String?) {
        println("Sending rejection notification:")
        println("- Claim ID: $claimId")
        println("- Rejection Reason: $rejectionReason")
        println("- Assessor Notes: $assessorNotes")
        
        // Here you would integrate with email/SMS service
        // For now, just log the notification
    }
    
    private fun logRejection(claimId: String?, rejectionReason: String?, assessorNotes: String?) {
        println("Audit Log - Claim Rejection:")
        println("- Timestamp: ${System.currentTimeMillis()}")
        println("- Claim ID: $claimId")
        println("- Reason: $rejectionReason")
        println("- Notes: $assessorNotes")
        
        // Here you would write to audit log or database
    }
}