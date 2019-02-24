var contextRootPath = '/imagecenter';

/**
 * 页面初始化后做些东西
 */
$(document).ready(function(){
	console.log('init');
	
	// 清空原有数据
	//$('#userCode_edit').textbox('setValue', '');  // 用户编码不需要清空
	$('#category_edit').textbox('setValue', '');
	$('#file_edit').filebox('setValue', '');
	
	$('#fileNewName_edit').textbox('setValue', '');
	var tags = $('#tags_edit').tagbox('clear');
	
});
	
function submit(){
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
	
	$.ajax({ 
		type: "POST", //因为是传输文件，所以必须是post 
		url: contextRootPath + '/ajaxupload?userCode=' + userCode + '&category=' + category + "&fileNewName=" + fileNewName + "&tags=" + tagstr, //对应的后台处理类的地址 
		data: formData, 
		processData: false, 
		contentType: false, 
		success: function (result) { 
			$.messager.alert("操作提示", result, "info", function () {
	        	// 关闭弹窗
	        	$('#dlg').dialog('close');
	        	// datagrid重新加载数据
	        	$('#dg').datagrid('reload');	// reload the user data
	        });
		},
		failure:function (result) {  
			$.messager.alert("操作提示", result, "error");
		}
	}); 
}