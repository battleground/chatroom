package com.abooc.im;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    void out(String message){
        System.out.println(message);
    }

    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);

        String time = time();
        out(time);
    }


    String time(){
        long millis = System.currentTimeMillis();

        Date date = new Date(millis);
        return date.toString();
    }
}