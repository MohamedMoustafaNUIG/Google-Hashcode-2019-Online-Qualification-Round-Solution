import java.util.*;
public class SortByOrient implements Comparator<Photo>
{

    public int compare(Photo a, Photo b){
        if(a.isHorizontal())
        {
            if(b.isHorizontal())
            {
                return 0;
            }else if(!b.isHorizontal())
            {
                return 1;
            }
        }
        else
            {
                if(b.isHorizontal())
                {
                    return -1;
                }else if(!b.isHorizontal())
                {
                    return 0;
                }
            }

            return 0;
    }
}

