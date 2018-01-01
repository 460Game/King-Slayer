
/*
basicly says that something wants to do something

i.e. unit wants to move to a position
or unit wants to shoot to the right
or building wants to create a thing

differs from updatge command in that updates are saying absolutes - holds the entire state of something at some time
actions are suggestions - unit wants to go up means if it is possible to go up then go up
so proccessing the unit wants to go up command would result in a updateCommand with the state of the unit going up


example: player presses right and up keys
this makes two action commands, one for right, one for left
this action commands are sent to the server
the server receives these. then the pkayers unit is going up and to the right
since the players unit has been mutated, the server sends an update message out to everyone with the new state of the plaer
 */


public abstract class ActionCommand {

    public abstract void execute(Model model);

    public void send(Model m) {
        m.receiveActionCommand(this);
    }
}
