package com.sally.exhibition;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetContentTask extends AsyncTask<String, Integer, Map<String, String>> {

    //정규식을
    private String imgUrlCheck = "\\b(https?):\\/\\/[A-Za-z0-9-+&@#\\/%?=~_|!:,.;]*";
    //private String tagCheck = "<p><br \\/></p>";
    //패턴으로 만들고
    private Pattern imgUrlPattern;
    //private Pattern tagPattern;
    private Matcher imgUrlmatcher;
    //private Matcher tagmatcher;
    private StringBuilder builder;

    private Context context;

    private ImageView content1ImageView, content2ImageView;
    private TextView contentsTextView;

    public SetContentTask(Context context){
        this.context=context;

        content1ImageView=(ImageView)((Activity)context).findViewById(R.id.content1ImageView);
        content2ImageView=(ImageView)((Activity)context).findViewById(R.id.content2ImageView);
        contentsTextView=(TextView) ((Activity)context).findViewById(R.id.contentsTextView);
    }

    @Override
    protected void onPostExecute(Map<String, String> map) {
        super.onPostExecute(map);
        //map 객체로 contents 뿌려주기!!
        String content1ImgUrl=map.get("content1ImgUrl");
        String content2ImgUrl=map.get("content2ImgUrl");

        if(content1ImgUrl!=null && content2ImgUrl!=null){ //content1ImgUrl content2ImgUrl가 null이 아니면
            content1ImageView.setVisibility(View.VISIBLE);
            content2ImageView.setVisibility(View.VISIBLE);
            contentsTextView.setVisibility(View.GONE);

            Glide.with(context)
                    .load(content1ImgUrl)
                    .into(content1ImageView);

            Glide.with(context)
                    .load(content2ImgUrl)
                    .into(content2ImageView);

        }else if(content1ImgUrl!=null && content2ImgUrl==null){//content1ImgUrl이 null이 아니면
            content1ImageView.setVisibility(View.VISIBLE);
            content2ImageView.setVisibility(View.GONE);
            contentsTextView.setVisibility(View.GONE);

            Glide.with(context)
                    .load(content1ImgUrl)
                    .into(content1ImageView);

        }else if(content1ImgUrl==null && content2ImgUrl!=null){//content2ImgUrl가 null이 아니면
            content1ImageView.setVisibility(View.GONE);
            content2ImageView.setVisibility(View.VISIBLE);
            contentsTextView.setVisibility(View.GONE);

            Glide.with(context)
                    .load(content2ImgUrl)
                    .into(content2ImageView);

        }else {//content1ImgUrl과 content2ImgUrl가 null 이면
            content1ImageView.setVisibility(View.GONE);
            content2ImageView.setVisibility(View.GONE);
            contentsTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected Map<String, String> doInBackground(String... strings) {
        String contents1=getContent(strings[0]);
        String contents2=getContent(strings[1]);
        Map<String, String> map=new HashMap<>();
        if(contents1!=null && contents2!=null){ //contents1과 contents2가 null이 아니면

            if(imgUrlPattern.matcher(contents1).matches()){
                //정규식과 매치가 되면 url을 map에 담기
                map.put("content1ImgUrl", contents1);
            }else{
                //정규식과 매치가 안 되면 null을 map에 담기
                map.put("content1ImgUrl", null);
            }

            if(imgUrlPattern.matcher(contents2).matches()){
                //정규식과 매치가 되면 url을 map에 담기
                map.put("content2ImgUrl", contents2);
            }else{
                //정규식과 매치가 안 되면 null을 map에 담기
                map.put("content2ImgUrl", null);
            }

        }else if(contents1!=null && contents2==null){//contents1이 null이 아니면

            if(imgUrlPattern.matcher(contents1).matches()){
                //정규식과 매치가 되면 url을 map에 담기
                map.put("content1ImgUrl", contents1);
            }else{
                //정규식과 매치가 안 되면 null을 map에 담기
                map.put("content1ImgUrl", null);
            }
            map.put("content2ImgUrl", null);

        }else if(contents1==null && contents2!=null){//contents2가 null이 아니면

            contents2=getContent(contents2);

            if(imgUrlPattern.matcher(contents2).matches()){
                //정규식과 매치가 되면 url을 map에 담기
                map.put("content2ImgUrl", contents2);
            }else{
                //정규식과 매치가 안 되면 null을 map에 담기
                map.put("content2ImgUrl", null);
            }
            map.put("content1ImgUrl", null);

        }else {//contents1과 contents2가 null 이면

            map.put("content1ImgUrl", null);
            map.put("content2ImgUrl", null);
        }

        return map;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        content1ImageView.setImageResource(R.drawable.loading);
        content2ImageView.setImageResource(R.drawable.loading);
    }

    public String getContent(String contents){
        //패턴으로 만들고
        imgUrlPattern= Pattern.compile(imgUrlCheck);
        //tagPattern=Pattern.compile(tagCheck);
        imgUrlmatcher=imgUrlPattern.matcher(contents);
        //tagmatcher=tagPattern.matcher(contents);

        builder=new StringBuilder();

        //이미지 url을 추출하기
        while(imgUrlmatcher.find()){
            builder.append(imgUrlmatcher.group());
        }

        if(imgUrlPattern.matcher(builder).matches()){//imgUrl 패턴과 맞으면
            return builder.toString(); //imgUrl 리턴
        }else { //tag 패턴과 일치하거나 null이면
            return null; //null 반환
        }

    }//getContent

}
