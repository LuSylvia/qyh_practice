仅做测试用

v0.01 添加了闪屏页，采用bottomNavigationView+ViewPager实现底部导航栏;下一次更新准备添加小红点

v0.02 去掉了闪屏页的action_bar，同时将类名均改为大写字母开头

v0.03 加入了代码混淆，混淆规则可能会进一步完善

v0.04 加入了module_common、module_login模块

其中，module_common模块加入了constants用于管理url,entity用于处理数据回调的实体，retrofit用于管理Retrofit实例

module_login模块加入了登录用接口，loginViewModel，以及留待扩展的LoginActivity
