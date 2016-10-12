package day81.activiti.e;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class SequenceFlow {
	private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	@Test
	public void deployProcessDefi(){
		
		Deployment deploy=processEngine.getRepositoryService()
				.createDeployment()
				.name("Sequence流程")
				.category("业务类别") 
				.addClasspathResource("diagrams/SequenceFlow.bpmn")
				.addClasspathResource("diagrams/SequenceFlow.png")
				.deploy();
		
		System.out.println("部署的名称："+deploy.getName());
		System.out.println("部署的id:"+deploy.getId());
	}
	

	@Test
	public void startProcess(){
		String deplymentKey="sequenceBill";  //bpmn 的process id属性
		ProcessInstance processInstance=processEngine.getRuntimeService().startProcessInstanceByKey(deplymentKey);
		
		System.out.println("流程执行对象的id:"+processInstance.getId());
		System.out.println("流程实现的对象id:"+processInstance.getProcessInstanceId());
		System.out.println("流程定义的id:"+processInstance.getProcessDefinitionId());
	}
	
	//完成任务
	@Test
	public void comileTask(){
		 String processDefikey="sequenceBill";//流程定义的key
		    
	    TaskService taskService=processEngine.getTaskService();
	    Task task=taskService.createTaskQuery().processDefinitionKey(processDefikey).orderByTaskCreateTime().desc().singleResult();
	    
	    
	    String taskId=task.getId();
		
		//taskID
	    Map<String,Object> params=new HashMap<String,Object>();
	    params.put("message", "知道不重要");
		processEngine.getTaskService().complete(taskId,params);
	}
	
	
	//删除流程定义
	@Test
	public void deleteProcessDi(){
		//通过部署的id来删除流程定义id
		String deploymentId="1101";
		
		processEngine.getRepositoryService().deleteDeployment(deploymentId);
	}

}
