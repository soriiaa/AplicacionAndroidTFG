package com.example.investlearntfg.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.investlearntfg.R
import com.example.investlearntfg.ui.theme.InvestLearnTFGTheme

@Composable
fun TitulosInvestLearn() {
    InvestLearnTFGTheme {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(R.string.nombreApp),
            style = TextStyle(
                fontSize = 50.sp
            )
        )
        Spacer(modifier = Modifier.height(45.dp))
    }
}