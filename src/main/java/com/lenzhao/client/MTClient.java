package com.lenzhao.client;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class MTClient {

    public static void main(String[] args) {
        Context context = ZMQ.context(1);

        //  Socket to talk to server
        Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:5559");

        System.out.println("launch and connect client.");

        for (int request_nbr = 0; request_nbr < 100; request_nbr++) {
            requester.send("Hello", 0);
            String reply = requester.recvStr(0);
            System.out.println("Received reply " + request_nbr + " [" + reply + "]");
        }

        //  We never get here but clean up anyhow
        requester.close();
        context.term();
    }
}
