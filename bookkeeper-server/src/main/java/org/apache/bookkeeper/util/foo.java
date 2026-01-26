package org.apache.bookkeeper.util;

public class foo {
    public int sum(int a, int b) {
        if (a > 0 || b > 0) {
            return a + b;
        } else if (a == 0 || b == 0) {
            return 0;
        }

        return - 1;
    }
}
