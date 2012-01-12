package vocab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import android.util.Log;

/**
 * Conducts a test of all words in the vocabulary, in random order
 */
public class SimpleTest<E extends RightWrong> implements Tester{

	private ArrayList<E> testlist;
	private int currentindex;
	private E currentword;
	private int num;
	
	/**
	 * Instantiates a new simple test.
	 *
	 * @param wlist the WordList
	 */
	public SimpleTest(ArrayList<E> wlist, String snum){
		testlist = new ArrayList<E>();
		testlist.addAll((Collection<? extends E>) wlist);
		currentindex = -1;
		currentword = null;
		num = 10;
		try{
			num = Integer.parseInt(snum);
		}catch (Exception e){
			//JOptionPane.showMessageDialog(null, "Invalid value. Will test 10 words.");
			num = 10;
		}
		if (num > wlist.size()){
			//JOptionPane.showMessageDialog(null, "The number is larger than the size of the list! All words will be tested.");
			num = wlist.size();
		}
		if (num < 1){
			//JOptionPane.showMessageDialog(null, "Invalid value. Will test 10 words.");
			num = 10;
		}
	}
	
	/* (non-Javadoc)
	 * @see wordtester.Tester#getNext()
	 */
	public E getNext(){
		if (num < 1){
			return null;
		}
		if (testlist.size() == 0){
			return null;
		}
		Random generator = new Random();
		currentindex = generator.nextInt(testlist.size());
		currentword = testlist.get(currentindex);
		return currentword;
	}
	
	/* (non-Javadoc)
	 * @see wordtester.Tester#fail()
	 */
	public void fail(){
		currentword.wrong++;
	}
	
	/* (non-Javadoc)
	 * @see wordtester.Tester#success()
	 */
	public void success(){
		num--;
		currentword.right++;
		testlist.remove(currentindex);
	}
}
