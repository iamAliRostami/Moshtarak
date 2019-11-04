package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactDeveloperActivity extends AppCompatActivity {
    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    Context context;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ali.rostami33@gmail.com", "infoshamsaii@gmail.com", "mantera.sh@gmail.com"});
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.contact_developer_activity);
        ButterKnife.bind(this);
        context = this;
        FontManager fontManager = new FontManager(context);
        fontManager.setFont(relativeLayout);
        linearLayout1.setOnClickListener(onClickListener);
        linearLayout2.setOnClickListener(onClickListener);
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        YoYo.with(Techniques.Tada)
//                .duration(1000)
//                .repeat(2)
//                .playOn(textViewInfo);
//        imageView1.animate()
//                .scaleX(2f)
//                .scaleY(2f)
//                .translationYBy(170f)
//                .rotation(360)
//                .setDuration(2000);
    }
}
