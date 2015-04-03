package org.apache.hadoop.mapreduce.lib.input;

/**
 * Created by Jiessie on 3/4/15.
 */
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class SmallFileCombineTextInputFormat extends CombineFileInputFormat<MultiFileInputWritableComparable, Text>
{
  @Override
  public RecordReader<MultiFileInputWritableComparable,Text> createRecordReader(InputSplit split,TaskAttemptContext context) throws IOException
  {
    return new org.apache.hadoop.mapreduce.lib.input.CombineFileRecordReader<MultiFileInputWritableComparable, Text>
            ((CombineFileSplit)split, context, CombineFileLineRecordReader.class);
//CombineFileLineRecordReader.class为自定义类，一个split可能对应多个path则系统自带类..input.CombineFileRecordReader会通过java反射,针对不同的path分别构建自定义的CombineFileLineRecordReader去读key,value数据,具体看input.F 类源码
  }

}
