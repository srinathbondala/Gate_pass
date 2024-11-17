package com.example.easyexit;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class activityUserData extends AppCompatActivity {

    private TextView monthYearTextView;
    private CalendarView calendarView;
    private YearMonth currentMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        monthYearTextView = findViewById(R.id.monthYearTextView);
        calendarView = findViewById(R.id.calendarView);
        ImageView prevButton = findViewById(R.id.prevButton);
        ImageView nextButton = findViewById(R.id.nextButton);
        ArrayList<LocalDate> greenDates = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            greenDates.add(LocalDate.of(2024, 11, 16));
            greenDates.add(LocalDate.of(2024, 11, 18));
            greenDates.add(LocalDate.of(2024, 11, 1));
            greenDates.add(LocalDate.of(2024, 11, 25));
        }

        // Set initial current month
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentMonth = YearMonth.now();
        }

//        setupCalendarView(greenDates);

        // Set up previous and next buttons
        prevButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                currentMonth = currentMonth.minusMonths(1);  // Go to previous month
            }
            //call get dates method
            setupCalendarView(greenDates);
        });

        nextButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                currentMonth = currentMonth.plusMonths(1);  // Go to next month
            }
            setupCalendarView(greenDates);
        });
        setupEdgeToEdge();
        setupPieChart();
        setupCalendarView(greenDates);
    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().hide();
    }

    private void setupPieChart() {
        PieChart pieChart = findViewById(R.id.pieChart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(75, "Present"));
        entries.add(new PieEntry(25, "Absent"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{Color.GREEN, Color.RED});
        dataSet.setSliceSpace(3f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(16f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setCenterText("Attendance");
        pieChart.setCenterTextSize(16f);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }


    private void setupCalendarView(ArrayList<LocalDate> greenDates) {
        updateMonthYearText(currentMonth);

        // Set up the CalendarView
        YearMonth startMonth = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startMonth = currentMonth.minusMonths(10);
        }
        YearMonth endMonth = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            endMonth = currentMonth.plusMonths(10);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            calendarView.setup(startMonth, endMonth, DayOfWeek.SUNDAY);
        }
        calendarView.scrollToMonth(currentMonth);

        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, CalendarDay calendarDay) {
                LocalDate date = calendarDay.getDate();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    container.dayTextView.setText(String.valueOf(date.getDayOfMonth()));
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (date.equals(LocalDate.now())) {
                        container.dayTextView.setTextColor(Color.RED);
                    }
                }

                if (greenDates.contains(date)) {
                    container.dayTextView.setBackgroundColor(Color.GREEN);
                    container.dayTextView.setOnClickListener(v ->
                            Toast.makeText(activityUserData.this, "Clicked on: " + date.toString(), Toast.LENGTH_SHORT).show()
                    );
                } else {
                    container.dayTextView.setBackgroundColor(Color.TRANSPARENT);
                    container.dayTextView.setOnClickListener(null);
                }
            }
        });
        calendarView.setMonthScrollListener(month -> {
            currentMonth = month.getYearMonth();
            updateMonthYearText(currentMonth);
            return null;
        });
    }

    static class DayViewContainer extends ViewContainer {
        TextView dayTextView;

        DayViewContainer(View view) {
            super(view);
            dayTextView = view.findViewById(R.id.textView);
        }
    }
    private void updateMonthYearText(YearMonth month) {
        String monthYearText = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            monthYearText = month.getMonth().name() + " " + month.getYear();
        }
        monthYearTextView.setText(monthYearText);
    }
}
