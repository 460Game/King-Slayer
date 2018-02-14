package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.map.Tile;

/**
 * Message sent to set a tile on a client's game model. This message
 * is sent by the server.
 */
public class SetTileCommand implements ToClientCommand {

    /**
     * X-coordinate of the upper left corner of the cell.
     */
    private int x;

    /**
     * Y-coordinate of the upper left corner of the cell.
     */
    private int y;

    /**
     * Tile type to be sent.
     */
    private Tile tile;

    /**
     * Default constructor needed for serialization.
     */
    public SetTileCommand() {

    }

    /**
     * Constructor for a message, given coordinates and a tile type.
     * @param x x-coordinate of the upper left corner of the cell
     * @param y y-coordinate of the upper left corner of the cell
     * @param tile tile type
     */
    public SetTileCommand(int x, int y, Tile tile) {
        this.tile = tile;
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the cell at the given coordinates to the specified tile type.
     * @param model the game model on the client
     */
    @Override
    public void executeClient(ClientGameModel model) {
        model.setTile(x, y, tile);
    }
}
