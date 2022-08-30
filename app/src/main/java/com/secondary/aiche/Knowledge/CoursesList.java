package com.secondary.aiche.Knowledge;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;


import com.secondary.aiche.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


// Put list of your Courses

public class CoursesList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_list);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager _sGridLayoutManager = new GridLayoutManager(CoursesList.this,
                2);
        recyclerView.setLayoutManager(_sGridLayoutManager);
        List<ItemObject> sList = getListItemData();

        /*if (CoursesType.choice == 0){
            GridViewAdapter rcAdapter = new GridViewAdapter(
                    CoursesList.this, sList);
            recyclerView.setAdapter(rcAdapter);

        }else {*/
        SampleRecyclerViewAdapter rcAdapter = new SampleRecyclerViewAdapter(
                    CoursesList.this, sList);
        // set LayoutManager for your RecyclerView header.attachTo(recyclerView);
        recyclerView.setAdapter(rcAdapter);

    }

    private List<ItemObject> getListItemData() {
        List<ItemObject> listViewItems = new ArrayList<ItemObject>();

        if (CoursesType.choice == 1) {//Technical
            listViewItems.add(new ItemObject(R.drawable.aspen_hysys, "Aspen Hysys"));
            listViewItems.add(new ItemObject(R.drawable.process_control, "Process Control"));
            listViewItems.add(new ItemObject(R.drawable.corrosion, "Corrosion"));
            listViewItems.add(new ItemObject(R.drawable.crude_oil, "Crude Oil"));
            listViewItems.add(new ItemObject(R.drawable.nh3, "Ammonia Fertilizer"));
            listViewItems.add(new ItemObject(R.drawable.urea, "Urea Fertilizer"));
            listViewItems.add(new ItemObject(R.drawable.fluid_mechanics, "Fluid Mechanics"));
            listViewItems.add(new ItemObject(R.drawable.furnaces, "Furnaces"));
            listViewItems.add(new ItemObject(R.drawable.hazop, "HAZOP"));
            listViewItems.add(new ItemObject(R.drawable.heat_exchanger, "Heat Exchangers"));
            listViewItems.add(new ItemObject(R.drawable.natural_gas_processing, "Natural Gas Processing"));
            listViewItems.add(new ItemObject(R.drawable.chemical_reactors_design, "Chemical reactors Design"));
            listViewItems.add(new ItemObject(R.drawable.pumps, "Pumps"));
            listViewItems.add(new ItemObject(R.drawable.thermodynamics, "Thermodynamics"));
            listViewItems.add(new ItemObject(R.drawable.valves, "Valves"));
            listViewItems.add(new ItemObject(R.drawable.water_treatment, "Water Treatment"));
            listViewItems.add(new ItemObject(R.drawable.process_design, "Process Design"));

        }else if (CoursesType.choice == 2){// Arabic Non Technical
            listViewItems.add(new ItemObject(R.drawable.cv_writing, "كتابة السيره الذاتيه"));
            listViewItems.add(new ItemObject(R.drawable.emotional_intelligence, "الذكاء العاطفي"));
            listViewItems.add(new ItemObject(R.drawable.english, "English"));
            listViewItems.add(new ItemObject(R.drawable.interview_skills, "مقابلة العمل"));
            listViewItems.add(new ItemObject(R.drawable.negotiation_skills, "التفاوض"));
            listViewItems.add(new ItemObject(R.drawable.time_managment, "إدارة الوقت"));
            listViewItems.add(new ItemObject(R.drawable.public_speaking, "التحدث أمام الجمهور"));

        } else {//English Non Technical
            listViewItems.add(new ItemObject(R.drawable.cv_writing, "CV Writing"));
            listViewItems.add(new ItemObject(R.drawable.emotional_intelligence, "Emotional Intelligence"));
            listViewItems.add(new ItemObject(R.drawable.how_to_learn, "How to Learn"));
            listViewItems.add(new ItemObject(R.drawable.interview_skills, "Interview"));
            listViewItems.add(new ItemObject(R.drawable.negotiation_skills, "Negotiation"));
            listViewItems.add(new ItemObject(R.drawable.time_managment, "Time Management"));
            listViewItems.add(new ItemObject(R.drawable.public_speaking, "Public Speaking"));
            listViewItems.add(new ItemObject(R.drawable.leadership, "Leadership"));
        }

        return listViewItems;

    }
}