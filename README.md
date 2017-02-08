#
# ZzhLibs本依赖库，自己总结的，欢迎有提意见
#简单的封装了BaseActivity,BaseFragment,还有一个ZUtils工具类。
#一、BaseActivity，BaseFragment
    1.封装Hanlder
    2.封装了Toast-在屏幕中心提示
    3.使用了一个滑动退出当前页面的手势
#二、ZUtils
    1.单位转换
    2.日期格式化
    3.网络判断
    4.屏幕宽高
    5.SD存储卡的简单操作
#状态栏透明（将布局延伸到状态栏）
    Android版本4.4之后才支持的，只是仅仅使用Theme进行设置的。
#使用方式
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
-----------
    dependencies {
	        compile 'com.github.zzhhz:ZzhLibs:0.0.1'
	}


