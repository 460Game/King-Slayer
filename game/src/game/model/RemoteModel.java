package game.model;

import game.message.Message;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class RemoteModel implements IModel {

    //TODO fix this parameter @tian
    RemoteModel(Socket connection, IModel localModel) {

        //when connection receives something
     //   connection.onreceive {
            //deserialize messages
            //for each one call
     //       localModel.processMessage(message);
     //   }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                //TODO serialize everytrhing in the queue and send it
                Message[] arr = toSend.toArray(new Message[0]);
                toSend.clear();
          //      connection.send(arr);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        });

        t.start();
    }

    BlockingQueue<Message> toSend = new LinkedBlockingDeque();

    @Override
    public void processMessage(Message m) {
        toSend.add(m);
    }

    @Override
    public long nanoTime() {
        //TODO use SNTP or something to guess remotes time
        return 0;
    }
}
