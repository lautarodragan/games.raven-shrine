package rs.questscript;

/**
 * Message command for QuestScript
 * @author lainmaster
 */
public class Message extends Command{
	/**
	 * Text contents of the message
	 */
	protected String _sText;
	/**
	 * Id of the Actor who is saying the message
	 */
	protected int _iId;
	
	public Message(){
		super();
		_eAction = Action.Message;
	}

	public void setText(String value){
		_sText = value;
	}

	public String getText(){
		return _sText;
	}

	/**
	 * Sets the Id param of this Message command.
	 * This would be the Id of the Actor who is saying the message.
	 * 
	 * @param value
	 */
	public void setId(int value){
		_iId = value;
	}

	/**
	 * Returns the Id param of this Message command.
	 * 
	 * @return
	 * @see #setId(int) 
	 */
	public int getId(){
		return _iId;
	}
}
