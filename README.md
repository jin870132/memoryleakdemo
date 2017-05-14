# 安卓内存泄露几种常见形式及解决方案

博客地址:http://blog.csdn.net/jinfulin/article/details/72053920


## 一.前言
1.内存溢出与内存泄露
内存溢出(oom)，是指程序在申请内存时，没有足够的内存空间供其使用，出现oom；比如申请了一个integer,但给它存了long才能存下的数，那就是内存溢出。

内存泄露 (memory leak)，是指程序在申请内存后，<font color = red>无法释放</font>已申请的内存空间，一次内存泄露危害可以忽略，但内存泄露堆积后果很严重，无论多少内存,迟早会被占光。

memory leak会最终会导致oom！
## 二.内存泄露的几种形式
### 1.匿名内部类的使用
#### a.Thread内存泄漏 
这里最常见的形式就是使用new thread开启一个子线程.
<font color = red>子线程会对当前activity有一个隐式的强引用
当activity退出时候,如果子线程还在运行activity就不会释放.</font>

```
		  running = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    SystemClock.sleep(1000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
                            String date = sDateFormat.format(new Date());
                            tv.setText(date);
                        }
                    });

                }

            }
        }).start();
```

LeakCanary检测结果

![这里写图片描述](http://img.blog.csdn.net/20170514154609430?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamluZnVsaW4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

解决办法:
调用onDestroy后结束子线程
```  
  @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
    }
```

#### b.Timer内存泄露
这里既然thread使用有问题,那么我们用hander+Timer的形式可以吗,我们来看看.
结果抱歉,使用Timer和Thread无论从原理还是结果上都与handler一样.
```
public class HandlerAndTimerErr extends AppCompatActivity {
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                tv.setText(msg.obj.toString());
        }
    };
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_err);
        tv = (TextView) findViewById(R.id.tv);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
                String date = sDateFormat.format(new Date());
                Message message = new Message();
                message.obj=date;
                mhandler.sendMessage(message);
            }
        }, 1, 1000);
    }
}
```
LeakCanary检测结果
![这里写图片描述](http://img.blog.csdn.net/20170514173455923?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamluZnVsaW4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

解决办法
```
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
```
### 2.纯handler的错误使用
如果仅使用handler还可以这样写
```
public class HandlerErr extends AppCompatActivity {
    Handler mhandler = new Handler();
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_err);
        tv = (TextView) findViewById(R.id.tv);
       mhandler.postDelayed(new Runnable() {
           @Override
           public void run() {
               SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
               String date = sDateFormat.format(new Date());
               tv.setText(date);
               mhandler.postDelayed(this,1000);
           }
       },1000);
    }
}
```
LeakCanary检测结果
![这里写图片描述](http://img.blog.csdn.net/20170514173828518?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamluZnVsaW4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

解决办法
```
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除当前handler发送的请求
        mhandler.removeCallbacksAndMessages(null);
    }
```

### 3.context导致内存泄露

做一个单例的ToastUtils来显示toast是很多人会做的.你的ToastUtils是否这么写的?
```

public class ToastUtils {

    private static Toast toast;

    public static void ShowToast(Context context, String text){
        if (toast==null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }else{
            toast.setText(text);
        }
        toast.show();
    }
}
```

这样写其实有很大问题:
如果此时传入的是 Activity 的 Context，当这个 Context 所对应的 Activity 退出时，由于该 Context 的引用被单例对象所持有，其生命周期等于整个应用程序的生命周期，所以当前 Activity退出时它的内存并不会被回收，这就造成泄漏了。

LeakCanary检测结果
![这里写图片描述](http://img.blog.csdn.net/20170514182700652?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamluZnVsaW4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

解决办法:
1.使用ApplicationContext代替Activity的Context
2.如果必须要Activity的Context请换种写法吧,哈哈.

### 4.leackCannary使用
leackCannary使用其实非常简单
githup链接: https://github.com/square/leakcanary
1.首先在你的build.gradle添加引用:
```
 dependencies {
   debugCompile 'com.squareup.leakcanary:leakcanary-android:1.5.1'
   releaseCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.5.1'
   testCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.5.1'
 }
```
 2.在你的application中初始化
  LeakCanary.install(this);

3.提醒
这里其实就已经配置完成可以运行使用了.
注意,当你进入一个activity再退出之后等个三五秒如果有溢出就会提示的.

4.讲解
![这里写图片描述](http://img.blog.csdn.net/20170514182700652?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamluZnVsaW4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
我们就拿这个例子来说
这个是告诉我们 ToastUtils下面有一个静态成员变量toast
它引用了一个context
这个context是ContextErr这个activity的一个实例(instance)
它导致了内存泄露.

### 5.源码地址
https://github.com/jin870132/memoryleakdemo
