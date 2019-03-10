import java.io.*;
import java.util.*;

public class Solution {
    private ArrayList<Photo> photos = new ArrayList();//get photos from txt files
    private ArrayList<Slide> slides = new ArrayList();//for storing greedy-style formed slides
    private ArrayList<Slide> sorted = new ArrayList();//for storing optimised-order slides
    private static String[] files={"a_example","b_lovely_landscapes","c_memorable_moments","d_pet_pictures","e_shiny_selfies"};
    public static void main(String[] args) {
        for(String s:files)
        {
            String input_file="C:\\Users\\...\\"+s+".txt";//replace with appropriate directory
            String output_file="C:\\Users\\...\\output"+s.charAt(0)+".txt";//replace with appropriate directory
            Solution sol = new Solution(input_file,output_file);
        }
    }

    public Solution(String input_file,String output_file    ) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(input_file));
            int photoNum = Integer.parseInt(br.readLine());
		//read in photos
            for (int i = 0; i < photoNum; i++) {
                String[] line = br.readLine().split(" ");
                Photo temp = new Photo();
                temp.setId(i);      // sets PHOTO ID
                if (line[0].equals("H")) temp.setHorizontal(true);
                else temp.setHorizontal(false);
                // sets the number of tags
                temp.setTagNum(Integer.parseInt(line[1]));

                for (int j = 0; j < Integer.parseInt(line[1]); j++) {
                    temp.addArray(line[2 + j]);
                }
                photos.add(temp);
            }

            Collections.sort(photos,new SortByOrient());//sorts photos by orientation
            int index = 0;//used to assign an id for each slide created
            for (int i = 0; i < photoNum; i++) {
                Slide temp = new Slide();

                if (i + 1 < photoNum) {
                    if (!photos.get(i).isHorizontal()) {
                        if ((!photos.get(i).isHorizontal()) && (!photos.get(i + 1).isHorizontal())) {
                            temp.addPhoto(photos.get(i));
                            temp.addPhoto(photos.get(i + 1));
                            i++;
                        } else {
                            temp.addPhoto(photos.get(i + 1));
                            i++;
                        }
                    } else {
                        temp.addPhoto(photos.get(i));
                    }

                } else if (photos.get(i).isHorizontal()) {
                    temp.addPhoto(photos.get(i));
                } else {
                    continue;
                }

                temp.setTags();
                temp.setId(index);
                index++;
                slides.add(temp);
            }

            Collections.sort(slides,new SortByTagNum());//sort slide by number of tags

            for(int x=0;x<slides.size();x++)
            {
                slides.get(x).setId(x);
            }//changes id of each slide to bee in order

            int start=0;//index of first slide in first bucket
            int end=14999;//index of last slide in first bucket;might not be used if N<end

            ArrayList<ArrayList<Slide>> buckets = new ArrayList();
		//break slides into several array lists (buckets) to accommodate heap space limits
		//each array list will be of size 10,000. Until the leftover slides are less
		//Then leave the leftovers as an arraylist
		//note: The larger the bucket,the higher the score (generally). But processing time increases as well.

            while(end!=slides.size())
            {
                if(end<slides.size()-1)
                {
                    ArrayList<Slide> temp = new ArrayList();
                    for(int y=start;y<end;y++)
                    {
                        temp.add(slides.get(y));
                    }
                    buckets.add(temp);
                    start+=15000;
                    end+=15000;
                }else
                    {
                        ArrayList<Slide> temp = new ArrayList();
                        for(int y=start;y<slides.size();y++)
                        {
                            temp.add(slides.get(y));
                        }
                        buckets.add(temp);
                        break;
                    }
            }

            for(ArrayList<Slide> temp:buckets)
            {
                for(int x=0;x<temp.size();x++)
                {
                    temp.get(x).setId(x);
                }
            }//sets id of each slide according to its position in respective bucket

            for(ArrayList<Slide> temp:buckets)
            {
                dynamicProgramming(temp);
            }//optimises slide order in each bucket

            printToFile(output_file);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public void dynamicProgramming(ArrayList<Slide> input)
    {
        int [][] scores = new int[input.size()][input.size()];//stores scores instead of recalculating each time
        int[] used=new int[input.size()];//stores indexes of used slides (i.e ones already optimised)
        int max=0;//maximum score,to decide which pair of slides are to be the first
        int maxI=0;//id of the first slide out of the first to-be pair
        int maxJ=0;//id of the second slide out of the first to-be pair

        for(int i=0;i<input.size();i++)
        {
            for(int j=i;j<input.size();j++)
            {
                if(i==j)
                {//diagonal
                    scores[i][j]=0;
                    scores[j][i]=0;
                }else
                    {
                        ArrayList<String> tags_1=new ArrayList();
                        for(String temp:input.get(i).getTags())
                        {
                            tags_1.add(temp);
                        }//loop used to avoid changin original value as Java uses pass-by-reference and not value
                        tags_1.retainAll(input.get(j).getTags());
                        int common = tags_1.size();
                        int only_first=input.get(i).getTags().size()-common;
                        int only_second=input.get(j).getTags().size()-common;
                        scores[i][j]=getLeast(common,only_first,only_second);
                        scores[j][i]=getLeast(common,only_first,only_second);
                        if(scores[i][j]>max)
                        {
                            max=scores[i][j];
                            maxI=i;
                            maxJ=j;
                        }
                    }
            }
        }
        sorted.add(input.get(maxI));
        sorted.add(input.get(maxJ));
        used[maxI]=1;
        used[maxJ]=1;
        while(Arrays.stream(used).sum()!=used.length)//the goal is to get all indexes to have value of 1 i.e all slides used
        {
            addPath(scores,used,input);
        }
    }

    public void addPath(int[][] input,int[] used,ArrayList<Slide> bucket)
    {   int max=0;//will store current maximum next transition score
        int currentI=sorted.get(sorted.size()-1).getId();//id of last pushed slide
        int maxJ=-1;

            for(int j=0;j<input.length;j++)//looks for the best next slide to attach
            {
                if(currentI==j)
                {
                    continue;
                }else if(used[j]==1)
                {
                    continue;
                }else
                {
                    if(input[currentI][j]>=max)
                    {
                        max=input[currentI][j];
                        maxJ=j;
                    }
                }
        }
        if(maxJ==-1)
        {
            for(int i=0;i<used.length;i++)
            {
                used[i]=1;
            }
        }else
            {
                sorted.add(bucket.get(maxJ));
                used[maxJ]=1;
            }

    }

    public int getLeast(int a,int b,int c)
    {
        if(a<b)
        {
            if(a<c)
            {
                return a;
            }else
                {
                    return c;
                }
        }else if(b<c)
        {
            return b;
        }else
            {
                return c;
            }
    }

    public void printToFile(String input)
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(input));
            int slideNum = sorted.size();
            writer.write(Integer.toString(slideNum));
            writer.write("\n");
            for(int i = 0; i<slideNum; i++) {
                String temp = "";
                for (int j = 0; j < sorted.get(i).getSize(); j++) {
                    temp += Integer.toString(sorted.get(i).getPhotoId(j));
                    temp += " ";
                }
                writer.write(temp);
                writer.write("\n");
            }

            writer.close();
        }catch(FileNotFoundException e) {
            System.out.println(e);
        }catch(IOException e) {
            System.out.println(e);
        }

    }

}

