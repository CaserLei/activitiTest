package day81.activiti.c;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class ProcessInstanceAndTask {
	
	private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	@Test
	public void startProcess(){
		String deplymentKey="buyBill";  //bpmn 的process id属性
		ProcessInstance processInstance=processEngine.getRuntimeService().startProcessInstanceByKey(deplymentKey);
		
		System.out.println("流程执行对象的id:"+processInstance.getId());
		System.out.println("流程实现的对象id:"+processInstance.getProcessInstanceId());
		System.out.println("流程定义的id:"+processInstance.getProcessDefinitionId());
	}
	
	//查询任务
		@Test
		public void queryTask(){
			//任务办理人
			TaskService taskService=processEngine.getTaskService();
			//创建一个任务查询对象
			TaskQuery taskQuery=taskService.createTaskQuery();
			
			List<Task> list=taskQuery.list();
			
			if(list!=null && list.size()>0){
				for (Task task : list) {
					System.out.println("任务的办理人："+task.getAssignee());
					System.out.println("任务的id:"+task.getId());
					System.out.println("任务的名称："+task.getName());
				}
			}
			
		}
		
		//完成任务
		@Test
		public void comileTask(){
			/*任务的办理人：小张
			任务的id:1004
			任务的名称：采购申请
			任务的办理人：张三
			任务的id:308
			任务的名称：请假申请*/
			
			String taskId="1004";
			
			//taskID
			processEngine.getTaskService().complete(taskId);
		}
		
		//获取流程实例的状态
		@Test
		public void getProcessInstanceState(){
			String processInstanceId="1001";
			ProcessInstance pi=processEngine.getRuntimeService()
			.createProcessInstanceQuery()
			.processInstanceId(processInstanceId)
			.singleResult();//返回的数据要么是单行，要么是空，其他情况报错
			
			if(pi!=null){
				System.out.println("该流程实例"+processInstanceId+"正在运行....."+pi.getActivityId());
			}else{
				System.out.println("当前的流程实例"+pi+"已经结束！！！！");
			}
		}
	
		
		//查看历史流程实例信息
		@Test
		public void queryHistoryProcInst(){
			List<HistoricProcessInstance> list=processEngine.getHistoryService()
					.createHistoricProcessInstanceQuery()
					.list();
			if(list!=null && list.size()>0){
				for (HistoricProcessInstance histori : list) {
					System.out.println("历史流程实例："+histori.getId());
					System.out.println("历史流程定义id:"+histori.getProcessDefinitionId());
					System.out.println("历史流程结束的时间："+histori.getStartTime()+"  "+histori.getEndTime());
					
					System.out.println("任务的名字："+histori.toString());
				}
			}
		}
		
		//查看历史流程任务信息
		
		@Test
		public void queryHistoryTasKProcInst(){
			String processInstanceId="201";
			List<HistoricTaskInstance> list=processEngine.getHistoryService()
					.createHistoricTaskInstanceQuery()
					.processInstanceId(processInstanceId)
					.list();
			if(list!=null && list.size()>0){
				for (HistoricTaskInstance histori : list) {
					System.out.println("历史流程实例："+histori.getId());
					System.out.println("历史流程定义id:"+histori.getProcessDefinitionId());
					System.out.println("历史流程结束的时间："+histori.getStartTime()+"  "+histori.getEndTime());
					
					System.out.println("任务的名字："+histori.getName());
					System.out.println("任务执行者："+histori.getAssignee());
				}
			}
		}

}
