
SELECT * FROM act_ge_bytearray;  #通用字节资源表	

SELECT * FROM act_ge_property;   #通用属性表，可以生成部署id	


SELECT * FROM act_re_deployment;  #部署表

SELECT * FROM act_re_procdef;     #流程定义表


###流程实例与任务

SELECT * FROM act_ru_execution;   #流程执行对象信息
SELECT * FROM act_ru_task;        #正在运行的任务表


SELECT * FROM act_hi_procinst;    #历史流程实例表、

SELECT * FROM act_hi_taskinst;    #历史流程任务


###流程变量相关

SELECT * FROM act_ru_variable;    #流程变量表

SELECT * FROM act_hi_varinst;     #历史流程变量表

