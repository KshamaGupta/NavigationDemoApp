package com.example.scrambled.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import com.example.scrambled.R
import com.example.scrambled.common.FormattedPriceLabel
import com.example.scrambled.data.OrderUiState


@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
){
    val resources = LocalContext.current.resources

    val numberOfCupcakes = resources.getQuantityString(
        R.plurals.cupcakes,
        orderUiState.quantity,
        orderUiState.quantity
    )
    //Load and format a string resource with the parameters.
    val orderSummary = stringResource(
        R.string.order_details,
        numberOfCupcakes,
        orderUiState.flavor,
        orderUiState.date,
        orderUiState.quantity
    )
    val newOrder = stringResource(R.string.new_cupcake_order)
    //Create a list of order summary to display
    val items = listOf(
        // Summary line 1: display selected quantity
        Pair(stringResource(R.string.quantity), numberOfCupcakes),
        // Summary line 2: display selected flavor
        Pair(stringResource(R.string.flavor), orderUiState.flavor),
        // Summary line 3: display selected pickup date
        Pair(stringResource(R.string.pickup_date), orderUiState.date)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEach { item ->
                Text(item.first.uppercase())
                Text(text = item.second, fontWeight = FontWeight.Bold)
                Divider(thickness = 4.dp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            FormattedPriceLabel(
                subtotal = orderUiState.price,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Row(
            modifier = Modifier
                .weight(1f, false)
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSendButtonClicked(newOrder, orderSummary) }
                ) {
                    Text(stringResource(R.string.send))
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

