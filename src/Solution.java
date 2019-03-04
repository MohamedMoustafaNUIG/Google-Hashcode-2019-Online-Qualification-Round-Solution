import java.io.*;
import java.util.*;

public class Solution {
    private ArrayList<Photo> photos = new ArrayList();
    private ArrayList<Slide> slides = new ArrayList();
    private ArrayList<Slide> sorted = new ArrayList();
    public int index;
    private static String[] files={"a_example","b_lovely_landscapes","c_memorable_moments","d_pet_pictures","e_shiny_selfies"};
    //private static String[] files={"d_pet_pictures","e_shiny_selfies"};
    public static void main(String[] args) {
        for(String s:files)
        {
            String input_file="C:\\Users\\Mohamad\\Desktop\\HashcodeJava\\"+s+".txt";
            String output_file="C:\\Users\\Mohamad\\Desktop\\HashcodeJava\\output"+s.charAt(0)+".txt";
            Solution sol = new Solution(input_file,output_file);
        }
        /*String input_file="C:\\Users\\Mohamad\\Desktop\\HashcodeJava\\b_lovely_landscapes.txt";
        String output_file="C:\\Users\\Mohamad\\Desktop\\HashcodeJava\\outputb.txt";
        Solution sol = new Solution(input_file,output_file);*/
    }

    public Solution(String input_file,String output_file    ) {
        /* reading in */

        try {
            BufferedReader br = new BufferedReader(new FileReader(input_file));
            int photoNum = Integer.parseInt(br.readLine());
            for (int i = 0; i < photoNum; i++) {
                String[] line = br.readLine().split(" ");
                Photo temp = new Photo();
                temp.setId(i);      // sets PHOTO ID
                if (line[0].equals("H")) temp.setHorizontal(true);
                else temp.setHorizontal(false);
                // sets the tagNum
                temp.setTagNum(Integer.parseInt(line[1]));
                //
                for (int j = 0; j < Integer.parseInt(line[1]); j++) {
                    temp.addArray(line[2 + j]);
                }
                photos.add(temp);
            }

            Collections.sort(photos,new SortByOrient());
            int index = 0;
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
            Collections.sort(slides,new SortByTagNum());
            for(int x=0;x<slides.size();x++)
            {
                slides.get(x).setId(x);
            }
            int start=0;
            int end=9999;
            ArrayList<ArrayList<Slide>> buckets = new ArrayList();
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
                    start+=10000;
                    end+=10000;
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
            }

            for(ArrayList<Slide> temp:buckets)
            {
                dynamicProgramming(temp);
            }

            printToFile(output_file);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

        /* Writing to file */
        //sortSlide(slides);
    }

    public void dynamicProgramming(ArrayList<Slide> input)
    {
        int [][] scores = new int[input.size()][input.size()];
        int[] used=new int[input.size()];
        int max=0;
        int maxI=0;
        int maxJ=0;

        for(int i=0;i<input.size();i++)
        {
            for(int j=i;j<input.size();j++)
            {
                if(i==j)
                {
                    scores[i][j]=0;
                    scores[j][i]=0;
                }else
                    {
                        ArrayList<String> tags_1=new ArrayList();
                        for(String temp:input.get(i).getTags())
                        {
                            tags_1.add(temp);
                        }
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
        while(Arrays.stream(used).sum()!=used.length)
        {
            addPath(scores,used,input);
        }


    }

    public void addPath(int[][] input,int[] used,ArrayList<Slide> bucket)
    {   int max=0;
        int currentI=sorted.get(sorted.size()-1).getId();
        int maxJ=-1;

            for(int j=0;j<input.length;j++)
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

