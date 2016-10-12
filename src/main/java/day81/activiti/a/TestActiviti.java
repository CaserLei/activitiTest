package day81.activiti.a;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

/**
 * 模拟Activiti 工作流框架 执行
 * @author leifeng
 * 下午6:00:09
 */
public class TestActiviti {
	
	private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	//取得流程引擎，且自动创建Activiti涉及的数据库和表
	
	
	public void createProcessEngine(){
		/**
		 * 1.通过代码形式创建
		 * --取得ProcessEngineConfiguration对象
		 * --设置数据库连接属性
		 * --通过ProcessEngineConfiguration对象创建ProcessEngine对象
		 * 
		 */
		/*ProcessEngineConfiguration  engineConfiguration=ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		
		//设置数据库连接属性
		engineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		engineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activitiDB?createDatabaseIfNotExist=true"
				+"&useUnicode=true&characterEncoding=utf8");
		engineConfiguration.setJdbcUsername("root");
		engineConfiguration.setJdbcPassword("lqf123");
		
		//设置建表的策略(没有表时，自动创建表)
		//public static final java.lang.String DB_SCHEMA_UPDATE_FALSE = "false";  //不会创建表，没有表，则报异常
	    //public static final java.lang.String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";//先删除，在创建表
		//public static final java.lang.String DB_SCHEMA_UPDATE_TRUE = "true";//假如没有表，则自动创建
		engineConfiguration.setDatabaseSchemaUpdate("true");
		
		//通过ProcessEngineConfiguration对象创建ProcessEngine对象
		ProcessEngine processEngine=engineConfiguration.buildProcessEngine();
		System.out.println("流程引擎创建成功！");*/
		
		/**
		 * 2.通过activiti.cfg.xml获取流程 引擎 和自动创建数据库及表
		 */
		//从类加载路径中查找资源：activiti.cfg.xml  文件名可以自定义
		/*ProcessEngineConfiguration  engineConfiguration
		      =ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		
		ProcessEngine processEngine=engineConfiguration.buildProcessEngine();
		
		System.out.println("使用配置文件Activiti.cfg.xml创建流程引擎");*/
		
		
		/**
		 * 3. 通过ProcessEngines 来获取默认的流程引擎
		 */
		//默认加载类路径下的activiti.cfg.xml 
		//ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
		
		System.out.println("通过ProcessEngines来加载流程引擎");
		
		
	}
	
	//部署流程定义
	
	public void deploy(){
		//ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
		//获取仓库服务
		RepositoryService repositoryService=processEngine.getRepositoryService();
		Deployment deploy=repositoryService.createDeployment() //创建一个部署的构建器
		.addClasspathResource("diagrams/LeaveBill.bpmn")//从类路径中加载资源，一次只能加载一个资源
		.addClasspathResource("diagrams/LeaveBill.bpmn")////从类路径中加载资源，一次只能加载一个资源
		.name("请假流程")  //设置部署的名称
		.category("办公类别")    //设置部署的类别
		.deploy();
		
		System.out.println("部署的ID:"+deploy.getId());
		System.out.println("部署的名字："+deploy.getName());
		
	}
	
	
	//执行流程
	
	public void startProcess(){
		
		String proessDefiKey="leaveBill";
		//获取运行时服务
		RuntimeService runtimeService=processEngine.getRuntimeService();
		
		//获取流程实例
		ProcessInstance pi=runtimeService.startProcessInstanceByKey(proessDefiKey);
		
		System.out.println("流程实例的id:"+pi.getId());
		System.out.println("流程定义的id"+pi.getProcessDefinitionId());
		
	}
	
	
	//查询任务
	@Test
	public void queryTask(){
		//任务办理人
		String assignee="王五";
		TaskService taskService=processEngine.getTaskService();
		//创建一个任务查询对象
		TaskQuery taskQuery=taskService.createTaskQuery();
		
		List<Task> list=taskQuery.taskAssignee(assignee).list();
		
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
		/*任务的办理人：张三
		任务的id:204
		任务的名称：请假申请
		任务的办理人：张三
		任务的id:308
		任务的名称：请假申请*/
		
		String taskId="502";
		
		//taskID
		processEngine.getTaskService().complete(taskId);
	}

}
