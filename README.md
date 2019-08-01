#SanMen-android


项目运行环境：java，androidSdk

工具：Android Studio 或 idea

运行方式：第一次进项目，gradle先导包，导完成后，可以按启动按钮 或 shift + F10

项目主体：用的mvc模式

MyApplication：app启动一些初始化
TerminalInfo：全局配置

主要包：
    base包：
        BaseActivity.Main主要全局侧滑返回

    activity包：
        SplashActivity是闪屏页
        MainActivity是程序主页面的导航
        LoginActivity是登录页


    fragment包：
        HomeFragment是首页的页面
        MineFragment是我的页面

    net包：
        RxService是网络请求配置
        DataLyer网络请求的接口文件
        repository：实现网络请求接口
        api：网络请求接口

    utils包： 项目所用的工具类

    view包：
        MyTextview是显示对齐的textview
        PopupList七牛播放器

res文件下

    layout：页面绘制

    drawble： 资源文件


app包下的build.gradle主要是依赖的一些第三方库

项目目录build.gradle是项目配置

项目用的第三方组件
    view注入框架： butterknife

    图片加载框架：glide

    网络请求框架：rxjava + retrofit2 + okhttp3

    视频播放器：七牛播放器

    视频互动模块：接入萤石云第三方

    弹框ui：jjdxm-dialogui











