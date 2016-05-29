package com.practice.files;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Arvin on 2016/5/29.
 */
public class FileInputStream {

    /***
     * 获取指定目录下的所有的文件（不包括文件夹），采用了递归遍历目录下的所有文件，返回文件名的list
     * @param obj
     * @return
     * @throws IOException
     */
    public static ArrayList<File> getListFiles(Object obj) throws IOException {
        File directory = null;
        if (obj instanceof File) {
            directory = (File) obj;
        } else {
            directory = new File(obj.toString());
        }
        ArrayList<File> files = new ArrayList<File>();
        if (directory.isFile()) {
            files.add(directory);
            return files;
        } else if (directory.isDirectory()) {
            File[] fileArr = directory.listFiles();
            for (int i = 0; i < fileArr.length; i++) {
                File fileOne = fileArr[i];
                files.addAll(getListFiles(fileOne));
            }
        }
        return files;
    }


    /***
     * 传入包含文件名的list，将文件添加到输入流，读取输入流，截取url并存储为list后返回。
     * @param list
     * @return
     * @throws IOException
     */
    public static List inputStream(List<File> list) throws IOException {
        SequenceInputStream sequenceInputStream = null;
        BufferedReader br = null;
        String line = null;
        List stringlist  = new ArrayList();

        for(int i = 0; i < list.size(); i++) {
            String input = new String(String.valueOf(list.get(i)));
            br = new BufferedReader(new FileReader(String.valueOf(new String(String.valueOf(list.get(i))))));
            while((line = br.readLine()) != null) {
                if(line.contains("Dialing connect") && line.contains("chained proxy")) {
                    //第一次截取，获得URL + 端口的格式
                    int preIndex = line.indexOf("Dialing connect") + 18;
                    int endIndex = line.indexOf("with (trusted)");
                    String strObject = line.substring(preIndex, endIndex).trim();
                    //第二次截取，去掉端口
                    preIndex = 0;
                    endIndex = strObject.indexOf(":");
                    strObject = strObject.substring(preIndex, endIndex).trim();

                    //去除制表符、换行、空格
                    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                    Matcher m = p.matcher(strObject);
                    String dest = m.replaceAll("");
                    stringlist.add(dest);
                    strObject = null;
                }
            }
        }
        return stringlist;
    }

    /***
     * 传入一个包含url的list和写入文件路径，去重，写入文件。
     * @param list
     * @param FileWrite
     * @throws IOException
     */
    public static void fileWrite(List list, String FileWrite) throws IOException {
        //去重
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        //return newList;
        //写入文件
        BufferedWriter bw = null;
        bw = new BufferedWriter(new FileWriter(FileWrite));
        for (int i = 0; i < newList.size(); i++) {
            bw.write(String.valueOf(newList.get(i)));
            bw.newLine();
        }
        bw.close();
    }

    public static void main(String[] args) throws IOException {
        String logPath = "C:\\Users\\Arvin\\AppData\\Roaming\\Lantern\\logs";
        //String logPath = "C:\\Logs\\test";
        String urlOutPath = logPath + "\\url.txt";

        try {
            //输入目录路径，获取目录下所有文件名
            ArrayList<File> logFiles=getListFiles(logPath);

            //输入文件名list。返回截取url后的list
            List url = inputStream(logFiles);

            //去重，写入
            fileWrite(url, urlOutPath);

            System.out.println("done");

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}
