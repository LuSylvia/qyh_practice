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

v0.09 解决了appBarconfig问题——该问题实际是未完成注册的用户才会出现，即单纯调用login.do接口并不会实际完整的注册一个用户，而只会核实该账号是否存在

通过cookieHelper实现了cookie的本地持久化，进而实现了自动登录

导入了ARouter和EventBus,已经成功实现页面路由跳转和事件发送，后续可能会添加ARouter的拦截器

进一步美化推荐页的UI，目前的难点是实现headerview的背景透明化