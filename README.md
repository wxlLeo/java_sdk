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
"uid":"69a658b08a6035fe80ea53867a57d1a3",
"sign":"e7d0e96c832fd0cce6411e30f7b59539298661476e4f5a568ca8109b9bb77e09e50c93b6ea3da5d0f1db221a7413a867",
"fcode":"6fbfe5dcfe16dd71500d2e638b794a45"}

  
2.2注册查询api

url:http://127.0.0.1:8181/flt/registerQuery
request type:post
content-type:application/json
param:
{
"t":"76a02352de0abdf1e3a55ff0a1f68cf3",
"uid":"69a658b08a6035fe80ea53867a57d1a3",
"sign":"e7d0e96c832fd0cce6411e30f7b59539298661476e4f5a568ca8109b9bb77e09e50c93b6ea3da5d0f1db221a7413a867",
"fcode":"6fbfe5dcfe16dd71500d2e638b794a45"}

2.3获取logintoken api

url:http://127.0.0.1:8181/flt/loginToken
request type:post
content-type:application/json
param:
{
"t":"76a02352de0abdf1e3a55ff0a1f68cf3",
"uid":"69a658b08a6035fe80ea53867a57d1a3",
"sign":"e7d0e96c832fd0cce6411e30f7b59539298661476e4f5a568ca8109b9bb77e09e50c93b6ea3da5d0f1db221a7413a867",
"register_token":"a714693b629eb2e73c31607a67c020c0",
"fcode":"6fbfe5dcfe16dd71500d2e638b794a45"}

2.4自动登录api

url:http://127.0.0.1:8181/flt/autoLogin?uid=69a658b08a6035fe80ea53867a57d1a3&t=76a02352de0abdf1e3a55ff0a1f68cf3&sign=e7d0e96c832fd0cce6411e30f7b59539298661476e4f5a568ca8109b9bb77e09e50c93b6ea3da5d0f1db221a7413a867&fcode=6fbfe5dcfe16dd71500d2e638b794a45&bid_url=1&source=998483c781d08c4793dd9bb909e30b7c&register_token=fdc90e8631afc2be896f7645558b4024&login_token=cc5d0ec295ba3cd4f18104f83776a74a

request type:get

2.5账户绑定api

url:http://127.0.0.1:8181/flt/userBind?t=76a02352de0abdf1e3a55ff0a1f68cf3&uid=69a658b08a6035fe80ea53867a57d1a3&sign=e7d0e96c832fd0cce6411e30f7b59539298661476e4f5a568ca8109b9bb77e09e50c93b6ea3da5d0f1db221a7413a867&fcode=6fbfe5dcfe16dd71500d2e638b794a45&bid_url=1&source=998483c781d08c4793dd9bb909e30b7c

request type:get


2.6投资记录查询api

url:http://127.0.0.1:8181/flt/investRecord?t=1478168947&start_time=2016-10-10&end_time=2016-10-10&sign=2f85b4203e6f4873f82d05521fc21fff

request type:get

2.7可投资标的列表查询api
http://127.0.0.1:8181/flt/bidList?pageCount=10&pageIndex=1&t=1479883243&sign=1f888607432655e1e139815fd026bc22


