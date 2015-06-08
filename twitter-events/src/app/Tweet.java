package app;

import java.util.Date;

public class Tweet {
	int ID;
	Date creationDate;
	String content;
	
	public Tweet(int _ID, String _date, String _content){
		ID = _ID;
		content = _content;
	}
}
