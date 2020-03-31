package com.sally.exhibition;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String contents1="<p><img src=\"http://culture.go.kr/upload/editor_upload/20190125181623798_11PN4U4N.jpg\" title=\"공포연극 조각\" alt=\"공포연극 조각\" style=\"vertical-align: baseline; border: 0px solid rgb(0, 0, 0);\" /></p>";
        StringBuilder sb=new StringBuilder();
        //정규식을
        String regEx = "\\b(https?):\\/\\/[A-Za-z0-9-+&@#\\/%?=~_|!:,.;]*";
        //패턴으로 만들고
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher1=null;
        //Matcher matcher2=null;
        if(contents1!=null){
            //패턴을 스트링과 매치시킨다.
            matcher1 = pattern.matcher(contents1);

            while(matcher1.find()){
                sb.append(matcher1.group());
            }
            System.out.println(sb.toString());
        }

    }
}
