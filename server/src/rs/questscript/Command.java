/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rs.questscript;

/**
 * Abstract superclass of all QuestScript commands available.
 * 
 * @author lainmaster
 */
public abstract class Command {
	protected Action _eAction;

//	public void setAction(Action e){
//		_eAction = e;
//	}

	public Action getAction(){
		return _eAction;
	}
}
