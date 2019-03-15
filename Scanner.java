package Scanner;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Java实现多线程扫描开放端口
 *
 * @author Administrator
 */
public class Scanner extends Thread {

    private List<Integer> openPorts = new ArrayList<>();
    private static List<Thread> Threads = new ArrayList<>();   //解决主线程先退出问题

    private int[] p;        //表示要扫描的端口范围
    Socket s;

    public Scanner(int[] p) {
        this.p = p;
    }

    /**
     * 线程1 , 扫描端口
     */
    @Override
    public void run() {
        //System.out.println("线程" + (p[0] + 1) + "已经启动!" );
        for (int i = p[0]; i < p[1]; i++) {
            try {
                s = new Socket("127.0.0.1", i); //通过这样的方法来判断端口是否开启 , 如果没有连接上会抛出异常
                openPorts.add(i);
                System.out.println("扫描到端口:" + i);  //解决的问题是每扫描完一个建立连接非常消耗资源 所以一定要关闭
                s.close();
            } catch (UnknownHostException e) {
            } catch (IOException e) {
            }


        }

        super.run();
    }

    /**
     * 主方法
     *
     * @param args
     */
    public static void main(String[] args) {
        Instant start = Instant.now();
        for (int i = 0; i < 6000; i += 100) {     //创建6000个线程........ 本质上扫描了60000个端口
            Thread t = new Scanner(new int[]{
                    i + 1, i + 100             // 1 , 100  ; 2 : 200
            });
            Threads.add(t);
            t.start();
        }

        //解决主线程先退出问题
        for (Thread v : Threads) {
            try {
                v.join();
            } catch (InterruptedException e) {
            }
        }

        Instant end = Instant.now();


        System.out.println("扫描时间为:" + Duration.between(start, end).toMinutes() + "分钟");

    }

}
