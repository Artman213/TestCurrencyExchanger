package com.example.testcurrencyexchanger.data

import com.example.testcurrencyexchanger.module.ExchangeRatesResponse
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val secureRepository: SecureRepository
) {

    suspend fun getExchangeRates(): Flow<Result<ExchangeRatesResponse>> {
        return flow {
            val result = remoteDataSource.getExchangeRates()
            result.onSuccess {
                if (secureRepository.getUserMoney() == null) {
                    secureRepository.setUserMoneyObject(it.rates)
                }
            }
            emit(result)
        }
    }
}