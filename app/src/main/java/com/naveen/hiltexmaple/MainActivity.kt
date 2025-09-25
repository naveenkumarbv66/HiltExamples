package com.naveen.hiltexmaple

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.naveen.hiltexmaple.dataClass.Person
import com.naveen.hiltexmaple.runTime.AssistedViewModel
import com.naveen.hiltexmaple.runTime.AssistedViewModelInterface
import com.naveen.hiltexmaple.ui.theme.HiltExmapleTheme
import com.naveen.hiltexmaple.usingInterface.TestInterface
import com.naveen.hiltexmaple.veiwModule.MyViewModel
import com.naveen.hiltexmaple.veiwModule.MyViewModelWithPara
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //Data Class : person.name
    @Inject
    lateinit var person: Person

    //Interface : InterfaceImple.printSomeThing()
    @Inject
    lateinit var InterfaceImple: TestInterface

    //ViewModule with no constructor : myViewModel.getSomethingFromViewModel()
    val myViewModel: MyViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            //ViewModule with constructor : data.name.plus(" ").plus(address)
            val myViewModelPara: MyViewModelWithPara by viewModels()
            val data by myViewModelPara.stateFlowDemoViewModelData.collectAsState()
            val address by myViewModelPara.stateFlowAddress.collectAsState()


            //Run time value passing.
            val viewModel by viewModels<AssistedViewModel>(
                extrasProducer = {
                    defaultViewModelCreationExtras.withCreationCallback<AssistedViewModelInterface> { factory ->
                        factory.create("Mysore....")
                    }
                }
            )

            HiltExmapleTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Greeting(
                            name = data.name.plus(" ").plus(address),
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                        
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, com.naveen.hiltexmaple.api.ui.activity.ApiDemoActivity::class.java)
                                startActivity(intent)
                            }
                        ) {
                            Text("Open API Demo")
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, com.naveen.hiltexmaple.api.ui.activity.UseCaseDemoActivity::class.java)
                                startActivity(intent)
                            }
                        ) {
                            Text("Open Use Case Demo")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

//    val myViewModelPara = hiltViewModel<MyViewModelWithPara>()
    //Run time value passing.
    val viewModelTwo = hiltViewModel<AssistedViewModel, AssistedViewModelInterface>(
        creationCallback = { factory -> factory.create("From Composable")}
    )

    Column {
        Text(
            text = "Hello [[[ $name! ]]]",
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HiltExmapleTheme {
        Greeting("Android")
    }
}