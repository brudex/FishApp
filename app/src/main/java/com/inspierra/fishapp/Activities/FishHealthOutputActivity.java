package com.inspierra.fishapp.Activities;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.inspierra.fishapp.Adapter.ChartDataAdapter;
import com.inspierra.fishapp.ChartModels.GraphService;
import com.inspierra.fishapp.HelpingClasses.FishHealthOutputResponse;
import com.inspierra.fishapp.HelpingClasses.OutPutRequestClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.PrefsUtil;
import com.inspierra.fishapp.listviewitems.BarChartItem;
import com.inspierra.fishapp.listviewitems.ChartItem;
import com.inspierra.fishapp.listviewitems.LineChartItem;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FishHealthOutputActivity extends AppCompatActivity
{
    protected Typeface tfRegular;
    protected Typeface tfLight;
    protected RelativeLayout imgBack;
    ProgressDialog progressDialog;
    AcquahService acquahService;
    OutPutRequestClass requestPayload;
    ArrayList<ChartItem> chartItems;
    ListView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fish_health_output);
        setTitle("Fish Health Output");
        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        imgBack = findViewById(R.id.imgback);
        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
        requestPayload = new OutPutRequestClass();//todo pass from and to date
        acquahService = AcquaApiClient.getClient().create(AcquahService.class);
        Utils.init(this);
        lv = findViewById(R.id.listView1);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Requesting Data"); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        chartItems = new ArrayList<>();
        progressDialog.show();
        new ProgressIndicator().execute();
    }


    class ProgressIndicator extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Call<FishHealthOutputResponse> fishHealthOutput = acquahService.FishHealthOutput(requestPayload,
                    PrefsUtil.getTempTokenData(FishHealthOutputActivity.this));
            fishHealthOutput.enqueue(new Callback<FishHealthOutputResponse>()
            {
                @Override
                public void onResponse(@NotNull Call<FishHealthOutputResponse> call,
                                       @NotNull Response<FishHealthOutputResponse> response)
                {
                    progressDialog.dismiss();
                    assert response.body() != null;
                    if(response.body().ammonia.size()>0)
                    {
                        {
                            Pair<BarData, ValueFormatter> pair = GraphService.getWaterQualityChartItem(response.body().waterQuality);
                            chartItems.add(new BarChartItem(pair.first, "Water Quality",null,response.body().waterQuality.size(), getApplicationContext()));
                        }
                        if(response.body().mortality.size()>0){
                            Pair<LineData, ValueFormatter> pair = GraphService.getLineDataFromValueLabels("Mortality ",response.body().mortality);
                            LineData lineData = pair.first;
                            chartItems.add(new LineChartItem(lineData,pair.second, getApplicationContext()));
                        }
                        if(response.body().phLevel.size()>0){
                            Pair<LineData, ValueFormatter> pair  = GraphService.getLineDataFromValueLabels("Ph Level",response.body().phLevel);
                            LineData lineData = pair.first;
                            chartItems.add(new LineChartItem(lineData,pair.second, getApplicationContext()));
                        }
                        if(response.body().dissolvedOxygen.size()>0){
                            Pair<LineData, ValueFormatter> pair  = GraphService.getLineDataFromValueLabels("Dissolved Oxygen",response.body().dissolvedOxygen);
                            LineData lineData = pair.first;
                            chartItems.add(new LineChartItem(lineData,pair.second, getApplicationContext()));
                        }
                        if(response.body().temperature.size()>0){
                            Pair<LineData, ValueFormatter> pair  =GraphService.getLineDataFromValueLabels("Temperature",response.body().temperature);
                            LineData lineData = pair.first;
                            chartItems.add(new LineChartItem(lineData,pair.second, getApplicationContext()));
                        }
                        if(response.body().ammonia.size()>0){
                            Pair<LineData, ValueFormatter> pair  = GraphService.getLineDataFromValueLabels("Ammonia",response.body().ammonia);
                            LineData lineData = pair.first;
                            chartItems.add(new LineChartItem(lineData,pair.second, getApplicationContext()));
                        }
                        if(response.body().nitrite.size()>0){
                            Pair<LineData, ValueFormatter> pair  = GraphService.getLineDataFromValueLabels("Nitrite",response.body().ammonia);
                            LineData lineData = pair.first;
                            chartItems.add(new LineChartItem(lineData,pair.second, getApplicationContext()));
                        }
                        if(response.body().nitrite.size()>0){
                            Pair<LineData, ValueFormatter> pair  = GraphService.getLineDataFromValueLabels("Turbidity",response.body().turbidity);
                            LineData lineData = pair.first;
                            chartItems.add(new LineChartItem(lineData,pair.second, getApplicationContext()));
                        }
                        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), chartItems);
                        runOnUiThread(() -> lv.setAdapter(cda));
                    }
                    else
                    {
                        new OoOAlertDialog.Builder(FishHealthOutputActivity.this)
                                .setTitle("No Data")
                                .setMessage("No available output data for fish health indicators.")
                                .setAnimation(Animation.POP)
                                .setPositiveButton("Ok", null)
                                .setPositiveButtonColor(R.color.green)
                                .build();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<FishHealthOutputResponse> call, @NotNull Throwable t)
                {
                    new OoOAlertDialog.Builder(FishHealthOutputActivity.this)
                            .setTitle("Error")
                            .setMessage("Could not get data for fish health indicators.")
                            .setAnimation(Animation.POP)
                            .setPositiveButton("Ok", null)
                            .setPositiveButtonColor(R.color.green)
                            .build();
                }
            });
            return null;
        }
    }

}
