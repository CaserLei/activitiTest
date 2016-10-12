package day81.activiti.f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class ExclusiveGatWay {
	
private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	@Test
	public void deployProcessDefi(){
		
		Deployment deploy=processEngine.getRepositoryService()
				.createDeployment()
				.name("ExclusiveGateway流程")
				.category("测试类别") 
				.addClasspathResource("diagrams/ExclusiveGateway.bpmn")
				.addClasspathResource("diagrams/ExclusiveGateway.png")
				.deploy();
		
		System.out.println("部署的名称："+deploy.getName());
		System.out.println("部署的id:"+deploy.getId());
	}
	

	@Test
	public void startProcess(){
		String deplymentKey="exclusiveGateWay";  //bpmn 的process id属性
		ProcessInstance processInstance=processEngine.getRuntimeService().startProcessInstanceByKey(deplymentKey);
		
		System.out.println("流程执行对象的id:"+processInstance.getId());
		System.out.println("流程实现的对象id:"+processInstance.getProcessInstanceId());
		System.out.println("流程定义的id:"+processInstance.getProcessDefinitionId());
	}
	
	//完成任务
	@Test
	public void comileTask(){
		 String processDefikey="exclusiveGateWay";//流程定义的key
		    
	    TaskService taskService=processEngine.getTaskService();
	    Task task=taskService.createTaskQuery().processDefinitionKey(processDefikey).orderByTaskCreateTime().desc().singleResult();
	    
	    
	    String taskId=task.getId();
		
		//taskID
	    Map<String,Object> params=new HashMap<String,Object>();
	    params.put("visitor", 4);
		processEngine.getTaskService().complete(taskId,params); //完成当前任务并且携带参数
		
		/*processEngine.getTaskService().complete(taskId);*/
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
	
	//删除流程定义
	@Test
	public void deleteProcessDi(){
		//通过部署的id来删除流程定义id
		String deploymentId="1101";
		
		processEngine.getRepositoryService().deleteDeployment(deploymentId);
	}

}
