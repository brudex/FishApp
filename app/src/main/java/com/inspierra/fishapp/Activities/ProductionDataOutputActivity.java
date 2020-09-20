package com.inspierra.fishapp.Activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.inspierra.fishapp.Adapter.ChartDataAdapter;
import com.inspierra.fishapp.ChartModels.GraphService;
import com.inspierra.fishapp.HelpingClasses.EconIndicatorOutputResponse;
import com.inspierra.fishapp.HelpingClasses.LabelValueField;
import com.inspierra.fishapp.HelpingClasses.OutPutRequestClass;
import com.inspierra.fishapp.HelpingClasses.ProductionDataOutput;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.PrefsUtil;
import com.inspierra.fishapp.listviewitems.BarChartItem;
import com.inspierra.fishapp.listviewitems.ChartItem;
import com.inspierra.fishapp.listviewitems.LineChartItem;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductionDataOutputActivity extends AppCompatActivity
{
    protected Typeface tfRegular;
    protected Typeface tfLight;
    protected RelativeLayout imgBack;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    OutPutRequestClass requestPayload;
    ArrayList<ChartItem> chartItems;
    TextView tvTitle;
    boolean dataAvailable;
    ListView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.econ_output_layout);
        setTitle("Production Data Output");
        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
        dataAvailable=false;
        requestPayload = new OutPutRequestClass();//todo pass from and to date
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        Utils.init(this);
        tvTitle = findViewById(R.id.tvTitle);
        lv = findViewById(R.id.listView1);
        tvTitle.setText("Production Data Output");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Requesting Data"); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        chartItems = new ArrayList<>();
        progressDialog.show();
        new ProgressIndicator().execute();

    }

    private  void  addToChartItems(String chartTitle, List<LabelValueField> fields){
        if(fields!=null && fields.size() > 0){
            Pair<LineData, ValueFormatter> pair =GraphService.getLineDataFromValueLabels(chartTitle,fields);
            LineData lineData = pair.first;
            dataAvailable=true;
            chartItems.add(new LineChartItem(lineData,pair.second, getApplicationContext()));
        }
    }

    class ProgressIndicator extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<ProductionDataOutput> prodData = acquahService.ProductionDataOutput(requestPayload,PrefsUtil.getTempTokenData(ProductionDataOutputActivity.this));
            prodData.enqueue(new Callback<ProductionDataOutput>()
            {
                @Override
                public void onResponse(@NotNull Call<ProductionDataOutput> call,
                                       @NotNull Response<ProductionDataOutput> response)
                {
                    progressDialog.dismiss();
                    assert response.body() != null;
                    ProductionDataOutput output = response.body();
                    addToChartItems("Hatchability" ,output.hatchability);
                    addToChartItems("Mortality during sex reversal" ,output.sexReversalGrowthRate);
                    addToChartItems("Sex Reversal Mortality" ,output.sexReversalMortality);
                    addToChartItems("Weight gain at Nursery" ,output.weightGainAtNursery);
                    addToChartItems("Growth Rate At Nursery",output.growthRateAtNursery);
                    addToChartItems("Recovery rate during nursery",output.rateRecoveryDuringNursery);
                    addToChartItems("Mortality during nursing",output.mortalityDuringNursery);
                    addToChartItems("Mortality rate during nursing",output.mortalityRateDuringNursery);
                    addToChartItems("Overall recovery from fry to nursery",output.overallRecoveryFryToNursery);
                    addToChartItems("Overall Recovery Rate" ,output.overallRecoveryRate);
                    addToChartItems("Duration of culture",output.durationOfCulture);
                    addToChartItems("Specific Growth rate",output.specificGrowthRate);
                    addToChartItems("Quantity of Fish",output.quantityOfFish);
                    addToChartItems("Size of fish",output.sizeOfFish);
                    addToChartItems("Quantity of feed per day",output.quantityOfFeedPerDay);
                    addToChartItems("Quantity of fish harvested",output.quantityOfFishHarvested);
                    addToChartItems("Weight of fish harvested",output.weightOfFishHarvested);
                    addToChartItems("Price per kg",output.pricePerKg);
                    if(dataAvailable){
                        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), chartItems);
                        runOnUiThread(() -> lv.setAdapter(cda));
                    }else{
                        new OoOAlertDialog.Builder(ProductionDataOutputActivity.this)
                                .setTitle("No Data")
                                .setMessage("No available output data for production data.")
                                .setAnimation(Animation.POP)
                                .setPositiveButton("Ok", null)
                                .setPositiveButtonColor(R.color.green)
                                .build();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<ProductionDataOutput> call, @NotNull Throwable t)
                {
                    progressDialog.dismiss();
                    new OoOAlertDialog.Builder(ProductionDataOutputActivity.this)
                            .setTitle("Error")
                            .setMessage("Could not get data for production data.")
                            .setAnimation(Animation.POP)
                            .setPositiveButton("Ok", null)
                            .setPositiveButtonColor(R.color.green)
                            .build();
                }
            });
            return null;
        }
    }




    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> values1 = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            values1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(values1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            values2.add(new Entry(i, values1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(values2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        return new LineData(sets);
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Bar data
     */
    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Pie data
     */
    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        return new PieData(d);
    }








}
