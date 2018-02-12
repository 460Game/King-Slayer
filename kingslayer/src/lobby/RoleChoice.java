package lobby;

public enum RoleChoice {
    SLAYER(0),
    KING(1);

    public int roleNum;
    RoleChoice(int role) {
        roleNum = role;
    }
}
