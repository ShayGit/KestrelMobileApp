
package com.hanar.kestrelmobileapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.content.Context.MODE_PRIVATE;


public class SequenceExample implements View.OnClickListener {

    private Button userGuideButton;
    private MainActivity activity;
    private ZoomLayout zl;
    private List<Pair<View, String[]>> viewPairsList;
    private MaterialButton frontBackButton;
    private MaterialShowcaseSequence sequence;
    private boolean isUsingLocation = false;
    public static final String DEFAULT_MASK_COLOUR = "#dd335075";



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
        viewPairsList.add(Pair.create(tempView, new String[]{"בית הסוללה", "החלפת סוללה כאשר המסך מעומעם או לא נדלק בעקבות סוללה מרוקנת."}));
        tempView = activity.findViewById(R.id.temperature_thermistor);
        viewPairsList.add(Pair.create(tempView, new String[]{"חיישן טמפרטורה", "משמש למדידת הטמפרטורה במיקום הנוכחי."}));
        tempView = activity.findViewById(R.id.kestrelfan);
        viewPairsList.add(Pair.create(tempView, new String[]{"גלגל מאיץ", "משמש למדידת מהירות הרוח."}));
        tempView = activity.findViewById(R.id.powerButton);
        viewPairsList.add(Pair.create(tempView, new String[]{"כפתור הפעלה/תאורת מסך", "משמש להדלקת המכשיר, כיבוי המכשיר לאחר לחיצה של 3 שניות.\nלחיצה כאשר המכשיר מופעל תדליק תאורת מסך ל10 שניות ותכבה במידה ונלחץ פעם נוספת לפני סוף ה-10 שניות."}));
        tempView = activity.findViewById(R.id.rightButton);
        viewPairsList.add(Pair.create(tempView, new String[]{"כפתור ניווט בין ערכים", "משמש למעבר בין הערכים הנמדדים ע\"י המכשיר. "}));
        tempView = activity.findViewById(R.id.leftButton);
        viewPairsList.add(Pair.create(tempView, new String[]{"כפתור ניווט בין ערכים", "משמש למעבר בין הערכים הנמדדים ע\"י המכשיר. \nלחיצה ממושכת על כפתור ההפעלה ולאחר מכן על כפתור זה תפעיל מצב \"HOLD\" לעצירת מדידת ערכים והקפאת הערכים המופיעים על המסך. על מנת לצאת ממצב זה לחץ שוב על כפתור ההפעלה ולאחר מכן על כפתור זה."}));
        tempView = activity.findViewById(R.id.kestrelLight);
        viewPairsList.add(Pair.create(tempView, new String[]{"מסך - מהירות רוח", "מציג את מהירות הרוח הנמדדת, ניתן לזהות ע\"י אייקון הרוח בתחתית המסך, המהירות ביחידות של m/s."}));
        viewPairsList.add(Pair.create(tempView, new String[]{"מסך - טמפרטורה", "מציג את הטמפרטורה הנמדדת, ניתן לזהות ע\"י אייקון מד החום בתחתית המסך, הטמפרטורה ביחידות של מעלות צלזיוס."}));
        viewPairsList.add(Pair.create(tempView, new String[]{"מסך - צינת הרוח (עומס קור)", "מציג את עומס הקור- הטמפרטורה המורגשת בעקבות הרוח, ניתן לזהות ע\"י אייקון הרוח ולצידו מד החום בתחתית המסך, עומס הקור מציג טמפרטורה ביחידות של מעלות צלזיוס."}));
        viewPairsList.add(Pair.create(tempView, new String[]{"מסך - לחות יחסית", "מציג את הלחות הנמדדת, ניתן לזהות ע\"י אייקון טיפה ולצידו אחוז בתחתית המסך, הלחות מוצגת באחוזים."}));
        viewPairsList.add(Pair.create(tempView, new String[]{"מסך - עומס חום", "מציג את עומס החום - הטמפרטורה המורגשת בעקבות הלחות, ניתן לזהות ע\"י אייקון טיפה, לצד אחוז, לצד מד החום בתחתית המסך, הטמפרטורה ביחידות של מעלות צלזיוס."}));

