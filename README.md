TemplatePlugin
===
ETL工具(kettle)与Velocity模板引擎的结合会产生一个非常变态的产物————代码自动化，通过简单的配置和图形界面流程设计，即可快速生成我们需要的代码！！！
它将大大提高你的开发效率，开发一个CRUD页面和开发几十上百个类似的功能页面，你花费的时间将会相差无几！！！<br>
#1 概述
一切皆模板！ txt、sql、jsp、html、java、php、xml只要能通过文本编辑器的文件都可以视为模板，
甚至word、excel、ppt经过转化成为xml后同样可以当模板使用。就像面向对象编程中的类一样，类相当于模板，
可以实例化出许多对象。<br>
例如，开发中很多代码块都有着相似的模块，比如下面的一个domain对象类：<br>
![](http://git.oschina.net/lucky110100/template/raw/master/doc/domain.png)<br>
    它有1类描述、2类名、3属性描述、4属性名，还有一些setter和getter方法，绝大多数domain对象都长这模样，如果有个模板，我们把上图红框中的内容替换掉，它就可以成一个新的类了。
那么，我们的模板插件可以做什么？我们的模板插件就是通过简单的配置和流程设计，替换图中方框的内容，快速生成我们需要的代码！！！它将大大提高你的开发效率，开发一个CRUD页面和开发几十上百个类似的功能页面，你花费的时间将会相差无几！！！<br>
***

#2 示例
 有这样一组数据，一个EXCEL中设计好的表
##<a name="table">表格</a>
| id	| type	| length| note |
| ----- | ----- | --------- | ------ |
| code	| varchar| 10	| 编码 |
| name	| varchar| 20	| 名称 |
| address| varchar| 100	| 地址 |
***
通过下面的ETL流程，<br>
![](http://git.oschina.net/lucky110100/template/raw/master/doc/etl.png)
![](http://git.oschina.net/lucky110100/template/raw/master/doc/codejob.png)
![](http://git.oschina.net/lucky110100/template/raw/master/doc/vmplugin.png)
<br>
我们看看输出的结果：<br>
![](http://git.oschina.net/lucky110100/template/raw/master/doc/files.png)<br>
###I、建表语句SQL<br>
![](http://git.oschina.net/lucky110100/template/raw/master/doc/sql.png)<br>
###II、domain对象<br>
![](http://git.oschina.net/lucky110100/template/raw/master/doc/java.png)<br>
###III、HTML代码<br>
![](http://git.oschina.net/lucky110100/template/raw/master/doc/html.png)<br>
在实际的生成开发环境中，要完成一个WEB页面的增删改查功能，还会需要的更多的文件和代码块，如controller、service、api……等方法模块，或者注册菜单和权限的SQL脚本，这些也都可以自动生成。
<br>
总而言之，在既定的框架下，一个简单的增删改查功能代码可以通过Kettle模板插件一键生成，甚至无需任何手工编码。
<br>
#3 总结
ETL工具（kettle）与模板引擎(velocity)结合可以使代码自动化变得非常强大，kettle能够完成非常多的任务处理，获取数据、文件处理，以及强大流程设计，velocity模板引擎则是，只要你提供有效的数据和丰富的模板，它就能生成你想要代码，标准、规范，一丝不苟！它们的结合是变态的，从最初的一个excel表格的表设计，它可以生成建表语句、菜单和权限脚本，并立即执行，接着生成domain、dao、service、controller、jsp……等一系列文件和代码块，然后自动输出到工程目录之下，只需要甚至一点点修改或根本不需要任何人工改动，就可以实现一个或多个功能，一气呵成！若是你的流程设计足够完美，还可以逆向这个过程，将一切还原到最初的样子！这要归功于Kettle的流程控制！
<br>
通过图形化拖拖拽拽实现一个功能还是梦想吗？不是，我们能够实现，并正在逐步完善！
<br>
我希望各位：
<br>
1、	熟练使用kettle和它的一系列组件，能够设计一个合理的流程生成所需代码，从那些简单重复的初级编码工作中脱离出来，节约时间，学习研究更精深的知识。
<br>
2、	学会velocity或freeMark模板引擎，能归纳整理并编写自己需要的功能模板。模板越丰富，代码自动化实现的功能就越多。
<br>
3、	加入kettle模板插件开发，使模板插件更加完善（可选）。补充一下，研究kettle源码可以学习很多知识，机不可失啊！！！
<br>
#4 相关资料
1、Kettle源码SVN地址：<br>
svn://source.pentaho.org/svnkettleroot/archive/Kettle/branches/4.4.0<br>
2、开发kettle插件：<br>
http://blog.fens.me/java-kettle-plugin-eclipse/<br>
3、插件代码（开源中国）<br>
https://git.oschina.net/lucky110100/template.git<br>
4、Eclipse Indigo 3.7.0 安装GIT插件<br>
http://www.cnblogs.com/taoweiji/p/3536543.html<br>
5、EGit 用户手册 —— 添加项目到版本控制<br>
http://www.oschina.net/translate/adding_a_project_to_version_control<br>
6、Velocity用户手册<br>
http://airport.iteye.com/blog/23634<br>
7、我的邮箱和QQ<br>
ydh110100@163.com<br>
380314706<br>
