package com.lenzhao.server;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Broker {
    public static void main (String[] args) {
        //  Prepare our context and sockets
        Context context = ZMQ.context(1);

        //  Socket facing clients
        Socket receiver = context.socket(ZMQ.PULL);
        receiver.bind("tcp://*:5557");
        receiver.setHWM(10000);

        //  Socket facing services
        Socket sender = context.socket(ZMQ.PUSH);
        sender.bind("inproc://workers");
        sender.setHWM(10000);

        for(int thread_nbr = 0; thread_nbr < 10; thread_nbr++) {
            Thread worker = new Worker(context);
            worker.start();
        }

        //  Start the proxy
        //ZMQ.proxy (frontend, backend, null);
        while (!Thread.currentThread ().isInterrupted ()) {
            String result = new String(receiver.recv(0)).trim();
            System.out.println("===============" + result);
            sender.send(result.getBytes(), 0);
        }
        //  We never get here but clean up anyhow
        receiver.close();
        sender.close();
        context.term();
    }
}
