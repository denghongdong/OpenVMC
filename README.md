# OpenVMC
      《红象云腾开放视频大数据管理平台V1.0使用文档》


#OpenVMC
1〉 支持以离线或准实时方式生成视频图像目标物的特征索引

2〉 提供接口支持特征索引的可配置
特点：视频数据存储在HDFS分布式文件系统
      视频处理基于分布式计算平台
      
#1.整体架构
![](https://raw.githubusercontent.com/ChinaOpenVideo/OpenVMC/master/img/%E5%9B%BE%E7%89%871.png)
#2.功能图
![](https://raw.githubusercontent.com/ChinaOpenVideo/OpenVMC/master/img/%E5%9B%BE%E7%89%872.png)
#3.软硬件环境
1、集群环境推荐使用Windows 2008 server 64bit

2、所有节点安装独立的符合要求的操作系统（64位）

3、正确配置网络环境,所有节点可以正常Ping通（多个节时）。

4、所有节点需要安装hadoop/hive。

5、所有节点需要安装python2.x。

6、操作节点需要安装Redhadoop Studio0.3。

7、配合hive mysql 元数据存储。

8、系统建议200G以上空闲磁盘空间

#4.安装
软件主模块无需安装，将seasbase.jar和 videoReaderdll.dll分别拷贝到hadoop安装目录下的./share/yarn和./bin

#5.摄像头管理及摄像头管理主界面
![](https://raw.githubusercontent.com/ChinaOpenVideo/OpenVMC/master/img/%E5%9B%BE%E7%89%873.png)
#6.摄像头参数修改
![](https://raw.githubusercontent.com/ChinaOpenVideo/OpenVMC/master/img/%E5%9B%BE%E7%89%874.png)
#7.视频接入、启动视频录制
![](https://raw.githubusercontent.com/ChinaOpenVideo/OpenVMC/master/img/%E5%9B%BE%E7%89%875.png)

#8.视频处理、启动python脚本
![](https://raw.githubusercontent.com/ChinaOpenVideo/OpenVMC/master/img/%E5%9B%BE%E7%89%876.png)
#9.准实时视频检索、Redhadoop studio 视频检索

图4-20 车辆检索结果（红色车）
![](https://raw.githubusercontent.com/ChinaOpenVideo/OpenVMC/master/img/%E5%9B%BE%E7%89%877.png)

#下载地址
http://pan.baidu.com/s/1kTL3OGn#path=%252FRedHadoopEnterpriseCRH3

