package com.example.investlearntfg.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.investlearntfg.data.model.CandleResponse

@Composable
fun GraficoPredeterminado(datosVelas: CandleResponse) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(start = 16.dp, top = 16.dp),
        factory = { contexto ->
            CandleStickChart(contexto).apply {
                description.isEnabled = false
                setPinchZoom(true)
                setDrawGridBackground(false)
            }
        },
        update = { grafico ->
            val velas = datosVelas.results
            if (!velas.isNullOrEmpty()) {

                val entradas = velas.mapIndexed { index, vela ->
                    CandleEntry(
                        index.toFloat(),
                        vela.h,
                        vela.l,
                        vela.o,
                        vela.c
                    )
                }

                val conjuntoDatos = CandleDataSet(entradas, "Acción XYZ").apply {
                    color = Color.rgb(80, 80, 80)
                    shadowColor = Color.DKGRAY
                    shadowWidth = 0.7f
                    decreasingColor = Color.RED
                    decreasingPaintStyle = Paint.Style.FILL
                    increasingColor = Color.rgb(122, 242, 84)
                    increasingPaintStyle = Paint.Style.FILL
                    neutralColor = Color.BLUE
                }

                grafico.axisLeft.textColor = Color.WHITE
                grafico.xAxis.textColor = Color.WHITE
                grafico.axisRight.isEnabled = false

                grafico.data = CandleData(conjuntoDatos)
                grafico.invalidate()
            }
        }
    )
}