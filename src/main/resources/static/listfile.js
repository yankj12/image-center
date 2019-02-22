
function newRecord(title){
	//打开新的标签，在新的标签中进行添加操作
	//addTab(title,'leave/editLeaveApplication?editType=new');
	
	$('#dlg').dialog('open').dialog('setTitle', title);
	$('#fm').form('clear');
//	//设置修改类型，否则action中保存方法不知道是什么修改类型
//	$('#editType_edit').val("new");

	// 清空原有数据
	//$('#userCode_edit').textbox('setValue', '');  // 用户编码不需要清空
	$('#category_edit').textbox('setValue', '');
	$('#file_edit').filebox('setValue', '');
	
	$('#fileNewName_edit').textbox('setValue', '');
	var tags = $('#tags_edit').tagbox('clear');
}


function editRecord(title){
	
	var row = $('#dg').datagrid('getSelected');    //这一步可以改造为从后台异步获取数据
	
	if(row != null){
		var id = row.id;
		
		var refuuid = row.uuid;
		var md5 = row.md5;
		var displayName = row.displayName;
		var suffix = row.suffix;
		var filename = displayName + "." + suffix;
		var category = row.category;
		
		$('#dlg1').dialog('open').dialog('setTitle', title);
		$('#fm1').form('clear');
		
		$('#category_edit_1').textbox('setValue', category);
		$('#file_edit_1').textbox('setValue', filename);
//		$('#fileNewName_edit_1').textbox('getValue');
//		$('#tags_edit_1').textbox('getValue');
		
		
	}else{
		//alert("请选择一条记录进行修改");
		//(提示框标题，提示信息)
		$.messager.alert('提示','请选择一条记录进行修改');
	}
}

function uploadFile(){
	var userCode = $('#userCode_edit').textbox('getValue');
	var category = $('#category_edit').textbox('getValue');
	var fileName = $('#file_edit').filebox('getValue');
	
	var fileNewName = $('#fileNewName_edit').textbox('getValue');
	console.log(fileName);
	
	var tags = $('#tags_edit').tagbox('getValues');
	console.log(tags);
	
	var tagstr = '';
	if(tags != null){
		for(var i=0;i<tags.length;i++){
			
			if(i == 0){
				tagstr = tagstr + tags[i];
			}else{
				tagstr = tagstr + "," + tags[i];
			}
		}
	}

	var fileList = $("input[type='file'].textbox-value")[0].files;

	if(fileList == null || fileList.length == 0){
		alert('请选择文件！');
	}else{
		if(fileList.length > 1){
			alert('暂不支持一次上传多个文件！');
		}
	}

	var type = "file"; 
	//后台接收时需要的参数名称，自定义即可 
	// 获取file对象
	var targetFile = $("input[type='file'].textbox-value")[0].files[0];

	var formData = new FormData(); 
	formData.append(type, targetFile); //生成一对表单属性 

	$.ajax({ 
		type: "POST", //因为是传输文件，所以必须是post 
		url: '/ajaxupload?userCode=' + userCode + '&category=' + category + "&fileNewName=" + fileNewName + "&tags=" + tagstr, //对应的后台处理类的地址 
		data: formData, 
		processData: false, 
		contentType: false, 
		success: function (result) { 
			alert(result); 
		},
		failure:function (result) {  
			alert(result);
		}
	}); 
}


function formatFileName(val,row){
	return row.displayName + "." + row.suffix;
}
