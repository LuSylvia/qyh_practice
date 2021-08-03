仅做测试用

v0.01 添加了闪屏页，采用bottomNavigationView+ViewPager实现底部导航栏;下一次更新准备添加小红点

v0.02 去掉了闪屏页的action_bar，同时将类名均改为大写字母开头

v0.03 加入了代码混淆，混淆规则可能会进一步完善

v0.04 加入了module_common、module_login模块

其中，module_common模块加入了constants用于管理url,entity用于处理数据回调的实体，retrofit用于管理Retrofit实例

module_login模块加入了登录用接口，loginViewModel，以及留待扩展的LoginActivity

v0.045 初步实现BaseActivity、ActivityManager、BaseApplication，但涉及标题栏相关的函数尚未全部实现

v0.05 基本实现了BaseActivity，验证码输入框DentifyingCodeView、LoginActivity的主要功能

v0.06 实现了登录功能，获取配置并自动登录功能还有待完善
      
推荐页面基本的网络请求逻辑已经在RecommendViewModel中实现

v0.07 添加了RequestInterceptor及相关类，已经能够获取ua信息

推荐页item的布局,adapter已经实现

后续会用Paing3完成分页加载，更新推荐页

v0.071 实现了权限申请工具类PermissionX,日志工具类LogUtil,通知工具类NotificationUtil

实现了强制下线功能（主要是为了熟练广播用法），权限申请，弹出系统通知

正在参考《第一行代码》修改系统架构，目前难点在于ViewModel的重构

v0.075 参考goole Paging3基本实现了推荐页的分页加载，可行性得先等appConfig问题修复完成才能测试

分页加载目前还有2个问题亟需解决：（1）用于获取用户Ids的workcity：Int的数据来源->文件？SharedPreferences?数据库？内存？

（2）RecommendUserRepository.kt里，在调用getPagingData前，必须让getIds函数跑完获取结果，目前的写法无法实现此效果
