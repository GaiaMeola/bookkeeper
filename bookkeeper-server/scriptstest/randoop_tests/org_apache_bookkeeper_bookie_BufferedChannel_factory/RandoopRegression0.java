import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RandoopRegression0 {

    public static boolean debug = false;

    public void assertBooleanArrayEquals(boolean[] expectedArray, boolean[] actualArray) {
        if (expectedArray.length != actualArray.length) {
            throw new AssertionError("Array lengths differ: " + expectedArray.length + " != " + actualArray.length);
        }
        for (int i = 0; i < expectedArray.length; i++) {
            if (expectedArray[i] != actualArray[i]) {
                throw new AssertionError("Arrays differ at index " + i + ": " + expectedArray[i] + " != " + actualArray[i]);
            }
        }
    }

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test01");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel3 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) (short) 100);
        boolean boolean4 = bufferedChannel3.isSealed();
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test02");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel3 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) (short) 100);
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel6 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, (int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test03");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel3 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) (short) 100);
        long long4 = bufferedChannel3.getCacheHitCount();
        bufferedChannel3.close();
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
        org.junit.Assert.assertTrue("'" + long4 + "' != '" + 0L + "'", long4 == 0L);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test04");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel6 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (byte) 0, (int) (byte) -1, (long) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test05");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel3 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) '4');
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test06");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel4 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) ' ');
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test07");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel5 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, 100, 0, (long) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test08");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.lang.Class<?> wildcardClass2 = byteBufAllocator0.getClass();
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(wildcardClass2);
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test09");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel6 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (byte) 100, (int) (short) 1, (long) (byte) 1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test10");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.lang.Class<?> wildcardClass3 = byteBufAllocator0.getClass();
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test11");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel7 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, (int) (byte) 1, (int) (short) -1, (long) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test12");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel5 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) ' ', (long) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test13");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel5 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) '4');
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test14");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel5 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) 'a', (long) (byte) 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test15");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel7 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (byte) 10, (int) (byte) 1, (long) (byte) 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test16");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel4 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test17");
        java.nio.channels.FileChannel fileChannel0 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        java.lang.Class<?> wildcardClass3 = fileChannel0.getClass();
        org.junit.Assert.assertNotNull(fileChannel0);
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test18");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel7 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, (int) (short) -1, (int) (byte) -1, (long) (byte) 10);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test19");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel6 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, (int) (byte) 10);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test20");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel6 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) (short) 1, (long) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test21");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel7 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (short) 10, (int) (byte) 1, 100L);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test22");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.lang.Class<?> wildcardClass1 = byteBufAllocator0.getClass();
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test23() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test23");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel6 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, 10, (int) (short) 0, (long) (short) 1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test24() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test24");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, 100, 1L);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test25() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test25");
        java.nio.channels.FileChannel fileChannel0 = customutils.BufferedChannelFactory.channel;
        java.lang.Class<?> wildcardClass1 = fileChannel0.getClass();
        org.junit.Assert.assertNotNull(fileChannel0);
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test26() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test26");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel6 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test27() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test27");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel5 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (short) 0, 0L);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test28() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test28");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel7 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (-1), (long) (-1));
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test29() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test29");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel11 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, 1, (long) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }

    @Test
    public void test30() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test30");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel8 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) (byte) 100, (long) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test31() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test31");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel10 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, (int) (byte) 1, (int) (byte) 1, (long) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }

    @Test
    public void test32() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test32");
        java.nio.channels.FileChannel fileChannel0 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        java.lang.Class<?> wildcardClass4 = fileChannel0.getClass();
        org.junit.Assert.assertNotNull(fileChannel0);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test33() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test33");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel10 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, (int) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }

    @Test
    public void test34() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test34");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel7 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, 100);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test35() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test35");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel11 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (short) 0, (int) '4', (long) 10);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test36() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test36");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel5 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel13 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel5, (int) ' ', 0L);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel5);
    }

    @Test
    public void test37() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test37");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, (int) ' ', (int) (short) 100, (long) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test38() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test38");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel6 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test39() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test39");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel11 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (short) 10, 100, (long) '4');
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test40() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test40");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel8 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (-1), (long) '4');
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test41() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test41");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel5 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel13 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel5, (int) (byte) 10, (long) 10);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel5);
    }

    @Test
    public void test42() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test42");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel12 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, (int) (byte) 100, (int) (short) 100, (long) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test43() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test43");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel11 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, (int) (byte) -1, (long) 100);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }

    @Test
    public void test44() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test44");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel10 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, (int) (byte) 1, (int) (short) 10, (long) (-1));
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test45() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test45");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, 0, 100L);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test46() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test46");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel7 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test47() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test47");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel8 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) 'a', 100, (long) '#');
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test48() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test48");
        java.nio.channels.FileChannel fileChannel0 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        org.junit.Assert.assertNotNull(fileChannel0);
    }

    @Test
    public void test49() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test49");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel13 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, 100, 10, (long) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test50() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test50");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.lang.Class<?> wildcardClass4 = byteBufAllocator0.getClass();
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test51() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test51");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel6 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel6, (int) ' ', (long) 'a');
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel6);
    }

    @Test
    public void test52() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test52");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel5 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel5, (int) (short) 10, (int) (short) 10, (long) 1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel5);
    }

    @Test
    public void test53() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test53");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel14 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, (int) ' ', (int) (byte) -1, (long) 10);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test54() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test54");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel6 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (byte) 10, (long) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test55() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test55");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel6 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel8 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel6, (-1));
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel6);
    }

    @Test
    public void test56() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test56");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel6 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel6;
        customutils.BufferedChannelFactory.channel = fileChannel6;
        customutils.BufferedChannelFactory.channel = fileChannel6;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel12 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel6, (int) 'a', (long) ' ');
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel6);
    }

    @Test
    public void test57() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test57");
        customutils.BufferedChannelFactory bufferedChannelFactory0 = new customutils.BufferedChannelFactory();
        java.lang.Class<?> wildcardClass1 = bufferedChannelFactory0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test58() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test58");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, (int) (byte) 1, (int) (byte) 1, (long) 100);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test59() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test59");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel6 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel6;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel6, (int) (byte) 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel6);
    }

    @Test
    public void test60() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test60");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel7 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel7;
        customutils.BufferedChannelFactory.channel = fileChannel7;
        customutils.BufferedChannelFactory.channel = fileChannel7;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel12 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel7, 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel7);
    }

    @Test
    public void test61() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test61");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (byte) 0, (int) (byte) 10, (long) 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test62() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test62");
        java.nio.channels.FileChannel fileChannel0 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        java.lang.Class<?> wildcardClass2 = fileChannel0.getClass();
        org.junit.Assert.assertNotNull(fileChannel0);
        org.junit.Assert.assertNotNull(wildcardClass2);
    }

    @Test
    public void test63() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test63");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel7 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel7;
        customutils.BufferedChannelFactory.channel = fileChannel7;
        customutils.BufferedChannelFactory.channel = fileChannel7;
        customutils.BufferedChannelFactory.channel = fileChannel7;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel14 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel7, (int) (byte) 0, (long) 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel7);
    }

    @Test
    public void test64() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test64");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, 0, (long) (short) -1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }

    @Test
    public void test65() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test65");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.lang.Class<?> wildcardClass6 = byteBufAllocator0.getClass();
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test66() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test66");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel5 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, (int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }

    @Test
    public void test67() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test67");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel7 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel11 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel7, (int) (short) 0, (int) '#', (long) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel7);
    }

    @Test
    public void test68() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test68");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel12 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, 100, (long) (short) 100);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test69() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test69");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, (int) (byte) 1, (long) '#');
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test70() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test70");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel5 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel11 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel5, (int) '#', 10, (long) (short) 100);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel5);
    }

    @Test
    public void test71() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test71");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel5 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel5, (-1), (int) ' ', (long) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel5);
    }

    @Test
    public void test72() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test72");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel5 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel10 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel5, (int) (byte) 10);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel5);
    }

    @Test
    public void test73() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test73");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel6 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel10 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel6, (int) 'a', (int) 'a', (long) (short) 10);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel6);
    }

    @Test
    public void test74() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test74");
        java.nio.channels.FileChannel fileChannel0 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        customutils.BufferedChannelFactory.channel = fileChannel0;
        java.lang.Class<?> wildcardClass5 = fileChannel0.getClass();
        org.junit.Assert.assertNotNull(fileChannel0);
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test75() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test75");
        java.lang.Object obj0 = new java.lang.Object();
        java.lang.Class<?> wildcardClass1 = obj0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test76() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test76");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel8 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, (int) (byte) -1, 0, (long) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test77() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test77");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel2 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        customutils.BufferedChannelFactory.channel = fileChannel2;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel10 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel2, (int) (byte) 1, (int) (byte) 0, (long) 'a');
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel2);
    }

    @Test
    public void test78() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test78");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        java.nio.channels.FileChannel fileChannel1 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        customutils.BufferedChannelFactory.channel = fileChannel1;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel9 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel1, (int) ' ', (-1), (long) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: initialCapacity : -1 (expected: >= 0)");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel1);
    }

    @Test
    public void test79() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test79");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel4 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        customutils.BufferedChannelFactory.channel = fileChannel4;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel11 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel4, (int) (short) 1, (int) (byte) 10, (long) (byte) 100);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel4);
    }

    @Test
    public void test80() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test80");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel5 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        customutils.BufferedChannelFactory.channel = fileChannel5;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel14 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel5, (int) (short) 10, 1L);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel5);
    }

    @Test
    public void test81() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test81");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel6 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, 10, (long) (byte) -1);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }

    @Test
    public void test82() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test82");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel7 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel7;
        customutils.BufferedChannelFactory.channel = fileChannel7;
        customutils.BufferedChannelFactory.channel = fileChannel7;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel12 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel7, (int) (short) 0);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel7);
    }

    @Test
    public void test83() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RandoopRegression0.test83");
        io.netty.buffer.ByteBufAllocator byteBufAllocator0 = customutils.BufferedChannelFactory.allocator;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        customutils.BufferedChannelFactory.allocator = byteBufAllocator0;
        java.nio.channels.FileChannel fileChannel3 = customutils.BufferedChannelFactory.channel;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        customutils.BufferedChannelFactory.channel = fileChannel3;
        // The following exception was thrown during execution in test generation
        try {
            org.apache.bookkeeper.bookie.BufferedChannel bufferedChannel13 = new org.apache.bookkeeper.bookie.BufferedChannel(byteBufAllocator0, fileChannel3, (int) (byte) 10, (int) (short) 100, (long) 100);
            org.junit.Assert.fail("Expected exception of type java.nio.channels.ClosedChannelException; message: null");
        } catch (java.nio.channels.ClosedChannelException e) {
            // Expected exception.
        }
        org.junit.Assert.assertNotNull(byteBufAllocator0);
        org.junit.Assert.assertNotNull(fileChannel3);
    }
}

