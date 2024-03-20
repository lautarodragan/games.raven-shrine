package rs.questscript;

/**
 * Enum of the possible actions a Command may execute.
 * @author lainmaster
 */
public enum Action {
	Message, MovePlayer, MoveNPC, ChangeItem;

	public static void main(String[] args){
		Action o = Action.ChangeItem;
		System.out.println(o.ordinal());

		Action p = Action.values()[o.ordinal()];
		System.out.println(p.toString());
	}
}
