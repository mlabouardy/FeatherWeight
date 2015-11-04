package checker.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.*;

public class ListUtil {

	public static <X,Y> List<X> mapFst(List<Tuple<X,Y>> list) {
		List<X> outList = new ArrayList<X>();
		
		for(Tuple<X,?> t : list) {
			outList.add(t.getX());
		}
		
		return outList;
		
	}
	
	public static <X,Y> List<Y> mapSnd(List<Tuple<X,Y>> list) {
		List<Y> outList = new ArrayList<Y>();
		
		for(Tuple<?,Y> t : list) {
			outList.add(t.getY());
		}
		
		return outList;
	}
    
    public static <X,Y> List<Tuple<X,Y>> zip(List<X> xs, List<Y> ys) {
        Iterator<X> xi = xs.iterator();
        Iterator<Y> yi = ys.iterator();
        
        List<Tuple<X,Y>> out = new ArrayList<Tuple<X,Y>>(min(xs.size(), ys.size()));
        
        while(xi.hasNext()) {
            if(yi.hasNext()) {
                out.add(new Tuple<X,Y>(xi.next(), yi.next()));
                continue;
            } 
            break;
        }
        
        return out;
    }
}
