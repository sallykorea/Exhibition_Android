package com.sally.exhibition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.Toast;

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

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private int seq;
    private String title, startDate, endDate, place, realmName, area, subTitle, thumbNail,
             price, contents1, contents2, url, phone, gpsX, gpsY, imgUrl, placeUrl, placeAddr;
    private  ImageView cantLoadMap;
    private SupportMapFragment mapFragment;
    private TextView phonetextView, placeTextView;

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
            placeAddr=detailObj.getString("placeAddr");
            placeUrl=detailObj.getString("placeUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView titleTextView=findViewById(R.id.titleTextView);
        ImageView thumbNail=findViewById(R.id.thumbNail);
        TextView startDateTextView=findViewById(R.id.startDateTextView);
        TextView endDateTextView=findViewById(R.id.endDateTextView);
        TextView priceTextView=findViewById(R.id.priceTextView);
        phonetextView=findViewById(R.id.phonetextView);
        Button paymentBtn=findViewById(R.id.paymentBtn);
        Button lIkeBtn=findViewById(R.id.lIkeBtn);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        cantLoadMap=findViewById(R.id.cantLoadMap);
        placeTextView=findViewById(R.id.placeTextView);
        if(!placeUrl.equals("null")){
            placeTextView.setClickable(true);
        }


        titleTextView.setText(Html.fromHtml(title));
        Glide.with(this).load(imgUrl).into(thumbNail);
        placeTextView.setText(place);
        startDateTextView.setText(startDate);
        endDateTextView.setText(endDate);
        priceTextView.setText(price);
        phonetextView.setText(phone);

        phonetextView.setOnClickListener(this);
        placeTextView.setOnClickListener(this);
        paymentBtn.setOnClickListener(this);
        lIkeBtn.setOnClickListener(this);

        mapFragment.getView().setVisibility(View.GONE);
        cantLoadMap.setVisibility(View.GONE);

        mapFragment.getMapAsync(this);

        new SetContentTask(this).execute(contents1, contents2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.placeTextView:
                Intent placeUrlIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(placeUrl));
                startActivity(placeUrlIntent);
                break;
            case R.id.phonetextView:
                Intent CallIntent=new Intent();
                CallIntent.setAction(Intent.ACTION_DIAL);
                CallIntent.setData(Uri.parse("tel:"+phonetextView.getText()));
                startActivity(CallIntent);
                break;
            case R.id.paymentBtn:
                Intent paymentIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(paymentIntent);
                break;
            case R.id.lIkeBtn:

                break;

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(!gpsX.equals("null") && !gpsY.equals("null")){ //gpsX 와 gpsY 가 null이 아닐 때
            //google map을 보여주고, imageView(cantLoadMap)를 숨긴다.
            mapFragment.getView().setVisibility(View.VISIBLE);
            cantLoadMap.setVisibility(View.GONE);
            LatLng place = new LatLng(Double.parseDouble(gpsX), Double.parseDouble(gpsY));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15));
            mMap.addMarker(new MarkerOptions().position(place).title(this.place));
        }else { //gpsX 와 gpsY 가 null일 때
            //google map을 숨기고, imageView(cantLoadMap)를 보여준다.
            mapFragment.getView().setVisibility(View.GONE);
            cantLoadMap.setVisibility(View.VISIBLE);
        }

    }

}
