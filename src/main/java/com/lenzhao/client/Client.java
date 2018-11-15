package com.lenzhao.client;

import org.zeromq.ZMQ;

import java.util.Random;

public class Client {
    public static void main (String[] args) throws Exception {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to send messages on
        ZMQ.Socket sender = context.socket(ZMQ.PUSH);
        sender.connect("tcp://*:7755");
        sender.setHWM(10000);
        System.out.println("Sending tasks to workers\n");

        //  Initialize random number generator
        Random srandom = new Random(System.currentTimeMillis());

        //  Send 100 tasks
        int task_nbr;
        int total_msec = 0;     //  Total expected cost in msecs
        for (task_nbr = 0; task_nbr < 1000; task_nbr++) {
            int workload;
            //  Random workload from 1 to 100msecs
            workload = srandom.nextInt(100) + 1;
            total_msec += workload;
            System.out.print(workload + ".");
            String string = String.format("%d", workload);
            sender.send(string, 0);
        }
        System.out.println("Total expected cost: " + total_msec + " msec");
        //Thread.sleep(1000);              //  Give 0MQ time to deliver

        sender.close();
        context.term();
    }
}
