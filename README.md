# 基础的MVVM安卓架构

- [基础的MVVM安卓架构](#基础的mvvm安卓架构)
  - [1.概述](#1概述)
  - [2.目的](#2目的)
  - [3.项目环境](#3项目环境)
  - [4.架构分析](#4架构分析)
    - [View层：](#view层)
    - [业务分层：](#业务分层)
    - [持久层](#持久层)
    - [网络层：](#网络层)
  - [5.设计思想](#5设计思想)
    - [1. 封装基类Fragment](#1-封装基类fragment)
    - [2. 公共ViewModel](#2-公共viewmodel)
    - [3.网络层的层层封装](#3网络层的层层封装)
  - [未完待续](#未完待续)



## 1.概述

>  这是一个使用Kotlin搭建的单Activity+Navigation多Fragment页面切换的主流安卓基础项目，项目使用的是MVVM的架构进行的设计，网络层使用的Retrofit2网络框架，对Retrofit2使用了Flow和sealed进行了二次封装简化了请求操作增强了API的复用和减少了许多重复的api定义冗余的代码。然后项目继承了ViewBinding，可自行增加DataBinding，通过对BaseFragment进行继承来创建Fragment，加快了Fragment的创建，并且基类BaseFragment中集成了ViewModel和ViewBinding，只需要写你ViewBinding和ViewModel的类型就会自动创建。本项目作为安卓基础项目架构搭建，解耦高，独立性强，并且侵入式很少，可以灵活的在这个基础上进行业务的开发，项目自带一个基本的Fragment流程：
>
> >登录->主页导航(使用BottomNavigationView再次进行了子Fragment的导航(主页和个人页面导航))->其他页面
>
> 

## 2.目的

> 减少不必要的项目创建时间，和基本项目内容搭建和一些依赖插件开启，可以快速的在一个mvvm的基础项目中进行接下来的开发，并且封装好的网络层可以直接编写APi即可，不用对应再写每个API的处理

## 3.项目环境

>IDE：AndroindStudio
>
>语言：Kotlin
>
>构建工具：Gradle7.4
>
>JDK版本：1.8以上(推荐JDK11)
>
>compileSdk：32

## 4.架构分析

### View层：

>使用单Activity+Navigation管理多fragment的模式可以减少Activity创建带来的性能损耗，而且也可以处理避免一些多activity生命周期管理隐患的问题，使用Navigation可以灵活的进行页面的跳转管理，而且Fragment创建也可以减少性能的消耗，而且Fragment的生命周期依赖于Activity可以更好的进行管理，并且官方现在推荐的View管理模式也是单Activity+Navigation管理多Fragment显示的方式，并且也可以多个Activity来同时使用Navigation，扩展灵活
>

### 业务分层：

>使用MVVM架构可以细化管理并且加强业务处理的独立性，避免View操作的地方出现许多数据层操作造成代码冗余，逻辑揉在一起不好管理。

### 持久层

> 持久层使用Room框架作为Sqlite处理，在ui->other中建了一个Fragment 用于演示Room框架的使用。
>
> 使用room框架可以快速的对数据库进行操作，它的优势在于快速生成数据表和快速编写Dao操作，并且Room框架的操作可以选择在内存中缓存数据或者直接写入文件当中非常的灵活，对于简单的版本更新可以直接指定@Database的Version版本号来修改版本，会自动对比Entity来进行更新，并且由于Room框架的操作是把数据在缓存中进行操作，所以对比对实际Sqlite文件中写入效率要高很多

### 网络层：


>
>使用Flow订阅的模式来管理网络的状态，单一数据流的方式和网络请求的过程也很相识，使用密封类来定义三种基本网络类型
>
>````kotlin
>sealed class ApiResponse<out T> {
>   data class Success<out T>(val data: T?) : ApiResponse<T>()
>   data class Error(val exception: Exception, val errMsg: String) : ApiResponse<Nothing>()
>   object Loading : ApiResponse<Nothing>()
>}
>````
>
>把原本的异步改为同步然后手动处理每一个环节的状态然后进行flow的emit直接通过when进行对sealed的类型判断就可以快速对网络请求的状态进行对应操作
>
>````kotlin
>when (status) {
>   is ApiResponse.Error -> LogUtil.e("${it.errMsg} ${it.errMsg}")
>   ApiResponse.Loading -> LogUtil.e("Loading")
>   is ApiResponse.Success -> LogUtil.e("${it.data.toString()}")
>}
>````
>
>网络层分为三层，通过三层的过滤来减少代码操作和完善流程管理
>
>>#### 流程处理：
>> RequestBuilder.KT的getResponse进行对网络的基本状态判断，判断是否请求成功，是否成功发送请求，是否成功响应来进行进本的流程处理，然后emit提交出去给下一层进行处理
>>#### 请求操作：
>> 因为网络请求工具是直接一次性实例化在PublicViewModel中进行调用的，所有的网络请求都应该在这里进行调用而不是直接去构建RequestBuidler对象来发起网络请求，在PublicViewModel的getResponse中开启协程发起网络请求实现网络请求异步的操作，然后上一层RequestBuidler中的getResponse会提交网络的请求状态然后在PublicViewModel的getResponse中获取到上层的提交后把flow传入getResponse的process中，在最外层调用的地方通过获取flow进行订阅来对网络请求的每步进行响应操作(最外层操作flow是在协程中，所以如果要进行UI或者主线程操作请withContext(Dispatchers.Main)切换到主线程进行操作)
>> ```kotlin
>> //PublicViewModel的getResponse
>> fun <T> Call<T>.getResponse(process: suspend (flow: Flow<ApiResponse<T>>) -> Unit) {
>>  viewModelScope.launch(Dispatchers.IO) {
>>      requestBuilder.apply {
>>          process(requestBuilder.getResponse {
>>              this@getResponse.execute()
>>          })
>>      }
>>  }
>> }
>> ```
>> #### 订阅处理：
>> 最外层的请求通过获取flow来进行when判断当前网络请求状态来达到管理网络请求整个流程的实现
>> ```kotlin
>> request(TestAPI::class.java).login().getResponse {
>>     it.collect{
>>         when (it) {
>>             is ApiResponse.Error -> LogUtil.e("${it.errMsg} ${it.errMsg}")
>>             ApiResponse.Loading -> LogUtil.e("Loading")
>>             is ApiResponse.Success -> {
>>                 LogUtil.e("${it.data.toString()}")
>>                 withContext(Dispatchers.Main){
>>                     //切换到主线程操作
>>                 }
>>             }
>>         }
>>     }
>> }
>> ```
>
>PS:(由于现在客户端很少做本地持久层处理并且安卓有SharedPreferences，所以就没在项目中使用数据库框架，可以自行根据业务需求集成进来，推荐框架**Room**)

## 5.设计思想

### 1. 封装基类Fragment

通过对基类的继承可以快速的进行Fragment的创建，并且在基类中集成了ViewBinding和ViewModel可以快速的通过填入类型来自动创建好ViewModel和ViewBinding，其中集成了一个公共的PublicViewModel，此PublicViewModel的生命周期是依赖整个Activity的，这样就可以实现多个Fragment之间共享数据，再加上响应式编程的Livedata，就可以实现一个页面操作，另一个页面对liveData的监听响应对应事件，实现一个小型的广播过程

```kotlin
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val viewModelClass: Class<VM>?,
    publicViewModelTag: Boolean = false
) : Fragment() {
    private var bufferRootView: View? = null
    private var binding: VB? = null
    private val viewModel: VM? by lazy {
        val viewModelProvider = ViewModelProvider(this)
        viewModelClass?.let {
            viewModelProvider[viewModelClass]
        }
    }
    val publicViewModel: PublicViewModel? by lazy {
        if (publicViewModelTag) {
            ViewModelProvider(requireActivity())[PublicViewModel::class.java]
        } else {
            null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bufferRootView?.let {
            return bufferRootView
        }
        binding = inflate(inflater, container, false)
        initFragment(binding!!, viewModel, savedInstanceState)
        bufferRootView = binding!!.root
        return binding!!.root
    }

    abstract fun initFragment(binding: VB, viewModel: VM?, savedInstanceState: Bundle?)
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

```



### 2. 公共ViewModel

创建一个公共的ViewModel来供所有的Fragment来共享，可以减少Fragment之间的参数传递和不必要的对象创建，比如在PublicViewModel中创建ReuqestBuilder就可以反复使用这个网络请求对象来发起网络请求，这里不写单例的原因是网络请求集成了cookie，需要上下文，如果单例持有上下文将会是很离谱的事情，如果在多Activity的环境下就会出现崩溃异常无法识别上下文等，这是很危险的操作，所以公共的ViewModel好处也在这，基类Fragment中的PublicViewModel是可选的，可以通过传入布尔值来选择这个Fragment是否需要公共的ViewModel，不建议使用多个ViewModel让Fragment进行操作，一般的推荐的方式是一个Fragment对应一个自己的ViewModel

### 3.网络层的层层封装

虽然网络层我分为了三层 但是相对之间入侵性很小 你也可以通过直接创建ReuqestBuidler的方式来进行网络请求，但是并不推荐这样做，原因上面对网络得到分析已经说的很明白了 三层的封装可以减少不必要的代码，相比以前对应一个API中的多个接口要单独写函数来调用是很麻烦的，并且我不希望把网络层处理数据那些麻烦的部分给操作者使用，最后只需要填写API对象然后发起请求，拿到flow来订阅进行状态管理，这是非常方便和简洁的。


## 未完待续
