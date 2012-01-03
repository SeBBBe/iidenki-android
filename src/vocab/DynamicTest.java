package vocab;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import vocab.RightWrong;


/**
 * Conducts a dynamic test, testing the n most difficult words
 * based on previous performance by the user.
 */
public class DynamicTest<E extends RightWrong> implements Tester{

	private ArrayList<E> testlist;
	private int currentindex;
	private E currentword;
	private String num;
	
	/**
	 * Instantiates a new dynamic test.
	 *
	 * @param wlist the WordList containing the complete vocabulary
	 * @param num 
	 */
	public DynamicTest(Collection wlist, String num){
		testlist = new ArrayList<E>();
		buildList(wlist);
		currentindex = -1;
		currentword = null;
		this.num = num;
	}

	private void buildList(Collection wlist) {
		testlist.addAll((Collection<? extends E>) wlist);
		int n = 10;
		try{
			n = Integer.parseInt(num);
		}catch (Exception e){
			//JOptionPane.showMessageDialog(null, "Invalid value. Will test 10 words.");
			n = 10;
		}
		if (n > testlist.size()){
			//JOptionPane.showMessageDialog(null, "The number is larger than the size of the list! All words will be tested.");
			n = testlist.size();
		}
		if (n < 1){
			//JOptionPane.showMessageDialog(null, "Invalid value. Will test 10 words.");
			n = 10;
		}
		Collections.sort(testlist);
		ArrayList<E> testlist2 = new ArrayList<E>();
		for (int i = 0; i < n; i++){
			testlist2.add(testlist.get(i));
		}
		testlist = testlist2;
	}
	
	/* (non-Javadoc)
	 * @see wordtester.Tester#getNext()
	 */
	public E getNext(){
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
		currentword.right++;
		testlist.remove(currentindex);
	}
}
