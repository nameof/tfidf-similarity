package com.nameof.tfidf;


import com.nameof.tfidf.data.DataLoader;
import com.nameof.tfidf.data.FileDataLoader;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 5)
@Threads(4)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
public class TFIDFCalculatorPerformanceTest {

    DataLoader dataLoader = new FileDataLoader("C:\\Users\\at\\Desktop\\A");

    @Benchmark
    public void testSerial() {
        DefaultTFIDFProcessor processor = new DefaultTFIDFProcessor();
        processor.analyzeAll(dataLoader);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TFIDFCalculatorPerformanceTest.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
