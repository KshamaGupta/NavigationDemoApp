package com.example.scrambled.Screen

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scrambled.R


@Composable
fun GameScreen(gameViewModel: GameViewModel= viewModel()){
    val gameUiState by gameViewModel.uiState.collectAsState()
    Column(modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(text = stringResource(R.string.app_name))
        GameLayout(gameUiState.currentWordCount,gameUiState.currentScrambledWord,gameViewModel.userGuess,
            onUserGuessChanged = {gameViewModel.updateUserGuess(it)}, onKeyboardDone = {gameViewModel.checkUserGuess()},gameUiState.isGuessedWordWrong)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick ={gameViewModel.checkUserGuess()}, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Submit", fontSize = 16.sp)
            }
            OutlinedButton(onClick = { gameViewModel.skipWord() }, modifier = Modifier.fillMaxWidth()

                ) {
                Text(text = "Skip", fontSize = 16.sp)
            }
        }
        if(gameUiState.isGameOver){
            GameAlertBox(onPlayAgain = { gameViewModel.resetGame()}, Score =gameUiState.score)
        }
        GameStatus(gameUiState.score)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameLayout(wordCount:Int,Word:String,userGuess:String,onUserGuessChanged:(String)->Unit,onKeyboardDone:()->Unit,isGuessWrong:Boolean
){
    
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
        ) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.word_count,wordCount),modifier= Modifier
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .background(color = Color.Blue)
                .align(Alignment.End),
                color = Color.White
            )
            Text(text = "$Word",fontSize = 45.sp)
            Text(text = stringResource(R.string.instructions), textAlign = TextAlign.Center, fontSize = 12.sp)
            OutlinedTextField(value = userGuess, onValueChange ={onUserGuessChanged(it) }, label = {
                if(isGuessWrong)
                    Text(text = "Wrong Word")
                else{
                    Text(stringResource(R.string.enter_your_word))}
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp)
            , isError = isGuessWrong, singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone() }
                ),
                )
        }
        
    }
}
@Composable
fun GameStatus(score:Int){
    Card(modifier = Modifier.padding(20.dp)) {
        Text(text = stringResource(R.string.score,score))
    }
}
@Composable
fun GameAlertBox(onPlayAgain:()->Unit,Score:Int){
    val activity= (LocalContext.current) as Activity
    AlertDialog(onDismissRequest = { activity.finish() }, title ={Text("Congratulations")},
        text = { Text(stringResource(R.string.score, Score)) },
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = "Exit")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPlayAgain()
                }
            ) {
                Text(text = "Play Again")
            }
        }
    )
}