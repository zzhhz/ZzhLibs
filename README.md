#
# ZzhLibs本依赖库，自己总结的，如果有不妥之处，敬请指出，我再修改。
#简单的封装了BaseActivity,BaseFragment,还有一个ZUtils工具类。
#一、BaseActivity，BaseFragment
    ###1.封装Hanlder
    ###2.封装了Toast-在屏幕中心提示
    ###3.使用了一个滑动退出当前页面的手势
#二、ZUtils
    ###1.单位转换<br>
    ###2.日期格式化
    ###3.网络判断
    ###4.屏幕宽高
    ###5.SD存储卡的简单操作
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
	        compile ('com.github.zzhhz:ZzhLibs:0.1.9'){
                exclude group: 'com.android.support'
	        }
	}
#问题
    1. 在主题中使用到了android:windowIsTranslucent true属性，但是这个属性将Activity的生命周期执行，有些影响。
        ~例如：A Activity 跳转到B Activity。A 的生命周期是没有走onStop方法的。同样，在B Activity 按下返回键，A 只走了onResume方法。
    在项目中如果需要依赖生命周期执行方法，要特别注意



