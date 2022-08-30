package com.secondary.aiche.Knowledge;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.domain.firebaseapp.R;

import androidx.recyclerview.widget.RecyclerView;


// Just place holder for your course


public class SampleViewHolders extends RecyclerView.ViewHolder implements
        View.OnClickListener {
    public ImageView courseImage;
    public TextView courseTitle;
    public static int expertNum;
    public static String url;
    Context context;

    public SampleViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        //if (CoursesType.choice == 0){
         //   courseImage = (ImageView) itemView.findViewById(R.id.expert_img);
          //  courseTitle = (TextView) itemView.findViewById(R.id.expert_name);
        //}else{}
            courseImage = (ImageView) itemView.findViewById(R.id.course_image);
            courseTitle = (TextView) itemView.findViewById(R.id.course_title);}


    @Override
    public void onClick(View view) {

        context = view.getContext();

        switch (courseTitle.getText().toString()) {
            case "CV Writing":
                url = "https://www.youtube.com/watch?v=_fP43gcBywU&feature=share";

                break;
            case "How to Learn":
                url = "https://www.youtube.com/playlist?list=PLievC1UeaSOD3EBaJaHstTKIXS51Sfjwo";
                
                break;
            case "Corrosion":
                url = "https://www.youtube.com/playlist?list=PLvfdyaivecUGuljfNKj0qcfITjTggsWAI";
                
                break;
            case "Chemical reactors Design":
                url = "https://www.youtube.com/playlist?list=PL25CBC8287575CFB4";
                
                break;
            case "Crude Oil":
                url = "https://www.youtube.com/playlist?list=PLXGHfCedMmsO0dIXuOgD7vwO-ISzGHJRx";
                
                break;
            case "Ammonia Fertilizer":
                url = "https://www.youtube.com/playlist?list=PLk_I1GyIyG8BKhAYy3uFsfhn7T5QrhMmb";
                
                break;
            case "Urea Fertilizer":
                url = "https://www.youtube.com/playlist?list=PLVLQRIUp6E5A6v3xCefE5gOKztLGsh1gT";
                
                break;
            case "Fluid Mechanics":
                url = "https://www.youtube.com/playlist?list=PLD9pq73a8huHl1ZuWKuxo9theiqs0OtBV";
                
                break;
            case "Furnaces":
                url = "https://www.youtube.com/playlist?list=PL7R01saoeOgHM3F87YBKoivBuP3dl1EQS";
                
                break;
            case "HAZOP":
                url = "https://www.youtube.com/playlist?list=PLz21YY8a3NK6w5Vf9xpGOhLC30J9ocLsx";
                
                break;
            case "Heat Exchangers":
                url = "https://www.youtube.com/playlist?list=PLFwsynB0PSxSVMGijPC-mQELFkjvXdlDI";
                
                break;
            case "Natural Gas Processing":
                url = "https://www.youtube.com/playlist?list=PLWizASkkbIXiqO9L6PEGSRc0r_tK0p2rJ";
                
                break;
            case "Process Control":
                url = "https://www.youtube.com/playlist?list=PLF404D44A280B5C77";
                
                break;
            case "Pumps":
                url = "https://www.youtube.com/playlist?list=PLiVOJXxKgsZiejVJy9BInUsX3xkXUSgd3";
                
                break;
            case "Thermodynamics":
                url = "https://www.youtube.com/playlist?list=PLwdnzlV3ogoVnCnIfjDHng_8biZSUEYtK";
                
                break;
            case "Valves":
                url = "https://www.youtube.com/playlist?list=PLjoH8XiKuSWlZA8IMStMGEbtMHx7WrU5s";
                
                break;
            case "Water Treatment":
                url = "https://www.youtube.com/playlist?list=PLbMMX_vdzydJjaLpAaASOY7nrzU7nFSHq";
                
                break;
            case "Emotional Intelligence":
                url = "https://youtu.be/auXNnTmhHsk";

                break;
            case "Interview":
            url = "https://www.youtube.com/playlist?list=PLvby6pHU7GVYvjquXNfNjc8Sjoe1qxppK";

                break;
            case "Leadership":
                url = "https://www.youtube.com/playlist?list=PLD26358825FD23EE1";
                
                break;
            case "Negotiation":
                url = "https://www.youtube.com/watch?v=hev-00t2o3E&feature=share";

                break;
            case "Time Management":
                url = "https://www.youtube.com/playlist?list=PLvby6pHU7GVa0Zy0rRJUsax-J0UyrV8xv";
                
                break;
            case "Public Speaking":
                url = "https://www.youtube.com/playlist?list=PL8EEC66CC5F02545C";
                
                break;
            case "Aspen Hysys":
                url = "https://www.youtube.com/playlist?list=PLUtRFpyY1F_QwTrKK1fdqf0EsL8cktZ1X";
                
                break;
            case "Compressors":
                url = "https://www.youtube.com/playlist?list=PL-ZHuJr4Hppc_mLBvnGSP11e8eZXhV0o-";
                
                break;
            case "Process Design":
                url = "https://www.youtube.com/playlist?list=PL4xAk5aclnUjEuE_fvbyEts_oBpHYcwLY";
                
                break;
            case "English":
                url = "https://www.youtube.com/channel/UCHrD4qdeQc1BbpG6EPqDBcA";

                break;
            case "التحدث أمام الجمهور":
                url = "https://www.youtube.com/playlist?list=PLJX-3-8cbm2UoxJdTBLtJDOLjLEdRE3vn";

                break;
            case "إدارة الوقت":
                url = "https://www.youtube.com/playlist?list=PLJX-3-8cbm2WkXfl4GPbzhPg_myHB4WSg";

                break;
            case "التفاوض":
                url = "https://www.youtube.com/playlist?list=PL-w6pQX5Vjlx4KDX-ySAOk7xizIkm2kWj";

                break;
            case "القيادة":
                url = "https://drive.google.com/folderview?id=1_RyUB1Vlu-DEc6Jp5TZGjbdpY5Bru0Hk";

                break;
            case "مقابلة العمل":
                url = "https://www.youtube.com/playlist?list=PLJX-3-8cbm2Vm8ATRbyaRPnkAPO2t8FiV";

                break;
            case "كتابة السيره الذاتيه":
                url = "https://www.youtube.com/playlist?list=PLJX-3-8cbm2UHrAqKpg9D4Ykjp0X1BZNR";

                break;
            case "الذكاء العاطفي":
                url = "https://www.youtube.com/playlist?list=PLJX-3-8cbm2XJQX1XlmgsoRawRfOTH2I-";

                break;
            default:
                Toast.makeText(view.getContext(),
                        "Doesn't match any choice " + getPosition(), Toast.LENGTH_SHORT)
                        .show();
                return; // TODO : Useless but make sure later
        }
        goToCourse();
    }

    private void goToCourse(){
        /*if (CoursesType.choice == 0){
            Intent intent = new Intent(context, Chat.class);
            context.startActivity(intent);
        }else{*/
            Intent intent = new Intent(context, CourseWebView.class);
            context.startActivity(intent);}
}
