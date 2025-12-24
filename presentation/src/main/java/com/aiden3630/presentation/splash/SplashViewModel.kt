package com.aiden3630.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.presentation.Route
import com.aiden3630.data.manager.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    // Состояние: Куда переходить? (null - пока ждем, String - маршрут)
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            // 1. Ждем 2 секунды (чтобы пользователь увидел логотип)
            delay(2000)

            // 2. Читаем токен из памяти (берем первое значение)
            val token = tokenManager.getToken().first()

            // 3. Принимаем решение
            if (!token.isNullOrEmpty()) {
                // Токен есть -> Идем вводить ПИН-КОД
                _startDestination.value = Route.SIGN_IN_PIN
            } else {
                // Токена нет -> Идем на ВХОД/РЕГИСТРАЦИЮ
                _startDestination.value = Route.SIGN_IN
            }
        }
    }
}