package com.zhanglijun.springbootdemo.util.concurrent.aijia.latch;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 可循环使用的latch
 * @author 夸克
 * @date 2018/9/10 01:26
 */
public class BarrierCountLatch {

    /**
     * Synchronization control For CountDownLatch. Uses AQS state to represent count.
     * 使用AQS状态表示计数 做成可循环用latch
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        private int count;

        Sync(int count) {
            setState(0);
            this.count = count;
        }

        int getCount() {
            return getState();
        }

        @Override
        public int tryAcquireShared(int acquires) {
            for (;;) {
                int c = getState();
                int nextc = c + 1;
                if (nextc <= count && compareAndSetState(c, nextc)) {
                    return 1;
                } else {
                    return -1;
                }

            }
        }

        @Override
        public boolean tryReleaseShared(int releases) {
            // Decrement count; signal when transition to zero
            boolean f = false;
            for (;;) {
                int c = getState();
                if (c == 0) {
                    return f;
                }
                int nextc = c - 1;
                if (compareAndSetState(c, nextc)) {
                    return true;
                }
            }
        }
    }

    private final Sync sync;

    public BarrierCountLatch(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        sync = new Sync(count);
    }

    public void acquire() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public void release() {
        sync.releaseShared(1);
    }

    public long getCount() {
        return sync.getCount();
    }

    public static void main(String[] args) {
        final BarrierCountLatch latch = new BarrierCountLatch(5);

        long s = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            final int index = i;

            try {
                latch.acquire();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 + index);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        latch.release();
                    }
                }
            }).start();
        }

        System.out.println(("cost time:") + (System.currentTimeMillis() - s));
    }
}
