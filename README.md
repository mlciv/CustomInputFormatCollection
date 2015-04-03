#【Hadoop 代码使用方式】

<pre>
job.setInputFormatClass(SmallFileCombineTextInputFormat.class);
</pre>

当前Maven提加如下依赖 讲无法从reposity中找到直接jar,需手动编译下载，后续加入到
sohu 公司内部repo二方库中
<pre>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-mapreduce-custom-inputformat</artifactId>
    <version>1.0-SNAPSHOT</version>
 </pre>

 运行命令如下：
 hadoop jar hadoop-mapreduce-custom-inputformat-1.0-SNAPSHOT.jar
 org.apache.hadoop.mapreduce.sample.SmallFileWordCount
 -Dmapreduce.input.fileinputformat.split.maxsize=1024000000
 -Dmapred.job.queue.name=rdc /user/alalei/yarn/* /user/alalei/out_wc_usingMulti
                                                 
其中split.maxsize 可以限制合并后的一个
split的大小。后续继续可修改为直接指定map数目，但建议其实还是指定大小较好 ，否则用户将不会对程
 序有性能意识。

#【Hadoop Streaing 使用方式】

注意:hadoop-streaming 要求使用hadoopV1的inputformat接口实现。
<pre>
hadoop  jar /usr/lib/hadoop-mapreduce/hadoop-streaming-2.4.1.jar
-Dmapred.job.queue.name=pvlog  -files
mapper.sh,reducer.sh,hadoop-mapreduce-custom-inputformat-1.0-SNAPSHOT.jar
-libjars hadoop-mapreduce-custom-inputformat-1.0-SNAPSHOT.jar -inputformat
org.apache.hadoop.mapred.lib.SmallFileCombineTextInputFormat  -input
/user/alalei/yarn/*     -output /user/alalei/out_multi_test     -mapper "sh
./mapper.sh" -reducer "sh ./reducer.sh
</pre>
