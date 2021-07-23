# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#不进行优化，防止apk在其他版本的Dalvik虚拟机上不能运行
-dontoptimize
#不进行预校验，这是给Java平台用的，Android平台不需要这个
-dontpreverify


#保留实现Serizable接口的类和类中的成员
-keepclasseswithmembers class * implements java.io.Serializable{*;}

# 删除 Log
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
    public static *** println(...);
}

#避免混淆自定义控件类（的get/set方法和构造函数）
-keep public class * extends android.view.View{
#        *** get*();
#        void set*(***);
#        public <init>(android.content.Context);
#        public <init>(android.content.Context,
#                                    android.util.AttributeSet);
#        public <init>(android.content.Context,
#                                    android.util.AttributeSet,int);
}