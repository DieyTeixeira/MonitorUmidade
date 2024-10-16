package com.codek.monitorumidade.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codek.monitorumidade.ui.viewmodels.AppAgroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentComboBoxWithLabel(
    equipmentList: List<String>,
    onEquipmentSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(if (equipmentList.isNotEmpty()) equipmentList[0] else "<Selecione o equipamento>") }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(top = 9.dp)
                .background(
                    Color.White,
                    RoundedCornerShape(10.dp)
                )
        ){}
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Equipamento",
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedItem,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            contentDescription = "Expand dropdown"
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(53.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .padding(0.dp)
                ) {
                    equipmentList.forEach { equipment ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = equipment,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                )
                            },
                            onClick = {
                                selectedItem = equipment
                                onEquipmentSelected(equipment)
                                expanded = false
                            },
                            modifier = Modifier
                                .background(Color.Transparent)
                                .height(35.dp)
                        )
                    }
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun EquipamentoDropDown(
    viewModel: AppAgroViewModel,
    modifier: Modifier = Modifier
) {
    val agroInfoList by viewModel.agroInfoList.collectAsState()
    val humidity = viewModel.humidityValue.collectAsState()

    val equipmentList = agroInfoList.map { it.equipamento }
    val humidityMap = agroInfoList.associate { it.equipamento to it.umidade }

    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        EquipmentComboBoxWithLabel(
            equipmentList = equipmentList as List<String>,
            onEquipmentSelected = { selectedEquipment ->
                humidityMap[selectedEquipment]?.let { humidity ->
                    viewModel.updateHumidity(humidity.toFloat())
                }
            }
        )
        Text(
            text = "Umidade: ${humidity.value}%",
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
    }
}
//
//@Preview
//@Composable
//private fun EquipmentComboBoxPreview() {
//    EquipamentoDropDown()
//}