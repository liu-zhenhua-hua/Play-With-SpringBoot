### Docker 容器技术



#### Docker 容器的基本操作
软件镜像 ---- 运行环境 ---- 产生一个容器 <br>

具体操作步骤:
```shell
搜索镜像
[root@localhost~]# docker search tomcat
拉取镜像
[root@loclahost~]# docker pull tomcat:tag
查看镜像文件
[root@localhost~]# docker images

启动容器 -d 表示后台运行, --name 给容器起个名字
[root@localhost~]# docker run --name mytomcat -d tomcat:latest

查看运行中的容器
[root@localhost~]# docker ps

查看所有的容器
[root@localhost~]# docker ps -a

停止运行的容器
[root@localhost~]# docker stop 4u6io76ps (Container ID)

删除容器
[root@localhost~]# docker rm container-id

-p 主机端口映射到容器内部端口
[root@localhost~]# docker run -d -p 1000:8080 tomcat

查看容器日志
[root@localhost~]# docker logs container-name/container-id
```
更多的参考命令:(https://docs.docker.com/engine/reference/commandline/docker/)
