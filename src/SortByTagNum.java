import java.util.*;
public class SortByTagNum implements Comparator<Slide>
{

    public int compare(Slide a, Slide b){
        if(a.getTags().size()==b.getTags().size()){return 0;}
        if(a.getTags().size()>b.getTags().size()){return 1;}
        if(a.getTags().size()<b.getTags().size()){return -1;}

        return 0;
    }
}