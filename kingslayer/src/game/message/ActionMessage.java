package game.message;

/**
 * Message sent from a client to the server indicating that the client
 * performed an action. The server should process the message and
 * synchronize the action among all clients.
 */
public abstract class ActionMessage implements ToServerMessage {

}
