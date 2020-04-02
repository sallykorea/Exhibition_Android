package com.sally.exhibition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int seq;
    private String title, startDate, endDate, place, realmName, area, subTitle, thumbNail,
             price, contents1, contents2, url, phone, gpsX, gpsY, imgUrl, placeUrl, placeAddr;
    private  ImageView content1ImageView, content2ImageView;
    private TextView contentsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String detailHref=getIntent().getStringExtra("detailHref");

        try {
            JSONObject detailObj=new JSONObject(detailHref);
            seq=detailObj.getInt("seq");
            title=detailObj.getString("title");
            startDate=detailObj.getString("startDate");
            endDate=detailObj.getString("endDate");
            place=detailObj.getString("place");
            realmName=detailObj.getString("realmName");
            area=detailObj.getString("area");
            subTitle=detailObj.getString("subTitle");
            thumbNail=detailObj.getString("thumbNail");
            price=detailObj.getString("price");
            contents1=detailObj.getString("contents1");
            contents2=detailObj.getString("contents2");
            url=detailObj.getString("url");
            phone=detailObj.getString("phone");
            gpsY=detailObj.getString("gpsX");//DB에 X,Y 좌표가 반대로 들어가 있음
            gpsX=detailObj.getString("gpsY");
            imgUrl=detailObj.getString("imgUrl");
            placeUrl=detailObj.getString("placeUrl");
            placeAddr=detailObj.getString("placeAddr");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView titleTextView=findViewById(R.id.titleTextView);
        ImageView thumbNail=findViewById(R.id.thumbNail);
        TextView placeTextView=findViewById(R.id.placeTextView);
        TextView startDateTextView=findViewById(R.id.startDateTextView);
        TextView endDateTextView=findViewById(R.id.endDateTextView);
        TextView priceTextView=findViewById(R.id.priceTextView);
        TextView phonetextView=findViewById(R.id.phonetextView);
        Button paymentBtn=findViewById(R.id.paymentBtn);
        Button lIkeBtn=findViewById(R.id.lIkeBtn);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        contentsTextView=findViewById(R.id.contentsTextView);
        content1ImageView=findViewById(R.id.content1ImageView);
        content2ImageView=findViewById(R.id.content2ImageView);

        titleTextView.setText(Html.fromHtml(title));
        Glide.with(this).load(imgUrl).into(thumbNail);
        placeTextView.setText(place);
        startDateTextView.setText(startDate);
        endDateTextView.setText(endDate);
        priceTextView.setText(price);
        phonetextView.setText(phone);

        mapFragment.getMapAsync(this);
        //Log.d("content", contents1);

        new SetContentTask().execute(contents1, contents2);

    }


    //정규식을
    private String imgUrlCheck = "\\b(https?):\\/\\/[A-Za-z0-9-+&@#\\/%?=~_|!:,.;]*";
    private String tagCheck = "<p><br \\/></p>";
    //패턴으로 만들고
    private Pattern imgUrlPattern;
    private Pattern tagPattern;
    private Matcher imgUrlmatcher;
    private Matcher tagmatcher;
    private StringBuilder builder;

    public class SetContentTask extends AsyncTask<String, Integer, Map<String, String>>{

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

                Glide.with(DetailActivity.this)
                        .load(content1ImgUrl)
                        .into(content1ImageView);

                Glide.with(DetailActivity.this)
                        .load(content2ImgUrl)
                        .into(content2ImageView);

            }else if(content1ImgUrl!=null && content2ImgUrl==null){//content1ImgUrl이 null이 아니면
                content1ImageView.setVisibility(View.VISIBLE);
                content2ImageView.setVisibility(View.GONE);
                contentsTextView.setVisibility(View.GONE);

                Glide.with(DetailActivity.this)
                        .load(content1ImgUrl)
                        .into(content1ImageView);

            }else if(content1ImgUrl==null && content2ImgUrl!=null){//content2ImgUrl가 null이 아니면
                content1ImageView.setVisibility(View.GONE);
                content2ImageView.setVisibility(View.VISIBLE);
                contentsTextView.setVisibility(View.GONE);

                Glide.with(DetailActivity.this)
                        .load(content2ImgUrl)
                        .into(content2ImageView);

            }else {//content1ImgUrl과 content2ImgUrl가 null 이면
                content1ImageView.setVisibility(View.GONE);
                content2ImageView.setVisibility(View.GONE);
                contentsTextView.setVisibility(View.VISIBLE);
            }

            /*if(content1ImgUrl!=null && content2ImgUrl!=null){

            }else if(imgUrl!=null){
                Glide.with(DetailActivity.this)
                        .load(imgUrl)
                        .into(contentImageView);
            }else if()

            if (contents==null){
                contentImageView.setImageResource(R.drawable.coming_soon);
            }*/

        }

        @Override
        protected Map<String, String> doInBackground(String... strings) {
            String contents1=strings[0];
            String contents2=strings[1];
            System.out.println(contents1);
            System.out.println(contents2);
            Map<String, String> map=new HashMap<>();

            if(contents1!=null && contents2!=null){ //contents1과 contents2가 null이 아니면
                contents1=getContent(contents1);
                contents2=getContent(contents2);
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

                contents1=getContent(contents1);

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

/*            if(contents2!=null){
                contents2=getContent(contents2);

                if (tagPattern.matcher(builder).matches()){
                    map.put("tag", builder.toString());
                }else{
                    map.put("tag",null);
                }
            }else { //contents2가 null이면 map에 null을 담기
                map.put("tag", null);
            }*/

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
            imgUrlPattern=Pattern.compile(imgUrlCheck);
            tagPattern=Pattern.compile(tagCheck);
            imgUrlmatcher=imgUrlPattern.matcher(contents);
            tagmatcher=tagPattern.matcher(contents);

            builder=new StringBuilder();

            //이미지 url을 추출하기
            while(imgUrlmatcher.find()){
                builder.append(imgUrlmatcher.group());
            }

            /*//<p><br \/></p> 이 들어 있는지 확인 및 추출
            while (tagmatcher.find()){
                builder.append(tagmatcher.group());
            }*/

            if(imgUrlPattern.matcher(builder).matches()){//imgUrl 패턴과 맞으면
                return builder.toString(); //imgUrl 리턴
            }else { //tag 패턴과 일치하거나 null이면
                return null; //null 반환
            }

        }//getContent

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng place = new LatLng(Double.parseDouble(gpsX), Double.parseDouble(gpsY));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15));
        mMap.addMarker(new MarkerOptions().position(place).title(this.place));
    }

}
