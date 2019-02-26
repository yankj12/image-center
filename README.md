# image-center

用来管理应用中涉及到的图片

## 待实现功能

- 上传图片
- 异步上传图片
- 将图片根据分类存储到硬盘固定位置
- 使用uuid值来命名存储到硬盘的图片，将图片名称和uuid值的映射关系存表
- 计算图片md5值
- 相同md5值的图片不重复保存，只在表中增加记录
- 支持给图片打标签
- 提供图床功能
- 支持根据条件检索图片（名称、分类、标签等）

## 数据结构

image_main
id
uuid
md5
location
url
insertTime
updateTime

image_ref
id
uuid
md5
displayName
userCode
category

image_tag
id
uuid
tagName
validStatus

image_user_tag
id
uuid
md5
userCode
tagName

## API

### 异步上传图片

### 给图片打标签

入参

|代码|参数名称|说明|
|---|---|---|
|uuid|图片uuid||
|md5|图片md5||
|userCode|用户代码||
|tagName|标签名||

请求类型 POST

### 取消图片的标签

入参

|代码|参数名称|说明|
|---|---|---|
|uuid|图片uuid||
|md5|图片md5||
|userCode|用户代码||
|tagName|标签名||

请求类型 DELETE

### 查询图片的标签

入参

|代码|参数名称|说明|
|---|---|---|
|uuid|图片uuid||
|md5|图片md5||
|userCode|用户代码||

请求类型 GET

## 图片服务器

目前使用的是nginx作为图片服务器

### 安装nginx

下载nginx的windows版本，解压，使用start nginx命令启动即可

[windows上使用nginx](http://nginx.org/en/docs/windows.html)

### nginx提供图片服务

在nginx.conf文件中增加location /images/部分

```nginx.conf
    server {
        listen       80;
        server_name  localhost;

		location /images/ {
			root E:/images/uploadfile;
			index  index.html index.htm;
		}
```

这样就可以提供`http://localhost/images/20190205/6f36c28d9f854125976880544c999f7e.jpg`的图片服务了

需要注意的是我们url中包含/images/，我在windows上测试的时候，root对应的文件夹（本例中是E:/images/uploadfile）中必须包含一个文件夹images才可以提供正常的服务，否则会报文件找不到的错误

## 架构设计

架构设计v0.1版主要考虑存储和提供图片url，其他方面先不考虑，可以参考下图

![图片中心架构设计v0.1版](./docs/images/image-center-arch-design-v0.1.jpg)

## 常见问题

### 元素类型 "input" 必须由匹配的结束标记 "</input>" 终止。

非严格的thymeleaf格式

你可能会发现在默认配置下，thymeleaf对.html的内容要求很严格，比如<meta charset="UTF-8" />，如果少最后的标签封闭符号/，就会报错而转到错误页。也比如你在使用Vue.js这样的库，然后有<div v-cloak></div>这样的html代码，也会被thymeleaf认为不符合要求而抛出错误。

因此，建议增加下面这段：

```Java
spring.thymeleaf.mode = LEGACYHTML5
```

spring.thymeleaf.mode的默认值是HTML5，其实是一个很严格的检查，改为LEGACYHTML5可以得到一个可能更友好亲切的格式要求。

需要注意的是，LEGACYHTML5需要搭配一个额外的库NekoHTML才可用。到项目根目录的build.gradle文件里这样添加它到dependencies：

```POM
<dependency>
    <groupId>net.sourceforge.nekohtml</groupId>
    <artifactId>nekohtml</artifactId>
    <version>1.9.22</version>
</dependency>
```

参考自：

- [Spring Boot简略入门手册](http://acgtofe.com/posts/2016/08/spring-boot-simple-start-guide)
- [spring-boot-starter-thymeleaf对没有结束符的HTML5标签解析出错](https://www.cnblogs.com/mymelody/p/7903906.html)

### easyui-tagbox插件不起作用

easyui-tagbox扩展自$.fn.combobox.defaults，使用$.fn.tagbox.defaults重写默认值对象。（该组件自1.5.1版开始可用）

需要升级easyui版本到1.5.1以上

参考自 [EasyUI tagbox（标签框）](http://www.jeasyui.net/plugins/760.html)

### 文件太大上传不上去

#### SpringBoot对上传文件大小的限制

SpringBoot版本1.5.9

```properties
# 单个文件最大大小
spring.http.multipart.maxFileSize=30Mb
# 单次请求最大大小
spring.http.multipart.maxRequestSize=100Mb
```

根据不同版本，对应的设置值不一样

```properties
# Spring Boot 1.3.x and earlier
multipart.maxFileSize
multipart.maxRequestSize

# Spring Boot 1.4.x and 1.5.x
spring.http.multipart.maxFileSize
spring.http.multipart.maxRequestSize

# Spring Boot 2.x
spring.servlet.multipart.maxFileSize
spring.servlet.multipart.maxRequestSize
```

比如在2.x的版本，则在项目的application.properties文件中设置30MB大小

```properties
spring.servlet.multipart.maxFileSize=30MB
spring.servlet.multipart.maxRequestSize=30MB

# 如果不限制大小，则设置为-1即可
spring.servlet.multipart.maxFileSize=-1
spring.servlet.multipart.maxRequestSize=-1
```

参考自 [SpringBoot 文件上传、下载、设置大小](https://www.cnblogs.com/chevin/p/9260842.html)

#### nginx对上传文件大小的限制

自己搭的服务器，用nginx做代理。上传超过1M大的客户端文件无法正常上传，nginx直接报错，上传文件太大，于是修改了下nginx的配置，就可以了。
按照网上所说的加上`client_max_body_size`字段，怎么重启nginx都不行。后来发现放的位置有问题！

```nginx.conf
server {
    listen       80;
    server_name  localhost;
    client_max_body_size 10M;

    location /web {
        alias   D:/web;
        index main.html;            
    }
    location /web/service {
        proxy_pass   http://192.168.1.188:8080/service;     
    }
    location /web/service/upload {
        proxy_pass   http://192.168.1.188/upload;
    }       
}
```

参考自 [nginx对上传文件大小的限制](https://blog.csdn.net/longzhoufeng/article/details/79737549)