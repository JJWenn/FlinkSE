# FlinkSE



## 初级功能



### 1、设计基础流式作业API



#### 1）定义一个抽象的数据结构来表示一个无界数据流（参考DataStream）



#### 2）定义API

+ 支持以下算子：

  + Source（输入Kafka；输出DataStream）

  + Sink（输入DataStream；输出外部文件）

  + Map（输入DataStream；输出DataStream）

  + KeyBy（输入DataStream；输出按key分割后的DataStream）

  + Reduce（输入按key分割后的DataStream；输出DataStream）



+ 支持基于处理时间的滚动窗口
  + 处理时间：执行处理操作的机器的系统时间
  + 滚动窗口：滚动窗口**有固定的大小**，是一种对数据进行**“均匀切片”**的划分方式。**窗口之间没有重叠，也不会有间隔，是“首尾相接”的状态。**



+ 支持为算子指定并发度的功能
  + .setParallelism();



+ 支持以DAG形式编排使用所有支持的算子
  + DAG：有向无环图
  + 把 "任务" 这个原子单位按照自己的方式进行编排，任务之间可能互相依赖。






> + Kafka自行搭建
>
> + 建议将API设计成一个用配置文件表达的形式，用户可以在配置文件中指定各个算子的上下游关系和具体实现



### 2、实现基础分布式流式计算引擎



+ 支持以上算子





+ 有一个基础的CLI/提交脚本用于方便的提交使用API编写的流式应用

  + 应用提交指令

    ```
    $ bin/flink run -m hadoop102:8081 -c com.atguigu.wc.StreamWordCount ./FlinkTutorial-1.0-SNAPSHOT.jar
    ```

    



+ 支持接收提交的流式应用后分发计算逻辑代码并调度算子到不同的计算节点上进行分布式计算
  + 接收提交的流式应用
    + 分发器（Dispatcher）--> JobMaster
      + 这里Dispatcher主要负责提供一个REST接口用于提交应用，并为应用启动一个新的JobMaster组件，其余先不考虑
  + 分发计算逻辑代码 
    + JobMaster --> TaskManager(slot)
  + 调度算子到不同的计算节点上进行分布式计算 
    + TaskManger





+ 支持在不同节点间对中间数据进行shuffle，包括上下游并发度不同时的分发、按key partition时的分发，具体实现方式不限
  + 这里的shuffle应该指转换算子操作
  + 并发度不同时的分发  -->  物理分区（Physical Partitioning）
  + 按key partition时的分发 --> keyBy





> + 可预先启好集群
>
> + 集群节点可通过配置写死
> + 不要求考虑节点崩溃问题



### 3、实现一个流式WordCount应用
