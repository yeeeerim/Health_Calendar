package com.example.healthcalendar

//import CalenderClass.LinearRegression
import android.os.Bundle

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_graph.*
import java.util.*
import kotlin.collections.ArrayList

class GraphActivity : AppCompatActivity(){
    val entriesForWeight:ArrayList<Entry> = ArrayList()
    var entriesForMuscle:ArrayList<Entry> = ArrayList()
    var entriesForFat:ArrayList<Entry> = ArrayList()
    var entriesForPrediction:ArrayList<Entry> = ArrayList()
    var lineDatasets=mutableListOf<ILineDataSet>()
    var entriesForBodyPart:ArrayList<PieEntry> = ArrayList()
    var bodyPartmap:MutableMap<String,Int> = mutableMapOf<String,Int>("어깨" to 0,"가슴" to 0,"복근" to 0,"등" to 0,"하체" to 0)


    var xPos:ArrayList<Float> = ArrayList()
    var yPos:ArrayList<Float> = ArrayList()
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
            //현재 시간을 가져옴.
            val time=System.currentTimeMillis()
            var dateFormat= SimpleDateFormat("yyyyMM01")
            var dateStarted=dateFormat.format(Date(time))

            //그래프 세팅
            lineChart.xAxis.apply{
                position=XAxis.XAxisPosition.BOTTOM
                textSize=10f
                setDrawGridLines(false)
                granularity=1f
            }
            lineChart.apply{
                axisLeft.axisMaximum=200f
                axisRight.isEnabled=false
                legend.apply{
                    textSize=15f
                    verticalAlignment= Legend.LegendVerticalAlignment.TOP
                    horizontalAlignment=Legend.LegendHorizontalAlignment.CENTER
                    orientation=Legend.LegendOrientation.HORIZONTAL
                    setDrawInside(false)
                }
            }

        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("일주일동안의 운동부위")

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);

        pieChart.setDrawCenterText(true);

        pieChart.setHighlightPerTapEnabled(true);

        pieChart.getLegend().setEnabled(false);


        pieChart.setEntryLabelColor(Color.WHITE)

        //pieChart.setEntryLabelTextSize(11f);

            var weight=1.0.toFloat()
            var muscle=1f
            var fat=1f
            val database :FirebaseDatabase= FirebaseDatabase.getInstance()
            val dbRef : DatabaseReference = database
                .getReference(FirebaseAuth.getInstance().currentUser!!.uid)

            dbRef.addValueEventListener(object :ValueEventListener
            {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    //신체정보 가져오기+화면에 띄움.
                    for(i in 0..31)
                    {if(snapshot.child(dateStarted).child("BodyProfile").value!=null) {
                        var bodyProfile = snapshot.child(dateStarted)
                            .child("BodyProfile").value as Map<String, Any>
                        weight= bodyProfile["weight"].toString().toFloat()
                        val dataentry=Entry((dateStarted.toInt()%10000).toString().toFloat(),weight).copy()
                        entriesForWeight.add(dataentry)
                        entriesForWeight.clone()
                        muscle=bodyProfile["muscle_kg"].toString().toFloat()
                        entriesForMuscle.add(Entry((dateStarted.toInt()%10000).toString().toFloat(),muscle))

                        fat=bodyProfile["fat_kg"].toString().toFloat()
                        entriesForFat.add(Entry((dateStarted.toInt()%10000).toString().toFloat(),fat))


                        //예측
                        xPos.add((dateStarted.toInt()%10000).toString().toFloat())
                        xPos.addAll(xPos)
                        yPos.add(weight)
                        yPos.addAll(yPos)
                        var linearRegression: LinearRegression = LinearRegression(xPos,yPos)
                        var prediction=linearRegression.predictValue((dateStarted.toInt()%10000).toString().toFloat())



                        dateStarted=((dateStarted.toInt()+1)).toString()


                        entriesForPrediction.add(Entry((dateStarted.toInt()%10000).toString().toFloat(),prediction))
                        //
                        var dataset :LineDataSet = LineDataSet(entriesForWeight,"체중")
                        dataset.setColor(Color.RED)

                        var dataset1 :LineDataSet = LineDataSet(entriesForMuscle,"골격근량")
                        dataset1.setColor(Color.BLUE)

                        var dataset2 :LineDataSet = LineDataSet(entriesForFat,"지방량")
                        dataset2.setColor(Color.GREEN)

                        var dataset3 :LineDataSet = LineDataSet(entriesForPrediction,"체중예측값")
                        dataset3.setColor(Color.MAGENTA)

                        lineDatasets.add(dataset)
                        lineDatasets.add(dataset1)
                        lineDatasets.add(dataset2)
                        lineDatasets.add(dataset3)
                        var datas= LineData(lineDatasets)
                        lineChart.data=datas





                    }else { dateStarted=(dateStarted.toInt()+1).toString() }}


                    dateFormat= SimpleDateFormat("yyyyMMdd")
                    dateStarted=dateFormat.format(Date(time))
                    for(i in 0..6)
                    {

                        if (snapshot.child(dateStarted).child("Routine").value != null) {

                        for (j in 0..(snapshot.child(dateStarted).child("Routine").childrenCount - 1)) {
                            var routine = snapshot.child(dateStarted.toString()).child("Routine").child(j.toString()).value as Map<String, Any>


                        bodyPartmap.set(routine["bodyPart"].toString() , bodyPartmap.getValue(routine["bodyPart"].toString())+1)


                        }}else { dateStarted=(dateStarted.toInt()-1).toString()}
                    }
                    if(bodyPartmap.getValue("어깨")!=0)
                    {entriesForBodyPart.add(PieEntry(bodyPartmap.getValue("어깨").toFloat(),"어깨").copy())}
                    if(bodyPartmap.getValue("복근")!=0)
                    {entriesForBodyPart.add(PieEntry(bodyPartmap.getValue("복근").toFloat(),"복근").copy())}
                    if(bodyPartmap.getValue("가슴")!=0)
                   {entriesForBodyPart.add(PieEntry(bodyPartmap.getValue("가슴").toFloat(),"가슴").copy())}
                    if(bodyPartmap.getValue("하체")!=0)
                   {entriesForBodyPart.add(PieEntry(bodyPartmap.getValue("하체").toFloat(),"하체").copy())}
                    if(bodyPartmap.getValue("등")!=0)
                   {entriesForBodyPart.add(PieEntry(bodyPartmap.getValue("등").toFloat(),"등").copy())}
                    //entriesForBodyPart.addAll(entriesForBodyPart)



                    var piedataset: PieDataSet = PieDataSet(entriesForBodyPart,"일주일간의 운동부위")
                    piedataset.setColors(Color.GREEN,Color.RED,Color.BLUE,Color.CYAN,Color.MAGENTA)
                    piedataset.valueTextSize=20f
                    var piedata :PieData= PieData(piedataset)
                    pieChart.data = piedata


                    dateStarted = ((dateStarted.toInt() + 1)).toString()
                }
            })
    }
    }

