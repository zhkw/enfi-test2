var selectProject;
var bdEditingIndex;
var bdsEditingIndex;
var bdcopEditingIndex;
var bdqEditingIndex;
var bdLiEditingIndex;
var basePath=$(".basePath").attr('basePath');
var constructList;
var constructListMap = new Array();
var costList;
var costListMap = new Array();
var purList;
var projectConstructVId;
var purListMap = new Array();
var operList=new Array();
var operListMap = new Array();
//提供服务范围
var serviceList;
var serviceListMap = new Array();
//工程类别
var worksMap = [];
//专业
var majorArr = [];
//单位
var unitListMap = [];

var expenseTypeList;
var expenseTypeListMap = [];
$(function(){
	getProjectSummary();
	queryBaseDataTypes();
	preparatData();
	queryBdListInfo();
	queryTabInfo();
});
//查询项目基本信息
function getProjectSummary(){
	
	$.ajax({
		url:basePath+'/project/getProjectSummary?projectId='+$('#base-info').attr("projectId"),
		type:'POST',
		success:function(data){
			//console.log(data);
			$(".project-code").text(data.NUM);
			$(".project-info").text(data.NAME);
		}
	});

	$.ajax({
		url: basePath+"/getDropDownItemDisplayData",
		data: "dropDownName=ExpenseType&condition=&isNotNull=false",
		method: "post",
		dataType: "json",
		success: function(data) {
			expenseTypeList = data;
			for (var i in data) {
				expenseTypeListMap[data[i].ID] = data[i].NAME;
			}
		}
	});
}
//检索基础类型
function queryBaseDataTypes(){
	$.ajax({
		url:basePath+'/purPlan/queryBaseDataTypes',
		type:'POST',
		success:function(data){
			//供应商提供服务范围
			if(data!=null&&data.serviceType!=null){
				serviceList=data.serviceType;
				for(var i in serviceList){
					serviceListMap[serviceList[i].ID]=serviceList[i].NAME;
				}
			}		
		}
	});
    $.ajax({
        url: basePath+"/constructPlan/getWorksCategory",
        method: "get",
        dataType: "json",
        success: function(data) {
            for (var i in data) {
                worksMap[data[i].ID] = data[i].NAME;
            }
        }
    });
	$.ajax({
		url:basePath+'/constructPlan/queryBaseDataTypes',
		type:'POST',
		success:function(data){
			//分包类型
			if(data!=null&&data.constructType!=null){
				constructList=data.constructType;
				for(var i in constructList){
					constructListMap[constructList[i].ID]=constructList[i].NAME;
				}
			}
			//计价方式
			if(data!=null&&data.costType!=null){
				costList=data.costType;
				for(var i in costList){
					costListMap[costList[i].ID]=costList[i].NAME;
				}
			}
			//采购方式
			if(data!=null&&data.purType!=null){
				purList=data.purType;
				for(var i in purList){
					purListMap[purList[i].ID]=purList[i].NAME;
				}
			}
			//逻辑运算符
			for(var i=0;i<2;i++){
				var obj=new Object();
				if(i==0){
					operListMap['and']='and';
					obj.key='and';
					obj.text='and';
				}else{
					operListMap['or']='or';
					obj.key='or';
					obj.text='or';
				}
				operList.push(obj);
			}
		}
	});
    //初始化专业信息
    Utils.ajaxJsonSync(basePath+"/manHour/getMajorInfo",{},function(obj){
        for (var i = 0; i < obj.length; i++) {
            majorArr[obj[i].ID] = obj[i].MAJORNAME;
        }
    });
    //初始化单位
    Utils.ajaxJsonSync(basePath+"/getDropDownItemDisplayData",
        {dropDownName:"MMUnitList",condition:"",isNotNull:false},
        function(data){
            for (var i in data) {
                unitListMap[data[i].ID] = data[i].UNITNAME;
            }
        });
}
//数据准备
function preparatData(){
	selectProject=$('#base-info').attr("projectId");
	projectConstructVId=$('#base-info').attr("planVersionId");
	var isNew=$("#base-info").attr("isNew");
	var status=$("#base-info").attr("status");
	var projectName=$("#base-info").attr("projectName");
	var planVersionCode=$("#base-info").attr("planVersionCode");
	//$(".project-info").text(projectName);
	$(".version-info").text(planVersionCode);
	changeBDStatusInfo(status,projectConstructVId,isNew);
}
//计划状态修改
function changeBDStatusInfo(status,versionId,isNewFlag){
			projectConstructVId=versionId;
            if(status==0){
				$('.status-info').text('草稿');
				$('.status-info').attr('bd_status',0);
				$('.status-info').attr('isNewFlag',isNewFlag);
			}else if(status==1){
				$('.status-info').text('审批中');
				$('.status-info').attr('bd_status',1);
				$('.status-info').attr('isNewFlag',isNewFlag);
			}else if(status==2){
				$('.status-info').text('已审批');
				$('.status-info').attr('bd_status',2);
				$('.status-info').attr('isNewFlag',isNewFlag);
			}else if(status==3){
                $('.status-info').text('已驳回');
                $('.status-info').attr('bd_status',3);
                $('.status-info').attr('isNewFlag',isNewFlag);
            }else if(status==-1){
				$('.status-info').text('已取消');
				$('.status-info').attr('bd_status',-1);
				$('.status-info').attr('isNewFlag',isNewFlag);
			}else{
				$('.status-info').text('草稿');
				$('.status-info').attr('bd_status',0);
				$('.status-info').attr('isNewFlag',isNewFlag);
			}	
}
//检索标段信息
function queryBdListInfo(){
	$('#bd-list-info').datagrid({
	url:basePath+'/constructPlan/queryConstructPlan',
	toolbar:'#bd-list-info_tb',
	height:'100%',
	width:'100%',
	idField: 'ID',
	singleSelect:true,
	queryParams: {
		versionId:function(){
				return projectConstructVId;
		},
		type:function(){
			return 0;
		},
		projectId:function(){
				return selectProject;
		},
		key:function(){
				return $('#search-construct-plan').searchbox('getValue');
		},
	},
    columns:[[
		{field:'CODE',title:'标段号',halign:'center',width:'10%'},
		{field:'NAME',title:'标段名称',halign:'center',width:'10%',editor:{type:'validatebox',options:{validType:'text',required:true}}},
		{field:'CONSTRUCTTYPEID',title:'类型',halign:'center',width:'10%',editor:{type:'combobox',options:{valueField:"ID",textField:"NAME"}},formatter:function(value,row,index){
			if(constructListMap[value]==null||constructListMap[value]==undefined){
				return '';
			}
			return constructListMap[value];
		}},
		{field:'ESTPRICE',title:'价格估算（万元）',halign:'center',width:'5%',editor:{type:"validatebox",options:{validType:'intOrFloat'}}},
		{field:'COSTTYPEID',title:'计价方式',halign:'center',width:'10%',editor:{type:'combobox',options:{valueField:"ID",textField:"NAME",multiple:true}},formatter:function(value,row,index){
			value+="";
			var costTypeIds=value.split(",");
			var costType='';
			for(var i in costTypeIds){
			 if(costListMap[costTypeIds[i]]!=null&&costListMap[costTypeIds[i]]!=undefined){
				if(i==0){
					costType+=costListMap[costTypeIds[i]]
				}else{
					costType+=","+costListMap[costTypeIds[i]]
				}
			  }
			}		
			return costType;
		}},
		{field:'PURTYPEID',title:'采购方式',halign:'center',width:'5%',editor:{type:'combobox',options:{valueField:"ID",textField:"NAME"}},formatter:function(value,row,index){
			if(purListMap[value]==null||purListMap[value]==undefined){
				return '';
			}
			return purListMap[value];
		}},
		{field:'REMARK',title:'备注',halign:'center',width:'10%',editor:'text'},
		{field:'RECEIVEREQFILETIME',title:'计划招标工作启动时间',halign:'center',width:'5%',editor:{type:'datebox'},formatter:function(value,row,index){
			if(value==null||value==undefined){
				return null;
			}else{
				return Utils.dateFormat(value,'yyyy-mm-dd');
			}
		}},
		{field:'CREATETIME',title:'计划招标文件发售时间',halign:'center',width:'5%',editor:{type:'datebox'},formatter:function(value,row,index){
			if(value==null||value==undefined){
				return null;
			}else{
				return Utils.dateFormat(value,'yyyy-mm-dd');
			}
		}},
		{field:'EQUIPREQCONFIRMTIME',title:'计划开标时间',halign:'center',width:'6%',editor:{type:'datebox'},formatter:function(value,row,index){
			if(value==null||value==undefined){
				return null;
			}else{
				return Utils.dateFormat(value,'yyyy-mm-dd');
			}
		}},
		{field:'DELIVERYTIME',title:'计划评标、定标时间',halign:'center',width:'6%',editor:{type:'datebox'},formatter:function(value,row,index){
			if(value==null||value==undefined){
				return null;
			}else{
				return Utils.dateFormat(value,'yyyy-mm-dd');
			}
		}},
		{field:'CONTRACTTIME2',title:'计划合同签订时间',halign:'center',width:'5%',editor:{type:'datebox'},formatter:function(value,row,index){
			if(value==null||value==undefined){
				return null;
			}else{
				return Utils.dateFormat(value,'yyyy-mm-dd');
			}
		}},
		{field:'ARRIVALTIME',title:'计划进场时间',halign:'center',width:'6%',editor:'text',editor:{type:'datebox'},formatter:function(value,row,index){
			if(value==null||value==undefined){
				return null;
			}else{
				return Utils.dateFormat(value,'yyyy-mm-dd');
			}
		}},
		{field:'INSTALLTIME',title:'计划开工时间',halign:'center',width:'6%',editor:'text',editor:{type:'datebox'},formatter:function(value,row,index){
			if(value==null||value==undefined){
				return null;
			}else{
				return Utils.dateFormat(value,'yyyy-mm-dd');
			}
		}},
		{field:'PROGRESSREMARK',title:'进度备注',halign:'center',width:'6%',editor:'text'}
		]],
	onSelect:function(){
		queryTabInfo();
	},
	onLoadSuccess:function(data){
		//默认选中第一条
		if(data!=null&&data.total>0){
			$('#bd-list-info').datagrid('selectRow',0);
		}else{
			$('.s_easyui-linkbutton').linkbutton('disable');
			$('.sc_easyui-linkbutton').linkbutton('disable');
			$('.q_easyui-linkbutton').linkbutton('disable');
			$('.l_easyui-linkbutton').linkbutton('disable');
			$('#bd-supplier-list').datagrid('loadData',{total:0,rows:[]});
			$('#bd-subcontract-scope-list').datagrid('loadData',{total:0,rows:[]});
			$('#bd-qualifications-list').datagrid('loadData',{total:0,rows:[]});
			$('#bd-license-list').datagrid('loadData',{total:0,rows:[]});
		}
	}
	});
}
//tabs 选中事件
function queryTabInfo(){
	//获取选中TAB
	var selectTab = $('.bdst-info-tabs').tabs('getSelected');
	var title=selectTab.panel('options').title;
		$('.bdst-info-tabs').tabs({   
		  border:false,   
		  onSelect:function(title){  
			if(title=='拟推荐供应商'){
			  querySupplier();
			}
			if(title=='分包范围'){
			  querySubcontractScope();
			}
			if(title=='资质要求'){
			  queryQualifications();
			}
			if(title=='许可要求'){
			  queryLicense();
			}
		}
		});
		if(title=='拟推荐供应商'){
		  querySupplier();
		}
		if(title=='分包范围'){
		  querySubcontractScope();
		}
		if(title=='资质要求'){
		  queryQualifications();
		}
		if(title=='许可要求'){
		  queryLicense();
		}
}
//初始化标段推荐供应商列表
function querySupplier(){
 var row=$('#bd-list-info').datagrid('getSelected');
 var constructPlanId="";
 if(row!=null){
	 constructPlanId=row.ID;
	 var purName=purListMap[row.PURTYPEID];
	 if(purName=="公开招标"||row.STATUS!=0){
	   $('.s_easyui-linkbutton').linkbutton("disable");
	 }else{
	   $('.s_easyui-linkbutton').linkbutton("enable");
	 }
 }else{
	  $('.s_easyui-linkbutton').linkbutton("disable");
 }
 $('#bd-supplier-list').datagrid({
	url:basePath+'/constructPlan/querySupplier',
	height:'100%',
	width:'100%',
	idField: 'ID',
	singleSelect:true,
	selectOnCheck:false,
	checkOnSelect:false,
    columns:[[
		{field:'NAME',title:'拟推荐供应商',halign:'center',width:'45%'},
		{field:'ISHANDINPUT',title:'是否手动录入',halign:'center',width:'30%',formatter:function(value,row,index){
			if(value==0){
				return '否';
			}else{
				return '是';
			}
		}},
		{field:'ISINCONTRACT',title:'是否合同承包约定',halign:'center',width:'20%',formatter:function(value,row,index){
			if(value==0){
				return  '<input type="checkbox" class="check_isincontract '+row.ID+"_"+index+'" disabled="disabled"  onclick="updateBdSupplierRow(this,'+index+');"/>';
			}else{
				return '<input type="checkbox"  class="check_isincontract '+row.ID+"_"+index+'" disabled="disabled" checked onclick="updateBdSupplierRow(this,'+index+');"/>';
			}
		}}
    ]],
	queryParams: {
		constructPlanId:function(){
				return constructPlanId;
		},
	},
	onClickRow:function(rowIndex,rowData){
			majorOfOtherSubcontract("bd-subcontract-major-list");
	},
	onLoadSuccess:function(data){
			if(data.total>0){
				 $('#bd-supplier-list').datagrid('clearSelections');
				 $('#bd-supplier-list').datagrid('clearChecked');
			}
	}
});
}
//初始化分包范围
function querySubcontractScope(){
	 var row=$('#bd-list-info').datagrid('getSelected');
	 var constructPlanId="";
	 if(row!=null){
		 constructPlanId=row.ID;
		 if(row.STATUS==0){
			 $('.sc_easyui-linkbutton').linkbutton("enable");
		 }else{
			 $('.sc_easyui-linkbutton').linkbutton("disable");
		 }
		 
	 }else{
		 $('.sc_easyui-linkbutton').linkbutton("disable");
	 }
	$('#bd-subcontract-scope-list').datagrid({
		url:basePath+'/constructPlan/queryConstructSubcontractScope',
		height:'100%',
		width:'100%',
		idField: 'ID',
		treeField: 'MATERIALCODE',
		singleSelect:true,
		selectOnCheck:false,
		checkOnSelect:false,
        columns:[[
            {field:'PBSCODE',title:'pbscode',hidden:true},
            {field:'PMMCODE',title:'物料编码',halign:'center',width:'10%',
                formatter: function(value,row) {
                    if (row.groupId != undefined) {
                        return "附";
                    } else {
                        return '<span title=\"' + value + '\" class=\"easyui-tooltip\">' + value + '</span>';
                    }
                }},
            {field:'PRJMATERIALNAME',title:'项目物料名称',halign:'center',width:'10%'},
            {title: '参数',field: 'DCP',width: "10%",halign: "center",align: "left",
                formatter: function(value,row) {
                    if (value) {
                        return '<span title=\"' + value + '\" class=\"easyui-tooltip\">' + value + '</span>';
                    }
                }
            },
            {field:'ICQTY',title:'该包包含数量',halign:'center',width:'10%'},
            {field:'QTY',title:'总数',halign:'center',width:'10%'},
            {
                field:'UNITWORKS',
                title:'单位工程类别',
                halign:'center',
                width:'10%',
                formatter: function(value) {
                    return worksMap[value];
                }
            },
            {
                field:'SUBWORKS',
                title:'分部工程类别',
                halign:'center',
                width:'10%',
                formatter: function(value) {
                    return worksMap[value];
                }
            },
            {field:'NODENAME',title:'所属子项',halign:'center',width:'10%'},
            {field:'MAJORID',title:'专业',halign:'center',width:'5%',
                formatter: function(value,row) {
                    if (value) {
                        return majorArr[value];
                    }
                }},
            {field:'UNITID',title:'单位',halign:'center',width:'5%',
                formatter: function(value) {
                    return unitListMap[value];
                }},
            {field:'UNITWEIGHT',title:'单重(吨)',halign:'center',width:'5%'},
            {field:'TOTLEWEIGHT',title:'总重(吨)',halign:'center',width:'5%',
                formatter: function(value,row) {
                    if (row.ICQTY && row.UNITWEIGHT) {
                        return (row.ICQTY*row.UNITWEIGHT).toFixed(2);
                    }else{
                        return "";
                    }
                }
            }
        ]],
		queryParams: {
			constructPlanId:function(){
					return constructPlanId;
			}
		},
		onClickRow:function(rowIndex,rowData){
			if(bdcopEditingIndex!=undefined){
				$('#bd-subcontract-scope-list').datagrid('endEdit',bdcopEditingIndex);
				bdcopEditingIndex=undefined;
			}
			majorOfOtherSubcontract("bd-subcontract-major-list");
			//加载费用分配情况
			loadPriceInfo(rowData.PBSCODE,rowData.ID);
		},
		onLoadSuccess:function(data){
			if(data.total>0){
				 $('#bd-subcontract-scope-list').datagrid('clearSelections');
				 $('#bd-subcontract-scope-list').datagrid('clearChecked');
			}
			$('#bd-subcontract-major-list').datagrid('loadData',{total:0,rows:[]})
		}
	});	
}
//资质要求
function queryQualifications(){
	 var row=$('#bd-list-info').datagrid('getSelected');
	 var constructPlanId="";
	 if(row!=null){
		 constructPlanId=row.ID;
		 if(row.STATUS==0){
			 $('.q_easyui-linkbutton').linkbutton("enable");
		 }else{
			 $('.q_easyui-linkbutton').linkbutton("disable");
		 }
	 }else{
		 $('.q_easyui-linkbutton').linkbutton("disable");
	 }
	$('#bd-qualifications-list').datagrid({
		url:basePath+'/constructPlan/queryConstructQualifications',
		height:'100%',
		width:'100%',
		idField: 'ID',
		treeField: 'MATERIALCODE',
		singleSelect:true,
		selectOnCheck:false,
		checkOnSelect:false,
		columns:[[
			{field:'NAME',title:'资质要求名称',halign:'center',width:'25%'},
			{field:'TYPES',title:'资质分类',halign:'center',width:'25%'},
			{field:'LEVELS',title:'资质级别',halign:'center',width:'20%'},
			{field:'OPERATOR',title:'逻辑运算符',halign:'center',width:'15%',editor:{type:'combobox',options:{valueField:"key",textField:"text"}},formatter:function(value){
				if(operListMap[value]==null||operListMap[value]==undefined){
					return '';
				}
				return operListMap[value];
			}},
			{field:'REMARK',title:'备注',halign:'center',width:'10%'}
		]],
		queryParams: {
			constructPlanId:function(){
					return constructPlanId;
			}
		},
		onLoadSuccess:function(data){
			if(data.total>0){
				 $('#bd-qualifications-list').datagrid('clearSelections');
				 $('#bd-qualifications-list').datagrid('clearChecked');
			}
		}
	});
}
//施工分包范围dialog
function subcontractDialog(){
	var row=$('#bd-list-info').datagrid('getSelected');
	var constructPlanId=row.ID;
	$('#construct-subcontract-dialog').dialog({
    title: '子项-专业查询',
    width: 400,
	content:"<div><input class='easyui-searchbox' id='pbsOrMajor_name' style='width:200px;height24px;' data-options='prompt:\"子项-专业搜索\",searcher:queryPbsAndNode' /><div style='min-height:150px;'><ul id='pbs-node-tree' class='ztree'></ul></div><div id='other_major_pbs' style='min-height:100px;display:none'>该专业在其他标段中的分包内容：<table id='all_major-subcontract-info'></table></div></div>",
    closed: false,
    cache: false,
    modal: true,
	buttons: [	
				{
					text:'关闭',
					iconCls:'icon-remove',
					handler:function(){
						$('#construct-subcontract-dialog').dialog("close");
					}
				},
				{
					text:'添加',
					iconCls:'icon-ok',
					handler:function(){
						addSubcontractScope();
					}
				}],
	});
	var treeData = $("#pbs-node-tree");
	var pbsNodeMajorTreeSetting = {
	async: {
		enable: true,
		dataType:"json" ,
		url:basePath+'/constructPlan/queryConstructNodeAndMajor',
		otherParam: {projectId:selectProject,constructPlanId:constructPlanId},
		dataFilter:function(treeId, parentNode, childNodes){
			var rNodes=new Array();
			var nodeMap=new Object();
			if (childNodes) {
				  for(var i =0; i < childNodes.length; i++) {
					 if(childNodes[i].flag==1){
						 childNodes[i].nocheck=true;
					 }
					 childNodes[i].open=true;
					 nodeMap[childNodes[i].id]=childNodes[i].id;
					 var key=$('#pbsOrMajor_name').searchbox('getValue');
					 if(""!=$.trim(key)){
						 if(childNodes[i].text.indexOf(key)!=-1){
							 rNodes.push(childNodes[i]); 
						 }
					 }else{
						rNodes.push(childNodes[i]); 
					 }
				  }
			}
			return rNodes;
		}
	},
	check : {
		enable :true,
		},
	view: {
		dblClickExpand: false,
		showLine: true
	},
	data: {
		simpleData: {
			enable:true,
			idKey: "id",
			pIdKey: "parentId",
			rootPId: ""
		},
		key:{
			name : "text",
		}
	},
	callback: {
		onAsyncSuccess:function(){
			var treeObj = $.fn.zTree.getZTreeObj("pbs-node-tree");
			var nodes = treeObj.getCheckedNodes(true);
			for (var i=0,l=nodes.length; i < l; i++) {
				treeObj.setChkDisabled(nodes[i], true);
			}
			treeObj.expandAll(true);
		},
	},
	}
	treeData = $.fn.zTree.init(treeData,pbsNodeMajorTreeSetting, null);
}
//PbsNodeAndMajor条件检索
function queryPbsAndNode(){
	var treeObj = $.fn.zTree.getZTreeObj("pbs-node-tree");
	treeObj.reAsyncChildNodes(null, "refresh");
}
//检索该专业在其他标段的分包内容
function majorOfOtherSubcontract(id){
	 var obj=new Object();
	 var row=$('#bd-list-info').datagrid('getSelected');
	 var constructPlanId="";
	 if(id!='all_major-subcontract-info'){
		var scoperow=$('#bd-subcontract-scope-list').datagrid('getSelected');
		constructPlanId=row.ID;
		obj.majorId=scoperow.MAJORID;
		obj.pbsNodeId=scoperow.PBSID;
	 }else{
		var treeObj = $.fn.zTree.getZTreeObj("pbs-node-tree");
		var nodes = treeObj.getSelectedNodes();
		obj.majorId=nodes[0].majorId;
		obj.pbsNodeId=nodes[0].parentId; 
	 }
	obj.constructPlanId=constructPlanId;
	obj.versionId=row.PLANVERSIONID;
	$("#"+id).datagrid({
    url:basePath+'/constructPlan/queryMajorOfConstructSubcontract',
    height:'100%',
    width:'100%',
    columns:[[
		{field:'CODE',title:'标段号',halign:'center',width:'32%'},
		{field:'NAME',title:'标段名称',halign:'center',width:'32%'},
		{field:'REMARK',title:'分包说明',halign:'center',width:'30%'}
    ]],
	queryParams:{majorId:obj.majorId,constructPlanId:obj.constructPlanId,versionId:obj.versionId,pbsNodeId:obj.pbsNodeId},
	
});
}
//许可要求列表查询
function queryLicense(){
  var row=$('#bd-list-info').datagrid('getSelected');
  var constructPlanId="";
  if(row!=null){
	  constructPlanId=row.ID;
	  if(row.STATUS==0){
		  $('.l_easyui-linkbutton').linkbutton("enable");
	  }else{
		   $('.l_easyui-linkbutton').linkbutton("disable");
	  }
  }else{
	  $('.l_easyui-linkbutton').linkbutton("disable");
  }
  $('#bd-license-list').datagrid({
		url:basePath+'/constructPlan/queryConstructLicensing',
		height:'100%',
		width:'100%',
		idField: 'ID',
		treeField: 'MATERIALCODE',
		singleSelect:true,
	    selectOnCheck:false,
	    checkOnSelect:false,
		columns:[[
			{field:'NAME',title:'许可要求名称',halign:'center',width:'40%'},
			{field:'ISHANDINPUT',title:'是否为手动录入',halign:'center',width:'10%',formatter:function(value,row,index){
				if(value==1){
					return '是';
				}else{
					return '否';
				}
			}},
			{field:'OPERATOR',title:'逻辑运算符',halign:'center',width:'10%',editor:{type:'combobox',options:{valueField:"key",textField:"text"}},formatter:function(value){
				if(operListMap[value]==null||operListMap[value]==undefined){
					return '';
				}
				return operListMap[value];
			}},
			{field:'REMARK',title:'备注',halign:'center',width:'30%'}
		]],
		queryParams:{constructPlanId:function(){
			return constructPlanId;
		}},
		onLoadSuccess:function(data){
			if(data.total>0){
				 $('#bd-license-list').datagrid('clearSelections');
				 $('#bd-license-list').datagrid('clearChecked');
			}
		}
	});
}
//设备供应商成套安装tab查询
function queryCompleteInstallTab(){
	//获取选中TAB
	var selectTab = $('#completeInstallInfo').tabs('getSelected');
	var title=selectTab.panel('options').title;
	$('#completeInstallInfo').tabs({   
		  border:false,   
		  onSelect:function(title){  
			if(title=='采购计划'){
			  queryPurchaseInfo();
			}
			if(title=='施工分包策划'){
			  queryConstructInfo();
			}
		}
		});
		if(title=='采购计划'){
		  queryPurchaseInfo();
		}
		if(title=='施工分包策划'){
		  queryConstructInfo();
		}
}
//查询采购计划成套安装
function queryPurchaseInfo(){
	$('#purchase-supplier-list').datagrid({
			url:basePath+'/constructPlan/queryConstructPlanInstallOfPur',
			height:'100%',
			width:'100%',
			idField: 'ID',
			treeField: 'CODE',
			columns:[[
				{field:'PCODE',title:'采购包号',halign:'center',width:'30%'},
				{field:'PNAME',title:'采购包名称',halign:'center',width:'35%'},
				{field:'SERVICENAME',title:'供应商提供服务范围',halign:'center',width:'30%'},
			]],
			 queryParams:{projectId:function(){
				return selectProject; 
			 }}
		});
}
//查询施工分包成套安装
function queryConstructInfo(){
		$('#construct-subcontract-list').datagrid({
		url:basePath+'/constructPlan/queryConstructPlanMaterial',
		height:'100%',
		width:'100%',
		idField: 'ID',
		treeField: 'MATERIALCODE',
		columns:[[
			{field:'PMMCODE',title:'物料编码',halign:'center',width:'10%'},
			{field:'PNAME',title:'项目物料名称',halign:'center',width:'12%'},
			{field:'MMDESCRIPTION',title:'参数',halign:'center',width:'10%'},
			{field:'PBSNAME',title:'所属子项',halign:'center',width:'10%'},
			{field:'MAJORNAME',title:'专业',halign:'center',width:'10%'},
			{field:'UNITNAME',title:'单位',halign:'center',width:'10%'},
			{field:'QTY',title:'数量',halign:'center',width:'10%'},
			{field:'PATENT',title:'是否含附属设备',halign:'center',width:'15%'},
			{field:'IMPORT',title:'是否进口',halign:'center',width:'5%'},
			{field:'REMARK',title:'备注',halign:'center',width:'5%'}
		]],
		 queryParams:{id:function(){
			 return projectConstructVId;
			},
			projectId:function(){
				return selectProject;
			}
		 },
	});
}
//设备成套安装dialog
function completeInstallDialog(){
	$('#complete-install-dialog').dialog({
    title:'供应商成套安装',
    width: 800,
	content:'<div class="easyui-tabs bdst-info-tabs" id="completeInstallInfo">'+
				'<div title="采购计划" class="bdst-info-tab bdst-tab">'+
					'<table id="purchase-supplier-list" style="min-height:250px;"></table>'+
				'</div>'+
				'<div title="施工分包策划"  class="bdst-info-tab bdst-tab" data-options="">'+
					'<table id="construct-subcontract-list" style="min-height:250px;"></table>'+
				'</div>'+
			'</div>',
    closed: false,
    cache: false,
    modal: true,
	buttons: [	
				{
					text:'关闭',
					iconCls:'icon-remove',
					handler:function(){
						$('#complete-install-dialog').dialog("close");
					}
				}]
	});
	if($('.status-info').attr('bd_status')==0&&$('.status-info').attr('isNewFlag')==1){
		$('.m_easyui-linkbutton').linkbutton('enable');
	}else{
		$('.m_easyui-linkbutton').linkbutton('disable');
	}
	queryCompleteInstallTab();
}
//手动录入供应商列表查看
function queryVsersionSupplierOfHand(){
	$('#license-dialog').dialog({
    title: '手动录入供应商列表',
    width: 600,
	content:"<div id='version_s_hand_list' style='min-height:150px;'><table id='v_s_h_list'></table></div>",
    closed: false,
    cache: false,
    modal: true,
	buttons: [	
				{
					text:'关闭',
					iconCls:'icon-remove',
					handler:function(){
						$('#license-dialog').dialog("close");
					}
				}]
	});
	$('#v_s_h_list').datagrid({
		url:basePath+'/constructPlan/querySupplierOfHand',
		columns:[[
			{field:'SNAME',title:'名称',halign:'center',width:'33%'},
			{field:'PNAME',title:'所属标段',halign:'center',width:'33%'},
			{field:'CODE',title:'所属标段号',halign:'center',width:'33%'},
		]],
		queryParams:{projId:function(){
						return selectProject;
					},
					id:function(){
						return projectConstructVId;
					}
					},
	});
}
//附件管理DIALOG
function manageFiles(){
	$('#file-mng-dialog').dialog({
    title:'附件管理',
    width: 800,
	content:'<div id="file_list"><table id="plan_file_list" style="min-height:250px;"></table></div>',
    closed: false,
    cache: false,
    modal: true,
	buttons: [	
				{
					text:'关闭',
					iconCls:'icon-remove',
					handler:function(){
						$('#file-mng-dialog').dialog("close");
					}
				}
			]
	});
	if($('.status-info').attr('bd_status')==0&&$('.status-info').attr('isNewFlag')==1){
		$(".f_easyui_linkbutton").linkbutton("enable");
	}else{
		$(".f_easyui_linkbutton").linkbutton("disable");
	}
	queryPlanFiles();
}
//附件检索
function queryPlanFiles(){
	$('#plan_file_list').datagrid({
		url:basePath+'/pbsCommonController/queryAttachment',
		height:'100%',
		width:'100%',
		idField: 'ID',
		columns:[[
			{field:'fileName',title:'文件名',halign:'center',width:'50%'},
			{field:'handle',title:'操作',halign:'center',width:'50%',formatter:function(value,row,index){
					return '<button type="button" onclick="downAttachment(\''+row.filePath+'\',\''+row.fileName+'\',\''+row.id+'\');" class="btn btn-default"><i class="icon-download"></i>下载</button>'	
			}}
		]],
	    queryParams:{
			targetId:function(){
				return projectConstructVId;
			},
			targetType:5,
		}
	});
}
//附件下载
function downAttachment(path,fileName,fileId){
	path = encodeURI(path);
	fileName = encodeURI(fileName);
	window.location.href = basePath+"/pbsCommonController/downloadAttachment?path="+path+"&fileName="+fileName+"&fileId="+fileId; 
}

