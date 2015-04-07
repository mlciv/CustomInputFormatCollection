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
  }

}
