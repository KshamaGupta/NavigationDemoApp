package com.example.scrambled.Screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.scrambled.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel:ViewModel() {
    var userGuess by mutableStateOf("")
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private lateinit var currentWord: String
    private var useWords: MutableSet<String> = mutableSetOf()
    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        if (useWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            useWords.add(currentWord)
            return shuffleCurrentWord(currentWord)


        }
    }

    init {
        resetGame()
    }

    private fun shuffleCurrentWord(Word: String): String {
        val temp = Word.toCharArray()
        temp.shuffle()
        while (String(temp).equals(Word)) {
            temp.shuffle()
        }
        return String(temp)
    }

    fun resetGame() {
        useWords.clear()
        _uiState.value = GameUiState(pickRandomWordAndShuffle(), false)
    }

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updateScore=_uiState.value.score.plus(20)
            updateGameState(updateScore)
        } else {
            _uiState.update { currentState -> currentState.copy(isGuessedWordWrong = true,
                currentWordCount = currentState.currentWordCount.inc()
            ) }
        }
        updateUserGuess("")
    }
    private fun updateGameState(updatedScore: Int) {
        if (useWords.size == 10) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        }
        else {

            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    score = updatedScore,
                    currentWordCount = _uiState.value.currentWordCount.inc()
                )
            }
        }


    }
    fun skipWord() {
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }
}
