package rs.resources;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GameMapReference implements Serializable{
	private int _iId;
	private String _sName;

	public GameMapReference(){

	}
	public GameMapReference(String sName){
		setName(sName);
	}
	public GameMapReference(int iId){
		setId(iId);
	}
	public GameMapReference(String sName, int iId){
		setId(iId);
		setName(sName);
	}

	public void setName(String value){
		_sName = value;
	}
	public String getName(){
		return _sName;
	}

	public void setId(int i){
		_iId = i;
	}
	public int getId(){
		return _iId;
	}

	@Override
	public String toString(){
		return getId() + ": " + getName();
	}

	public static GameMapReference read(ObjectInputStream oFile){
		GameMapReference o = new GameMapReference();
		try{
			o.setId(oFile.readInt());
			o.setName((String)oFile.readObject());
		}catch(java.io.EOFException ex){
			return null;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return o;
	}

	public void write(ObjectOutputStream oFile){
		try{
			oFile.writeInt(_iId);
			oFile.writeObject(_sName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
