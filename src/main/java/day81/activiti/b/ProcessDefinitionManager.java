package day81.activiti.b;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ProcessDefinitionManager {
	
	private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	@Test
	public void deployProcessDefi(){
		
		Deployment deploy=processEngine.getRepositoryService()
				.createDeployment()
				.name("请假流程")
				.category("办公类别") 
				.addClasspathResource("diagrams/LeaveBill.bpmn")
				.addClasspathResource("diagrams/LeaveBill.png")
				.deploy();
		
		System.out.println("部署的名称："+deploy.getName());
		System.out.println("部署的id:"+deploy.getId());
	}
	
	//部署流程定义，资源来自zip格式
	@Test
   public void deployProcessDefiZib(){
		
		InputStream in=getClass().getClassLoader().getResourceAsStream("BuyBill.zip");
		
		Deployment deploy=processEngine.getRepositoryService()
				.createDeployment()
				.name("采购流程")
				.category("公司运营采购")
				.addZipInputStream(new ZipInputStream(in))
				.deploy();
			

		System.out.println("部署的名称："+deploy.getName());
		System.out.println("部署的id:"+deploy.getId());
		System.out.println("部署的类别："+deploy.getCategory());
				
	}
	
	@Test
	public void queryProcessDefination(){
		
		String processDefikey="leaveBill";//流程定义的key
		
		List<ProcessDefinition> list=processEngine.getRepositoryService().createProcessDefinitionQuery()
		//查询，好比where
		//.processDefinitionCategory(category)//流程定义的类别
		//.processDefinitionId(proDefiId)   //流程定义名称
		.processDefinitionKey(processDefikey)
		//.processDefinitionName(name)   //流程定义名称
		
		//.processDefinitionVersion(version)// 流程定义的版本
		//.latestVersion()  //最新版本
		
		//排序
		.orderByProcessDefinitionVersion().desc()  //按版本的降序排序
		
		
		//结果
		//.count()//统计结果
		//.listPage(arg0, arg1)//分页查询
		.list();
		
		//遍历结果
		if(list!=null && list.size()>0){
			for (ProcessDefinition processDefinition : list) {
				System.out.print("流程定义的Id:"+processDefinition.getId());
				System.out.print("流程定义的key:"+processDefinition.getKey());
				System.out.print("流程定义的版本："+processDefinition.getVersion());
				System.out.print("流程定义部署的id:"+processDefinition.getDeploymentId());
				System.out.print("流程定义名称:"+processDefinition.getName());
				System.out.println("--------------------------------------");
			}
		}
		
	}
	
	
	//查看资源图片
	@Test
	public void viewImage() throws IOException{
		String deploymentId="101";
		String imageName=null;
		//取得某个部署资源的名称  deploymentId
		
		List<String> resourceNames=processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
		
		if(resourceNames!=null && resourceNames.size()>0){
			for (String temp : resourceNames) {
				if(temp.indexOf(".png")>0){
					imageName=temp;
				}
			}
		}
		
		/**
		 * 读取资源
		 * deploymentId:部署的Id
		 * resourceName:资源的名字
		 */
		InputStream resourceAsStream=processEngine.getRepositoryService().getResourceAsStream(deploymentId, imageName);
		
		//把文件输入流程写入到文件中
		File file=new File("d:/"+imageName);
		
		FileUtils.copyInputStreamToFile(resourceAsStream, file);
		
	}
	
	//删除流程定义
	@Test
	public void deleteProcessDi(){
		//通过部署的id来删除流程定义id
		String deploymentId="701";
		
		processEngine.getRepositoryService().deleteDeployment(deploymentId);
	}


}
