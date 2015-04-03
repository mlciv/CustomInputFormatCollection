package org.apache.hadoop.mapred.lib;

/**
 * Created by Jiessie on 3/4/15.
 */

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;

public class SmallFileCombineTextInputFormat extends CombineFileInputFormat<MultiFileInputWritableComparable, Text>
{
  @Override
  public RecordReader<MultiFileInputWritableComparable, Text> getRecordReader(InputSplit split, JobConf job, Reporter reporter) throws IOException {
    return new CombineFileRecordReader(job, (CombineFileSplit)split, reporter,
            CombineFileLineRecordReader.class);
  }

}
