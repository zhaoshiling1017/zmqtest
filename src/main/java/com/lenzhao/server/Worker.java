package com.lenzhao.server;

import org.zeromq.ZMQ;

public class Worker extends Thread {
    private ZMQ.Context context;

    public Worker (ZMQ.Context context) {
        this.context = context;
    }
    @Override
    public void run() {
        ZMQ.Socket socket = context.socket(ZMQ.PULL);
        socket.connect ("inproc://workers");

        while (true) {

            //  Wait for next request from client (C string)
            String request = socket.recvStr (0);
            System.out.println ( Thread.currentThread().getName() + " Received request: [" + request + "]");

            //  Do some 'work'
            try {
                Thread.sleep (10000);
            } catch (InterruptedException e) {
            }
        }
    }
}