        frontBackButton = activity.findViewById(R.id.frontBackButton);



    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.userGuideButton) {
            TextView title = new TextView(activity);
            title.setText("שימו לב!");
            title.setPadding(10, 10, 30, 10);
            title.setGravity(Gravity.RIGHT);
            title.setTextColor(activity.getResources().getColor(R.color.red));
            title.setTypeface(null, Typeface.BOLD);

            final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
            builder.setCustomTitle(title).setMessage("האפליקציה אינה מהווה תחליף לשימוש במכשיר הקסטרל ומשמשת לתרגול והבנה בלבד.")
                    .setCancelable(false)
                    .setPositiveButton("הבנתי", (dialog, id) -> {
                        dialog.dismiss();
                    }).create().show();
            MaterialShowcaseView.resetSingleUse(activity, SHOWCASE_ID);
            presentShowcaseSequence();


            // Toast.makeText(activity, "Showcase reset", Toast.LENGTH_SHORT).show();

        }
    }

    private void presentShowcaseSequence() {
        zl.resetZoom();


        ShowcaseConfig config = new ShowcaseConfig();

        //config.setDelay(500); // half second between each showcase view
        sequence = new MaterialShowcaseSequence(activity);
        sequence.singleUse(SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                //Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();

                switch (position) {
                    case 0: {
                        if (activity.getIsFront()) {
                            activity.changeFrontBackImage();
                        }
                        preSequenceActions();
                        break;
                    }
                    case 4: {
                        if (!activity.getIsFront()) {
                            activity.changeFrontBackImage();
                        }
                        break;
                    }
                    case 9: {
                        activity.getKestrelLogic().setKestrelMeasurementViewAndIcons(eKestrelMeasurement.WindSpeed);
                        break;
                    }
                    case 10: {
                        activity.getKestrelLogic().setKestrelMeasurementViewAndIcons(eKestrelMeasurement.Temperature);
                        break;
                    }
                    case 11: {
                        activity.getKestrelLogic().setKestrelMeasurementViewAndIcons(eKestrelMeasurement.WindChill);
                        break;
                    }
                    case 12: {
                        activity.getKestrelLogic().setKestrelMeasurementViewAndIcons(eKestrelMeasurement.Humidity);
                        break;
                    }
                    case 13: {
                        {
                            activity.getKestrelLogic().setKestrelMeasurementViewAndIcons(eKestrelMeasurement.DiscomfortIndex);
                            break;
                        }
                    }

                }
            }
        });
        sequence.setOnItemSkippedListener((itemView, position) ->
                postSequenceActions()
        );
        sequence.setOnItemDismissedListener((itemView, position) ->
        {
            if (position == 13) {
                postSequenceActions();
            }
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
                                    .setMaskColour(ContextCompat.getColor(activity,R.color.guideMask))
                                    .setTitleTextColor(ContextCompat.getColor(activity,R.color.guideTitle))
                                    .setContentTextColor(ContextCompat.getColor(activity,R.color.guideContent))
                                    .setDismissTextColor(ContextCompat.getColor(activity,R.color.guideTitle))
                                    .setSequence(true)
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
                                    .setMaskColour(ContextCompat.getColor(activity,R.color.guideMask))
                                    .setTitleTextColor(ContextCompat.getColor(activity,R.color.guideTitle))
                                    .setContentTextColor(ContextCompat.getColor(activity,R.color.guideContent))
                                    .setDismissTextColor(ContextCompat.getColor(activity,R.color.guideTitle))
                                    .renderOverNavigationBar()
                                    .setSequence(true)
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
                                    .setMaskColour(ContextCompat.getColor(activity,R.color.guideMask))
                                    .setTitleTextColor(ContextCompat.getColor(activity,R.color.guideTitle))
                                    .setContentTextColor(ContextCompat.getColor(activity,R.color.guideContent))
                                    .setDismissTextColor(ContextCompat.getColor(activity,R.color.guideTitle))
                                    .renderOverNavigationBar()
                                    .setSequence(true)
                                    .build()
                    );
                }
            }
        }

        sequence.start();

    }

    private void preSequenceActions() {
        userGuideButton.setEnabled(false);
        if (activity.getKestrelLogic().getIsPowerOn()) {
            activity.getKestrelLogic().onPowerButtonPressed3Sec();
        }
        isUsingLocation = activity.getKestrelLogic().getLocationSettingItemState();
        if (isUsingLocation) {
            activity.getKestrelLogic().getLocationSettingItem().setChecked(false);
            activity.getKestrelLogic().getLocationHandling().setIsRandomValues(true);
        }
        activity.getKestrelLogic().onPowerButtonClicked();
        activity.getKestrelLogic().invisibleKestrelButtons();
        activity.getFrontBackButton().setEnabled(false);


    }

    private void postSequenceActions() {
        activity.getKestrelLogic().visibleKestrelButtons();
        if (isUsingLocation) {
            activity.getKestrelLogic().getLocationSettingItem().setChecked(true);
            activity.getKestrelLogic().getLocationHandling().setIsRandomValues(false);

        }
        activity.getKestrelLogic().setDefaultMeasurement();
        activity.getKestrelLogic().onPowerButtonPressed3Sec();
        activity.getFrontBackButton().setEnabled(true);
        userGuideButton.setEnabled(true);
        if (!activity.getIsFront()) {
            activity.changeFrontBackImage();
        }
    }

}
