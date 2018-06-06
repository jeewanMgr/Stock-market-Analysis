package classprojects;
import java.util.*;


public class barArray {

	//has member Vector of bars
	Vector<Bar> list;
		public barArray() {
			list = new Vector<Bar>(400);
		}
		public barArray(int s) {
			list = new Vector<Bar>(s);
		}
		public barArray(int s, int incr) {
			list = new Vector<Bar>(s,incr);
		}
		//method to insert a bar at specific index
		public void insert(Bar b) //call the Vector method insetElement
		{
		 list.addElement(b);	
		}
		public Bar elementat(int index)
		{
			return list.elementAt(index);
		}
		public int size()
		{
			return list.size();
		}
}
