package com.test;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * User: weimeng
 * Date: 2018/3/23 10:46
 */
public class ReceiverTest {


    @Test
    public void testConsumer() {

        ArrayList<String> arrayList = Lists.newArrayList("A", "B");

        arrayList.forEach(((Consumer<String>) s -> {
            System.out.println(s.toUpperCase());
        }).andThen(e -> System.out.println(e + "_")));

    }
}