package com.saturn.claims.service.workflow

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.HistoryService
import org.flowable.engine.runtime.ProcessInstance
import org.flowable.task.api.Task
import org.flowable.task.api.history.HistoricTaskInstance
import org.springframework.stereotype.Service

@Service
class ClaimsWorkflowService(
    private val processEngine: ProcessEngine,
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    private val historyService: HistoryService
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

    fun getActiveTasksByProcessInstance(processInstanceId: String): List<Task> {
        return taskService.createTaskQuery()
            .processInstanceId(processInstanceId)
            .active()
            .list()
    }

    fun getTasksByProcessInstance(processInstanceId: String): List<Task> {
        return taskService.createTaskQuery()
            .processInstanceId(processInstanceId)
            .list()
    }

    fun getCompletedTasksByProcessInstance(processInstanceId: String): List<HistoricTaskInstance> {
        return historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(processInstanceId)
            .finished()
            .list()
    }

    fun getAllTasksByProcessInstance(processInstanceId: String): List<HistoricTaskInstance> {
        return historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(processInstanceId)
            .list()
    }

    fun getTasksByClaimId(claimId: String): List<Task> {
        return taskService.createTaskQuery()
            .processInstanceBusinessKey(claimId)
            .list()
    }

    fun getActiveTasksByClaimId(claimId: String): List<Task> {
        return taskService.createTaskQuery()
            .processInstanceBusinessKey(claimId)
            .active()
            .list()
    }

}