package com.saturn.claims.service

import org.flowable.engine.delegate.DelegateExecution
import org.flowable.engine.delegate.JavaDelegate
import org.springframework.stereotype.Service

@Service
class ClaimApprovalService : JavaDelegate {
    
    override fun execute(execution: DelegateExecution) {
        val claimId = execution.getVariable("claimId") as? String
        val claimAmount = execution.getVariable("claimAmount") as? Double
        val assessorNotes = execution.getVariable("assessorNotes") as? String
        
        println("Processing approved claim: $claimId")
        
        // Process the approved claim
        val paymentId = processPayment(claimId, claimAmount)
        
        // Set variables for subsequent tasks
        execution.setVariable("paymentId", paymentId)
        execution.setVariable("claimStatus", "APPROVED")
        execution.setVariable("approvalDate", System.currentTimeMillis())
        
        println("Claim $claimId approved with payment ID: $paymentId")
        
        // Send notification to claimant
        sendApprovalNotification(claimId, claimAmount, paymentId)
    }
    
    private fun processPayment(claimId: String?, claimAmount: Double?): String {
        // Simulate payment processing
        val paymentId = "PAY-${System.currentTimeMillis()}"
        
        println("Processing payment of $$claimAmount for claim $claimId")
        println("Payment ID: $paymentId")
        
        // Here you would integrate with payment system
        // For now, just simulate the process
        
        return paymentId
    }
    
    private fun sendApprovalNotification(claimId: String?, claimAmount: Double?, paymentId: String) {
        println("Sending approval notification:")
        println("- Claim ID: $claimId")
        println("- Amount: $$claimAmount")
        println("- Payment ID: $paymentId")
        
        // Here you would integrate with email/SMS service
        // For now, just log the notification
    }
}