/**
 * 加载物料的费用分配情况
 */
function loadPriceInfo(pbsCode,planLineId){
	$('#price-list-info').datagrid({
		url: basePath + '/purPlan/getMaterialPriceLine',
		queryParams: {
			pbsCode: function () {
				return pbsCode;
			},
			type: function () {
				return 2;
			},
			projectId: function () {
				return selectProject;
			},
			planLineId:function () {
				return planLineId
			}
		},
		toolbar:'#price-list-info_tb',
		height: "100%",
		width: "100%",
		border: false,
		//idField: 'id',
		columns:[[
			{
				field:'ck',
				title:"包含的费用",
				width: "5%",
				checkbox:true
			},{
				field: "expenseTypeId",
				title: "费用名称",
				width: "10%",
				halign:"center",
				align:"left",
				formatter: function(value) {
					return expenseTypeListMap[value];
				}
			},{
				field: "ratio",
				title: "费率(%)",
				width: "10%",
				halign:"center",
				align:"left"
			},{
				field: "isComputed",
				title: "根据费率计算价格",
				width: "10%",
				halign:"center",
				align:"left",
				formatter: function(value) {
					return (value===true||value===1)?"是":"否";
				}
			},{
				field: "price",
				title: "单价(万)",
				width: "20%",
				halign:"center",
				align:"left",
				formatter: function(value,row,index) {
					var base, ratio1, ratio2, ratio3;
					if (row.base===undefined) {
						return value;
					} else {
						if (row.isComputed===false) {
							return value;
						}
						var token = row.base.split(",");
						var allRows = $("#price-list-info").datagrid("getRows");
						var result;
						if (token.length===1) {
							for (var j in allRows) {
								if (allRows[j].expenseTypeId===token[0]) {
									result = allRows[j].price * row.ratio / 100;
								}
							}
						} else if (token.length===2) {
							for (var i in token) {
								for (var j in allRows) {
									if (allRows[j].expenseTypeId===token[0]) {
										base = allRows[j].price;
									} else if (allRows[j].expenseTypeId===token[1]) {
										ratio1 = allRows[j].ratio / 100;
									}
								}
							}
							result = base * (1 + ratio1) * (row.ratio / (100 - row.ratio));
						} else if (token.length==3) {
							for (var i in token) {
								for (var j in allRows) {
									if (allRows[j].expenseTypeId==token[0]) {
										base = allRows[j].price;
									} else if (allRows[j].expenseTypeId==token[1]) {
										ratio1 = allRows[j].ratio / 100;
									} else if (allRows[j].expenseTypeId==token[2]) {
										ratio2 = allRows[j].ratio / (100 - allRows[j].ratio);
									}
								}
							}
							result = base * (1 + ratio1 + ratio2 + ratio1*ratio2) * row.ratio / 100;
						} else if (token.length==4) {
							for (var i in token) {
								for (var j in allRows) {
									if (allRows[j].expenseTypeId==token[0]) {
										base = allRows[j].price;
									} else if (allRows[j].expenseTypeId==token[1]) {
										ratio1 = allRows[j].ratio / 100;
									} else if (allRows[j].expenseTypeId==token[2]) {
										ratio2 = allRows[j].ratio / (100 - allRows[j].ratio);
									} else if (allRows[j].expenseTypeId==token[3]) {
										ratio3 = allRows[j].ratio / 100;
									}
								}
							}
							result = base * (1 + ratio1 + ratio2 + ratio3 + ratio1*ratio2 + ratio1*ratio3 + ratio2*ratio3 + ratio1*ratio2*ratio3) * row.ratio / 100;
						}
						$("#price-list-info").datagrid("updateRow", {
							index: index,
							row: {
								price: decimalHandel(result,6)
							}
						});
						return decimalHandel(result,6);
					}
				}
			},{
				field: "totalPrice",
				title: "合价(万)",
				width: "15%",
				halign:"center",
				align:"left",
				formatter: function(value,row,index) {
					var qty = $("#bd-subcontract-scope-list").datagrid("getSelected").ICQTY;
					return decimalHandel((row.price * qty),6);
				}
			},{
				field: "base",
				title: "计算基数",
				width: "10%",
				halign:"center",
				align:"left",
				formatter: function(value) {
					if (value!="" && value!=undefined) {
						var token = value.split(",");
						var result = "";
						for (var i=0; i<token.length; i++) {
							result += expenseTypeListMap[token[i]];
							if (i<token.length-1) {
								result += "+";
							}
						}
						return result;
					}
				}
			},{
				field: "remark",
				title: "备注",
				width: "20%",
				halign:"center",
				align:"left",
				formatter: function(value,row,index) {
					if (!row.isEnable) {
						return '<a style="color: red;">费用已完全分配，不可选</a>';
					}
				}
			}
		]],
		onLoadSuccess: function(data){
			var rows = data.rows;
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				// var expenseTypeId = row.expenseTypeId;
				// var base = row.base;
				// //130为设备费
				// if (expenseTypeId === '130' || base === '130') {
				// 	if (expenseTypeId === '260') {
				//         //只有epc或pc设备安装费属于采购分包
				//         if (serviceListMap[serviceScopeId] === 'EPC' || serviceListMap[serviceScopeId] === 'EPC') {
				//             $("#price-list-info").datagrid("checkRow",i);
				//         }
				// 	}else {
				//         $("#price-list-info").datagrid("checkRow",i);
				// 	}
				// }
				// // $("#price-list-info").prev().find("input[type='checkbox']")[i+1].disabled = true;
				if (row.isCheck) {
					$("#price-list-info").datagrid("checkRow",i);
				}
				if (!row.isEnable) {
					$("#price-list-info").prev().find("input[type='checkbox']")[i + 1].disabled = true;
				}
			}
		}
	});
}


