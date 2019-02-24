var contextRootPath = '/imagecenter';

function newRecord(title){
	//打开新的标签，在新的标签中进行添加操作
	//addTab(title,'leave/editLeaveApplication?editType=new');
	
	$('#dlg').dialog('open').dialog('setTitle', title);
	$('#fm').form('clear');
//	//设置修改类型，否则action中保存方法不知道是什么修改类型
//	$('#editType_edit').val("new");

	// 先判断隐藏域中是否有userCode，如果没有从cookie中取值
	var userCode = $('#userCode_hidden').val();
	
	if(userCode == null || userCode == ''){
		var userId = $.cookie('userId');
		if(userId != null && userId != ''){
			userCode = userId;
		}
	}
	// 清空原有数据
	$('#userCode_edit').textbox('setValue', userCode);  // 用户编码不需要清空
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
		var userCode = row.userCode;
		
		$('#dlg1').dialog('open').dialog('setTitle', title);
		$('#fm1').form('clear');
		
		$('#userCode_edit_1').textbox('setValue', userCode);
		$('#category_edit_1').textbox('setValue', category);
		$('#file_edit_1').textbox('setValue', filename);
//		$('#fileNewName_edit_1').textbox('getValue');
//		$('#tags_edit_1').textbox('getValue');
		
		// 将uuid和md5写入隐藏域
		$("#refuuid_edit_1").val(refuuid);
		$("#md5_edit_1").val(md5);
		
	}else{
		//alert("请选择一条记录进行修改");
		//(提示框标题，提示信息)
		$.messager.alert('提示','请选择一条记录进行修改');
	}
}


function destroyRecord(){
	var rows = $('#dg').datagrid('getSelections');
	if (rows != null && rows.length != null && rows.length > 1){
		$.messager.alert('提示','不允许选择多条记录进行修改');
		return false;
	}
	
	var row = $('#dg').datagrid('getSelected');
	if (row){
		$.messager.confirm('Confirm','确定删除这条记录吗？',function(r){
			if (r){
				var refuuid = row.uuid;
				$.post(contextRootPath + '/deletefile?refuuid=' + refuuid, {},function(result){
					if (result.success){
						$('#dg').datagrid('reload');	// reload the user data
					} else {
						$.messager.show({	// show error message
							title: 'Error',
							msg: result.errorMsg
						});
					}
				},'json');
			}
		});
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
		$.messager.alert("操作提示", '请选择文件！', "info");
		return ;
	}else{
		if(fileList.length > 1){
			$.messager.alert("操作提示", '暂不支持一次上传多个文件！', "info");
			return ;
		}
	}

	var type = "file"; 
	//后台接收时需要的参数名称，自定义即可 
	// 获取file对象
	var targetFile = $("input[type='file'].textbox-value")[0].files[0];

	var formData = new FormData(); 
	formData.append(type, targetFile); //生成一对表单属性 

	// 页面隐藏域中填写上最近一次上传文件的userCode
	if(userCode != null && userCode != ''){
		$('#userCode_hidden').val(userCode);
		
		// 如果cookie中没有userCode，填写上
		var userId = $.cookie('userId');
		if(userId == null || userId == ''){
			$.cookie('userId', userCode, { expires: 7, path: '/' });
		}
	}
	
	
	$.ajax({ 
		type: "POST", //因为是传输文件，所以必须是post 
		url: contextRootPath + '/ajaxupload?userCode=' + userCode + '&category=' + category + "&fileNewName=" + fileNewName + "&tags=" + tagstr, //对应的后台处理类的地址 
		data: formData, 
		processData: false, 
		contentType: false, 
		success: function (result) { 
			$(function () {
		        $.messager.alert("操作提示", result, "info", function () {
		        	// 关闭弹窗
		        	$('#dlg').dialog('close');
		        	// datagrid重新加载数据
		        	$('#dg').datagrid('reload');	// reload the user data
		        });
		    });
		},
		failure:function (result) {  
			$.messager.alert("操作提示", result, "error");
		}
	}); 
}

function editFileInfo(){
	
	var refuuid = $("#refuuid_edit_1").val();
	var md5 = $("#md5_edit_1").val();
	
	var userCode = $('#userCode_edit_1').textbox('getValue');
	var category = $('#category_edit_1').textbox('getValue');
	
	var fileNewName = $('#fileNewName_edit_1').textbox('getValue');
	console.log(fileNewName);
	
	var tags = $('#tags_edit_1').tagbox('getValues');
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
	
	$.ajax({
        type:"POST", 
        url: contextRootPath + "/editfile?refuuid=" + refuuid + "&userCode=" + userCode + '&category=' + category + "&fileNewName=" + fileNewName + "&tags=" + tagstr,
        //url:"leave/saveLeaveApplication?editType=新增",
        dataType:"json", 
        //data:postData,
        contentType: "text/html;charset=UTF-8", 
        success:function(result){
        	if (result.success){
				
        		$.messager.alert('提示',result.errorMsg);
        	}else{
        		$.messager.alert('提示',result.errorMsg);
        	}
        },
       	failure:function (result) {  
       		//(提示框标题，提示信息)
    		$.messager.alert('提示','加载失败');
       	}
	});
}

function formatFileName(val,row){
	return row.displayName + "." + row.suffix;
}
