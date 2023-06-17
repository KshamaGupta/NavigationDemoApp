package com.example.scrambled.Screen

data class GameUiState(val currentScrambledWord:String="",val isGuessedWordWrong:Boolean=false,val score:Int=0,
                       val currentWordCount: Int = 0,val isGameOver:Boolean=false
)
