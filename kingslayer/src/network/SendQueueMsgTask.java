package network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import game.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class SendQueueMsgTask implements Runnable {
    Map<Integer, LinkedBlockingQueue<Message>> messageQueues;
    volatile boolean running = false;
    boolean isServer;
    Server server;
    Client client;

    public SendQueueMsgTask(Map<Integer, LinkedBlockingQueue<Message>> messageQueues, boolean isServer,
                            Server server, Client client) {
        this.messageQueues = messageQueues;
        this.isServer = isServer;
        this.server = server;
        this.client = client;
    }
    @Override
    public void run() {

        running = true;
        sendQueueMsg();
    }

    public void stop() {

    }

    private void sendQueueMsg() {
        while (running) {

            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.err.println(sendQMark + " running send Q " + currentThread() + " " + sendQueueMsgThread);
            if (isServer) {
                for (int connectId : messageQueues.keySet()) {
                    List<Message> messageList = new ArrayList<>();
                    LinkedBlockingQueue q = messageQueues.get(connectId);
                    q.drainTo(messageList);
                    if (messageList.size() <= 0) continue;
                    server.sendToTCP(connectId, messageList);
                }
            } else {
                if (messageQueues.size() < 1) continue;
                List<Message> messageList = new ArrayList<>();
                LinkedBlockingQueue q = messageQueues.get(client.getID());
                q.drainTo(messageList);
                if (messageList.size() <= 0) continue;
                client.sendTCP(messageList);
            }
        }
    }
}
