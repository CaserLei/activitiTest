package day81.activiti.d;

import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.omg.IOP.TAG_CODE_SETS;

public class ProcessVariable {
	private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	@Test
	public void deployProcessDefi(){
		
		Deployment deploy=processEngine.getRepositoryService()
				.createDeployment()
				.name("支付流程")
				.category("办公类别") 
				.addClasspathResource("diagrams/AppayBill.bpmn")
				.addClasspathResource("diagrams/AppayBill.png")
				.deploy();
		
		System.out.println("部署的名称："+deploy.getName());
		System.out.println("部署的id:"+deploy.getId());
	}
	
	//模拟设置流程变量
	public void getAndSetProcessVariable(){
		//有两种设置方式
		TaskService taskService=processEngine.getTaskService();
		RuntimeService runtimeService=processEngine.getRuntimeService();
		
		/**
		 * 1.通过runtimeService设置流程变量
		 * exxcutionId  执行对象
		 * variableName  变量名
		 * value  变量值 是一个Object对象
		 * 
		 */
		//runtimeService.setVariable(exxcutionId, variableName, value);
		//runtimeService.setVariableLocal(exxcutionId, variableName, value);  //设置本执行对象的变量，该变量的作用域只在当前的对象exxcutionId
		
		//runtimeService.setVariables(exxcutionId, variables);  //可以设置多个变量 放在Map<key,Value)
		
		
		/**
		 * 2.通过TaskService设置流程变量
		 * taskId 任务的id
		 * variableName 变量的名字
		 * value 变量值
		 * 
		 */
		
		//taskService.setVariable(taskId, variableName, value);
		//taskService.setVariableLocal(taskId, variableName, value);  //设置本执行对象的变量，该变量的作用域只在当前的对象exxcutionId
		//taskService.setVariables(taskId, variables); //可以设置多个变量 放在Map<key,Value)
		
		/**3.当流程开始执行的时候，设置流程变量
		 * processDefiKey 流程定义的key
		 * variables: 设置多个变量Map<key,value>
		 */
		//processEngine.getRuntimeService().startProcessInstanceByKey(processDefiKey, variables);
		
		/**4.当执行任务的时候，设置变量
		 * 
		 *  processDefiKey 任务id
		 * variables: 设置多个变量Map<key,value>
		 */
		//processEngine.getTaskService().complete(taskId, variables);
		
		/**
		 * 通过RuntimeService获取变量值
		 * exxcutionId  当前执行对象
		 * 
		 */
		//runtimeService.getVariable(exxcutionId, variableName); //取变量
		//runtimeService.getVariableLocal(exxcutionId, variableName); //取本执行对象的某个变量
		//runtimeService.getVariables(variablesName); //取当前执行对象的索引变量
		
		/**
		 * 6.通过TaskServie取变量的值
		 * taskId
		 */
		//TaskServie.getVariable(exxcutionId, variableName); //取变量
		//TaskServie.getVariableLocal(exxcutionId, variableName); //取本执行对象的某个变量
		//TaskServie.getVariables(variablesName); //取当前执行对象的索引变量
		
	}
	
	
	//设置流程变量值
	@Test
	public void setVariable(){
		//1.通过流程定义的key 获取流程定义的id
        String processDefikey="appayBill";//流程定义的key
	    
	    TaskService taskService=processEngine.getTaskService();
	    Task task=taskService.createTaskQuery().processDefinitionKey(processDefikey).orderByTaskCreateTime().desc().singleResult();
	    
	    
	    String taskId=task.getId();
	    
	  /*  System.out.println("流程任务的ID:"+taskId);
		taskService.setVariable(taskId, "cost", 1000);  //设置单一的变量
		taskService.setVariable(taskId, "申请时间", new Date());
		taskService.setVariableLocal(taskId, "申请人", "何某某");
		
		System.out.println("设置成功!");*/
		
		/**
		 * 设置变量的类型
		 * 简单类型：String,boolean,Integer,double,date
		 * 自定义对象：bean
		 */
	    //taskService.setAssignee("老张", arg1);
		//传递一个自定义bean对象
	    AppayBillBean appayBillBean=new AppayBillBean();
	    appayBillBean.setId(1);
	    appayBillBean.setCost(1000);
	    appayBillBean.setAppayPerson("校长");
	    appayBillBean.setDate(new Date());
	    
	    taskService.setVariable(taskId, "appayBillBean", appayBillBean);//最好用类名的第一字母小写，不然会报错，找不到对应的对象
	    
		
	}
	
	
	@Test
	public void getVariable(){
		//1.通过流程定义的key 获取流程定义的id
        String processDefikey="appayBill";//流程定义的key
	    
	    TaskService taskService=processEngine.getTaskService();
	    Task task=taskService.createTaskQuery().processDefinitionKey(processDefikey).orderByTaskCreateTime().desc().singleResult();
	    
	    
	    String taskId=task.getId();
	    
	   /* Integer ass=(Integer)taskService.getVariable(taskId, "cost");
	    Date bb=(Date)taskService.getVariable(taskId, "申请时间");
	    String cc=(String)taskService.getVariableLocal(taskId,"申请人");
	    
	    System.out.println("cost:"+ass.toString()+"  申请时间："+bb+" 申请人："+cc);*/
	    
	    //读取实现序列化的变量数据
	    AppayBillBean appayBillBean=(AppayBillBean)taskService.getVariable(taskId, "appayBillBean");
	    
	    System.out.println(appayBillBean);
	    
	}
	
	@Test
	public void startProcess(){
		String deplymentKey="appayBill";  //bpmn 的process id属性
		ProcessInstance processInstance=processEngine.getRuntimeService().startProcessInstanceByKey(deplymentKey);
		
		System.out.println("流程执行对象的id:"+processInstance.getId());
		System.out.println("流程实现的对象id:"+processInstance.getProcessInstanceId());
		System.out.println("流程定义的id:"+processInstance.getProcessDefinitionId());
	}
	
	//完成任务
	@Test
	public void comileTask(){
		 String processDefikey="appayBill";//流程定义的key
		    
	    TaskService taskService=processEngine.getTaskService();
	    Task task=taskService.createTaskQuery().processDefinitionKey(processDefikey).orderByTaskCreateTime().desc().singleResult();
	    
	    
	    String taskId=task.getId();
		
		//taskID
		processEngine.getTaskService().complete(taskId);
	}
	
	
	//删除流程定义
		@Test
		public void deleteProcessDi(){
			//通过部署的id来删除流程定义id
			String deploymentId="1401";
			
			processEngine.getRepositoryService().deleteDeployment(deploymentId);
		}


}
