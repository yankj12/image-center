function submit2(){ 
	var type = "file"; 
	//后台接收时需要的参数名称，自定义即可 
	var id = "cert"; 
	//即input的id，用来寻找值 
	var formData = new FormData(); 
	formData.append(type, $("#"+id)[0].files[0]); //生成一对表单属性 
	$.ajax({ 
		type: "POST", //因为是传输文件，所以必须是post 
		url: '/ajaxupload', //对应的后台处理类的地址 
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
	
