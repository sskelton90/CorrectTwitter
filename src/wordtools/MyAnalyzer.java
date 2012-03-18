package wordtools;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;


public class MyAnalyzer extends Analyzer { 

    public TokenStream tokenStream(String fieldName, Reader reader) { 
        return 
                new StandardFilter(
                        new StandardTokenizer(Version.LUCENE_30, reader)  
                ); 
    } 

    private static void printTokens(String string) throws IOException { 
        TokenStream ts = new MyAnalyzer().tokenStream("default", new 
StringReader(string)); 
        TermAttribute termAtt = ts.getAttribute(TermAttribute.class); 
        while(ts.incrementToken()) { 
            System.out.print(termAtt.term()); 
            System.out.print(" "); 
        } 
        System.out.println(); 
    } 

    public static void main(String[] args) throws IOException { 
        printTokens("one_two_three");           // prints "one two three" 
        printTokens("four4_five5_six6");        // prints "four4_five5_six6" 
        printTokens("seven7_eight_nine");       // prints "seven7_eight nine" 
        printTokens("ten_eleven11_twelve");     // prints "ten_eleven11_twelve" 
    } 
} 

