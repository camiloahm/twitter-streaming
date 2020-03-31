package org.interview.writer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WriterTest(profiles = "writer")
final class LocalOutputWriterTest {

    @Autowired
    private OutputWriter outputWriter;

    @Test
    void shouldInitializeProperties() {
        assertThat(outputWriter, is(notNullValue()));
    }

    @Test
    void shouldReturnTrueIfWriteIsExecuted() {
        OutputWriter mockOutputWriter = mock(LocalOutputWriter.class);
        Dataset<Row> dataSet = mock(Dataset.class);
        when(mockOutputWriter.write(dataSet)).thenReturn(true);
        assertThat(mockOutputWriter.write(dataSet), is(true));
    }
}
