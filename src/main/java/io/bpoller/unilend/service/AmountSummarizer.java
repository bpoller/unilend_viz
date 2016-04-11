package io.bpoller.unilend.service;


import reactor.core.tuple.Tuple;
import reactor.core.tuple.Tuple2;
import reactor.core.tuple.Tuple3;

import java.util.ArrayList;

public class AmountSummarizer extends ArrayList<Tuple3> {

    private Integer accumulator;

    public AmountSummarizer() {
        accumulator = 0;
    }

    Integer getValue() {
        return accumulator;
    }

    void acc(Integer i) {
        accumulator += i;
    }

    public static void accept(AmountSummarizer summarizer, Tuple2<String, Integer> stringIntegerTuple2) {
        summarizer.acc(stringIntegerTuple2.getT2());
        summarizer.add(Tuple.of(stringIntegerTuple2.t1, stringIntegerTuple2.t2, summarizer.getValue()));
    }

    public static void combine(AmountSummarizer o, AmountSummarizer o1) {

        System.out.println();
    }

}
