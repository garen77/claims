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

    @GetMapping("/instance/{processInstanceId}/tasks")
    fun getTasksByProcessInstance(@PathVariable processInstanceId: String): ResponseEntity<Map<String, Any>> {
        val activeTasks = claimsWorkflowService.getActiveTasksByProcessInstance(processInstanceId)
        val completedTasks = claimsWorkflowService.getCompletedTasksByProcessInstance(processInstanceId)
        
        val activeTasksResponse = activeTasks.map { task ->
            mapOf(
                "id" to task.id,
                "name" to task.name,
                "assignee" to task.assignee,
                "createTime" to task.createTime,
                "description" to task.description,
                "priority" to task.priority,
                "status" to "active"
            )
        }
        
        val completedTasksResponse = completedTasks.map { task ->
            mapOf(
                "id" to task.id,
                "name" to task.name,
                "assignee" to task.assignee,
                "createTime" to task.createTime,
                "endTime" to task.endTime,
                "description" to task.description,
                "priority" to task.priority,
                "status" to "completed",
                "duration" to task.durationInMillis
            )
        }
        
        return ResponseEntity.ok(
            mapOf(
                "processInstanceId" to processInstanceId,
                "activeTasks" to activeTasksResponse,
                "completedTasks" to completedTasksResponse,
                "totalActiveTasks" to activeTasks.size,
                "totalCompletedTasks" to completedTasks.size
            )
        )
    }

    @GetMapping("/instance/{processInstanceId}/tasks/active")
    fun getActiveTasksByProcessInstance(@PathVariable processInstanceId: String): ResponseEntity<List<Map<String, Any?>>> {
        val tasks = claimsWorkflowService.getActiveTasksByProcessInstance(processInstanceId)
        
        val response = tasks.map { task ->
            mapOf(
                "id" to task.id,
                "name" to task.name,
                "assignee" to task.assignee,
                "createTime" to task.createTime,
                "description" to task.description,
                "priority" to task.priority,
                "processInstanceId" to task.processInstanceId
            )
        }
        
        return ResponseEntity.ok(response)
    }

    @GetMapping("/instance/{processInstanceId}/tasks/completed")
    fun getCompletedTasksByProcessInstance(@PathVariable processInstanceId: String): ResponseEntity<List<Map<String, Any?>>> {
        val tasks = claimsWorkflowService.getCompletedTasksByProcessInstance(processInstanceId)
        
        val response = tasks.map { task ->
            mapOf(
                "id" to task.id,
                "name" to task.name,
                "assignee" to task.assignee,
                "createTime" to task.createTime,
                "endTime" to task.endTime,
                "description" to task.description,
                "priority" to task.priority,
                "processInstanceId" to task.processInstanceId,
                "duration" to task.durationInMillis
            )
        }
        
        return ResponseEntity.ok(response)
    }

    @GetMapping("/claim/{claimId}/tasks")
    fun getTasksByClaimId(@PathVariable claimId: String): ResponseEntity<List<Map<String, Any?>>> {
        val tasks = claimsWorkflowService.getTasksByClaimId(claimId)
        
        val response = tasks.map { task ->
            mapOf(
                "id" to task.id,
                "name" to task.name,
                "assignee" to task.assignee,
                "createTime" to task.createTime,
                "description" to task.description,
                "priority" to task.priority,
                "processInstanceId" to task.processInstanceId
            )
        }
        
        return ResponseEntity.ok(response)
    }

    @GetMapping("/claim/{claimId}/tasks/active")
    fun getActiveTasksByClaimId(@PathVariable claimId: String): ResponseEntity<List<Map<String, Any?>>> {
        val tasks = claimsWorkflowService.getActiveTasksByClaimId(claimId)
        
        val response = tasks.map { task ->
            mapOf(
                "id" to task.id,
                "name" to task.name,
                "assignee" to task.assignee,
                "createTime" to task.createTime,
                "description" to task.description,
                "priority" to task.priority,
                "processInstanceId" to task.processInstanceId
            )
        }
        
        return ResponseEntity.ok(response)
    }

    @GetMapping("/instance/{processInstanceId}/{taskId}/decide")
    fun decision(@PathVariable processInstanceId :String, @PathVariable taskId :String, @RequestParam(required = true) decision: String ) : ResponseEntity<String> {
        var response = claimsWorkflowService.makeDecisionClaim(processInstanceId, taskId, decision)
        return ResponseEntity.ok(response)
    }
}