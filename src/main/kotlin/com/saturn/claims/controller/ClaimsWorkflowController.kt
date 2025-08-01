package com.saturn.claims.controller

import com.saturn.claims.dto.StartProcessRequest
import com.saturn.claims.service.workflow.ClaimsWorkflowService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/workflow")
class ClaimsWorkflowController(
    private val claimsWorkflowService: ClaimsWorkflowService
) {

    @PostMapping("/start")
    fun startProcess(@RequestBody request: StartProcessRequest): ResponseEntity<Map<String, String>> {
        val variables = mutableMapOf<String, Any>(
            "claimAmount" to request.claimAmount,
            "claimType" to request.claimType,
            "customerName" to request.customerName,
            "assessor" to request.assessor
        )
        variables.putAll(request.additionalVariables)
        
        val processInstanceId = claimsWorkflowService.startClaimProcess(
            claimId = request.claimId,
            variables = variables
        )
        
        return ResponseEntity.ok(
            mapOf(
                "processInstanceId" to processInstanceId,
                "claimId" to request.claimId,
                "status" to "started"
            )
        )
    }
    
    @GetMapping("/instance/{processInstanceId}")
    fun getProcessInstance(@PathVariable processInstanceId: String): ResponseEntity<Map<String, Any?>> {
        val processInstance = claimsWorkflowService.getProcessInstance(processInstanceId)
            ?: return ResponseEntity.notFound().build()
        
        return ResponseEntity.ok(
            mapOf(
                "processInstanceId" to processInstance.id,
                "businessKey" to processInstance.businessKey,
                "processDefinitionKey" to processInstance.processDefinitionKey,
                "activityId" to processInstance.activityId,
                "isEnded" to processInstance.isEnded,
                "isSuspended" to processInstance.isSuspended
            )
        )
    }
    
    @GetMapping("/instances")
    fun getAllProcessInstances(): ResponseEntity<List<Map<String, Any?>>> {
        val instances = claimsWorkflowService.getAllProcessInstances()
        
        val response = instances.map { instance ->
            mapOf(
                "processInstanceId" to instance.id,
                "businessKey" to instance.businessKey,
                "processDefinitionKey" to instance.processDefinitionKey,
                "activityId" to instance.activityId,
                "isEnded" to instance.isEnded,
                "isSuspended" to instance.isSuspended,
                "startTime" to instance.startTime
            )
        }
        
        return ResponseEntity.ok(response)
    }
    
    @GetMapping("/instances/active")
    fun getActiveProcessInstances(): ResponseEntity<List<Map<String, Any?>>> {
        val instances = claimsWorkflowService.getActiveProcessInstances()
        
        val response = instances.map { instance ->
            mapOf(
                "processInstanceId" to instance.id,
                "businessKey" to instance.businessKey,
                "processDefinitionKey" to instance.processDefinitionKey,
                "activityId" to instance.activityId,
                "startTime" to instance.startTime
            )
        }
        
        return ResponseEntity.ok(response)
    }
}