
/**
 * Write a description of MarkovWord here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
public class MarkovWord implements IMarkovModel
{
    private String[] myText;
    private Random myRandom;
    private int myOrder;
    
    public MarkovWord(int order) 
    {
        myRandom = new Random();
        myOrder = order;
    }
    
    public void setRandom(int seed) 
    {
        myRandom = new Random(seed);
    }
    
    public void setTraining(String text)
    {
        myText = text.split("\\s+");
    }
    
    public String getRandomText(int numWords)
    {
        StringBuilder sb = new StringBuilder();
        String [] gramArr = new String[myOrder]; 
        int index = myRandom.nextInt(myText.length-myOrder);  
        for(int i=0;i<myOrder;i++)
        {
            gramArr[i] = myText[index+i];
        }
        WordGram gramKey = new WordGram(gramArr,0,myOrder);
        for(int i=0; i<myOrder;i++)
        {
            sb.append(gramKey.wordAt(i));
            sb.append(" ");
        }
        for(int k=0; k < numWords-myOrder; k++)
        {
            ArrayList<String> follows = getFollows(gramKey);
            if (follows.size() == 0) 
            {
                break;
            }
            index = myRandom.nextInt(follows.size());
            String next = follows.get(index);
            sb.append(next);
            sb.append(" ");
            gramKey = gramKey.shiftAdd(next);
        }
        return sb.toString().trim();
    }
    
    private int indexOf(String [] words, WordGram target, int start)
    {
        for(int i=start; i<words.length;i++)
        {
            WordGram temp = takeInRange(words,i);
            if(temp.equals(target))
            {
                return i;
            }
        }
        return -1;
    }
    private WordGram takeInRange(String [] words, int start)
    {
        String [] arr = new String [myOrder];
        for(int i=0 ; i<myOrder; i++)
        {
            if(start+i<words.length)
            arr[i] = words[start+i];
        }
        WordGram out = new WordGram(arr,0,myOrder);
        return out;
    }
    
    private ArrayList<String> getFollows(WordGram kGram) 
    {
        ArrayList<String> follows = new ArrayList<String>();
        int pos =0;
        while(pos<myText.length)
        {
            int indexOf = indexOf(myText,kGram,pos);
            if(indexOf ==-1)
            {
                break;
            }
            pos = indexOf+myOrder;
            if(pos<myText.length)
            {
                follows.add(myText[pos]);
            }
        }
        return follows;
    }
}
