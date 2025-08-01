package com.saturn.claims.service.workflow

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.runtime.ProcessInstance
import org.springframework.stereotype.Service

@Service
class ClaimsWorkflowService(
    private val processEngine: ProcessEngine,
    private val runtimeService: RuntimeService,
    private val taskService: TaskService
) {

    fun startClaimProcess(claimId: String, variables: Map<String, Any> = emptyMap()): String {
        val processVariables = mutableMapOf<String, Any>()
        processVariables["claimId"] = claimId
        processVariables.putAll(variables)
        
        val processInstance = runtimeService.startProcessInstanceByKey(
            "claimManagementProcess",
            claimId,
            processVariables
        )
        
        return processInstance.id
    }

    fun getProcessInstance(processInstanceId: String): ProcessInstance? {
        return runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult()
    }

    fun getProcessInstanceByClaimId(claimId: String): ProcessInstance? {
        return runtimeService.createProcessInstanceQuery()
            .processInstanceBusinessKey(claimId)
            .singleResult()
    }

    fun getAllProcessInstances(): List<ProcessInstance> {
        return runtimeService.createProcessInstanceQuery()
            .processDefinitionKey("claimManagementProcess")
            .list()
    }

    fun getActiveProcessInstances(): List<ProcessInstance> {
        return runtimeService.createProcessInstanceQuery()
            .processDefinitionKey("claimManagementProcess")
            .active()
            .list()
    }

}