class LinearRegression(xdata: ArrayList<Float>, YData: ArrayList<Float>) {
    private val Xdata: ArrayList<Float>
    private val YData: ArrayList<Float>
    private var result1: Float = 0f
    private var result2: Float = 0f
    fun predictValue(inputValue: Float): Float {
        val X1 = Xdata[0]
        val Y1 = YData[0]
        val Xmean = getXMean(Xdata)
        val Ymean = getYMean(YData)
        val lineSlope = getLineSlope(Xmean, Ymean, X1, Y1)
        val YIntercept = getYIntercept(Xmean, Ymean, lineSlope)
        return lineSlope * inputValue + YIntercept
    }

    fun getLineSlope(Xmean: Float, Ymean: Float, X1: Float, Y1: Float): Float {
        val num1 = X1 - Xmean
        val num2 = Y1 - Ymean
        val denom = (X1 - Xmean) * (X1 - Xmean)
        return (num1 * num2 / denom)*1.003f
    }

    fun getYIntercept(Xmean: Float, Ymean: Float, lineSlope: Float): Float {
        return (Ymean - lineSlope * Xmean)
    }

    fun getXMean(Xdata: ArrayList<Float>): Float {
        result1 = 0.0f
        for (i in 0 until Xdata.size) {
            result1 = result1 + Xdata[i]
        }
        return result1 as Float
    }

    fun getYMean(Ydata: ArrayList<Float>): Float {
        result2 = 0.0f
        for (i in 0 until Ydata.size) {
            result2 = result2 + Ydata[i]
        }
        return (result2) as Float
    }

    init {
        Xdata = xdata
        this.YData = YData
    }
}