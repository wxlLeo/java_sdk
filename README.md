# java_sdk
fanlitou auto register api sdk

注意：需要根据格子平台自身逻辑实现所有TODO标签的逻辑

1.setup

cd java_sdk/

mvn clean
mvn compile
mvn package
java -jar target/java_sdk-1.1.jar

2.test api

2.1注册api

url:http://127.0.0.1:8181/flt/register
request type:post
content-type:application/json
param:
{"phone_num":"69a658b08a6035fe80ea53867a57d1a3",
"t":"76a02352de0abdf1e3a55ff0a1f68cf3",
"serial_num":"1",
"uid":"69a658b08a6035fe80ea53867a57d1a3",
"sign":"1fbc415e3cf13ee3683bd223dd7af4dd",
"fcode":"fanlitou"}

  
2.2注册查询api

url:http://127.0.0.1:8181/flt/registerQuery
request type:post
content-type:application/json
param:
{"phone_num":"69a658b08a6035fe80ea53867a57d1a3",
"t":"76a02352de0abdf1e3a55ff0a1f68cf3",
"serial_num":"1",
"uid":"69a658b08a6035fe80ea53867a57d1a3",
"sign":"1fbc415e3cf13ee3683bd223dd7af4dd",
"fcode":"fanlitou"}

2.3获取logintoken api

url:http://127.0.0.1:8181/flt/loginToken
request type:post
content-type:application/json
param:
{"phone_num":"69a658b08a6035fe80ea53867a57d1a3",
"t":"76a02352de0abdf1e3a55ff0a1f68cf3",
"serial_num":"1",
"uid":"69a658b08a6035fe80ea53867a57d1a3",
"register_token":"register token",
"sign":"1fbc415e3cf13ee3683bd223dd7af4dd",
"fcode":"fanlitou"}

2.4自动登录api

url:http://127.0.0.1:8181/flt/autoLogin?phone_num=69a658b08a6035fe80ea53867a57d1a3&t=76a02352de0abdf1e3a55ff0a1f68cf3&uid=69a658b08a6035fe80ea53867a57d1a3&sign=1fbc415e3cf13ee3683bd223dd7af4dd&fcode=fanlitou&bid_url=1&source=pc&register_token=a&login_token=b

request type:get

2.5账户绑定api

url:http://127.0.0.1:8181/flt/userBind?phone_num=69a658b08a6035fe80ea53867a57d1a3&t=76a02352de0abdf1e3a55ff0a1f68cf3&uid=69a658b08a6035fe80ea53867a57d1a3&sign=1fbc415e3cf13ee3683bd223dd7af4dd&fcode=fanlitou&bid_url=1&source=pc

request type:get


2.6投资记录查询api

url:http://127.0.0.1:8181/flt/investRecord?t=1478168947&start_time=2016-10-10&end_time=2016-10-10&sign=22972e7a3f02b9e57df7ed5b23b059e6&fcode=fanlitou

request type:get


