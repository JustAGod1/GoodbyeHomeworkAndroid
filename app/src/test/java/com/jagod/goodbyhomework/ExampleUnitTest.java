package com.jagod.goodbyhomework;

import com.jagod.goodbyehomework.client.Client;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println(Client.signUp("huy", "huy", "huy@huy.huy", "frkfrkokro", null));
        System.out.println(Client.signIn("huy@huy.huy", "frkfrkokro", null));
    }
}