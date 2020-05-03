
/**
 * Write a description of EfficientMarkovWord here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
public class EfficientMarkovWord implements IMarkovModel
{
    private String[] myText;
    private Random myRandom;
    private int myOrder;
    private HashMap<Integer,ArrayList<String>> myMap;
    public EfficientMarkovWord(int order) 
    {
        myRandom = new Random();
        myOrder = order;
        myMap = new HashMap<Integer,ArrayList<String>>();
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
        int myHash = gramKey.hashCode();
        for(int i=0; i<myOrder;i++)
        {
            sb.append(gramKey.wordAt(i));
            sb.append(" ");
        }
        for(int k=0; k < numWords-myOrder; k++)
        {
            myMap = buildMap(myMap,myHash,gramKey);
            ArrayList<String> follows = myMap.get(myHash);
            if (follows.size() == 0) 
            {
                break;
            }
            index = myRandom.nextInt(follows.size());
            String next = follows.get(index);
            sb.append(next);
            sb.append(" ");
            gramKey = gramKey.shiftAdd(next);
            myHash = gramKey.hashCode();
        }
        myMap = buildMap(myMap,myHash,gramKey);
        return sb.toString().trim();
    }
    
    private int indexOf(String [] words, WordGram target, int start)
    {
        for(int i=start; i<words.length;i++)
        {
            WordGram temp = takeInRange(words,i);
            if(temp.wordAt(temp.length()-1)==null)
                break;
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
            {
                arr[i] = words[start+i];
            }
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
    public HashMap<Integer,ArrayList<String>> buildMap(HashMap<Integer,ArrayList<String>> hash, int key,WordGram word)
    {
        
        if(!myMap.containsKey(key))
        {
            myMap.put(key,getFollows(word));
        }
        return hash;
    }
    public void printHashMapInfo()
    {
        int maxArray = 0;
        ArrayList<Integer> keys = new ArrayList<Integer>();
        System.out.println("Number of keys in the HashMap :"+myMap.size());
        for(Integer key :myMap.keySet())
        {
            int temp = myMap.get(key).size() ;
            if(temp>=maxArray)
            {
                maxArray = temp;
            }
        }
        System.out.println("The maximum number of elements following a key is :"+maxArray);
        for(Integer key :myMap.keySet())
        {
            int temp = myMap.get(key).size() ;
            if(temp==maxArray)
            {
                keys.add(key);
            }
        }
        System.out.println("Keys that have the maximum size value ");
        for(Integer key : keys)
        {
           System.out.println(key);
        }
        System.out.println("All Keys in The Hash :  ");
        for(Integer key :myMap.keySet())
        {
            System.out.print(Integer.toHexString(key)+" ");
        }
    }
}
