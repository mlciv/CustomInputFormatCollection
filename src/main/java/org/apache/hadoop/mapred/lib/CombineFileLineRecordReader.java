package org.apache.hadoop.mapred.lib;

/**
 * Created by Jiessie on 3/4/15.
 */

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TaskAttemptContext;
import org.apache.hadoop.mapred.lib.CombineFileSplit;
import org.apache.hadoop.mapred.lib.MultiFileInputWritableComparable;
import org.apache.hadoop.util.LineReader;
import org.apache.hadoop.conf.Configuration;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class CombineFileLineRecordReader implements RecordReader<MultiFileInputWritableComparable, Text> {

  private long startOffset; // offset of the chunk;
  private long end; // end of the chunk;
  private long pos; // current pos
  private FileSystem fs;
  private Path path; // path of hdfs
  private MultiFileInputWritableComparable key;
  private Text value; // value should be string(hadoop Text)

  private FSDataInputStream fileIn;
  private LineReader reader;

  public CombineFileLineRecordReader(CombineFileSplit split, Configuration jobConf, Reporter reporter, Integer index) throws IOException
  {
    fs = FileSystem.get(jobConf);
    this.path = split.getPath(index);
    this.startOffset = split.getOffset(index);
    this.end = startOffset + split.getLength(index);
    boolean skipFirstLine = false;

    fileIn = fs.open(path); // open the file
    if (startOffset != 0) {
      skipFirstLine = true;
      --startOffset;
      fileIn.seek(startOffset);
    }
    reader = new LineReader(fileIn);
    if (skipFirstLine) // skip first line and re-establish "startOffset".
    {
      int readNum = reader.readLine(new Text(),0,(int) Math.min((long) Integer.MAX_VALUE, end - startOffset));
      startOffset += readNum;
    }
    this.pos = startOffset;
  }

  public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException
  {}

  public void close() throws IOException
  {
    reader.close();
  }

  public float getProgress() throws IOException
  {
    if (startOffset == end) {
      return 0.0f;
    } else {
      return Math.min(1.0f, (pos - startOffset) / (float) (end - startOffset));
    }
  }

  @Override
  public boolean next(MultiFileInputWritableComparable key, Text value) throws IOException {
    if (key == null) {
      key = new MultiFileInputWritableComparable();
      key.setFileName(path.getName());
    }
    key.setOffset(pos);
    if (value == null) {
      value = new Text();
    }
    int newSize = 0;
    if (pos < end) {
      newSize = reader.readLine(value);
      pos += newSize;
    }
    if (newSize == 0) {
      key = null;
      value = null;
      return false;
    } else {
      return true;
    }
  }

  @Override
  public MultiFileInputWritableComparable createKey() {
    return new MultiFileInputWritableComparable();
  }

  @Override
  public Text createValue() {
    return new Text();
  }

  @Override
  public long getPos() throws IOException {
    return pos;
  }

  public boolean nextKeyValue() throws IOException
  {
    if (key == null) {
      key = new MultiFileInputWritableComparable();
      key.setFileName(path.getName());
    }
    key.setOffset(pos);
    if (value == null) {
      value = new Text();
    }
    int newSize = 0;
    if (pos < end) {
      newSize = reader.readLine(value);
      pos += newSize;
    }
    if (newSize == 0) {
      key = null;
      value = null;
      return false;
    } else {
      return true;
    }
  }

  public MultiFileInputWritableComparable getCurrentKey() throws IOException, InterruptedException
  {
    return key;
  }

  public Text getCurrentValue() throws IOException, InterruptedException
  {
    return value;
  }
}
