
package com.hanar.kestrelmobileapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class SequenceExample implements View.OnClickListener {

    private Button userGuideButton;
    private MainActivity activity;
    private ZoomLayout zl;
    private List<Pair<View, String[]>> viewPairsList;
    private MaterialButton frontBackButton;
    private MaterialShowcaseSequence sequence;
    private boolean isUsingLocation = false;

    private static final String SHOWCASE_ID = "sequence example";

    public SequenceExample(MainActivity activity) {

        this.activity = activity;
        userGuideButton = activity.findViewById(R.id.userGuideButton);
        userGuideButton.setOnClickListener(this);

        zl = activity.findViewById(R.id.zoom_layout);
        View tempView;
        viewPairsList = new ArrayList<>();
        tempView = activity.findViewById(R.id.pressure_sensor);
        viewPairsList.add(Pair.create(tempView, new String[]{"", "חיישן לחץ"}));
        tempView = activity.findViewById(R.id.serial_number);
        viewPairsList.add(Pair.create(tempView, new String[]{"", "מספר סידורי של המכשיר"}));
        tempView = activity.findViewById(R.id.humidity_sensor);
        viewPairsList.add(Pair.create(tempView, new String[]{"", "חיישן לחות"}));
        tempView = activity.findViewById(R.id.battery_door);
        viewPairsList.add(Pair.create(tempView, new String[]{"בית הסוללה", "החלפת סוללה כאשר המסך מעומעם או לא נדלק בעקבות סוללה מרוקנת"}));
        tempView = activity.findViewById(R.id.temperature_thermistor);
        viewPairsList.add(Pair.create(tempView, new String[]{"חיישן טמפרטורה", "משמש למדידת הטמפרטורה במיקום הנוכחי"}));
        tempView = activity.findViewById(R.id.kestrelfan);
        viewPairsList.add(Pair.create(tempView, new String[]{"גלגל מאיץ", "משמש למדידת מהירות הרוח"}));
        tempView = activity.findViewById(R.id.powerButton);
        viewPairsList.add(Pair.create(tempView, new String[]{"כפתור הפעלה/תאורת מסך", "משמש להדלקת המכשיר, כיבוי המכשיר לאחר לחיצה של 3 שניות.\n לחיצה כאשר המכשיר מופעל תדליק תאורת מסך ל10 שניות, ותכבה במידה ונלחץ פעם נוספת לפני סוף ה-10 שניות."}));
        tempView = activity.findViewById(R.id.rightButton);
        viewPairsList.add(Pair.create(tempView, new String[]{"כפתור ניווט בין ערכים", "משמש למעבר בין הערכים הנמדדים ע\"י המכשיר "}));
        tempView = activity.findViewById(R.id.leftButton);
        viewPairsList.add(Pair.create(tempView, new String[]{"כפתור ניווט בין ערכים", "משמש למעבר בין הערכים הנמדדים ע\"י המכשיר. \n בלחיצה ביחד עם כפתור ההפעלה מפעיל מצב \"HOLD\" לעצירת מדידת ערכים והקפאת הערכים המופיעים על המסך. על מנת לצאת ממצב זה לחץ על הכפתור שוב ביחד עם כפתור ההפעלה"}));
        tempView = activity.findViewById(R.id.kestrelLight);
        viewPairsList.add(Pair.create(tempView, new String[]{"מסך - מהירות רוח", "מציג את מהירות הרוח הנמדדת, ניתן לזהות ע\"י אייקון הרוח בתחתית המסך, המהירות ביחידות של m/s"}));
        viewPairsList.add(Pair.create(tempView, new String[]{"מסך - טמפרטורה", "מציג את הטמפרטורה הנמדדת, ניתן לזהות ע\"י אייקון מד החום בתחתית המסך, הטמפרטורה ביחידות של מעלות צלזיוס"}));
        viewPairsList.add(Pair.create(tempView, new String[]{"מסך - צינת הרוח (עומס קור)", "מציג את עומס הקור- הטמפרטורה המורגשת בעקבות הרוח, ניתן לזהות ע\"י אייקון הרוח ולצידו מד החום בתחתית המסך, עומס הקור מציג טמפרטורה ביחידות של מעלות צלזיוס"}));
        viewPairsList.add(Pair.create(tempView, new String[]{"מסך - לחות יחסית", "מציג את הלחות הנמדדת, ניתן לזהות ע\"י אייקון טיפה ולצידו אחוז בתחתית המסך, הלחות מוצגת באחוזים"}));
        viewPairsList.add(Pair.create(tempView, new String[]{"מסך - עומס חום", "מציג את עומס החום - הטמפרטורה המורגשת בעקבות הלחות, ניתן לזהות ע\"י אייקון טיפה, לצד אחוז, לצד מד החום בתחתית המסך, הטמפרטורה ביחידות של מעלות צלזיוס"}));

        frontBackButton = activity.findViewById(R.id.frontBackButton);


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.userGuideButton) {


            MaterialShowcaseView.resetSingleUse(activity, SHOWCASE_ID);
            presentShowcaseSequence();



            // Toast.makeText(activity, "Showcase reset", Toast.LENGTH_SHORT).show();

        }
    }

    private void presentShowcaseSequence() {
        zl.resetZoom();


        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        sequence = new MaterialShowcaseSequence(activity,SHOWCASE_ID);


        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();

                switch (position) {
                    case 0: {
                        if (activity.getIsFront()) {
                            activity.changeFrontBackImage();
                        }
                        break;
                    }
                    case 4: {
                        if (!activity.getIsFront()) {
                            activity.changeFrontBackImage();
                        }
                        break;
                    }
                    case 9: {
                        activity.getKestrelLogic().invisibleKestrelButtons();
                        if (activity.getKestrelLogic().getIsPowerOn()) {
                            activity.getKestrelLogic().onPowerButtonPressed3Sec();
                        }
                        activity.getKestrelLogic().setDefaultMeasurement();
                        isUsingLocation = activity.getKestrelLogic().getLocationSettingItemState();
                        if (isUsingLocation) {
                            activity.getKestrelLogic().getLocationSettingItem().setChecked(false);
                            activity.getKestrelLogic().getLocationHandling().setIsRandomValues(true);
                        }
                        activity.getKestrelLogic().onPowerButtonClicked();
                        break;
                    }
                    case 10:
                    case 11:
                    case 12:
                    case 13: {
                        activity.getKestrelLogic().onRightButtonClick();
                        break;
                    }

                }
            }
        });
        sequence.setOnItemDismissedListener((itemView, position) ->
        {
           /* if(sequence.hasFired())
            {
                activity.getKestrelLogic().onPowerButtonPressed3Sec();
                activity.getKestrelLogic().visibleKestrelButtons();
                if(isUsingLocation)
                {
                    activity.getKestrelLogic().getLocationSettingItem().setChecked(true);
                    activity.getKestrelLogic().getLocationHandling().setIsRandomValues(false);

                }
                activity.getKestrelLogic().setDefaultMeasurement();
            }*/
        });

        sequence.setConfig(config);

        for (Pair<View, String[]> pair : viewPairsList) {
            switch (pair.first.getId()) {
                case R.id.serial_number:
                case R.id.kestrelLight:
                case R.id.powerButton:
                case R.id.leftButton:
                case R.id.rightButton: {
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(activity)
                                    .setSkipText("סגור מדריך")
                                    .setTarget(pair.first)
                                    .setDismissText("הבנתי")
                                    .setTitleText(pair.second[0])
                                    .setGravity(Gravity.TOP)
                                    .setContentText(pair.second[1])
                                    .withRectangleShape()
                                    .renderOverNavigationBar()
                                    .build());
                    break;
                }
                case R.id.kestrelfan: {
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(activity)
                                    .setSkipText("סגור מדריך")
                                    .setTarget(pair.first)
                                    .setDismissText("הבנתי")
                                    .setTitleText(pair.second[0])
                                    .setGravity(Gravity.TOP)
                                    .setContentText(pair.second[1])
                                    .withCircleShape()
                                    .renderOverNavigationBar()
                                    .build());
                    break;
                }
                default: {
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(activity)
                                    .setSkipText("סגור מדריך")
                                    .setTarget(pair.first)
                                    .setDismissText("הבנתי")
                                    .setTitleText(pair.second[0])
                                    .setGravity(Gravity.TOP)
                                    .setContentText(pair.second[1])
                                    .withCircleShape()
                                    .renderOverNavigationBar()
                                    .build()
                    );
                }
            }
        }

        sequence.start();

    }

}
