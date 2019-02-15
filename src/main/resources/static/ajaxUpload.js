function submit2(){ 
	var category = $("#category").val();
	var userCode = $("#userCode").val();
	var fileNewName = $("#fileNewName").val();
	
	var type = "file"; 
	//后台接收时需要的参数名称，自定义即可 
	var id = "cert"; 
	//即input的id，用来寻找值 
	var formData = new FormData(); 
	formData.append(type, $("#"+id)[0].files[0]); //生成一对表单属性 
	
	console.log($("#"+id)[0].files[0]);
	
	$.ajax({ 
		type: "POST", //因为是传输文件，所以必须是post 
		url: '/ajaxupload?userCode=' + userCode + '&category=' + category + "&fileNewName=" + fileNewName, //对应的后台处理类的地址 
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
	
function submit3(){
	var userCode = $('#userCode_edit').textbox('getValue');
	var category = $('#category_edit').textbox('getValue');
	var fileName = $('#file_edit').filebox('getValue');
	
	var fileNewName = $('#fileNewName_edit').textbox('getValue');
	console.log(fileName);
	
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
		url: '/ajaxupload?userCode=' + userCode + '&category=' + category + "&fileNewName=" + fileNewName, //对应的后台处理类的地址 
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